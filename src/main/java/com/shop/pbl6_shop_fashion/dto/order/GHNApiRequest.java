package com.shop.pbl6_shop_fashion.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class GHNApiRequest {
    @JsonProperty("from_district_id")
    private int fromDistrictId;
    @JsonProperty("from_ward_code")
    private String fromWardCode;
    @JsonProperty("service_id")
    private int serviceId;
    @JsonProperty("service_type_id")
    private Integer serviceTypeId;
    @JsonProperty("to_district_id")
    private int toDistrictId;
    @JsonProperty("to_ward_code")
    private String toWardCode;
    private int height;
    private int length;
    private int weight;
    private int width;
    @JsonProperty("insurance_value")
    private int insuranceValue;
    @JsonProperty("cod_failed_amount")
    private int codFailedAmount;
    private String coupon; // Điều chỉnh kiểu dữ liệu nếu cần
}
