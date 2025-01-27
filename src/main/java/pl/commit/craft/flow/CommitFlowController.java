package pl.commit.craft.flow;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.commit.craft.service.CommitTranslateService;

@RestController
@RequestMapping("/api/v1/commit-flow")
public class CommitFlowController {

    private final CommitTranslateService commitTranslateService;

    public CommitFlowController(CommitTranslateService commitTranslateService) {
        this.commitTranslateService = commitTranslateService;
    }

    @PostMapping("/craft")
    public String generateCommit(@RequestBody CommitFlowRequest commitFlowRequest) {
        return commitTranslateService.generateFlowCommit(
                commitFlowRequest.major(),
                commitFlowRequest.type(),
                commitFlowRequest.component(),
                commitFlowRequest.changeDescription(),
                commitFlowRequest.details(),
                commitFlowRequest.wholeGitCommand()
        );
    }
}
