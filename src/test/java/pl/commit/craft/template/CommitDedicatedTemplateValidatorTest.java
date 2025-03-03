package pl.commit.craft.template;

import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CommitDedicatedTemplateValidatorTest {

    @Test
    void shouldValidatePatternAndModelScope_Success() {
        Map<String, Object> model = Map.of(
                "scope", "core",
                "message", "message"
        );

        CommitCraftTemplate validTemplate = new CommitCraftTemplate("feat-{scope}-{message}", "Standard commit", "feat-{scope}-{message}", model);

        ValidationResult result = CommitDedicatedTemplateValidator.validatePatternAndModelScopeDetailed(validTemplate);

        assertTrue(result.isValid());
        assertEquals("Template is valid", result.getErrorMessage());
    }

    @Test
    void shouldValidatePatternAndModelScope_MissingModelKey() {
        Map<String, Object> model = Map.of(
                "type", "feat",
                "message", "message",
                "scope", "core"
        );

        CommitCraftTemplate invalidTemplate = new CommitCraftTemplate("feat-{scope}-{message}", "Invalid commit", "feat-{scope}-{message}", model);

        ValidationResult result = CommitDedicatedTemplateValidator.validatePatternAndModelScopeDetailed(invalidTemplate);

        assertFalse(result.isValid());
        assertEquals("Invalid template format: Keys missing in pattern: [type]", result.getErrorMessage());
    }

    @Test
    void shouldValidatePatternAndModelScope_ExtraModelKey() {
        Map<String, Object> model = Map.of(
                "type", "feat",
                "scope", "core",
                "extraKey", "extra"
        );

        CommitCraftTemplate invalidTemplate = new CommitCraftTemplate("feat-{scope}", "Invalid commit", "feat-{scope}", model);

        ValidationResult result = CommitDedicatedTemplateValidator.validatePatternAndModelScopeDetailed(invalidTemplate);

        assertFalse(result.isValid());
        assertEquals("Invalid template format: Keys missing in pattern: [extraKey, type]", result.getErrorMessage());
    }

    @Test
    void shouldValidatePatternAndModelScope_BothMissingAndExtraKeys() {
        Map<String, Object> model = Map.of(
                "type", "feat",
                "scope", "core",
                "extraKey", "extra"
        );

        CommitCraftTemplate invalidTemplate = new CommitCraftTemplate("feat-{scope}-{message}", "Invalid commit", "feat-{scope}-{message}", model);

        ValidationResult result = CommitDedicatedTemplateValidator.validatePatternAndModelScopeDetailed(invalidTemplate);

        assertFalse(result.isValid());
        assertEquals("Invalid template format: Keys missing in pattern: [extraKey, type]; Missing keys in model: [message]", result.getErrorMessage());
    }
}