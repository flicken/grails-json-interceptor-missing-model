package json.interceptor.missing.model

import grails.testing.mixin.integration.Integration
import io.micronaut.http.client.HttpClient

@Integration
class ModelInterceptorWithWorkaroundIntSpec extends ModelInterceptorIntSpec {
    HttpClient httpClient

    void setupSpec() {
        System.setProperty('WORKAROUND', 'true')
    }

    void cleanupSpec() {
        System.properties.remove('WORKAROUND')
    }
}
