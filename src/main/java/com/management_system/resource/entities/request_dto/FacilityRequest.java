package com.management_system.resource.entities.request_dto;

import com.management_system.resource.infrastucture.constant.FacilityStatusEnum;
import com.management_system.resource.infrastucture.constant.FacilityTypeEnum;
import com.management_system.utilities.entities.api.request.ApiRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FacilityRequest extends ApiRequest implements Serializable {
    String id;
    String name;
    FacilityTypeEnum type;
    int quantity;
    String image;
    String note;
    FacilityStatusEnum status;
}
