package json.interceptor.missing.model


import grails.testing.mixin.integration.Integration
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification
import spock.lang.Unroll

@Integration
class ModelInterceptorIntSpec extends Specification {
    HttpClient httpClient

    @Autowired
    private ModelInterceptor modelInterceptor

    def setup() {
        httpClient = HttpClient.create("http://localhost:$serverPort".toURL())
    }

    def cleanup() {
        httpClient?.close()
    }

    @Unroll
    void "interceptor should get model from #controller/map" (String controller, String id) {
        given:
        def request = HttpRequest.GET("/$controller/$id")
                        .contentType(MediaType.APPLICATION_JSON)
        def response = httpClient.toBlocking().exchange(request, Map)

        expect:
        response.status == HttpStatus.OK
        modelInterceptor.latestModel != null

        where:
        controller     || id
        "modelAndView" || "map"
        "respond"      || "map"
        "return"       || "map"
    }
}
