package builder.template

/**
 * Created by lrollus on 3/26/14.
 */
class CommentClassTemplate {

    String description

    String author

    String version

    public static CommentClassTemplate build(String description, String author, String version) {
        new CommentClassTemplate(description:description,author:author,version:version)
    }

}
