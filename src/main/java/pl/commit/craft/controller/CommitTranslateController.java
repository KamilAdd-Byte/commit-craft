package pl.commit.craft.controller;

import org.springframework.web.bind.annotation.*;
import pl.commit.craft.service.CommitService;

@RestController
@RequestMapping("/api/v1/commit-translate")
public class CommitTranslateController {

    private final CommitService commitService;

    public CommitTranslateController(CommitService commitService) {
        this.commitService = commitService;
    }

    @PostMapping("/craft")
    public String generateCommit(@RequestBody CommitTranslateRequest commitTranslateRequest) {
        return commitService.generateTranslateCommit(
                commitTranslateRequest.major(),
                commitTranslateRequest.type(),
                commitTranslateRequest.component(),
                commitTranslateRequest.changeDescription(),
                commitTranslateRequest.details(),
                commitTranslateRequest.wholeGitCommand()
        );
    }
}
