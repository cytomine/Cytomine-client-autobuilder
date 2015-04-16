package builder.code

import builder.template.AbstractClassTemplate
import builder.template.CodeTemplate
import builder.template.MethodTemplate

/**
 * Created by lrollus on 4/9/14.
 */
class AddDomain {


    static String pattern = """
#GET_PATH#
#REPLACE_PARAMETERS#
#CALL_REQUEST#
#FILL_OBJECT#
    """

    public static Map createTemplate(AbstractClassTemplate abstractClassTemplate,MethodTemplate methodTemplate) {

        def code = [:]
        code["JAVA"] = createTemplateJava(abstractClassTemplate,methodTemplate)
        return code
    }

    public static String createTemplateJava(AbstractClassTemplate abstractClassTemplate,MethodTemplate methodTemplate) {

        String template = pattern

        String path = "\t\tString path = \"${methodTemplate.path}?\";"
        template = template.replace("#GET_PATH#",path)

        String replaceParam = CodeTemplate.replaceParametersInPathJava(methodTemplate)
        template = template.replace("#REPLACE_PARAMETERS#",replaceParam)

        template = template.replace("#CALL_REQUEST#",'\t\tJSONObject json = server.doPOST(path,this.toJSON());\n')

        String builder = "\t\tthis.build((JSONObject)json.get(\"${abstractClassTemplate.domainName.toLowerCase()}\"));\n"

        template = template.replace("#FILL_OBJECT#",builder)

        println "template=$template"

        template

    }



//    public void add(Server server)
//            throws Exception
//    {
//
//        String path = "/api/project.json?";
//        //replace parameters
//
//        JSONObject json = server.doPOST(path,super.toJSON());
//        System.out.println(json);
//        this.build((JSONObject)json.get("project"));
//
//    }

}