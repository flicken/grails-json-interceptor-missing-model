package json.interceptor.missing.model

import grails.artefact.Controller
import grails.artefact.controller.RestResponder

class RespondController implements Controller, RestResponder {

    def show(String id) {
        if (id == 'map') {
            respond([
                    one: new DomainObject(id: 'id', value: 1),
                    two: new DomainObject(id: 'id2', value: 2),
            ])
        } else if (id == 'list') {
            respond([
                    new DomainObject(id: "id", value: 1),
                    new DomainObject(id: "id2",value: 2),
            ])
        } else if (id == 'empty') {
            respond([])
        } else {
            respond([id, id, id])
        }
    }
}

