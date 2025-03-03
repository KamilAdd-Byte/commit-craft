package pl.commit.craft.template;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/craft-template")
@RequiredArgsConstructor
public class CommitCraftTemplateController {

    private final CommitTemplateService commitTemplateService;

    @Operation(
            summary = "Get all commit templates",
            description = "Fetches a list of all available commit craft templates."
    )
    @GetMapping("/all")
    public ResponseEntity<List<CommitCraftTemplate>> getAllTemplates() throws IOException {
        List<CommitCraftTemplate> templates = commitTemplateService.getAllTemplates();
        return ResponseEntity.ok(templates);
    }

    @Operation(
            summary = "Create a dedicated commit template",
            description = "Creates a new dedicated commit template if the pattern and model scope are valid.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Template created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid template format or template already exists")
            }
    )
    @PostMapping("/dedicated")
    public ResponseEntity<Map<String, String>> createDedicatedTemplate(@RequestBody CommitCraftTemplate template) throws IOException {
        TemplateOperationResult result = commitTemplateService.createDedicatedTemplate(template);

        if (result.success()) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Collections.singletonMap("message", result.message()));
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("error", result.message()));
    }

    @Operation(
            summary = "Remove a commit template",
            description = "Removes a dedicated commit template by name.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Template removed successfully")
            }
    )
    @DeleteMapping("/removed/{name}")
    public ResponseEntity<String> addTemplate(@PathVariable("name") String name) throws IOException {
            commitTemplateService.removeDedicatedTemplate(name);
            return ResponseEntity.ok("Template removed successfully.");
    }

    @Operation(
            summary = "Generate commit template JSON",
            description = "Generates a JSON representation of the commit template based on its name.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "JSON generated successfully")
            }
    )
    @PostMapping("/generate-json/{name}")
    public Map<String, Object> generateJson(@PathVariable String name) throws IOException {
        CommitCraftJson commitCraftJson = commitTemplateService.prepareJsonByModel(name);
        return commitCraftJson.getJsonContent();
    }
}
