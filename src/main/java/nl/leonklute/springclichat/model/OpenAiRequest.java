package nl.leonklute.springclichat.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OpenAiRequest {
    private String model;
    private List<OpenAiMessage> messages;
    private Double temperature;

    public OpenAiRequest(String model, List<OpenAiMessage> messages, Double temperature) {
        this.model = model;
        this.messages = messages;
        this.temperature = temperature;
    }
}