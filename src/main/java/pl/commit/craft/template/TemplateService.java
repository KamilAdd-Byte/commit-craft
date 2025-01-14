package pl.commit.craft.template;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TemplateService {
    public static final String PROPERTY_TEMPLATES = "templates";
    public static final String PROPERTY_DEDICATED_TEMPLATES = "dedicated";
    public static final String PATH_NAME_ELEMENT = "name";
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

    private CommitCraftJson getCommitCraftJson(CommitCraftTemplate selectedTemplate, String selectedTemplateName) throws IOException {
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

    public void createDedicatedTemplate(CommitCraftTemplate newTemplate) throws IOException {
        List<CommitCraftTemplate> templates = readTemplates();
        templates.add(newTemplate);
        saveTemplates(templates);
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
