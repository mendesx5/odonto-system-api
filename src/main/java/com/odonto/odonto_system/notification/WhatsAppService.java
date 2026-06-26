package com.odonto.odonto_system.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class WhatsAppService {

    private final RestClient restClient;

    @Value("${EVOLUTION_API_URL:http://localhost:8080}")
    private String apiUrl;

    @Value("${EVOLUTION_API_KEY:sua_api_key}")
    private String apiKey;

    @Value("${EVOLUTION_INSTANCE:odontosystem}")
    private String instance;

    public WhatsAppService() {
        this.restClient = RestClient.builder().build();
    }

    @Async
    public void sendMessage (String toPhone, String text) {
        String formattedPhone = toPhone.replaceAll("[^0-9]", "");

        try {
            restClient.post()
                    .uri(apiUrl + "/message/sendText")
                    .header("apiKey", apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "number", formattedPhone,
                            "options", Map.of("delay", 1200, "presence", "composing"),
                            "textMessage", Map.of("text", text)
                    ))
                    .retrieve()
                    .toBodilessEntity();
            System.out.println("WhatsApp disparado com sucesso para : " + formattedPhone);
        } catch (Exception e) {
            System.err.println("Falha ao conectar na Evolution API: " + e.getMessage());
        }
    }

}
