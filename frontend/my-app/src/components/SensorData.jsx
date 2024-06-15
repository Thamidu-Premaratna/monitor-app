import React from 'react';

const SensorData = ({ data }) => {
    return (
        <div>
            <h3>Sensor Data</h3>
            <ul>
                {data.map((sensor) => (
                    <li key={sensor.id}>
                        {sensor.sensorId}: {sensor.dataValue} at {new Date(sensor.date).toLocaleString()}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default SensorData;
