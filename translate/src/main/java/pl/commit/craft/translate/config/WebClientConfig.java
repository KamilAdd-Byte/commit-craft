package pl.commit.craft.translate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    public static final String FREE_DEEPL_COM_V_2_TRANSLATE = "https://api-free.deepl.com/v2/translate";

    @Bean
    public WebClient webClient() {
        return WebClient.create(FREE_DEEPL_COM_V_2_TRANSLATE);
    }
}
