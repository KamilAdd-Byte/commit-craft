package pl.commit.craft.translate;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
class DeeplWebClient implements TranslateCommitCraft {
    private final WebClient webClient;

    @Value("${deepl.api.key}")
    private String apiKey;

    @Value("${deepl.api.url:https://api-free.deepl.com/v2/translate}")
    private String deeplApiUrl;

    /**
     * Translates text using the DeepL API.
     *
     * @param text     The text to translate.
     * @param targetLang The target language code (e.g., "EN", "DE", "FR").
     * @return Translated text.
     */
    public String translate(String text, String targetLang) {
        try {
            DeeplResponse response = webClient.post()
                    .uri(deeplApiUrl)
                    .header("Authorization", "DeepL-Auth-Key " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(buildRequestPayload(text, targetLang))
                    .retrieve()
                    .bodyToMono(DeeplResponse.class)
                    .block();
            return response.getTranslations().stream().map(DeeplResponse.Translation::getText).findFirst().orElseThrow();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("DeepL API call failed: " + e.getMessage(), e);
        }
    }

    private String buildRequestPayload(String text, String targetLang) {
        return "{\"text\": [\"" + text + "\"], \"target_lang\": \"" + targetLang + "\"}";
    }
}
