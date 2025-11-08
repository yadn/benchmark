package com.yadnyesh.benchmark.Service;

import com.yadnyesh.benchmark.model.Request;
import com.yadnyesh.benchmark.model.Response;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

@Service("reactive")
public class ReactiveBenchmarkStrategy implements BenchmarkStrategy{

  private final WebClient webClient = WebClient.builder().clientConnector(
          new ReactorClientHttpConnector(
                  HttpClient.create()
                          .compress(true)
                          .keepAlive(true)
          )
  ).build();

  @Override
  public Response evaluate(Request request) throws Exception {
    int totalRequests = request.getRequests();
    String url = request.getUrl();

    Instant overallStart = Instant.now();
    AtomicInteger successCount = new AtomicInteger(0);

    // Launch N async requests concurrently using WebFlux
    Flux.range(0, totalRequests)
            .flatMap(i -> sendRequest(url)
                    .doOnNext(success -> {
                      if (success) successCount.incrementAndGet();
                    })
            )
            .blockLast(); // Wait until all are done

    long totalTimeMilli = Instant.now().toEpochMilli() - overallStart.toEpochMilli();
    double throughput = (totalRequests * 1000.0) / totalTimeMilli;

    return Response.builder()
            .mechanism("reactive = async coding + non-blocking")
            .totalTimeMilli(totalTimeMilli)
            .throughput(throughput)
            .successCount(successCount.get())
            .totalRequests(totalRequests)
            .build();
  }
  private Mono<Boolean> sendRequest(String url) {
    return webClient.get()
            .uri(url)
            .retrieve()
            .toBodilessEntity()
            .map(resp -> resp.getStatusCode().is2xxSuccessful())
            .onErrorReturn(false);
  }
}
