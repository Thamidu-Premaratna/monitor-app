package com.monitor.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SensorDTO {
    private Integer id;
    private String name;
    private String type;
    private String location;
    private String status;
    private List<SensorDataDTO> data;
}
