package builder.template

import builder.Generator
import builder.StringUtils

/**
 * Created by lrollus on 3/26/14.
 */
class SubClassTemplate {

    boolean isAbstract = false

    static String fullPackage = "be.cytomine.client"

    String name

    List<FieldTemplate> fields

    List<MethodTemplate> methods

    CommentClassTemplate comment

    String parentClassName

    static SubClassTemplate build(def jsonApiDocObject) {

        SubClassTemplate template = new SubClassTemplate()

        template.name = StringUtils.docObjectNameToClassName(jsonApiDocObject.name)

        template.comment = CommentClassTemplate.build(jsonApiDocObject.description, "ClientBuilder (Loïc Rollus)", Generator.version)

        template
    }

    static SubClassTemplate build(String name, String description) {

        SubClassTemplate template = new SubClassTemplate()

        template.name = StringUtils.docObjectNameToClassName(name)

        template.comment = CommentClassTemplate.build(description, "ClientBuilder (Loïc Rollus)", Generator.version)

        template
    }
}
