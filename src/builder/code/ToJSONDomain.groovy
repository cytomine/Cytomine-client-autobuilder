package builder.code

import builder.template.AbstractClassTemplate
import builder.template.FieldTemplate

/**
 * Created by lrollus on 4/9/14.
 */
class ToJSONDomain {

    static String pattern = """
#INST_JSON#
#FIELD_TO_JSON#
#RETURN_JSON#
    """

    public static Map createTemplate(AbstractClassTemplate abstractClassTemplate) {

       def code = [:]
        code["JAVA"] = createTemplateJava(abstractClassTemplate)
        return code
    }

    public static String createTemplateJava(AbstractClassTemplate abstractClassTemplate) {
        String template = pattern

        String fieldToJSON = ""

        template = template.replace("#INST_JSON#","\t\tJSONObject json=new JSONObject();")

        //only get fields with getter (presentInResponse)?????????
        def fields = abstractClassTemplate.fields.findAll{it.useInGetter}// it.useInGetter}

        fields.each { field ->
            fieldToJSON = fieldToJSON + "\t\tjson.put(\"${FieldTemplate.cleanName(field.name,true)}\",JSONUtils.formatJSON(this.${field.name}));\n"

            //jsonToField = jsonToField + "\t\tthis.${field.name} =JSONUtils.extractJSON${field.type.type}(json.get(\"${field.name}\"));\n"
        }

        template = template.replace("#FIELD_TO_JSON#",fieldToJSON)

        template = template.replace("#RETURN_JSON#","\t\treturn json;")

        template
    }
}