package dev.me.webquizengine.quiz;

import dev.me.webquizengine.solvedquiz.SolvedQuiz;
import dev.me.webquizengine.validation.NotNullAndMinSizeConstraint;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String author;

    @NotBlank(message = "Title of message can't be null or blank")
    private String title;

    @NotBlank(message = "Quiz can't be formed without a question")
    private String text;

    @ElementCollection()
    @CollectionTable(name = "Options", joinColumns = @JoinColumn(name = "quiz_id"))
    @Column(name =  "options")
    @JoinColumn(name = "quiz_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Cascade(value={org.hibernate.annotations.CascadeType.ALL})
    @NotNullAndMinSizeConstraint(message = "Options can't be null and the minimum options must be at least 2")
    private List<@NotBlank String> options;

    @Nullable
    @ElementCollection
    @CollectionTable(name = "Answer", joinColumns = @JoinColumn(name = "quiz_id"))
    @Column(name = "answer")
    @JoinColumn(name = "quiz_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Cascade(value={org.hibernate.annotations.CascadeType.ALL})
    private Set<Integer> answer = new HashSet<>();

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private Set<SolvedQuiz> solvedByUsers;

    protected Quiz() {
    }

    public Quiz(String title, String text, List<String> options) {
        this.title = title;
        this.text = text;
        this.options = options;
    }

    public Quiz(String title, String text,  List<String> options, Set<Integer> answers) {
        this.title = title;
        this.text = text;
        this.options = options;
        if (answers == null) {
            this.answer = new HashSet<>();
        }
        else{
            this.answer = answers;
        }
    }

    public Set<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(Set<Integer> answer) {
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void deleteAnswersAndOptions(){
        this.options.clear();
        if (answer !=null && !answer.isEmpty()){
            this.answer.clear();
        }
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", options=" + options +
                ", answers=" + answer +
                ", solvedByUsers=" + solvedByUsers +
                '}';
    }
}
