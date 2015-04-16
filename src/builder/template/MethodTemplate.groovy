package builder.template

import builder.Generator
import builder.StringUtils

/**
 * Created by lrollus on 3/26/14.
 */
class MethodTemplate {

    String name

    List<ParameterTemplate> queryParameters = []

    List<ParameterTemplate> pathParameters = []

    List<ParameterTemplate> methodParameters = []

    TypeTemplate type

    CodeTemplate code

    boolean isPublic

    boolean isStatic

    boolean isAbstract

    String path

    String verb

    public List<ParameterTemplate> getAllParameters() {
        List<ParameterTemplate> all = []
        all.addAll(methodParameters)
        all.addAll(pathParameters)
        all.addAll(queryParameters)
        return all
    }


    static MethodTemplate build(def jsonApiMethodObject) {

        MethodTemplate template = new MethodTemplate()

        template.name = createClientMethodName(jsonApiMethodObject.methodName,jsonApiMethodObject.response.object)

        jsonApiMethodObject.pathparameters.each {
            template.pathParameters << ParameterTemplate.build(it)
        }

        jsonApiMethodObject.queryparameters.each {
            template.queryParameters << ParameterTemplate.build(it)
        }

        template.path = jsonApiMethodObject.path
        template.verb = jsonApiMethodObject.verb

        template.type = TypeTemplate.build(StringUtils.docObjectNameToClassName(jsonApiMethodObject.response.object),true)

        template.isPublic = true
        template.isStatic = true

        template
    }

    static MethodTemplate buildCRUD(def jsonApiMethodObject, AbstractClassTemplate classTemplate) {

        def template = build(jsonApiMethodObject)

        if(jsonApiMethodObject.methodName.equals("show")) {
            template.isPublic = true
            template.isStatic = true
            template.methodParameters << ParameterTemplate.build("server",TypeTemplate.build("Server",true))
            template.code = CodeTemplate.build(CodeType.GET,classTemplate,template)
        }
        if(jsonApiMethodObject.methodName.equals("add")) {
            template.type = TypeTemplate.build("void")
            template.isPublic = true
            template.isStatic = false
            template.methodParameters << ParameterTemplate.build("server",TypeTemplate.build("Server",true))
            template.code = CodeTemplate.build(CodeType.ADD,classTemplate,template)
            template.pathParameters = []
            template.queryParameters = []
        }
        if(jsonApiMethodObject.methodName.equals("update")) {
            template.type = TypeTemplate.build("void")
            template.isPublic = true
            template.isStatic = false
            template.methodParameters << ParameterTemplate.build("server",TypeTemplate.build("Server",true))
            template.code = CodeTemplate.build(CodeType.EDIT,classTemplate,template)
            template.pathParameters = []
            template.queryParameters = []
        }
        if(jsonApiMethodObject.methodName.equals("delete")) {
            template.type = TypeTemplate.build("void")
            template.isPublic = true
            template.isStatic = false
            template.methodParameters << ParameterTemplate.build("server",TypeTemplate.build("Server",true))
            template.code = CodeTemplate.build(CodeType.DELETE,classTemplate,template)
            template.pathParameters = []
            template.queryParameters = []
        }
        template
    }

    //buildNotImplemened
    static MethodTemplate buildNotImplemened(def jsonApiMethodObject, AbstractClassTemplate classTemplate) {

        def template = build(jsonApiMethodObject)
        template.isStatic = true
        template.isPublic = true
        template.methodParameters << ParameterTemplate.build("server",TypeTemplate.build("Server",true))
        template.code = CodeTemplate.build(CodeType.NOT_IMPLEMENTED,classTemplate,template)
        template
    }

    static List<MethodTemplate> buildLists(def jsonApiMethodObject, AbstractClassTemplate classTemplate) {

        def templates = []
        templates << build(jsonApiMethodObject) //without max/offset
        templates << build(jsonApiMethodObject) //with max/offset

            templates[0].queryParameters = templates[0].queryParameters.findAll{it.name!="max" && it.name!="offset"}
            templates.each { template ->
                template.type = TypeTemplate.build("List")
                template.isPublic = true
                template.isStatic = true
                template.methodParameters << ParameterTemplate.build("server",TypeTemplate.build("Server",true))
                template.code = CodeTemplate.build(CodeType.LIST,classTemplate,template)
            }

        templates
    }


    static MethodTemplate build(String name,List<ParameterTemplate> methodParameters,List<ParameterTemplate> pathParameters,List<ParameterTemplate> queryParameters,TypeTemplate type,CodeTemplate code,boolean isPublic,boolean isStatic) {
        return new MethodTemplate(name:name,methodParameters:methodParameters,pathParameters:pathParameters,queryParameters:queryParameters,type:type,code:code,isPublic:isPublic,isStatic:isStatic)
    }

    static MethodTemplate buildGetter(FieldTemplate fieldTemplate) {
        MethodTemplate methodTemplate = new MethodTemplate()

    }



    static String createClientMethodName(String methodName, String returnType) {
        if(methodName.equals("show")) {
            return "get" //+StringUtils.docObjectNameToClassName(returnType)
        } else if(methodName.equals("update")) {
            return "edit" //+StringUtils.docObjectNameToClassName(returnType)
        }else if(methodName.equals("add")) {
            return "add" //+StringUtils.docObjectNameToClassName(returnType)
        }else if(methodName.equals("delete")) {
            return "delete" //+StringUtils.docObjectNameToClassName(returnType)
        }else if(methodName.startsWith("list")) {
            return methodName //+StringUtils.docObjectNameToClassName(returnType)
        }
        return methodName
//        throw new Exception(methodName + " Not yet support");
    }

}
