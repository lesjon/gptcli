package nl.leonklute.springclichat;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@lombok.Getter
@Configuration
@PropertySource(value = "file:${LOCALAPPDATA}/clichat/config", ignoreResourceNotFound = true)
@PropertySource("classpath:application.properties")
public class CliChatConfiguration {

    @Value("${clichat.system-message:}")
    private transient String systemMessage;
    @Value("#systemEnvironment[OPENAI_API_KEY] ?: '${openai.api.key}'")
    private String apiKey;
    @Value("${openai.api.model:gpt-3.5-turbo}")
    private String model;
    @Value("${clichat.no-history:false}")
    private boolean noHistory;
    @Value("${clichat.display-models:false}")
    private boolean displayModels = false;
    @Value("${clichat.file-name:history.chat}")
    private String fileName;

    public void updateFromHistory(CliChatHistory cliChatHistory) {
        CliChatConfiguration historyConfig = cliChatHistory.getCliChatConfiguration();
        this.model = historyConfig.getModel();
        if (historyConfig.getFileName() != null) {
            this.fileName = historyConfig.getFileName();
        }
    }
}
