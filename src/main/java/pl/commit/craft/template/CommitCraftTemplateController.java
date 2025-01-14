package pl.commit.craft.template;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/craft-template")
@RequiredArgsConstructor
public class CommitCraftTemplateController {

    private final TemplateService templateService;

    @GetMapping
    public ResponseEntity<List<CommitCraftTemplate>> getAllTemplates() throws IOException {
        List<CommitCraftTemplate> templates = templateService.getAllTemplates();
        return ResponseEntity.ok(templates);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createDedicatedTemplate(@RequestBody CommitCraftTemplate template) {
        try {
            boolean patternAndModelScope = CommitDedicatedTemplateValidator.validatePatternAndModelScope(template);
            if (patternAndModelScope) {
                templateService.createDedicatedTemplate(template);
                return ResponseEntity.ok("Template added successfully.");
            }
           return ResponseEntity.badRequest().body("Template already exists.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error adding template: " + e.getMessage());
        }
    }

    @DeleteMapping("/removed/{name}")
    public ResponseEntity<String> addTemplate(@PathVariable("name") String name) {
        try {
            templateService.removeDedicatedTemplate(name);
            return ResponseEntity.ok("Template removed successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error removing template: " + e.getMessage());
        }
    }

    @PostMapping("/generate-json/{name}")
    public Map<String, Object> generateJson(@PathVariable String name) throws IOException {
        CommitCraftJson commitCraftJson = templateService.prepareJsonByModel(name);
        return commitCraftJson.getJsonContent();
    }
}
