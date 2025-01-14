package pl.commit.craft.flow;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.commit.craft.service.CommitService;

@RestController
@RequestMapping("/api/v1/commit-flow")
public class CommitFlowController {

    private final CommitService commitService;

    public CommitFlowController(CommitService commitService) {
        this.commitService = commitService;
    }

    @PostMapping("/craft")
    public String generateCommit(@RequestBody CommitFlowRequest commitFlowRequest) {
        return commitService.generateFlowCommit(
                commitFlowRequest.major(),
                commitFlowRequest.type(),
                commitFlowRequest.component(),
                commitFlowRequest.changeDescription(),
                commitFlowRequest.details(),
                commitFlowRequest.wholeGitCommand()
        );
    }
}
