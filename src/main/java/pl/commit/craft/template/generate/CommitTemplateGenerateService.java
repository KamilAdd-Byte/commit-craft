package pl.commit.craft.template.generate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
class CommitTemplateGenerateService {
    private static final String ROOT_TEMPLATE_NAME = "templates";
    private static final String ROOT_DEDICATED_NAME = "dedicated";
    private static final String TEMPLATES_SCHEMA_PATH = "src/main/resources/templates/meta-schema.json";
    private static final String DEDICATED_SCHEMA_PATH = "src/main/resources/templates/dedicated-meta-schema.json";

    private final ObjectMapper objectMapper;

    /**
     * Generates a commit message using a standard template
     */
    String generateCommit(String templateName, JsonNode commitData) throws IOException {
        return generateCommitFromSchema(
                TEMPLATES_SCHEMA_PATH,
                ROOT_TEMPLATE_NAME,
                templateName,
                commitData
        );
    }

    /**
     * Generates a commit message using a dedicated template
     */
    String generateDedicatedCommit(String templateName, JsonNode commitData) throws IOException {
        return generateCommitFromSchema(
                DEDICATED_SCHEMA_PATH,
                ROOT_DEDICATED_NAME,
                templateName,
                commitData
        );
    }

    /**
     * Core method for generating commit messages from any schema
     */
    private String generateCommitFromSchema(
            String schemaPath,
            String rootNodeName,
            String templateName,
            JsonNode commitData) throws IOException {

        JsonNode rootNode = objectMapper.readTree(new File(schemaPath));
        JsonNode templatesArray = rootNode.path(rootNodeName);

        JsonNode template = findTemplateByName(templatesArray, templateName)
                .orElseThrow(() -> new IllegalArgumentException("Template with name " + templateName + " not found"));

        validateCommitData(template.path("model"), commitData);

        String pattern = template.path("pattern").asText();
        return fillPatternWithData(pattern, commitData);
    }

    /**
     * Finds a template by name in the templates array
     */
    private Optional<JsonNode> findTemplateByName(JsonNode templatesArray, String templateName) {
        return StreamSupport.stream(templatesArray.spliterator(), false)
                .filter(template -> templateName.equals(template.path("name").asText()))
                .findFirst();
    }

    /**
     * Validates the commit data against the template model
     */
    private void validateCommitData(JsonNode model, JsonNode commitData) {
        List<String> missingFields = new ArrayList<>();

        model.fields().forEachRemaining(field -> {
            String fieldName = field.getKey();
            JsonNode fieldModel = field.getValue();

            if (!commitData.has(fieldName)) {
                missingFields.add(fieldName);
            } else if (fieldModel.isArray()) {
                validateArrayField(fieldName, fieldModel, commitData);
            }
        });

        if (!missingFields.isEmpty()) {
            throw new IllegalArgumentException("Missing required fields: " + String.join(", ", missingFields));
        }
    }

    /**
     * Validates that field values match allowed values in the model
     */
    private void validateArrayField(String fieldName, JsonNode fieldModel, JsonNode commitData) {
        JsonNode valueNode = commitData.path(fieldName);

        if (!valueNode.isTextual()) {
            throw new IllegalArgumentException("Field '" + fieldName + "' should be a string");
        }

        String value = valueNode.asText();
        boolean isValid = StreamSupport.stream(fieldModel.spliterator(), false)
                .anyMatch(allowedValue -> allowedValue.asText().equals(value));

        if (!isValid) {
            throw new IllegalArgumentException("Invalid value for field '" + fieldName + "': " + value);
        }
    }

    /**
     * Fills the template pattern with the commit data
     */
    private String fillPatternWithData(String pattern, JsonNode commitData) {
        String result = pattern;

        Iterator<String> fieldNames = commitData.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            String placeholder = "\\{" + fieldName + "\\}";
            String value = commitData.path(fieldName).asText();
            result = result.replaceAll(placeholder, value);
        }

        return result.trim();
    }
}
