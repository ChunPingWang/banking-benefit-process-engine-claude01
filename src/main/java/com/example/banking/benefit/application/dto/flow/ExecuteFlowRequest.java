package com.example.banking.benefit.application.dto.flow;

import com.example.banking.benefit.domain.model.common.CustomerData;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "流程執行請求")
public class ExecuteFlowRequest {
    
    @NotBlank(message = "流程ID不能為空")
    @Pattern(regexp = "^[a-zA-Z0-9-]+$", message = "流程ID只能包含字母、數字和連字符")
    @Schema(description = "流程ID", example = "f123-456-789")
    private String flowId;
    
    @NotBlank(message = "流程版本不能為空")
    @Pattern(regexp = "^\\d+\\.\\d+\\.\\d+$", message = "流程版本格式必須為 x.y.z")
    @Schema(description = "流程版本", example = "1.0.0")
    private String version;
    
    @NotNull(message = "客戶資料不能為空")
    @Valid
    @Schema(description = "客戶資料")
    private CustomerData customerData;
}