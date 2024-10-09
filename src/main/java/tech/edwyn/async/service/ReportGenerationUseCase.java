
package tech.edwyn.async.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class ReportGenerationUseCase {

    @Autowired
    private ReportService reportService;

    public List<String> waitAndGetReportNames(List<String> userNames) {
        var reportFutures = generateReportsForAllUsers(userNames);

        return reportFutures.stream()
                .map(CompletableFuture::join) // Wait for all futures to complete
                .collect(Collectors.toList());
    }

    private List<CompletableFuture<String>> generateReportsForAllUsers(List<String> userNames) {
        return userNames.stream()
                .map(reportService::generateReport)
                .collect(Collectors.toList());
    }
}