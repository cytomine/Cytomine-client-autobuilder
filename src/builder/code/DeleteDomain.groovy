package builder.code

import builder.template.CodeTemplate
import builder.template.MethodTemplate

/**
 * Created by lrollus on 4/9/14.
 */
class DeleteDomain {

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

            String replaceParam = CodeTemplate.replaceParametersInPathJava(methodTemplate)
            template = template.replace("#REPLACE_PARAMETERS#",replaceParam)

            template = template.replace("#CALL_REQUEST#",'\t\tserver.doDELETE(path);')

            String builder = "\t\tbuild(new JSONObject());\n";

            template = template.replace("#RETURN_OBJECT#",builder)

        println "template=$template"

        template

    }

//    public void delete(Server server)
//            throws Exception
//    {
//
//        String path = "/api/project/{id}.json";
//        path = path.replace("{id}",getId()+"");
//        System.out.println(path);
//        server.doDELETE(path);
//
//        this.build(new JSONObject());
//
//
//    }

}