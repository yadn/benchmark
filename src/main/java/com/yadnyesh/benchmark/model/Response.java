package com.yadnyesh.benchmark.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {
  private String mechanism;
  private long totalTimeMilli;
  private double throughput;
  private int successCount;
  private int totalRequests;
}
