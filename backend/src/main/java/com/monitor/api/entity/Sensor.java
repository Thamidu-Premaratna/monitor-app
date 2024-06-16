package com.monitor.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String type;
    private String location;
    private String status;

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL, orphanRemoval = true) // One sensor can have many sensor data
    @JsonBackReference // To prevent infinite loop
    private List<SensorData> data;

}