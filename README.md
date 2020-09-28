See https://stackoverflow.com/questions/64087536/grails-json-renderer-not-exposing-model-for-interceptor/

I want to use the `model` field in an `Interceptor` for a JSON REST response.

The controller method responds with the domain object:

```groovy
def show(String id) {
    respond User.get(id)
}
```

The JSON view uses `JsonViewJsonRenderer` which extends `DefaultViewRenderer`, which directly calls

```groovy
  view.render(model, request, response)
```

and doesn't to expose a `ModelAndView` instance.  Other renderers e.g. `DefaultHtmlRenderer`, expose the model via:

```groovy
 applyModel(context, object)
```

Is there a way to get the model field to be filled out when responding with a JSON response?   Do I need to build my own subclass of `DefaultHtmlRenderer` that sets the `ModelAndView`?

As a workaround, I could create a `ModelAndView` for each response, but this seems to not be in the ease-of-use spirit of Grails:

```groovy
def show(String id) {
    respond new ModelAndView('user/show', User.get(id))
}
```
