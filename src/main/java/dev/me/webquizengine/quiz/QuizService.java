package dev.me.webquizengine.quiz;

import dev.me.webquizengine.quizresult.QuizResult;
import dev.me.webquizengine.solvedquiz.SolvedQuizService;
import dev.me.webquizengine.validation.exceptions.NoQuizzesUploadedException;
import dev.me.webquizengine.validation.exceptions.QuizDoesNotExistsException;
import dev.me.webquizengine.validation.exceptions.UnauthorizedAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import java.security.Principal;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Validated
public class QuizService {

    private final QuizRepository quizRepository;
    private final SolvedQuizService solvedQuizService;

    @Autowired
    public QuizService(QuizRepository quizRepository, SolvedQuizService solvedQuizService) {
        this.quizRepository = quizRepository;
        this.solvedQuizService = solvedQuizService;
    }

    private QuizDTO convertFromQuizToDTO(Quiz quiz) {
        return new QuizDTO(quiz.getId(), quiz.getTitle(), quiz.getText(), quiz.getOptions());
    }

    public QuizDTO getQuiz() throws NoQuizzesUploadedException {
        if (quizRepository.count() < 1) {
            throw new NoQuizzesUploadedException();
        }

        long limit = quizRepository.findMaxIdNumber(PageRequest.of(0, 1)).get(0);
        long generatedLong = ThreadLocalRandom.current().nextLong(limit);
        Optional<Quiz> quiz = quizRepository.findById(generatedLong);

        while (quiz.isEmpty()) {
            generatedLong = ThreadLocalRandom.current().nextLong(limit);
            quiz = quizRepository.findById(generatedLong);
        }
        return convertFromQuizToDTO(quiz.get());
    }

    public QuizResult getQuizResult(int indexOfList) {
        if (indexOfList == 2) {
            return new QuizResult(true, "Congratulations, you're right!");
        }

        return new QuizResult(false, "Wrong answer! Please, try again.");
    }

    public QuizDTO getQuizDTOById(Long id) {
        if (!quizRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found");
        }
        Quiz quiz = quizRepository.findById(id).get();

        return new QuizDTO(quiz.getId(), quiz.getTitle(), quiz.getText(), quiz.getOptions());
    }

    public Quiz getQuizById(Long id) {
        if (!quizRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found");
        }

        return quizRepository.findById(id).get();
    }

    public Page<QuizDTO> getQuizzes(int page) {
        int sizeOfPage = 10;
        PageRequest pageRequest = PageRequest.of(page, sizeOfPage);
        Page<Quiz> returnedPage = quizRepository.findAll(pageRequest);
        return returnedPage.map(quiz -> new QuizDTO(quiz));
    }

    public QuizDTO createQuiz(@Valid Quiz newQuiz, String author) {
        newQuiz.setAuthor(author);
        quizRepository.save(newQuiz);
        return new QuizDTO(newQuiz.getId(), newQuiz.getTitle(), newQuiz.getText(), newQuiz.getOptions());
    }

    public QuizResult solveQuizById(QuizDTO quiz, String id) throws QuizDoesNotExistsException {
        Long convertedId = Long.parseLong(id);

        if (!quizRepository.existsById(convertedId)) {
            throw new QuizDoesNotExistsException();  //Exception's message handled by ExceptionHandler in ErrorHandlingControllerAdvice.class
        }

        Optional<Quiz> optional = quizRepository.findById(convertedId);

        if (optional.get().getAnswer().equals(quiz.getAnswer())) {
            solvedQuizService.saveSolvedQuiz(optional.get());
            return new QuizResult(true, "Congratulations, you're right!");
        }

        return new QuizResult(false, "Wrong answer! Please, try again.");
    }

    public void deleteQuiz(String id, Principal principal) throws QuizDoesNotExistsException, UnauthorizedAccessException {
        Long convertedId = Long.valueOf(id);

        if (!quizRepository.existsById(convertedId)) {
            throw new QuizDoesNotExistsException();  // Exception's message handled by ExceptionHandler in ErrorHandlingControllerAdvice.class
        }

        if (!principal.getName().equals(getQuizById(convertedId).getAuthor())) {
            throw new UnauthorizedAccessException();
        }

        quizRepository.deleteById(convertedId);
    }
}
