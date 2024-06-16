package com.monitor.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SensorDataDTO {
    private Integer sensor_id;
    private String date;
    private String data_value;
}

