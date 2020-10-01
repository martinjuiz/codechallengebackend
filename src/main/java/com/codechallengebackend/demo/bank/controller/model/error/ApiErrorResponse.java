package com.codechallengebackend.demo.bank.controller.model.error;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ApiModel(description = "Object that represents a response body when an error occurs")
public class ApiErrorResponse {

    @ApiModelProperty(name = "Error code describing the failure", dataType = "string")
    private String errorCode;

    @ApiModelProperty(name = "Message error with a brief description of the failure", dataType = "string")
    private String message;
}
