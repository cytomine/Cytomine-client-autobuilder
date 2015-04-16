package builder.code

import builder.template.AbstractClassTemplate
import builder.template.MethodTemplate

/**
 * Created by lrollus on 4/9/14.
 */
class BuildFromParamsDomain {

    static String pattern = """
#PARAMS_TO_FIELD#
    """

    public static Map createTemplate(MethodTemplate methodTemplate) {

       def code = [:]
        code["JAVA"] = createTemplateJava(methodTemplate)
        return code
    }

    public static String createTemplateJava(MethodTemplate methodTemplate) {

        String template = pattern

        def parameters = methodTemplate.methodParameters

        String initCode = ""
        parameters.each { param ->
            initCode = initCode + "\t\tthis.${param.name}=${param.name};\n"
        }

        template = template.replace("#PARAMS_TO_FIELD#",initCode)
        return template;

    }
}