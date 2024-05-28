package com.placement.Placement.model.response;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.placement.Placement.constant.EQuota;
import com.placement.Placement.model.entity.QuotaBatch;
import com.placement.Placement.model.entity.Stage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class QuotaResponse {
    private String id;
    private int total;
    private int available;
    private Stage stage;
    private EQuota type;
    private List<QuotaBatchResponse> quotaBatches;
}
