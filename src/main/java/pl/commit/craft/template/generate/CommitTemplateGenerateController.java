package pl.commit.craft.template.generate;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/craft-template")
@RequiredArgsConstructor
public class CommitTemplateGenerateController {
    private final CommitTemplateGenerateService service;

    @PostMapping("/generate")
    public ResponseEntity<String> generateCommit(@RequestParam String templateName, @RequestBody JsonNode commitData) throws IOException {
        String commitMessage = service.generateCommit(templateName, commitData);
        return ResponseEntity.ok(commitMessage);
    }
}
