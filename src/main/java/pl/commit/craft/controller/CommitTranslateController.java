package pl.commit.craft.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import pl.commit.craft.service.CommitTranslateService;

@RestController
@RequestMapping("/api/v1/commit-translate")
@Tag(name = "Commit Translation Controller", description = "Translate commit wit deepl api")
public class CommitTranslateController {

    private final CommitTranslateService commitTranslateService;

    public CommitTranslateController(CommitTranslateService commitTranslateService) {
        this.commitTranslateService = commitTranslateService;
    }

    @Operation(
            summary = "Generate commit translation",
            description = "Generates a translated commit message based on the provided request information."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully generated commit translation",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input data",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/craft")
    public String generateCommit(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Commit translation request data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CommitTranslateRequest.class)))
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
