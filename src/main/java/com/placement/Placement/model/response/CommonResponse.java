package com.placement.Placement.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> {
    private Integer status;
    private String message;
    private T data;
    private PagingResponse pagingResponse;
}
