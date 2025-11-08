package com.yadnyesh.benchmark.Service;

import com.yadnyesh.benchmark.model.Request;
import com.yadnyesh.benchmark.model.Response;
import com.yadnyesh.benchmark.util.Helper;
import org.springframework.stereotype.Service;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



@Service("threadpool")
public class ThreadPoolBenchmarkStrategy implements BenchmarkStrategy {


  @Override
  public Response evaluate(Request request) throws Exception {
    int totalRequests = request.getRequests();
    int poolSize = Math.min(totalRequests, 100);
    ExecutorService executorService = Executors.newFixedThreadPool(poolSize);

    Response response = Helper.syncCodingRequestMakingHelper(
            totalRequests,
            request.getUrl(),
            "threadpool = sync coding + blocking",
            executorService
    );

    return response;
  }
}
