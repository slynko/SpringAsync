package tech.edwyn.async.service;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.edwyn.async.exception.InvalidUsernameException;

import java.util.concurrent.CompletableFuture;

@Service
public class ReportService {

    @Async
    @Retryable(
            retryFor = {InvalidUsernameException.class}, // The exception types to retry on
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public CompletableFuture<String> generateReport(String userName) {
        System.out.printf("Starting report generation for: %s in thread: %s%n", userName, Thread.currentThread().getName());

        verifyUsername(userName);
        sleepFor(1000L);
        var reportName = "report_for_" + userName + ".pdf";

        System.out.println("Finished generating report: " + reportName);

        return CompletableFuture.completedFuture(reportName);
    }

    private void sleepFor(Long millis) {
        try {
            System.out.println("%s is sleeping for %s ms".formatted(Thread.currentThread().getName(), millis));
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void verifyUsername(String userName) {
        if (userName.equalsIgnoreCase("Unknown")) {
            System.out.println("Username is not valid, throwing an exception");
            throw new InvalidUsernameException("User name is unknown");
        }
    }

}