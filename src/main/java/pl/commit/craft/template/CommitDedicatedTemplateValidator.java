package pl.commit.craft.template;

import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import java.util.Set;

@Slf4j
class CommitDedicatedTemplateValidator {
    private static final String PATTERN_VALIDATE_MODEL = "\\{(\\w+)\\}(-\\{(\\w+)\\})*";

    static boolean validatePatternAndModelScope(CommitCraftTemplate template) {
        log.info("Validating pattern and model scope starting");
        String pattern = template.getPattern();
        Map<String, Object> model = template.getModel();

        Set<String> modelKeys = model.keySet();

        boolean matches = true;
        for (String key : modelKeys) {
            if (!pattern.contains(key)) {
                log.warn("Pattern is missing key: {}", key);
                matches = false;
            }
        }

        String[] patternWords = pattern.split(PATTERN_VALIDATE_MODEL);
        for (String word : patternWords) {
            if (!modelKeys.contains(word)) {
                log.warn("Pattern contains an extra key not in the model: {}", word);
                matches = false;
            }
        }

        if (matches) {
            log.info("Pattern matches the model keys.");
        } else {
            log.warn("Pattern does not match the model keys.");
        }

        return matches;
    }
}
