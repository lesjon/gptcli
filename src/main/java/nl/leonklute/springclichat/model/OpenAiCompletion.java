package nl.leonklute.springclichat.model;

import lombok.Getter;

import java.util.List;

@Getter
public class OpenAiCompletion {
    private String id;
    private String object;
    private long created;
    private String model;
    private OpenAiUsage usage;
    private List<OpenAiChoice> choices;

    public void setId(String id) {
        this.id = id;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setUsage(OpenAiUsage usage) {
        this.usage = usage;
    }

    public void setChoices(List<OpenAiChoice> choices) {
        this.choices = choices;
    }
}
