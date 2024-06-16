package com.monitor.api.controller;

import com.monitor.api.dto.SensorDTO;
import com.monitor.api.dto.SensorDataDTO;
import com.monitor.api.dto.response.SuccessResponse;
import com.monitor.api.exceptions.SensorAlreadyExistsException;
import com.monitor.api.exceptions.SensorNotFoundException;
import com.monitor.api.services.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("sensor")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class SensorController {
    private final SensorService sensorService;

    // Get all sensor data
    @GetMapping("/sensor-data/get/all/{sensorId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllSensorDataBySensorId(
            @PathVariable Integer sensorId
    ) throws SensorNotFoundException {
        return sensorService.getAllSensorDataBySensorId(sensorId);
    }

    // Get sensor data by date
    @GetMapping("/sensor-data/get/by-date/{sensorId}/{date}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllSensorDataBySensorIdAndDate(
            @PathVariable Integer sensorId,
            @PathVariable String date
    ) throws SensorNotFoundException {

        return sensorService.getAllSensorDataBySensorIdAndDate(sensorId,date);
    }

    // Get sensor data between two dates
    @GetMapping("/sensor-data/get/between-dates/{sensorId}/{startDate}/{endDate}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getSensorDataBySensorIdAndBetweenDates(
            @PathVariable Integer sensorId,
            @PathVariable String startDate,
            @PathVariable String endDate
    ) throws SensorNotFoundException {
        return sensorService.getSensorDataBetweenDatesAndBySensorId(sensorId, startDate, endDate);
    }

    // Save sensor data
    @PostMapping("/sensor-data/save")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SuccessResponse> saveSensorData(
            @RequestBody SensorDataDTO sensorDataDTO
    ) throws SensorNotFoundException {
        return ResponseEntity.ok(sensorService.saveSensorData(sensorDataDTO).getBody());
    }

    // Save sensor
    @PostMapping("/save")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SuccessResponse> saveSensor(
            @RequestBody SensorDTO sensorDTO
    ) throws SensorAlreadyExistsException {
        return ResponseEntity.ok(sensorService.saveSensor(sensorDTO).getBody());
    }


}