package pl.commit.craft.template;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
class CommitTemplateService {
    private static final String PROPERTY_TEMPLATES = "templates";
    private static final String PROPERTY_DEDICATED_TEMPLATES = "dedicated";
    private static final String PATH_NAME_ELEMENT = "name";
    private static final String RESOURCES_TEMPLATES_META_SCHEMA_JSON = "src/main/resources/templates/meta-schema.json";
    private static final String RESOURCES_DEDICATED_META_SCHEMA_JSON = "src/main/resources/templates/dedicated-meta-schema.json";
    private final ObjectMapper objectMapper;

    public List<CommitCraftTemplate> getAllTemplates() throws IOException {
        JsonNode rootNode = objectMapper.readTree(new File(RESOURCES_TEMPLATES_META_SCHEMA_JSON));
        JsonNode templatesNode = rootNode.path(PROPERTY_TEMPLATES);
        List<CommitCraftTemplate> templatesList = objectMapper.readValue(
                templatesNode.toString(),
                new TypeReference<>() {
                }
        );
        List<CommitCraftTemplate> templates = new ArrayList<>(templatesList);

        JsonNode rootDedicatedNode = objectMapper.readTree(new File(RESOURCES_DEDICATED_META_SCHEMA_JSON));
        JsonNode dedicatedNode = rootDedicatedNode.path(PROPERTY_DEDICATED_TEMPLATES);
        List<CommitCraftTemplate> dedicatedList = objectMapper.readValue(
                dedicatedNode.toString(),
                new TypeReference<>() {
                }
        );
        templates.addAll(dedicatedList);
        return templates;
    }

    public CommitCraftJson prepareJsonByModel(String name) throws IOException {
        List<CommitCraftTemplate> templates = getAllTemplates();
        CommitCraftTemplate selectedTemplate = templates.stream()
                .filter(template -> template.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Template not found"));
        return getCommitCraftJson(selectedTemplate, selectedTemplate.getName());
    }

    CommitCraftJson getCommitCraftJson(CommitCraftTemplate selectedTemplate, String selectedTemplateName) throws IOException {
        Map<String, Object> model = selectedTemplate.getModel();
        CommitCraftJson commitCraftJson = new CommitCraftJson();
        commitCraftJson.addField(PATH_NAME_ELEMENT, selectedTemplateName);
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof List<?> list) {
                commitCraftJson.addField(key, list);
            } else {
                commitCraftJson.addField(key, value);
            }
        }
        return commitCraftJson;
    }

    public List<CommitCraftTemplate> readTemplates() throws IOException {
        JsonNode rootNode = objectMapper.readTree(new File(RESOURCES_DEDICATED_META_SCHEMA_JSON));
        JsonNode dedicatedNode = rootNode.path(PROPERTY_DEDICATED_TEMPLATES);
        return objectMapper.convertValue(dedicatedNode, new TypeReference<List<CommitCraftTemplate>>() {
        });
    }

    /**
     * Creates a dedicated template after validating it.
     *
     * @param template The template to create
     * @return Result containing success/failure status and a message
     * @throws IOException If there's an issue accessing the template storage
     */
    public TemplateOperationResult createDedicatedTemplate(CommitCraftTemplate template) throws IOException {
        ValidationResult validationResult =
                CommitDedicatedTemplateValidator.validatePatternAndModelScopeDetailed(template);

        if (!validationResult.isValid()) {
            String errorMessage = validationResult.getErrorMessage();
            log.warn("Template validation failed: {}", errorMessage);
            return new TemplateOperationResult(false, errorMessage);
        }

        if (templateExists(template.getName())) {
            log.warn("Template with name '{}' already exists", template.getName());
            return new TemplateOperationResult(false, "Template with name '" + template.getName() + "' already exists");
        }

        saveTemplate(template);
        log.info("Template '{}' created successfully", template.getName());
        return new TemplateOperationResult(true, "Template created successfully");
    }

    /**
     * Checks if a template with the given name already exists
     *
     * @param templateName Name of the template to check
     * @return true if the template exists, false otherwise
     * @throws IOException If there's an issue accessing the template storage
     */
    private boolean templateExists(String templateName) throws IOException {
        List<CommitCraftTemplate> existingTemplates = getAllTemplates();
        return existingTemplates.stream()
                .anyMatch(template -> template.getName().equals(templateName));
    }

    /**
     * Saves a new template to the dedicated templates file
     *
     * @param template The template to save
     * @throws IOException If there's an issue accessing or writing to the template storage
     */
    private void saveTemplate(CommitCraftTemplate template) throws IOException {
        List<CommitCraftTemplate> existingTemplates = readTemplates();
        existingTemplates.add(template);
        saveTemplates(existingTemplates);
        log.debug("Template saved successfully: {}", template.getName());
    }

    public void removeDedicatedTemplate(String dedicatedTemplateName) throws IOException {
        JsonNode rootNode = objectMapper.readTree(new File(RESOURCES_DEDICATED_META_SCHEMA_JSON));
        ArrayNode dedicatedArray = (ArrayNode) rootNode.path(PROPERTY_DEDICATED_TEMPLATES);
        for (Iterator<JsonNode> nodeIterator = dedicatedArray.elements(); nodeIterator.hasNext(); ) {
            JsonNode element = nodeIterator.next();
            if (dedicatedTemplateName.equals(element.path(PATH_NAME_ELEMENT).asText())) {
                nodeIterator.remove();
            }
        }
        objectMapper.writeValue(new File(RESOURCES_DEDICATED_META_SCHEMA_JSON), rootNode);
    }

    private void saveTemplates(List<CommitCraftTemplate> templates) throws IOException {
        JsonNode rootNode = objectMapper.createObjectNode();
        ((ObjectNode) rootNode).putPOJO(PROPERTY_DEDICATED_TEMPLATES, templates);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(RESOURCES_DEDICATED_META_SCHEMA_JSON), rootNode);
    }
}
