package com.yadnyesh.benchmark.Service;

import com.yadnyesh.benchmark.model.Request;
import com.yadnyesh.benchmark.model.Response;

public interface BenchmarkStrategy {

  Response evaluate(Request request) throws Exception;
}
