package com.placement.Placement.helper.response;

import com.placement.Placement.model.response.CommonResponse;
import com.placement.Placement.model.response.PagingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Response {
    public static ResponseEntity<Object> responseData(HttpStatus httpStatus, String message, Object data, PagingResponse pagingResponse) {
        CommonResponse<Object> response = CommonResponse.<Object>builder()
                .status(httpStatus.value())
                .message(message)
                .data(data)
                .pagingResponse(pagingResponse)
                .build();

        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }
}
