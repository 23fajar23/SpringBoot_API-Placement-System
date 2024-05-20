package com.placement.Placement.helper.response;

import com.placement.Placement.model.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Response {
    public static ResponseEntity<Object> responseData(HttpStatus httpStatus,String message, Object data) {
        CommonResponse<Object> response = CommonResponse.<Object>builder()
                .statusCode(httpStatus.value())
                .message(message)
                .data(data)
                .build();

        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }
}
