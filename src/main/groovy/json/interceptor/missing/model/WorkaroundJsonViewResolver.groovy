package json.interceptor.missing.model

import grails.core.support.proxy.ProxyHandler
import grails.plugin.json.renderer.ErrorsJsonViewRenderer
import grails.plugin.json.view.mvc.JsonViewResolver
import grails.rest.render.RenderContext
import grails.rest.render.RendererRegistry
import grails.views.mvc.SmartViewResolver
import grails.web.mime.MimeType
import groovy.transform.InheritConstructors
import org.grails.plugins.web.rest.render.ServletRenderContext
import org.grails.plugins.web.rest.render.html.DefaultHtmlRenderer
import org.grails.web.util.GrailsApplicationAttributes
import org.springframework.validation.Errors
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.AbstractUrlBasedView

import javax.annotation.PostConstruct

import static grails.views.mvc.SmartViewResolver.OBJECT_TEMPLATE_NAME

/**
 * Copied from JsonViewResolver, but modified to use WorkaroundJsonViewJsonRenderer instead
 * in order to allow interceptors to have access to the model.
 * @see https://stackoverflow.com/questions/64087536/grails-json-renderer-not-exposing-model-for-interceptor
 */
@InheritConstructors
class WorkaroundJsonViewResolver extends JsonViewResolver {

    @PostConstruct
    void initialize() {
        if (rendererRegistry != null) {
            def errorsRenderer = new ErrorsJsonViewRenderer((Class) Errors)
            errorsRenderer.setJsonViewResolver(this)
            rendererRegistry.addRenderer(errorsRenderer)
            viewConfiguration.mimeTypes.each { String mimeTypeString ->
                MimeType mimeType = new MimeType(mimeTypeString, 'json')
                def renderer = new WorkaroundJsonViewJsonRenderer<Object>(Object.class, mimeType, this,
                        proxyHandler, rendererRegistry)
                rendererRegistry.addDefaultRenderer(renderer)
            }
        }
    }

}

/**
 * Copied from JsonViewJsonRenderer/DefaultViewRenderer, but modified to use the ModelAndView instead
 * in order to allow interceptors to have access to the model.
 * @see https://stackoverflow.com/questions/64087536/grails-json-renderer-not-exposing-model-for-interceptor
 */
@InheritConstructors
class WorkaroundJsonViewJsonRenderer<T> extends DefaultHtmlRenderer<T> {

    public static final String MODEL_OBJECT = 'object'
    final SmartViewResolver viewResolver
    final ProxyHandler proxyHandler
    final RendererRegistry rendererRegistry

    WorkaroundJsonViewJsonRenderer(Class<T> targetType, SmartViewResolver viewResolver, ProxyHandler proxyHandler, RendererRegistry rendererRegistry) {
        this(targetType, MimeType.JSON, viewResolver, proxyHandler, rendererRegistry)
    }

    WorkaroundJsonViewJsonRenderer(Class<T> targetType, MimeType mimeType, SmartViewResolver viewResolver, ProxyHandler proxyHandler, RendererRegistry rendererRegistry) {
        super(targetType, mimeType)
        this.viewResolver = viewResolver
        this.proxyHandler = proxyHandler
        this.rendererRegistry = rendererRegistry
    }

    @Override
    void render(T object, RenderContext context) {
        def arguments = context.arguments
        def ct = arguments?.contentType

        if (ct) {
            context.setContentType(ct.toString())
        }
        else {
            final mimeType = context.acceptMimeType ?: mimeTypes[0]
            if (!mimeType.equals(MimeType.ALL)) {
                context.setContentType(mimeType.name)
            }
        }

        String viewName
        if (arguments?.view) {
            viewName = arguments.view.toString()
        }
        else {
            viewName = context.actionName
        }

        String viewUri
        if (viewName?.startsWith('/')) {
            viewUri = viewName
        } else {
            viewUri = "/${context.controllerName}/${viewName}"
        }

        def webRequest = ((ServletRenderContext) context).webRequest
        def request = webRequest.currentRequest
        def response = webRequest.currentResponse

        AbstractUrlBasedView view
        String namespace = webRequest.controllerNamespace
        if (namespace) {
            view = (AbstractUrlBasedView)viewResolver.resolveView("/${namespace}${viewUri}", request, response)
        }

        if (view == null) {
            view = (AbstractUrlBasedView)viewResolver.resolveView(viewUri, request, response)
        }

        if (view == null) {
            if (proxyHandler != null) {
                object = (T)proxyHandler.unwrapIfProxy(object)
            }

            def cls = object.getClass()
            // Try resolve template. Example /book/_book
            view = (AbstractUrlBasedView)viewResolver.resolveView(cls, request, response)
        }

        String resolvedViewName
        if (view) {
            def url = view.url
            resolvedViewName = url.take(url.lastIndexOf('.'))
        } else {
            resolvedViewName = OBJECT_TEMPLATE_NAME
        }

        Map<String, Object> model
        if (object instanceof Map) {
            def map = (Map) object
            model = map
            if (resolvedViewName == OBJECT_TEMPLATE_NAME) {
                // avoid stack overflow by making a copy of the map
                model.put(MODEL_OBJECT, new LinkedHashMap(map))
            }
        } else {
            model = [(resolveModelVariableName(object)): object]
            if (resolvedViewName == OBJECT_TEMPLATE_NAME) {
                model.put(MODEL_OBJECT, object)
            }
        }
        if (arguments?.model) {
            model.putAll((Map)arguments.model)
        }
        applyModel(context, model)

        ModelAndView modelAndView = (ModelAndView) request.getAttribute(GrailsApplicationAttributes.MODEL_AND_VIEW)
        if (view) {
            modelAndView.setView(view)
        } else {
            context.setViewName(resolvedViewName)
        }
    }

}
