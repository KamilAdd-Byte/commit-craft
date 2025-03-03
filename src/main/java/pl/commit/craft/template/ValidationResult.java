package pl.commit.craft.template;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
class ValidationResult {
    @Getter
    private final boolean valid;
    private final Set<String> missingInPattern;
    private final Set<String> extraInPattern;

    /**
     * Creates a successful validation result
     */
    public static ValidationResult valid() {
        return new ValidationResult(true, Set.of(), Set.of());
    }

    /**
     * Creates a validation result with errors
     */
    public static ValidationResult invalid(Set<String> missingInPattern, Set<String> extraInPattern) {
        return new ValidationResult(false, missingInPattern, extraInPattern);
    }

    /**
     * Generates a detailed error message for invalid templates
     */
    public String getErrorMessage() {
        if (valid) {
            return "Template is valid";
        }

        StringBuilder message = new StringBuilder("Invalid template format: ");

        if (!missingInPattern.isEmpty()) {
            message.append("Keys missing in pattern: [")
                    .append(String.join(", ", missingInPattern))
                    .append("]");

            if (!extraInPattern.isEmpty()) {
                message.append("; ");
            }
        }

        if (!extraInPattern.isEmpty()) {
            message.append("Missing keys in model: [")
                    .append(String.join(", ", extraInPattern))
                    .append("]");
        }

        return message.toString();
    }
}
