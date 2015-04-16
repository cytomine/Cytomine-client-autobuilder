package builder.template

/**
 * Created by lrollus on 3/26/14.
 */
class ParameterTemplate {

    String name

    TypeTemplate type

    static ParameterTemplate build(String name,TypeTemplate typeTemplate) {
        return new ParameterTemplate(name:name,type:typeTemplate)
    }

    static ParameterTemplate build(String name,String type) {
        return new ParameterTemplate(name:name,type:TypeTemplate.build(type,false))
    }

    static ParameterTemplate build(def jsonApiParameterObject) {

//        "jsondocId": "64eb9c35-874d-4bb7-98dd-3b5a6931fba3",
//        "description": "The project id",
//        "name": "id",
//        "allowedvalues": [],
//        "format": "",
//        "required": "true",
//        "type": "long"

        return new ParameterTemplate(name:jsonApiParameterObject.name,type:TypeTemplate.build(jsonApiParameterObject.type))
    }

}
