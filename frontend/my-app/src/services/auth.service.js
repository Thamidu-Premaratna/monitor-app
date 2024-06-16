import axios from 'axios';
import jwtDecode from 'jwt-decode';

const API_URL = 'http://localhost:8080/api/auth/';

const login = (email, password) => {
    return axios.post(API_URL + 'authenticate', {
        email,
        password
    }).then(response => {
        if (response.data.token) {
            localStorage.setItem('user', JSON.stringify(response.data));
        }
        return response.data;
    });
};

const logout = () => {
    localStorage.removeItem('user');
};

const register = (firstName, lastName, email, password, role) => {
    return axios.post(API_URL + 'register', {
        firstName,
        lastName,
        email,
        password,
        role
    });
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
