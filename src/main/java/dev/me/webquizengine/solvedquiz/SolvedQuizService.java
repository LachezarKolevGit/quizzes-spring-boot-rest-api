package dev.me.webquizengine.solvedquiz;


import dev.me.webquizengine.quiz.Quiz;
import dev.me.webquizengine.user.User;
import dev.me.webquizengine.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SolvedQuizService {

    private final SolvedQuizRepository repository;
    private final UserService userService;

    @Autowired
    public SolvedQuizService(SolvedQuizRepository solvedQuizRepository,  UserService userService) {
        this.repository = solvedQuizRepository;
        this.userService = userService;
    }

    public Page<SolvedQuizDTO> getSolvedQuizzes(String username, int pageIndex) {
        int size = 10;

        Page<SolvedQuiz> page = repository.findAllByUser(username,
                PageRequest.of(pageIndex, size, Sort.by(Sort.Direction.DESC, "completedAt")));

        return page.map(solvedQuiz -> new SolvedQuizDTO(solvedQuiz));
    }

    public boolean saveSolvedQuiz(Quiz quiz) {
        Optional<User> userOptional = userService.getLoggedInUser();
        if (userOptional.isEmpty()) {
            return false;
        }

        userOptional.ifPresent((user) ->
                repository.save(new SolvedQuiz( user, quiz, LocalDateTime.now()))
        );

        return true;
    }

}
