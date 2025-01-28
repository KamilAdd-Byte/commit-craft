package pl.commit.craft.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommitCraftTemplate {
    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Description cannot be null")
    private String description;

    @NotNull(message = "Pattern cannot be null")
    private String pattern;

    @NotNull(message = "Model cannot be null")
    private Map<String, Object> model;
}
