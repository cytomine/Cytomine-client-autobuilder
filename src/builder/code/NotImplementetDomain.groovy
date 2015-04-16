package builder.code

import builder.template.AbstractClassTemplate
import builder.template.CodeTemplate
import builder.template.MethodTemplate

/**
 * Created by lrollus on 4/9/14.
 */
class NotImplementetDomain {


    static String pattern = """
#THROW_ERROR#
    """

    public static Map createTemplate() {

        def code = [:]
        code["JAVA"] = createTemplateJava()
        return code
    }

    public static String createTemplateJava() {
        String template = pattern
        template = template.replace("#THROW_ERROR#",'\t\tthrow new Exception("Not yet implemented");')
        template
    }
}