package pl.commit.craft.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import pl.commit.craft.service.CommitTranslateService;

@RestController
@RequestMapping("/api/v1/commit-translate")
public class CommitTranslateController {

    private final CommitTranslateService commitTranslateService;

    public CommitTranslateController(CommitTranslateService commitTranslateService) {
        this.commitTranslateService = commitTranslateService;
    }

    @Operation(
            summary = "Generate commit translation",
            description = "Generates a translated commit message based on the provided request information."
    )
    @PostMapping("/craft")
    public String generateCommit(
            @RequestBody CommitTranslateRequest commitTranslateRequest) {
        return commitTranslateService.generateTranslateCommit(
                commitTranslateRequest.major(),
                commitTranslateRequest.type(),
                commitTranslateRequest.component(),
                commitTranslateRequest.changeDescription(),
                commitTranslateRequest.details(),
                commitTranslateRequest.wholeGitCommand(),
                commitTranslateRequest.language()
        );
    }
}
