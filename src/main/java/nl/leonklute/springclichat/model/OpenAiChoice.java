package nl.leonklute.springclichat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OpenAiChoice {
    private OpenAiMessage message;
    @JsonProperty("finish_reason")
    private String finishReason;
    private int index;

    public OpenAiChoice(OpenAiMessage message, String finishReason, int index) {
        this.message = message;
        this.finishReason = finishReason;
        this.index = index;
    }

    public void setMessage(OpenAiMessage message) {
        this.message = message;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
