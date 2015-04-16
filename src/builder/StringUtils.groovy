package builder

/**
 * Created by lrollus on 3/27/14.
 */
class StringUtils {

    //project => Project
    static String docObjectNameToClassName(String docObjectName) {
        docObjectName = docObjectName.replace("[","")
        docObjectName = docObjectName.replace("]","")
        String result = splitWordAndJoinUppercase(docObjectName," ")
        result = splitWordAndJoinUppercase(result,"_")
        return result
    }

    public static String splitWordAndJoinUppercase(String docObjectName, String splitter) {
        def eachWord = docObjectName.split(splitter)
        def eachWordCamelCase = eachWord.collect { word ->
            def firstChar = word.substring(0, 1).toUpperCase();
            firstChar = firstChar + word.substring(1)
            firstChar
        }
        return eachWordCamelCase.join("")
    }

    //SoftwareProject => softwareProject
    static String classNameToDefineName(String clasName) {
       def firstChar = clasName.substring(0, 1).toLowerCase()
       firstChar + clasName.substring(1)

    }
}
