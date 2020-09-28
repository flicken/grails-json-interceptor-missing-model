package json.interceptor.missing.model

import grails.artefact.Controller
import grails.artefact.controller.RestResponder
import org.springframework.web.servlet.ModelAndView

class ModelAndViewController implements Controller, RestResponder {

    def show(String id) {
        if (id == 'map') {
            new ModelAndView('/object/_object', [
                    object: [
                            one: new DomainObject(id: 'id', value: 1),
                            two: new DomainObject(id: 'id2', value: 2),
                    ]
            ])
        } else if (id == 'list') {
            new ModelAndView('/object/_object', [
                    object: [
                            new DomainObject(id: "id", value: 1),
                            new DomainObject(id: "id2", value: 2),
                    ]])
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

