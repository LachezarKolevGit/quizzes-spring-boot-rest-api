package dev.me.webquizengine.quiz;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.me.webquizengine.validation.NotNullAndMinSizeConstraint;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuizDTO {
    private Long id;

    @NotBlank(message = "Title of message can't be null or blank")
    private String title;

    @NotBlank(message = "Quiz can't be formed without a question")
    private String text;

    @NotNullAndMinSizeConstraint(message = "Options can't be null and the minimum options must be at least 2")
    private List<@NotBlank String> options;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private  Set<Integer> answer;

    public QuizDTO() {
    }

    public QuizDTO(String title, String text, List<String> options, Set<Integer> answers) { //used for uploading a Quiz
        this.title = title;
        this.text = text;
        this.options = options;
        if (answers == null) {
            this.answer = new HashSet<>();
        } else {
            this.answer = answers;
        }
    }

    public QuizDTO(Long id, String title, String text, List<String> options) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.options = options;
    }

    public QuizDTO(Quiz quiz) {
        this.id = quiz.getId();
        this.title = quiz.getAuthor();
        this.text = quiz.getText();
        this.options = quiz.getOptions();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }

    public Set<Integer> getAnswer() {
        return answer;
    }
}
