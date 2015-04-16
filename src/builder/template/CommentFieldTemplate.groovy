package builder.template

/**
 * Created by lrollus on 3/26/14.
 */
class CommentFieldTemplate {

    String description

    static CommentFieldTemplate build(String description) {
        return new CommentFieldTemplate(description:description)
    }
}
