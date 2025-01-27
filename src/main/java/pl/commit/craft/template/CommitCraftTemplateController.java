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

    private final CommitTemplateService commitTemplateService;

    @GetMapping("/all")
    public ResponseEntity<List<CommitCraftTemplate>> getAllTemplates() throws IOException {
        List<CommitCraftTemplate> templates = commitTemplateService.getAllTemplates();
        return ResponseEntity.ok(templates);
    }

    @PostMapping("/dedicated")
    public ResponseEntity<String> createDedicatedTemplate(@RequestBody CommitCraftTemplate template) throws IOException {
            boolean patternAndModelScope = CommitDedicatedTemplateValidator.validatePatternAndModelScope(template);
            if (patternAndModelScope) {
                commitTemplateService.createDedicatedTemplate(template);
                return ResponseEntity.ok("Template added successfully.");
            }
           return ResponseEntity.badRequest().body("Template already exists.");
    }

    @DeleteMapping("/removed/{name}")
    public ResponseEntity<String> addTemplate(@PathVariable("name") String name) throws IOException {
            commitTemplateService.removeDedicatedTemplate(name);
            return ResponseEntity.ok("Template removed successfully.");
    }

    @PostMapping("/generate-json/{name}")
    public Map<String, Object> generateJson(@PathVariable String name) throws IOException {
        CommitCraftJson commitCraftJson = commitTemplateService.prepareJsonByModel(name);
        return commitCraftJson.getJsonContent();
    }
}
