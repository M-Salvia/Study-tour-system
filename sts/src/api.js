// api.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api/', // 确保是正确的后端地址
  headers: {
    'Content-Type': 'application/json',
  }
});
//所有通过这个实例发送的HTTP请求都会将api作为前缀
export default api;
