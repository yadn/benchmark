package com.yadnyesh.benchmark.model;

import lombok.Data;

@Data
public class Request {
  private String url;
  private String mechanism;
  private int requests;
}
