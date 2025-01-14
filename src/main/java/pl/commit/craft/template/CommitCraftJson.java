package pl.commit.craft.template;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

@Getter
public class CommitCraftJson {
    private Map<String, Object> jsonContent;

    public CommitCraftJson() {
        jsonContent = new HashMap<>();
    }

    public void addField(String key, Object value) {
        jsonContent.put(key, value);
    }

    public Map<String, Object> getJsonContent() {
        return jsonContent;
    }
}
