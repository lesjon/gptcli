package nl.leonklute.springclichat.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OpenAiMessage implements Serializable {
    private String role;
    private String content;

    public String toString() {
        return this.role + ":" + this.content;
    }
}