package pl.commit.craft.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class MajorNumberPreparerTest {
    @Test
    void shouldExtractMajorNumberFromValidLink() {
        // given
        String validLink = "https://apilia.atlassian.net/browse/SREK-2645";

        // when
        MajorNumberPreparer preparer = MajorNumberPreparer.of(validLink);

        // then
        assertThat(preparer.getMajorNumber().issueNumber()).isEqualTo("SREK-2645");
    }

    @Test
    void shouldThrowExceptionWhenLinkIsNull() {
        // given
        String invalidLink = null;

        // when & then
        assertThatThrownBy(() -> MajorNumberPreparer.of(invalidLink))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid issueNumber format");
    }

    @Test
    void shouldThrowExceptionWhenLinkHasNoSlash() {
        // given
        String invalidLink = "https:apilia.atlassian.net-browse-SREK-2645";

        // when & then
        assertThatThrownBy(() -> MajorNumberPreparer.of(invalidLink))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid issueNumber format");
    }

    @Test
    void shouldExtractCorrectMajorNumberFromComplexLink() {
        // given
        String complexLink = "https://some.other.domain/path/to/issue/SREK-2645";

        // when
        MajorNumberPreparer preparer = MajorNumberPreparer.of(complexLink);

        // then
        assertThat(preparer.getMajorNumber().issueNumber()).isEqualTo("SREK-2645");
    }

    @Test
    void shouldHandleLinkWithTrailingSlash() {
        // given
        String linkWithTrailingSlash = "https://apilia.atlassian.net/browse/SREK-2645/";

        // when
        MajorNumberPreparer preparer = MajorNumberPreparer.of(linkWithTrailingSlash);

        // then
        assertThat(preparer.getMajorNumber().issueNumber()).isEqualTo("");
    }
}
