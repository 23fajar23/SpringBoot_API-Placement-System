package com.placement.Placement.model.response;

import com.placement.Placement.constant.EMessage;
import com.placement.Placement.constant.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private String id;
    private String customer_id;
    private String sender;
    private String content;
    private EMessage read;
    private EStatus status;
}
