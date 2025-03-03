package pl.commit.craft.template.generate;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/craft-template")
@RequiredArgsConstructor
@Tag(
        name = "Commit Template Generation",
        description = "API for generating commit messages based on predefined templates."
)
public class CommitTemplateGenerateController {
    private final CommitTemplateGenerateService service;

    @Operation(
            summary = "Generate commit message",
            description = "Generates a commit message based on a specified template and commit data."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Commit message generated successfully",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Server error during commit generation",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/generate")
    public ResponseEntity<String> generateCommit(
            @Parameter(description = "Name of the commit template", required = true)
            @RequestParam String templateName,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Commit data as JSON model", required = true,
                    content = @Content(schema = @Schema(implementation = JsonNode.class)))
            @RequestBody JsonNode commitData) throws IOException {
        String commitMessage = service.generateCommit(templateName, commitData);
        return ResponseEntity.ok(commitMessage);
    }


    @Operation(
            summary = "Generate dedicated commit message",
            description = "Generates a commit message using a dedicated template with provided commit data."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Commit message generated successfully",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Server error during commit generation",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/generate-dedicated")
    public ResponseEntity<String> generateDedicatedCommit(
            @Parameter(description = "Name of the dedicated commit template", required = true)
            @RequestParam String templateName,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Commit data as JSON", required = true,
                    content = @Content(schema = @Schema(implementation = JsonNode.class)))
            @org.springframework.web.bind.annotation.RequestBody JsonNode commitData) throws IOException {
        String commitMessage = service.generateDedicatedCommit(templateName, commitData);
        return ResponseEntity.ok(commitMessage);
    }
}
