package tech.edwyn.async.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.edwyn.async.exception.InvalidUsernameException;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ReportGenerationUseCaseTest {

    @Autowired
    private ReportGenerationUseCase reportGenerationUseCase;

    private List<String> userNames;

    @BeforeEach
    void setUp() {
        userNames = Arrays.asList("User1", "User2", "User3");
    }

    @Test
    void should_generate_reports_and_return_its_names() {
        // When
        List<String> reportNames = reportGenerationUseCase.generateAndGetReportNames(userNames);

        // Then
        assertEquals(userNames.size(), reportNames.size());
        assertThat(reportNames).containsExactlyInAnyOrder("report_for_User1.pdf", "report_for_User2.pdf", "report_for_User3.pdf");
    }

    @Test
    void should_generate_reports_and_retry_if_fails() {
        // When
        var unknownName = List.of("unknown", "User1");
        var exception = assertThrows(CompletionException.class, () -> reportGenerationUseCase.generateAndGetReportNames(unknownName));

        // Then
        assertThat(exception.getCause()).isInstanceOf(InvalidUsernameException.class);
        assertThat(exception.getCause().getMessage()).isEqualTo("User name is unknown");
    }
}