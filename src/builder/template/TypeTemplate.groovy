package builder.template

import builder.Generator

/**
 * Created by lrollus on 3/26/14.
 */
class TypeTemplate {

    String type



    public static TypeTemplate build(def jsonApiDocType, boolean isDomain = false) {

        println "TypeTemplate.buildFromDoc arg=${jsonApiDocType}"

        TypeTemplate template = new TypeTemplate()

        def type = firstCharUpper(jsonApiDocType)

        if(Generator.mapType.containsKey(type)) {
            type = Generator.mapType.get(type)
        }

        if(!isDomain && !Generator.allowedType.contains(type)) {
            //throw new Exception("Type $type is not allowed in TypeTemplate.allowedType!")
        }
        template.type = type
        return template
    }

    public static String firstCharUpper(String word) {
        word.substring(0, 1).toUpperCase() + word.substring(1)
    }

}
