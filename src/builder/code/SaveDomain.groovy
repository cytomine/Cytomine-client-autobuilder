package builder.code

import builder.template.AbstractClassTemplate
import builder.template.CodeTemplate
import builder.template.MethodTemplate

/**
 * Created by lrollus on 4/9/14.
 */
class SaveDomain {

//    {
//
//        if(getId()!=null && getId()>0) {
//            edit(server);
//        } else {
//            add(server);
//        }
//
//
//    }


    static String pattern = """
#CHECK_ADD_OR_EDIT#
    """

    public static Map createTemplate() {

        def code = [:]
        code["JAVA"] = createTemplateJava()
        return code
    }

    public static String createTemplateJava() {

        String template = pattern

        template = template.replace("#CHECK_ADD_OR_EDIT#","""
        if(getId()!=null && getId()>0) {
            edit(server);
        } else {
            add(server);
        }""")

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