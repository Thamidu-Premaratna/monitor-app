package com.monitor.api.services;

import com.monitor.api.dto.SensorDTO;
import com.monitor.api.dto.SensorDataDTO;
import com.monitor.api.dto.response.SuccessResponse;
import com.monitor.api.entity.Sensor;
import com.monitor.api.entity.SensorData;
import com.monitor.api.exceptions.SensorAlreadyExistsException;
import com.monitor.api.exceptions.SensorNotFoundException;
import com.monitor.api.repository.SensorDataRepository;
import com.monitor.api.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository sensorRepository;
    private final SensorDataRepository sensorDataRepository;

    // ----------------------- GET Sensor Data - start -----------------------

    //Get sensor data between two dates
    public ResponseEntity<List<SensorDataDTO>> getSensorDataBetweenDatesAndBySensorId(Integer sensorId, String startDate, String endDate) throws SensorNotFoundException {
        // Find sensor by id
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new SensorNotFoundException("Sensor not found"));
        // Get sensor data between two dates from the database
        List<SensorData> sensorDataList = sensorDataRepository
                .findAllByDateBetweenAndSensor(LocalDateTime.parse(startDate), LocalDateTime.parse(endDate), sensor);
        return ResponseEntity.ok(convertToDTOList(sensorDataList));
    }

    // Get all sensor data by date from the database
    public ResponseEntity<List<SensorDataDTO>> getAllSensorDataBySensorIdAndDate(Integer sensorId, String date) throws SensorNotFoundException {
        // Find sensor by id
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new SensorNotFoundException("Sensor not found"));
        // Get all sensor data by date from the database
        List<SensorData> sensorDataList = sensorDataRepository.findAllByDateAndSensor(LocalDateTime.parse(date), sensor);
        return ResponseEntity.ok(convertToDTOList(sensorDataList));
    }

    // Get all sensor data from the database
    public ResponseEntity<List<SensorDataDTO>> getAllSensorDataBySensorId(Integer sensorId) throws SensorNotFoundException {
        // Find sensor by id
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new SensorNotFoundException("Sensor not found"));
        // Get all sensor data from the database
        List<SensorData> sensorDataList = sensorDataRepository.findAllBySensor(sensor);
        return ResponseEntity.ok(convertToDTOList(sensorDataList));
    }

    // ----------------------- GET Sensor Data - end -----------------------

    // ----------------------- Save Sensor Data and Sensor - start -----------------------

    // Save sensor data to the database
    public ResponseEntity<SuccessResponse> saveSensorData(SensorDataDTO sensorDataDTO) throws SensorNotFoundException {
        // Check if sensor exists in the database
        Sensor sensor = sensorRepository.findById(sensorDataDTO.getSensor_id())
                .orElseThrow(() -> new SensorNotFoundException("Sensor not found"));

        // Trigger alerts
        checkAndTriggerAlerts(sensor, sensorDataDTO);

        // Create a new sensor data entity
        SensorData sensorData = SensorData.builder()
                .sensor(sensor)
                .date(LocalDateTime.parse(sensorDataDTO.getDate()))
                .dataValue(sensorDataDTO.getData_value())
                .build();
        sensorDataRepository.save(sensorData); // Save sensor data to the database
        return ResponseEntity.ok(SuccessResponse.builder()
                .message("Sensor data saved successfully")
                .build());
    }

    // Save sensor to the database
    public ResponseEntity<SuccessResponse> saveSensor(SensorDTO sensorDTO) throws SensorAlreadyExistsException {
        // Check if sensor already exists in the database by location
        if (sensorRepository.findByLocation(sensorDTO.getLocation()).isPresent()) {
            throw new SensorAlreadyExistsException("Sensor already exists with the given location");
        }

        // Create a new sensor entity
        Sensor sensor = Sensor.builder()
                .name(sensorDTO.getName())
                .type(sensorDTO.getType())
                .location(sensorDTO.getLocation())
                .status(sensorDTO.getStatus())
                .data(null) // When creating a new sensor, the data will be empty
                .build();
        sensorRepository.save(sensor); // Save sensor to the database
        return ResponseEntity.ok(SuccessResponse.builder()
                .message("Sensor saved successfully")
                .build());
    }

    // ----------------------- Save Sensor Data - end -----------------------

    // ----------------------- Util functions - start -----------------------

    // Trigger functions for the sensor data

    public void checkAndTriggerAlerts(Sensor senor, SensorDataDTO sensorDataDTO) {
        // If the sensor's type is temperature, then the data value will always be a temperature value in the format where the last character is 'C'
        // So remover the last character 'C' and convert the remaining string to a double and store it in a Double variable

        double dataValue = getDataValue(senor, sensorDataDTO);

        // Check if the data value is above the threshold
        if (dataValue > 35) {
            // Trigger alert
            System.out.println("Alert triggered for sensor: " + senor.getName() + " with data value: " + sensorDataDTO.getData_value());

            // Send email to the user
            // sendEmail(senor, sensorDataDTO);
            // print the alert for sms and call functions

        }
    }

    private static double getDataValue(Sensor senor, SensorDataDTO sensorDataDTO) {
        double dataValue = 0;
        if (senor.getType().equalsIgnoreCase("temperature")) { // Check if the sensor type is temperature
            dataValue = Double.parseDouble(sensorDataDTO.getData_value().substring(0, sensorDataDTO.getData_value().length() - 1));
        }
        return dataValue;
    }

    // Convert sensor entity to sensor DTO
    private SensorDataDTO convertToDTO(SensorData sensordata) {

        // Check if date is null, and return null if it is otherwise parse the date
        String dateValue = sensordata.getDate() != null ? String.valueOf(sensordata.getDate()) : null;

        return SensorDataDTO.builder()
                .sensor_id(sensordata.getId())
                .date(dateValue)
                .data_value(sensordata.getDataValue())
                .build();
    }

    // Convert List of sensor entities to List of sensor DTOs
    private List<SensorDataDTO> convertToDTOList(List<SensorData> sensorDataList) {
        return sensorDataList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ----------------------- Util functions - end -----------------------
}SensorService.java

