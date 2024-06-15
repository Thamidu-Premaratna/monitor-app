import React, { useEffect, useState } from 'react';
import sensorService from '../services/sensor.service';
import SensorData from './SensorData';
import SensorChart from './SensorChart';

const Dashboard = () => {
    const [sensorData, setSensorData] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const data = await sensorService.getSensorData();
                setSensorData(data);
            } catch (error) {
                console.error('Failed to fetch sensor data', error);
            }
        };

        fetchData();
    }, []);

    return (
        <div>
            <h2>Dashboard</h2>
            <SensorChart data={sensorData} />
            <SensorData data={sensorData} />
        </div>
    );
};

export default Dashboard;
