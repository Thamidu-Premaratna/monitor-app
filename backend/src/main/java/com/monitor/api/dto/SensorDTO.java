package com.monitor.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class SensorDTO {
    private Integer sensor_id;
    private LocalDateTime date;
    private String data_value;
}
