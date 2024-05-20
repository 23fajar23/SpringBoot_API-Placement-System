package com.placement.Placement.service;

import com.placement.Placement.model.request.TestRequest;
import com.placement.Placement.model.response.GetTestResponse;
import com.placement.Placement.model.response.TestRemoveResponse;
import com.placement.Placement.model.response.TestResponse;

import java.util.List;

public interface TestService {
    List<GetTestResponse> getAll();
    GetTestResponse getById(String id);
    TestResponse create(TestRequest testRequest);
    TestResponse update(TestRequest testRequest);
    TestRemoveResponse remove(String id);
}