package com.monitor.api.services;

import com.monitor.api.dto.SensorDTO;
import com.monitor.api.dto.SensorDataDTO;
import com.monitor.api.dto.response.SuccessResponse;
import com.monitor.api.entity.Sensor;
import com.monitor.api.entity.SensorData;
import com.monitor.api.exceptions.SensorAlreadyExistsException;
import com.monitor.api.exceptions.SensorNotFoundException;
import com.monitor.api.repository.SensorDataRepository;
import com.monitor.api.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository sensorRepository;
    private final SensorDataRepository sensorDataRepository;

    // ----------------------- GET Sensor Data - start -----------------------

    //Get sensor data between two dates
    public ResponseEntity<List<SensorDataDTO>> getSensorDataBetweenDatesAndBySensorId(Integer sensorId, String startDate, String endDate) throws SensorNotFoundException {
        // Find sensor by id
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new SensorNotFoundException("Sensor not found"));
        // Get sensor data between two dates from the database
        List<SensorData> sensorDataList = sensorDataRepository
                .findAllByDateBetweenAndSensor(LocalDateTime.parse(startDate), LocalDateTime.parse(endDate), sensor);
        return ResponseEntity.ok(convertToDTOList(sensorDataList));
    }

    // Get all sensor data by date from the database
    public ResponseEntity<List<SensorDataDTO>> getAllSensorDataBySensorIdAndDate(Integer sensorId, String date) throws SensorNotFoundException {
        // Find sensor by id
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new SensorNotFoundException("Sensor not found"));
        // Get all sensor data by date from the database
        List<SensorData> sensorDataList = sensorDataRepository.findAllByDateAndSensor(LocalDateTime.parse(date), sensor);
        return ResponseEntity.ok(convertToDTOList(sensorDataList));
    }

    // Get all sensor data from the database
    public ResponseEntity<List<SensorDataDTO>> getAllSensorDataBySensorId(Integer sensorId) throws SensorNotFoundException {
        // Find sensor by id
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new SensorNotFoundException("Sensor not found"));
        // Get all sensor data from the database
        List<SensorData> sensorDataList = sensorDataRepository.findAllBySensor(sensor);
        return ResponseEntity.ok(convertToDTOList(sensorDataList));
    }

    // ----------------------- GET Sensor Data - end -----------------------

    // ----------------------- Save Sensor Data and Sensor - start -----------------------

    // Save sensor data to the database
    public ResponseEntity<SuccessResponse> saveSensorData(SensorDataDTO sensorDataDTO) throws SensorNotFoundException {
        // Check if sensor exists in the database
        Sensor sensor = sensorRepository.findById(sensorDataDTO.getSensor_id())
                .orElseThrow(() -> new SensorNotFoundException("Sensor not found"));

        // Trigger alerts
        checkAndTriggerAlerts(sensor, sensorDataDTO);

        // Create a new sensor data entity
        SensorData sensorData = SensorData.builder()
                .sensor(sensor)
                .date(LocalDateTime.parse(sensorDataDTO.getDate()))
                .dataValue(sensorDataDTO.getData_value())
                .build();
        sensorDataRepository.save(sensorData); // Save sensor data to the database
        return ResponseEntity.ok(SuccessResponse.builder()
                .message("Sensor data saved successfully")
                .build());
    }

    // Save sensor to the database
    public ResponseEntity<SuccessResponse> saveSensor(SensorDTO sensorDTO) throws SensorAlreadyExistsException {
        // Check if sensor already exists in the database by location
        if (sensorRepository.findByLocation(sensorDTO.getLocation()).isPresent()) {
            throw new SensorAlreadyExistsException("Sensor already exists with the given location");
        }

        // Create a new sensor entity
        Sensor sensor = Sensor.builder()
                .name(sensorDTO.getName())
                .type(sensorDTO.getType())
                .location(sensorDTO.getLocation())
                .status(sensorDTO.getStatus())
                .data(null) // When creating a new sensor, the data will be empty
                .build();
        sensorRepository.save(sensor); // Save sensor to the database
        return ResponseEntity.ok(SuccessResponse.builder()
                .message("Sensor saved successfully")
                .build());
    }

    // ----------------------- Save Sensor Data - end -----------------------

    // ----------------------- Util functions - start -----------------------

    // Trigger functions for the sensor data

    public void checkAndTriggerAlerts(Sensor senor, SensorDataDTO sensorDataDTO) {
        // If the sensor's type is temperature, then the data value will always be a temperature value in the format where the last character is 'C'
        // So remover the last character 'C' and convert the remaining string to a double and store it in a Double variable

        double dataValue = getDataValue(senor, sensorDataDTO);

        // Check if the data value is above the threshold
        if (dataValue > 35) {
            // Trigger alert
            System.out.println("Alert triggered for sensor: " + senor.getName() + " with data value: " + sensorDataDTO.getData_value());

            // Send email to the user
            // sendEmail(senor, sensorDataDTO);
            // print the alert for sms and call functions

        }
    }

    private static double getDataValue(Sensor senor, SensorDataDTO sensorDataDTO) {
        double dataValue = 0;
        if (senor.getType().equalsIgnoreCase("temperature")) { // Check if the sensor type is temperature
            dataValue = Double.parseDouble(sensorDataDTO.getData_value().substring(0, sensorDataDTO.getData_value().length() - 1));
        }
        return dataValue;
    }

    // Convert sensor entity to sensor DTO
    private SensorDataDTO convertToDTO(SensorData sensordata) {

        // Check if date is null, and return null if it is otherwise parse the date
        String dateValue = sensordata.getDate() != null ? String.valueOf(sensordata.getDate()) : null;

        return SensorDataDTO.builder()
                .sensor_id(sensordata.getId())
                .date(dateValue)
                .data_value(sensordata.getDataValue())
                .build();
    }

    // Convert List of sensor entities to List of sensor DTOs
    private List<SensorDataDTO> convertToDTOList(List<SensorData> sensorDataList) {
        return sensorDataList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ----------------------- Util functions - end -----------------------
}