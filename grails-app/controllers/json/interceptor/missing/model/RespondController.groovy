package json.interceptor.missing.model

import grails.artefact.Controller
import grails.artefact.controller.RestResponder

import static json.interceptor.missing.model.DomainObject.EXAMPLE_MAP
import static json.interceptor.missing.model.DomainObject.EXAMPLE_LIST

class RespondController implements Controller, RestResponder {

    def show(String id) {
        if (id == 'map') {
            respond(EXAMPLE_MAP())
        } else if (id == 'list') {
            respond(EXAMPLE_LIST())
        } else if (id == 'empty') {
            respond([])
        } else {
            respond([id, id, id])
        }
    }
}

