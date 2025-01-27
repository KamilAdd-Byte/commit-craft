package pl.commit.craft.quick;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/commit-quick")
@RequiredArgsConstructor
public class CommitQuickController {

    final CommitQuickService commitQuickService;

    @PostMapping("/craft")
    public String generateCommit(@RequestBody CommitQuickRequest commitQuickRequest) {
        return commitQuickService.generateQuickCommit(
                commitQuickRequest.topicScope(),
                commitQuickRequest.isGitCommand()
        );
    }
}
