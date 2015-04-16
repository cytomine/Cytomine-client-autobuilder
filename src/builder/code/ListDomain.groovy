package builder.code

import builder.template.AbstractClassTemplate
import builder.template.CodeTemplate
import builder.template.MethodTemplate

/**
 * Created by lrollus on 4/9/14.
 */
class ListDomain {

    static String pattern = """
#GET_PATH#
#REPLACE_PARAMETERS#
#CALL_REQUEST#
#INIT_LIST#
#JSONARRAY_TO_OBJECTLIST#
#RETURN_LIST#
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

            String replaceParam = CodeTemplate.replaceParametersInPathJava(methodTemplate,false)
            template = template.replace("#REPLACE_PARAMETERS#",replaceParam)

            template = template.replace("#CALL_REQUEST#",'\t\tJSONArray json = server.doGETList(path);')

            template = template.replace("#INIT_LIST#","\t\tList<${abstractClassTemplate.domainName}> domains = new ArrayList<${abstractClassTemplate.domainName}>();")

            String listInit = "\t\tfor(int i=0;i<json.size();i++) {\n"
            listInit = listInit + "\t\t\t"+abstractClassTemplate.domainName+" domain = new "+abstractClassTemplate.domainName + "();\n"
            listInit = listInit+"\t\t\tdomain.build((JSONObject)json.get(i));\n";
            listInit = listInit + "\t\t\t domains.add(domain);\n"
            listInit = listInit + "\t\t }"
            template = template.replace("#JSONARRAY_TO_OBJECTLIST#",listInit)

        template = template.replace("#JSONARRAY_TO_OBJECTLIST#",listInit)


            template = template.replace("#RETURN_LIST#","\t\treturn domains;\n")

        println "template=$template"

        template

    }
//    JSONArray json = server.doGETList(path);
//    List<Project> domains = new ArrayList<Project>();
//    for(int i=0;i<json.size();i++) {
//        Project domain = new Project();
//        domain.build((JSONObject)json.get(i));
//        domains.add(domain);
//    }
//    return domains;
}