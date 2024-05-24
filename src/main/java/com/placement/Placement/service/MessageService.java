package com.placement.Placement.service;

import com.placement.Placement.model.request.MessageRequest;
import com.placement.Placement.model.response.MessageResponse;
import org.springframework.http.ResponseEntity;

public interface MessageService {
    ResponseEntity<Object> getAll();
    MessageResponse findById(String id);
    ResponseEntity<Object> getById(String id);
    ResponseEntity<Object> getByCustomerId(String id);
    ResponseEntity<Object> openMessage(String id);
    ResponseEntity<Object> create(MessageRequest messageRequest);
    ResponseEntity<Object> update(MessageRequest messageRequest);
    ResponseEntity<Object> remove(String id);
}
