package com.yadnyesh.benchmark.Service;

import com.yadnyesh.benchmark.model.Request;
import com.yadnyesh.benchmark.model.Response;
import com.yadnyesh.benchmark.util.Helper;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service("virtual")
public class VirtualThreadBenchmarkStrategy implements BenchmarkStrategy {

  @Override
  public Response evaluate(Request request) throws Exception {
    int totalRequests = request.getRequests();
    ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
    Response response = Helper.syncCodingRequestMakingHelper(
            totalRequests,
            request.getUrl(),
            "virtual = synchronous coding + non block",
            executorService
    );
    return response;
  }

}
