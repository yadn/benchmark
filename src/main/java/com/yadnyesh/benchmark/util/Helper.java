package com.yadnyesh.benchmark.util;

import com.yadnyesh.benchmark.model.Response;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class Helper {

  private Helper() {

  }

  public static Response syncCodingRequestMakingHelper(int totalRequests,
                                                       String url,
                                                       String mechanism,
                                                       ExecutorService executorService) throws Exception {
    HttpClient httpClient = HttpClient.newHttpClient();
    HttpRequest httpRequest = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build();

    // using countDownLatch, since we are calling many requessts here and we want to wait until all threads finish the work..
    CountDownLatch countDownLatch = new CountDownLatch(totalRequests);

    Instant overallStart = Instant.now();
    AtomicInteger successCount = new AtomicInteger(0);
    for (int i = 0; i < totalRequests; i++) {
      executorService.submit(() -> {
        try {
          HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
          if (response.statusCode() == 200) {
            successCount.incrementAndGet();
          } else {
            // System.out.println("Failed at index: " + index + " with status code: " + response.statusCode());
          }
        } catch (Exception e) {
          // System.out.println("Error: " + e.getMessage());
        } finally {
          countDownLatch.countDown(); // decreasing count once a thread finishes its work
        }
      });
    }
    countDownLatch.await(); // this is basically checking if we came back to count 0
    Instant overallEnd = Instant.now();
    executorService.shutdown();
    long totalTimeMilli = overallEnd.toEpochMilli() - overallStart.toEpochMilli();
    double throughput = (double) totalRequests / (totalTimeMilli / 1000.0);
    return Response.builder()
            .mechanism("virtual = synchronous coding + non block")
            .totalTimeMilli(totalTimeMilli)
            .throughput(throughput)
            .successCount(successCount.get())
            .totalRequests(totalRequests)
            .build();
  }
}
