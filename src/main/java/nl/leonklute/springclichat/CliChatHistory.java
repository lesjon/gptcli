package nl.leonklute.springclichat;


import lombok.Getter;
import nl.leonklute.springclichat.model.OpenAiMessage;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class CliChatHistory implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String MESSAGE_SEPARATOR = "--------------------------------\n";

    private final List<OpenAiMessage> messages;
    private final CliChatConfiguration cliChatConfiguration;

    public CliChatHistory(CliChatConfiguration cliChatConfiguration) {
        this.messages = new ArrayList<>();
        this.cliChatConfiguration = cliChatConfiguration;
    }

    public void readHistory() {
        if (cliChatConfiguration.isNoHistory()) {
            return;
        }
        String fileName = cliChatConfiguration.getFileName();
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            System.out.println("History file:'" + fileName + "' does not exist, creating...");
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
            var readHistory = (CliChatHistory) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Could not read history file. Error: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public void writeHistory() {
        var fileName = this.cliChatConfiguration.getFileName();
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.err.println("Could not write history file. Error: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public void addMessage(OpenAiMessage openAiMessage) {
        this.messages.add(openAiMessage);
    }

    public String toString() {
        var sb = new StringBuilder();
        for (OpenAiMessage message : this.messages) {
            sb.append(message.toString()).append("\n");
            sb.append(MESSAGE_SEPARATOR);
        }
        return sb.toString();
    }

    public String lastMessage() {
        return this.messages.get(this.messages.size() - 1).toString() + "\n" + MESSAGE_SEPARATOR;
    }

    public void addSystemMessage(String systemMessage) {
        OpenAiMessage openAiMessage = new OpenAiMessage();
        openAiMessage.setContent(systemMessage);
        openAiMessage.setRole("system");
        this.messages.add(openAiMessage);
    }
}
