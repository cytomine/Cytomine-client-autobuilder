package builder.template

/**
 * Created by lrollus on 3/26/14.
 */
class FieldTemplate {

    String name

    TypeTemplate type

    boolean useInConstructor

    boolean useInGetter

    boolean useInSetter

    CommentFieldTemplate comment


    static FieldTemplate build(def jsonApiDocField) {

        FieldTemplate template = new FieldTemplate()
        template.name = cleanName(jsonApiDocField.name)

        template.type = TypeTemplate.build(jsonApiDocField.type)

        template.useInConstructor = jsonApiDocField.useForCreation && jsonApiDocField.mandatory

        template.useInGetter = jsonApiDocField.presentInResponse

        template.useInSetter = jsonApiDocField.useForCreation

        template.comment = CommentFieldTemplate.build(jsonApiDocField.description)


        template

    }



    static String cleanName(String name, boolean inverse = false) {
        def cleanMap = [
                "class" : "clazz"
        ]

        String cleanName = name
        if(!inverse && cleanMap.containsKey(name)) {
            cleanName = cleanMap.get(name)
        } else {
            cleanMap.each {
                if(name.equals(it.value)) {
                    cleanName = it.key
                }
            }
        }
        return cleanName

    }

}
