package pl.commit.craft.quick;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/commit-quick")
@RequiredArgsConstructor
@Tag(name = "Commit Quick Controller", description = "Very quick commit audit or PR review")
public class CommitQuickController {

    final CommitQuickService commitQuickService;

    @Operation(
            summary = "Generate a quick commit message based on the provided details",
            description = "This endpoint receives the commit details and generates a quick commit message."
    )
    @PostMapping("/craft")
    public String generateCommit(
            @RequestBody @Parameter(
                    description = "Request body containing the commit details, including topic scope and Git command flag. Actual use *audit* message - Audit fix, *fix*: message - Pull request comments improved, *test*: message -Fixed tests",
                    required = true
            ) CommitQuickRequest commitQuickRequest) {
        return commitQuickService.generateQuickCommit(
                commitQuickRequest.topicScope(),
                commitQuickRequest.isGitCommand()
        );
    }
}
