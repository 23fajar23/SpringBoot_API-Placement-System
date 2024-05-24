package com.placement.Placement.controller;

import com.placement.Placement.constant.AppPath;
import com.placement.Placement.model.request.BatchRequest;
import com.placement.Placement.model.request.MessageRequest;
import com.placement.Placement.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = AppPath.API + AppPath.MESSAGE)
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<?> getAllMessage() {
        return messageService.getAll();
    }

    @GetMapping(AppPath.CUSTOMER + AppPath.BY_ID)
    public ResponseEntity<?> getByCustomerId(@PathVariable String id) {
        return messageService.getByCustomerId(id);
    }

    @GetMapping(AppPath.BY_ID)
    public ResponseEntity<?> getBatchById(@PathVariable String id) {
        return messageService.getById(id);
    }

    @GetMapping(AppPath.OPEN + AppPath.BY_ID)
    public ResponseEntity<?> openMessage(@PathVariable String id) {
        return messageService.openMessage(id);
    }

    @PostMapping
    public ResponseEntity<?> createBatch(@RequestBody MessageRequest messageRequest) {
        return messageService.create(messageRequest);
    }

    @PutMapping
    public ResponseEntity<?> updateBatch(@RequestBody MessageRequest messageRequest) {
        return messageService.update(messageRequest);
    }

    @DeleteMapping(AppPath.BY_ID)
    public ResponseEntity<?> deleteBatch(@PathVariable String id) {
        return messageService.remove(id);
    }
}
