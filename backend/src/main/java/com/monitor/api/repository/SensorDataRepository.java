
package com.monitor.api.repository;

import com.monitor.api.entity.Sensor;
import com.monitor.api.entity.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Integer> {
    List<SensorData> findAllByDateAndSensor(LocalDateTime date, Sensor sensor);

    List<SensorData> findAllByDateBetweenAndSensor(LocalDateTime date, LocalDateTime date2, Sensor sensor);

    List<SensorData> findAllBySensor(Sensor sensor);

}
