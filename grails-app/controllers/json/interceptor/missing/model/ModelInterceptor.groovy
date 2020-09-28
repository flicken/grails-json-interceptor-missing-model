package json.interceptor.missing.model

class ModelInterceptor {

    Object latestModel

    ModelInterceptor() {
        matchAll()
    }

    boolean before() {
        true
    }

    boolean after() {
        if (model) {
            println "Got model: $model"
        } else {
            println "The model is empty!"
        }
        latestModel = model
        true
    }
}
