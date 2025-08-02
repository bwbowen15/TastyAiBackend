package dev.brian.TastyAiBackend;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    private final WebClient webClient;

//    public OpenAIService(@Value("${openai.api.key}") String openaiApiKey){
//        this.webClient = WebClient.builder()
//                .baseUrl("https://api.openai.com/v1")
//                .defaultHeader("Authorization", "Bearer " + openaiApiKey)
//                .defaultHeader("Content-Type", "application/json")
//                .build();
//    }

    public OpenAIService(@Value("${openai.api.key}") String openaiApiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + openaiApiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }


//    public Mono<String> getChatResponse(String prompt){
//        Map<String, Object> body = Map.of(
//                "model", "gpt-3.5-turbo",  // fixed here
//                "messages", List.of(
//                        Map.of("role", "user", "content", prompt)
//                )
//        );
    public Mono<String> getChatResponse(String prompt) {
        Map<String, Object> body = Map.of(
            "model", "gpt-3.5-turbo",
            "messages", List.of(
                    Map.of("role", "user", "content", prompt)
            )
        );


//        return webClient.post()
//                .uri("/chat/completions")
//                .bodyValue(body)
//                .retrieve()
//                .onStatus(status -> true, response -> response.bodyToMono(String.class).map(RuntimeException::new))
//                .bodyToMono(Map.class)
//                .map(response -> {
//                    List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
//                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
//                    return (String) message.get("content");
//                });
        return webClient.post()
                .uri("/chat/completions")
                // Send the request body
                .bodyValue(body)
                // Only treat non-success HTTP statuses as errors
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.err.println("OpenAI API error: " + errorBody);
                                    return Mono.error(new RuntimeException("OpenAI API error: " + errorBody));
                                })
                )
                // Parse the response JSON into a Map
                .bodyToMono(Map.class)
                .map(response -> {
                    // Safely extract the "content" from the first choice
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                    if (choices == null || choices.isEmpty()) {
                        return "No choices returned from OpenAI";
                    }
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    if (message == null) {
                        return "No message content in OpenAI response";
                    }
                    return (String) message.get("content");
                });

    }
}
