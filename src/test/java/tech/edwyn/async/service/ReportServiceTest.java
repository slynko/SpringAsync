
package tech.edwyn.async.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReportServiceTest {
    @Autowired
    private ReportService reportService;

    @Test
    void testGenerateReport() throws Exception {
        // Given
        var userName = "testUser";

        // When
        CompletableFuture<String> reportFuture = reportService.generateReport(userName);

        // Then
        String reportName = reportFuture.get(10, TimeUnit.SECONDS);
        assertThat(reportName).isEqualTo("report_for_testUser.pdf");
    }
}