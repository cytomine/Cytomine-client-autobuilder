package builder.template

import builder.Generator
import builder.StringUtils

/**
 * Created by lrollus on 3/26/14.
 */
class AbstractClassTemplate {

    boolean isAbstract = true

    String fullPackage = "be.cytomine.client.abst"

    String name

    List<FieldTemplate> fields

    List<MethodTemplate> methods = []

    CommentClassTemplate comment

    String domainName

    static String format(String name) {
        return name.replace("[","").replace("]","")
    }

    static AbstractClassTemplate buildFromDoc(def jsonApiDocObject) {

        AbstractClassTemplate template = new AbstractClassTemplate()

        template.domainName = StringUtils.docObjectNameToClassName(jsonApiDocObject.name)

        template.name = "Abstract"+format(template.domainName)

        List<FieldTemplate> fields = new ArrayList<FieldTemplate>()
        jsonApiDocObject.fields.each {
            fields.add(FieldTemplate.build(it))
        }
        template.fields = fields

        template.comment = CommentClassTemplate.build(jsonApiDocObject.description, "ClientBuilder (Loïc Rollus)", Generator.version)

        template.methods << createBuildFromParamsMethod(template)

        template.methods << createBuildFromJSONMethod(template)

        template.methods << createToJSONMethod(template)

        template
    }

    static AbstractClassTemplate build(String fullPackage, String name,List<FieldTemplate> fields,List<MethodTemplate> methods,CommentClassTemplate comment) {
        new AbstractClassTemplate([fullPackage:fullPackage,name:name,fields:fields,methods:methods,comment:comment])
    }

    private static MethodTemplate createBuildFromJSONMethod(AbstractClassTemplate template) {
        CodeTemplate builderCode = CodeTemplate.build(CodeType.BUILD_FROM_JSON, template, null)
        MethodTemplate method = MethodTemplate.build("build", [ParameterTemplate.build("json", "JSONObject")], [], [], TypeTemplate.build("void", true), builderCode, true, false)
        method
    }

    private static MethodTemplate createBuildFromParamsMethod(AbstractClassTemplate template) {

        List<FieldTemplate> fieldsMandatoryForCreation = template.fields.findAll{it.useInConstructor}

        List<ParameterTemplate> parameters = fieldsMandatoryForCreation.collect{ParameterTemplate.build(it.name,it.type)}

        MethodTemplate method = MethodTemplate.build("build", parameters, [], [], TypeTemplate.build("void", true), null, true, false)

        method.code = CodeTemplate.build(CodeType.BUILD_FROM_PARAMS, null,method)

        method
    }

    private static MethodTemplate createToJSONMethod(AbstractClassTemplate template) {
        CodeTemplate builderCode = CodeTemplate.build(CodeType.TO_JSON, template, null)
        MethodTemplate method = MethodTemplate.build("toJSON", [], [], [], TypeTemplate.build("JSONObject", true), builderCode, true, false)
        method
    }


}
