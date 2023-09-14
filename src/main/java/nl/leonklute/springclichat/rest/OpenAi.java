package nl.leonklute.springclichat.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.leonklute.springclichat.CliChatConfiguration;
import nl.leonklute.springclichat.CliChatHistory;
import nl.leonklute.springclichat.model.OpenAiChoice;
import nl.leonklute.springclichat.model.OpenAiCompletion;
import nl.leonklute.springclichat.model.OpenAiMessage;
import nl.leonklute.springclichat.model.OpenAiRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class OpenAi {
    private static final URI COMPLETION_URL = URI.create("https://api.openai.com/v1/chat/completions");
    private static final URI MODELS_URL = URI.create("https://api.openai.com/v1/models");
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CliChatConfiguration cliChatConfiguration;
    private final CliChatHistory cliChatHistory;

    public OpenAi(CliChatConfiguration cliChatConfiguration, CliChatHistory cliChatHistory) {
        this.cliChatHistory = cliChatHistory;
        this.cliChatConfiguration = cliChatConfiguration;
    }

    public CompletableFuture<List<String>> getModels() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(MODELS_URL)
                .header("Authorization", "Bearer " + cliChatConfiguration.getApiKey())
                .GET().build();
        var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        return response.thenApply(HttpResponse::body).thenApply(List::of);
    }

    public CompletableFuture<OpenAiChoice> getCompletion(String prompt) throws IOException {
        OpenAiMessage openAiMessage = new OpenAiMessage();
        openAiMessage.setContent(prompt);
        openAiMessage.setRole("user");
        cliChatHistory.addMessage(openAiMessage);

        OpenAiRequest openAiRequest = this.createRequest();
        String body = objectMapper.writeValueAsString(openAiRequest);
        HttpRequest promptRequest = HttpRequest.newBuilder().uri(COMPLETION_URL)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + cliChatConfiguration.getApiKey())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = client.sendAsync(promptRequest, HttpResponse.BodyHandlers.ofString());
        CompletableFuture<OpenAiChoice> replyFuture = responseFuture
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    try {
                        return objectMapper.readValue(json, OpenAiCompletion.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .thenApply(OpenAiCompletion::getChoices)
                .thenApply(choices -> choices.get(0));
        return replyFuture;
    }


    private OpenAiRequest createRequest() {
        OpenAiRequest openAiRequest = new OpenAiRequest(cliChatConfiguration.getModel(), cliChatHistory.getMessages(), 0.9);
        return openAiRequest;
    }
}
