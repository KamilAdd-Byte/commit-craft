package pl.commit.craft.template;

import lombok.extern.slf4j.Slf4j;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
class CommitDedicatedTemplateValidator {
    private static final String PLACEHOLDER_REGEX = "\\{(\\w+)\\}";
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile(PLACEHOLDER_REGEX);

    /**
     * For backward compatibility
     */
    public static boolean validatePatternAndModelScope(CommitCraftTemplate template) {
        return validatePatternAndModelScopeDetailed(template).isValid();
    }

    /**
     * Validates that all keys in the model exist in the pattern and vice versa.
     *
     * @param template The commit template to validate
     * @return result of validation with details about any mismatches
     */
    public static ValidationResult validatePatternAndModelScopeDetailed(CommitCraftTemplate template) {
        log.info("Validating pattern and model scope starting for template: {}", template.getName());

        Set<String> modelKeys = template.getModel().keySet();
        Set<String> patternKeys = extractPlaceholdersFromPattern(template.getPattern());

        Set<String> missingInPattern = findMissingKeys(modelKeys, patternKeys);
        Set<String> extraInPattern = findMissingKeys(patternKeys, modelKeys);

        boolean isValid = missingInPattern.isEmpty() && extraInPattern.isEmpty();

        if (isValid) {
            log.info("Pattern matches the model keys.");
            return ValidationResult.valid();
        } else {
            if (!missingInPattern.isEmpty()) {
                log.warn("Pattern is missing keys: {}", missingInPattern);
            }
            if (!extraInPattern.isEmpty()) {
                log.warn("Pattern contains extra keys not in the model: {}", extraInPattern);
            }
            return ValidationResult.invalid(missingInPattern, extraInPattern);
        }
    }


    /**
     * Extracts all placeholder keys from the pattern string.
     *
     * @param pattern The pattern string containing placeholders
     * @return A set of placeholder keys
     */
    private static Set<String> extractPlaceholdersFromPattern(String pattern) {
        Set<String> patternKeys = new HashSet<>();
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(pattern);

        while (matcher.find()) {
            patternKeys.add(matcher.group(1));
        }

        return patternKeys;
    }

    /**
     * Finds keys that are in the first set but not in the second set
     */
    private static Set<String> findMissingKeys(Set<String> sourceKeys, Set<String> targetKeys) {
        return sourceKeys.stream()
                .filter(key -> !targetKeys.contains(key))
                .collect(Collectors.toSet());
    }
}
