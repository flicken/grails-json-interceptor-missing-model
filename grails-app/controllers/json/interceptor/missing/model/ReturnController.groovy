package json.interceptor.missing.model

import grails.artefact.Controller
import grails.artefact.controller.RestResponder

import static json.interceptor.missing.model.DomainObject.EXAMPLE_LIST
import static json.interceptor.missing.model.DomainObject.EXAMPLE_MAP

class ReturnController implements Controller, RestResponder {

    def show(String id) {
        if (id == 'map') {
            return EXAMPLE_MAP()
        } else if (id == 'list') {
            return EXAMPLE_LIST()
        } else if (id == 'empty') {
            return []
        } else {
            return [id, id, id]
        }
    }
}

