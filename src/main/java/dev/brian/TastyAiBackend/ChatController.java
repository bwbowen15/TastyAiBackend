package dev.brian.TastyAiBackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;


@RestController
@RequestMapping
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class ChatController {

    private final OpenAIService openAIService;

    @Autowired
    public ChatController(OpenAIService openAIService){
        this.openAIService = openAIService;
    }

    @PostMapping("/chat")
    public Mono<Map<String, String>> chat(@RequestBody ChatRequest request){
        String fullPrompt = String.format(
                "I am looking for a recipe that follows these instructions: \"%s\". " +
                "I have these dietary restrictions(each restriction is separated by commas): \"%s\" and I am looking to meet these goals(each goal is separated by a comma and if it says none then ignore the restrictions all together): \"%s\". " +
                "Always respond with a recipe, even if they do not give the best instructions just give something, it has to be relevant to the instructions given though and include ingrediends asked for if those are included" +
                "Include all macros for this dish",
                request.getRecipe(),
                request.getRestrictions(),
                request.getGoals()
        );
        try {
            return openAIService.getChatResponse(fullPrompt)
                    .map(reply -> Map.of("reply", reply));
        } catch (Exception e) {
            e.printStackTrace();
            // Return an error message wrapped in Mono
            return Mono.just(Map.of("reply", "Server error: " + e.getMessage()));
        }
    }
}
