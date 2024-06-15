import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8080/api/sensors/';

const getSensorData = async () => {
    const response = await axios.get(API_URL, { headers: authHeader() });
    return response.data;
};

const sensorService = {
    getSensorData,
};

export default sensorService;
