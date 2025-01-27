package pl.commit.craft.template;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@AllArgsConstructor
class CommitCraftTemplate {
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private String pattern;
    @NotNull
    private Map<String, Object> model;

}
