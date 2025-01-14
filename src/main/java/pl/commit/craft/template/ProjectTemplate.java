package pl.commit.craft.template;

import lombok.Data;

import java.util.Map;

@Data
public class ProjectTemplate {
    private String name;
    private String description;
    private String pattern;
    private Map<String, Object> model;

}
