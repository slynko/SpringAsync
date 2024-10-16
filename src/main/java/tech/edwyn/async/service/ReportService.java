package tech.edwyn.async.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.edwyn.async.exception.InvalidUsernameException;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ReportService {
    @Async
    @Retryable(
            retryFor = {InvalidUsernameException.class}, // The exception types to retry on
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public CompletableFuture<String> generateReport(String userName) {
        log.info("Starting report generation for: {} in thread: {}", userName, Thread.currentThread().getName());
        verifyUsername(userName);
        sleepFor(1000L);
        var reportName = "report_for_" + userName + ".pdf";

        log.info("Finished generating report: {}", reportName);

        return CompletableFuture.completedFuture(reportName);
    }

    private void sleepFor(Long millis) {
        try {
            log.info("{} is sleeping for {} ms", Thread.currentThread().getName(), millis);
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void verifyUsername(String userName) {
        if (userName.equalsIgnoreCase("Unknown")) {
            log.error("Username is not valid, throwing an exception");
            throw new InvalidUsernameException("User name is unknown");
        }
    }

}