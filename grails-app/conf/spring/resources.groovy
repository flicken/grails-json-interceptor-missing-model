import grails.views.resolve.PluginAwareTemplateResolver
import json.interceptor.missing.model.WorkaroundJsonViewResolver

// Place your Spring DSL code here
beans = {
    if (System.getenv("WORKAROUND") || System.getProperty("WORKAROUND")) {
        println("Enabling WorkaroundJsonViewResolver")
        jsonSmartViewResolver(WorkaroundJsonViewResolver, jsonTemplateEngine) {
            templateResolver = bean(PluginAwareTemplateResolver, jsonViewConfiguration)
        }
    } else {
        println("Not enabling WorkaroundJsonViewResolver")
    }
}
