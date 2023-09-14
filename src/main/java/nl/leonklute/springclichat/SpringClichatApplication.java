package nl.leonklute.springclichat;

import nl.leonklute.springclichat.model.OpenAiMessage;
import nl.leonklute.springclichat.rest.OpenAi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;

@SpringBootApplication
@ShellComponent
public class SpringClichatApplication {

    private final OpenAi openAi;
    private final CliChatHistory history;

    public SpringClichatApplication(OpenAi openAi, CliChatHistory history) {
        this.openAi = openAi;
        this.history = history;
    }

    @ShellMethod
    public String models() {
        return openAi.getModels().join().toString();
    }

    @ShellMethod
    String chat(@ShellOption String... message) throws IOException {
        var aiMessage = openAi.getCompletion(String.join(" ", message)).join().getMessage();
        history.addMessage(aiMessage);
        return history.toString();
    }
    @ShellMethod
    String system(@ShellOption String... message) {
        OpenAiMessage systemMessage = new OpenAiMessage();
        systemMessage.setContent(String.join(" ", message));
        systemMessage.setRole("system");
        history.addMessage(systemMessage);
        return "added system message";
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringClichatApplication.class, args);
    }

}
