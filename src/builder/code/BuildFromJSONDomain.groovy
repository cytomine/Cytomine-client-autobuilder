package builder.code

import builder.JavaCodeBuilder
import builder.template.AbstractClassTemplate
import builder.template.FieldTemplate
import builder.template.MethodTemplate

/**
 * Created by lrollus on 4/9/14.
 */
class BuildFromJSONDomain {

    def changeFields = ["clazz"]

    static String pattern = """
#JSON_TO_FIELD#
    """

    public static Map createTemplate(AbstractClassTemplate abstractClassTemplate) {

       def code = [:]
        code["JAVA"] = createTemplateJava(abstractClassTemplate)
        return code
    }

    public static String createTemplateJava(AbstractClassTemplate abstractClassTemplate) {
        String template = pattern

        String jsonToField = ""

        def fields = abstractClassTemplate.fields.findAll{true}

        fields.each { field ->
            jsonToField = jsonToField + "\t\tthis.${field.name} =JSONUtils.extractJSON${field.type.type}(json.get(\"${FieldTemplate.cleanName(field.name,true)}\"));\n"
        }

        template = template.replace("#JSON_TO_FIELD#",jsonToField)

        template
    }
}