package pl.commit.craft.flow;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.commit.craft.service.CommitTranslateService;

@RestController
@RequestMapping("/api/v1/commit-flow")
@Tag(name = "Commit Flow Controller", description = "Flow commit")
public class CommitFlowController {

    private final CommitTranslateService commitTranslateService;

    public CommitFlowController(CommitTranslateService commitTranslateService) {
        this.commitTranslateService = commitTranslateService;
    }

    @Operation(
            summary = "Generate a commit message based on the provided commit flow data",
            description = "This endpoint receives commit flow details and generates the corresponding commit message."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully generated commit message",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input data",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/craft")
    public String generateCommit(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Commit flow request data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CommitFlowRequest.class)))
            @RequestBody CommitFlowRequest commitFlowRequest) {
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
