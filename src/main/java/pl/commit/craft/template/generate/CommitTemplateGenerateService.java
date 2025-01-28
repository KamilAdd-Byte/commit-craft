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

@Service
@RequiredArgsConstructor
class CommitTemplateGenerateService {

    private final ObjectMapper objectMapper;
    private static final String RESOURCES_TEMPLATES_META_SCHEMA_JSON = "src/main/resources/templates/meta-schema.json";

    String generateCommit(String templateName, JsonNode commitData) throws IOException {
        JsonNode rootNode = objectMapper.readTree(new File(RESOURCES_TEMPLATES_META_SCHEMA_JSON));
        JsonNode templatesArray = rootNode.path("templates");

        Optional<JsonNode> matchingTemplate = findTemplateByName(templatesArray, templateName);

        if (matchingTemplate.isEmpty()) {
            throw new IllegalArgumentException("Template with name " + templateName + " not found");
        }

        JsonNode template = matchingTemplate.get();
        validateCommitData(template.path("model"), commitData);

        String pattern = template.path("pattern").asText();
        return fillPatternWithData(pattern, commitData);
    }

    private Optional<JsonNode> findTemplateByName(JsonNode templatesArray, String templateName) {
        for (Iterator<JsonNode> it = templatesArray.elements(); it.hasNext(); ) {
            JsonNode template = it.next();
            if (templateName.equals(template.path("name").asText())) {
                return Optional.of(template);
            }
        }
        return Optional.empty();
    }

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
            throw new IllegalArgumentException("Missing required fields: " + missingFields);
        }
    }

    private void validateArrayField(String fieldName, JsonNode fieldModel, JsonNode commitData) {
        JsonNode valueNode = commitData.path(fieldName);

        if (valueNode.isTextual()) {
            String value = valueNode.asText();
            boolean isValid = false;

            for (JsonNode allowedValue : fieldModel) {
                if (allowedValue.asText().equals(value)) {
                    isValid = true;
                    break;
                }
            }

            if (!isValid) {
                throw new IllegalArgumentException("Invalid value for field '" + fieldName + "': " + value);
            }
        } else {
            throw new IllegalArgumentException("Field '" + fieldName + "' should be a string");
        }
    }

    private String fillPatternWithData(String pattern, JsonNode commitData) {
        String result = pattern;

        Iterator<String> fieldNames = commitData.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            String placeholder = "{" + fieldName + "}";
            if (result.contains(placeholder)) {
                String value = commitData.path(fieldName).asText();
                result = result.replace(placeholder, value);
            }
        }

        return result.trim();
    }
}
