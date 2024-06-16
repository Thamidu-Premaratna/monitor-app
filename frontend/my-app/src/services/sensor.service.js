// services/sensor.service.js
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/v1/sensor/sensor-data/get/all';

const getSensorData = async () => {
    const response = await axios.get(`${API_URL}/1`);
    return response.data;
};

const sensorService = {
    getSensorData,
};

export default sensorService;
