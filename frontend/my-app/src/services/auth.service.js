import axios from 'axios';
import jwtDecode from 'jwt-decode';

const API_URL = 'http://localhost:8080/api/auth/';

const login = async (username, password) => {
    const response = await axios.post(API_URL + 'signin', { username, password });
    if (response.data.accessToken) {
        localStorage.setItem('user', JSON.stringify(response.data));
    }
    return response.data;
};

const logout = () => {
    localStorage.removeItem('user');
};

const register = async (username, password) => {
    await axios.post(API_URL + 'signup', { username, password });
};

const getCurrentUser = () => {
    return JSON.parse(localStorage.getItem('user'));
};

const authService = {
    login,
    logout,
    register,
    getCurrentUser,
};

export default authService;
