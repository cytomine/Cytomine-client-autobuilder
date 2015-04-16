package builder.template

import builder.JavaCodeBuilder
import builder.code.AddDomain
import builder.code.BuildFromJSONDomain
import builder.code.BuildFromParamsDomain
import builder.code.DeleteDomain
import builder.code.EditDomain
import builder.code.GetDomain
import builder.code.ListDomain
import builder.code.NotImplementetDomain
import builder.code.SaveDomain
import builder.code.ToJSONDomain

/**
 * Created by lrollus on 3/26/14.
 */
class CodeTemplate {

    Map<String,String> code = [:]

    public static build(CodeType type, AbstractClassTemplate abstractClassTemplate,MethodTemplate methodTemplate) {
        CodeTemplate codeTemplate = new CodeTemplate()
        if(type==CodeType.GET) {
            codeTemplate.code = GetDomain.createTemplate(methodTemplate)
        }
        if(type==CodeType.LIST) {
            codeTemplate.code = ListDomain.createTemplate(abstractClassTemplate,methodTemplate)
        }
        if(type==CodeType.ADD) {
            codeTemplate.code = AddDomain.createTemplate(abstractClassTemplate,methodTemplate)
        }
        if(type==CodeType.EDIT) {
            codeTemplate.code = EditDomain.createTemplate(abstractClassTemplate,methodTemplate)
        }
        if(type==CodeType.DELETE) {
            codeTemplate.code = DeleteDomain.createTemplate(methodTemplate)
        }
        if(type==CodeType.NOT_IMPLEMENTED) {
            codeTemplate.code = NotImplementetDomain.createTemplate()
        }
        if(type==CodeType.SAVE) {
            codeTemplate.code = SaveDomain.createTemplate()
        }
        if(type==CodeType.BUILD_FROM_JSON) {
            codeTemplate.code = BuildFromJSONDomain.createTemplate(abstractClassTemplate)
        }
        if(type==CodeType.BUILD_FROM_PARAMS) {
            codeTemplate.code = BuildFromParamsDomain.createTemplate(methodTemplate)
        }
        if(type==CodeType.TO_JSON) {
            codeTemplate.code = ToJSONDomain.createTemplate(abstractClassTemplate)
        }
        return codeTemplate
    }

    public static String replaceParametersInPathJava(MethodTemplate methodTemplate, boolean useGetter = true) {
        String replaceParam = ""
        methodTemplate.pathParameters.each { param ->
            String field = param.name
            if(useGetter) {
                field = JavaCodeBuilder.getGetterName(param.name) + "()"
            }

            replaceParam = replaceParam + "\t\tpath = path.replace(\"{${param.name}}\",$field+\"\");\n"
        }

        methodTemplate.queryParameters.each { param ->
            String field = param.name
            if(useGetter) {
                field = JavaCodeBuilder.getGetterName(param.name) + "()"
            }
            replaceParam = replaceParam + "\t\tpath = path + \"&${param.name}=\" + $field;\n"
        }
        replaceParam
    }
}

public enum CodeType {
    GET, BUILD_FROM_JSON, BUILD_FROM_PARAMS, TO_JSON, ADD, EDIT,DELETE, SAVE, LIST, NOT_IMPLEMENTED
}