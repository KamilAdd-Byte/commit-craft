package pl.commit.craft.translate;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
class DeeplResponse {

    private List<Translation> translations;

   @Getter
   @Setter
   static class Translation {
        private String detectedSourceLanguage;
        private String text;
    }
}