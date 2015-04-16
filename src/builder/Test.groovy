package builder

import builder.template.AbstractClassTemplate
import builder.template.FieldTemplate
import builder.template.TypeTemplate
import groovy.json.JsonSlurper

/**
 * Created by lrollus on 3/26/14.
 */
class Test {

    def json

    public static void main(String[] args) {

        Test test = new Test()
        test.json = new JsonSlurper().parseText(new File("testdoc.json").text)
        test.runTest()


    }


    public void runTest() {
        testTypeTemplate()

        testFieldTemplate()


        testClassTemplate()
    }


    void testTypeTemplate() {

        assert TypeTemplate.build(json.objects[0].fields[0].type).type.equals("String")

        assert TypeTemplate.build("string").type.equals("String")

        assert TypeTemplate.build("Int").type.equals("Integer")

        try {
            TypeTemplate.build("toto")
            assert false
        } catch(Exception e) {

        }

    }

    void testFieldTemplate() {

//        "jsondocId": "dc120ea6-5424-4a45-8ea1-9bca73b4ed29",
//        "description": "The full class name of the domain",
//        "presentInResponse": true,
//        "mandatory": false,
//        "name": "class",
//        "allowedvalues": null,
//        "useForCreation": false,
//        "format": null,
//        "defaultValue": null,
//        "type": "string",
//        "multiple": "false"

        FieldTemplate template = FieldTemplate.build(json.objects[0].fields[0])

        assert template.name == "class"
        assert template.type.type == "String"
        assert !template.useInConstructor
        assert !template.useInSetter
        assert template.useInGetter
    }


    void testClassTemplate() {

        AbstractClassTemplate template = AbstractClassTemplate.buildFromDoc(json.objects[0])

        assert template.name == "Project"
        assert template.abstractName == "AbstractProject"
        assert template.fields.first().name == "class"

        assert AbstractClassTemplate.docObjectNameToClassName("image instance").equals("ImageInstance")

    }
}
