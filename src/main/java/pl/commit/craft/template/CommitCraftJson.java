package pl.commit.craft.template;

import lombok.Getter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
class CommitCraftJson {
    private final Map<String, Object> jsonContent;

    public CommitCraftJson() {
        jsonContent = new HashMap<>();
    }

    public void addField(String key, Object value) {
        jsonContent.put(key, value);
    }

    public Map<String, Object> getFields() {
        return Collections.unmodifiableMap(jsonContent);
    }
}
