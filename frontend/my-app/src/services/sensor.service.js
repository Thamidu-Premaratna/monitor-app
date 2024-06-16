// services/sensor.service.js
import axios from 'axios';

const API_URL = '/api/v1/sensor/sensor-data/get/all';

const getSensorData = async () => {
    const response = await axios.get(`${API_URL}`);
    return response.data;
};

const sensorService = {
    getSensorData,
};

export default sensorService;
