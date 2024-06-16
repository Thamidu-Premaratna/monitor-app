package com.monitor.api.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sensor_data")
public class SensorData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Unique identifier for the sensor data
    @ManyToOne
    @JoinColumn(name = "sensor_id")
    @JsonManagedReference
    private Sensor sensor; // Will be sensor_id in the SensorDataDTO
    private LocalDateTime date;
    private String dataValue;
}