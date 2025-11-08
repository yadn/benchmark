package com.yadnyesh.benchmark.controller;


import com.yadnyesh.benchmark.Service.BenchmarkStrategy;
import com.yadnyesh.benchmark.model.Request;
import com.yadnyesh.benchmark.model.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


@Controller
@RequiredArgsConstructor
class RestController {

  @Autowired
  private Map<String, BenchmarkStrategy> map;

  @RequestMapping(path = "/benchmark", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
  @ResponseBody
  public Response evaluate(@RequestBody Request request) throws Exception {
    BenchmarkStrategy strategy = map.get(request.getMechanism());
    return strategy.evaluate(request);
  }
}