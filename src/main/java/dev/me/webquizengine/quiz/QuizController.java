package dev.me.webquizengine.quiz;

import dev.me.webquizengine.quizresult.QuizResult;
import dev.me.webquizengine.solvedquiz.SolvedQuizDTO;
import dev.me.webquizengine.solvedquiz.SolvedQuizService;
import dev.me.webquizengine.validation.exceptions.QuizDoesNotExistsException;
import dev.me.webquizengine.validation.exceptions.UnauthorizedAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api")
@Validated
public class QuizController {

    private final QuizService quizService;

    private final SolvedQuizService solvedQuizService;

    @Autowired
    public QuizController(QuizService quizService, SolvedQuizService solvedQuizService) {
        this.quizService = quizService;
        this.solvedQuizService = solvedQuizService;
    }

    @GetMapping("/quiz")
    public QuizDTO getQuiz() {
        return quizService.getQuiz();
    }

    @PostMapping("/quiz")
    public QuizResult getQuizResult(@RequestParam(name = "answer") int indexOfList) {
        return quizService.getQuizResult(indexOfList);
    }

    @GetMapping("/quizzes/{id}")
    public QuizDTO getQuizById(@PathVariable Long id) {
        return quizService.getQuizDTOById(id);
    }

    @GetMapping("/quizzes")
    public Page<QuizDTO> getQuizzes(@RequestParam int page) {
        return quizService.getQuizzes(page);
    }

    @PostMapping("/quizzes")
    public QuizDTO createQuiz(@Valid @RequestBody Quiz newQuiz, Principal principal) {
        return quizService.createQuiz(newQuiz, principal.getName());
    }

    @PostMapping("/quizzes/{id}/solve")
    public QuizResult solveQuizById(@RequestBody QuizDTO quiz, @PathVariable String id) throws QuizDoesNotExistsException {
        return quizService.solveQuizById(quiz, id);
    }

    @DeleteMapping("/quizzes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuiz(@PathVariable String id, Principal principal) throws QuizDoesNotExistsException, UnauthorizedAccessException {
        quizService.deleteQuiz(id, principal);
    }

    @GetMapping("/quizzes/completed")
    public Page<SolvedQuizDTO> getSolvedQuizzes(Principal principal , @RequestParam int page){
        return solvedQuizService.getSolvedQuizzes(principal.getName() ,page);
    }
}

