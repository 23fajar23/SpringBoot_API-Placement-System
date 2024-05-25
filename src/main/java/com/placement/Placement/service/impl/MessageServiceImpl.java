package com.placement.Placement.service.impl;

import com.placement.Placement.constant.EMessage;
import com.placement.Placement.helper.convert.entity.Entity;
import com.placement.Placement.helper.response.Response;
import com.placement.Placement.model.entity.Message;
import com.placement.Placement.model.entity.auth.Customer;
import com.placement.Placement.model.request.MessageRequest;
import com.placement.Placement.model.response.CustomerResponse;
import com.placement.Placement.model.response.MessageResponse;
import com.placement.Placement.repository.MessageRepository;
import com.placement.Placement.service.MessageService;
import com.placement.Placement.service.auth.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final CustomerService customerService;

    @Override
    public ResponseEntity<Object> getAll() {
        List<MessageResponse> messageResponseList = messageRepository.findAll().stream()
                .map(Entity::convertToDto).toList();
        return Response.responseData(HttpStatus.OK, "Successfully get all message", messageResponseList, null);
    }

    @Override
    public MessageResponse findById(String id) {
        Message message = messageRepository.findById(id).orElse(null);
        if (message != null) {
            return Entity.convertToDto(message);
        }

        return null;
    }

    @Override
    public ResponseEntity<Object> getById(String id) {
        Message message = messageRepository.findById(id).orElse(null);
        if (message != null) {
            return Response.responseData(HttpStatus.OK, "Successfully get message", message, null);
        }else{
            return Response.responseData(HttpStatus.NOT_FOUND, "Message not found", null, null);
        }
    }

    @Override
    public ResponseEntity<Object> getByCustomerId(String id) {
        List<MessageResponse> messageResponseList = messageRepository.findByCustomer(id).stream()
                .map(Entity::convertToDto).toList();
        return Response.responseData(HttpStatus.OK, "Successfully get customer message", messageResponseList, null);
    }

    @Override
    public ResponseEntity<Object> openMessage(String id) {
        Message message = messageRepository.findById(id).orElse(null);
        if (message != null) {
            message.setRead(EMessage.READ);
            messageRepository.save(message);
            return Response.responseData(HttpStatus.OK, "Successfully open message", null, null);
        }else{
            return Response.responseData(HttpStatus.NOT_FOUND, "Message not found", null, null);
        }
    }

    @Override
    public ResponseEntity<Object> create(MessageRequest messageRequest) {
        CustomerResponse customerResponse = customerService.findById(messageRequest.getCustomer_id());
        try {
            if (customerResponse != null) {
                Customer customer = Customer.builder()
                        .id(customerResponse.getId())
                        .name(customerResponse.getName())
                        .address(customerResponse.getAddress())
                        .mobilePhone(customerResponse.getPhoneNumber())
                        .batch(customerResponse.getBatch())
                        .education(customerResponse.getEducation())
                        .build();

                Message message = Message.builder()
                        .recipient(customer)
                        .sender(messageRequest.getSender())
                        .content(messageRequest.getContent())
                        .dateTime(LocalDateTime.now())
                        .read(EMessage.ACCEPTED)
                        .status(messageRequest.getStatus())
                        .build();

                messageRepository.save(message);
                return Response.responseData(HttpStatus.OK, "Successfully send message", Entity.convertToDto(message), null);
            }else{
                return Response.responseData(HttpStatus.FOUND, "Recipient not found", null, null);
            }
        }catch (Exception e) {
            return Response.responseData(HttpStatus.BAD_REQUEST, "Status Invalid", null, null);
        }
    }

    @Override
    public ResponseEntity<Object> update(MessageRequest messageRequest) {
        Message message = messageRepository.findById(messageRequest.getId()).orElse(null);
        try {
            if (message != null) {

                if (message.getRead() == EMessage.READ){
                    return Response.responseData(HttpStatus.BAD_REQUEST, "Message has been read", null, null);
                }

                message.setContent(messageRequest.getContent());
                message.setStatus(messageRequest.getStatus());
                messageRepository.save(message);
                return Response.responseData(HttpStatus.OK, "Successfully update message", Entity.convertToDto(message), null);
            }else{
                return Response.responseData(HttpStatus.FOUND, "Message not found", null, null);
            }
        }catch (Exception e) {
            return Response.responseData(HttpStatus.BAD_REQUEST, "Status Invalid", null, null);
        }
    }

    @Override
    public ResponseEntity<Object> remove(String id) {
        Message message = messageRepository.findById(id).orElse(null);
        if (message != null) {
            messageRepository.deleteById(id);
            return Response.responseData(HttpStatus.OK, "Successfully delete message", null, null);
        }else{
            return Response.responseData(HttpStatus.NOT_FOUND, "Message not found", null, null);
        }
    }
}
