package json.interceptor.missing.model

import grails.artefact.Controller
import grails.artefact.controller.RestResponder
import org.springframework.web.servlet.ModelAndView

import static json.interceptor.missing.model.DomainObject.EXAMPLE_LIST
import static json.interceptor.missing.model.DomainObject.EXAMPLE_MAP
import static json.interceptor.missing.model.DomainObject.EXAMPLE_MAP
import static json.interceptor.missing.model.DomainObject.EXAMPLE_LIST

class ModelAndViewController implements Controller, RestResponder {

    def show(String id) {
        if (id == 'map') {
            new ModelAndView('/object/_object', [
                    object: EXAMPLE_MAP()
            ])
        } else if (id == 'list') {
            new ModelAndView('/object/_object', [
                    object: EXAMPLE_LIST()])
        } else if (id == 'empty') {
            new ModelAndView('/object/_object', [
                    object: [],
                    emptyCollection: []
            ])
        } else {
            new ModelAndView('/object/_object', [
                    object: [id, id, id],
            ])
        }
    }
}

