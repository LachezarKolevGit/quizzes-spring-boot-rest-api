package dev.me.webquizengine.solvedquiz;

import java.time.LocalDateTime;

public class SolvedQuizDTO {

    private Long id;

    private LocalDateTime completedAt;

    public SolvedQuizDTO(Long id, LocalDateTime completedAt) {
        this.id = id;
        this.completedAt = completedAt;
    }

    public SolvedQuizDTO(SolvedQuiz solvedQuiz) {
       this.id = solvedQuiz.getQuiz().getId();
        this.completedAt = solvedQuiz.getLocalDateTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
