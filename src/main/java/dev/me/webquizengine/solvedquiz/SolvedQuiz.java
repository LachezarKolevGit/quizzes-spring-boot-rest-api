package dev.me.webquizengine.solvedquiz;

import dev.me.webquizengine.quiz.Quiz;
import dev.me.webquizengine.user.User;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class SolvedQuiz  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime completedAt = LocalDateTime.now();

    public SolvedQuiz() {
    }

    public SolvedQuiz(User user, Quiz quiz, LocalDateTime completedAt) {
        this.user = user;
        this.quiz = quiz;
        this.completedAt = completedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public LocalDateTime getLocalDateTime() {
        return completedAt;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.completedAt = localDateTime;
    }

}
