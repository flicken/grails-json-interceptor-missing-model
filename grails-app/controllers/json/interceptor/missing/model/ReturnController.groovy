package json.interceptor.missing.model

import grails.artefact.Controller
import grails.artefact.controller.RestResponder

class ReturnController implements Controller, RestResponder {

    def show(String id) {
        if (id == 'map') {
            return [
                    one: new DomainObject(id: 'id', value: 1),
                    two: new DomainObject(id: 'id2', value: 2),
            ]
        } else if (id == 'list') {
            return [
                    new DomainObject(id: 'id', value: 1),
                    new DomainObject(id: 'id2', value: 2),
            ]
        } else if (id == 'empty') {
            return []
        } else {
            return [id, id, id]
        }
    }
}

