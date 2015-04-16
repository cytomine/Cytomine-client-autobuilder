package builder.code

import builder.template.CodeTemplate
import builder.template.MethodTemplate

/**
 * Created by lrollus on 4/9/14.
 */
class GetDomain {

    static String pattern = """
#GET_PATH#
#REPLACE_PARAMETERS#
#CALL_REQUEST#
#RETURN_OBJECT#
    """

    public static Map createTemplate(MethodTemplate methodTemplate) {

       def code = [:]
        code["JAVA"] = createTemplateJava(methodTemplate)
        return code
    }

    public static String createTemplateJava(MethodTemplate methodTemplate) {

            String template = pattern

            String path = "\t\tString path = \"${methodTemplate.path}?\";"
            template = template.replace("#GET_PATH#",path)

            String replaceParam = CodeTemplate.replaceParametersInPathJava(methodTemplate,false)
            template = template.replace("#REPLACE_PARAMETERS#",replaceParam)

            template = template.replace("#CALL_REQUEST#",'\t\tJSONObject json = server.doGET(path);')

            String builder = "\t\t"+methodTemplate.type.type+" domain = new "+methodTemplate.type.type + "();\n"
             builder = builder+"\t\tdomain.build(json);\n";
             builder = builder+"\t\treturn domain;\n";

            template = template.replace("#RETURN_OBJECT#",builder)

        println "template=$template"

        template

    }

}