package json.interceptor.missing.model

import groovy.transform.ToString

@ToString(includePackage = false, includeNames = true)
class DomainObject {
    String id
    Integer value

    static Map<String, DomainObject> EXAMPLE_MAP() {
        [
                one: new DomainObject(id: 'id', value: 1),
                two: new DomainObject(id: 'id2', value: 2),
        ]
    }

    static List<DomainObject> EXAMPLE_LIST() {
        EXAMPLE_MAP().values().toList()
    }
}
