import axios from "axios";
axios.defaults.withCredentials = true;
export const axiosInstance = axios.create({
    withCredentials: true,
    baseURL: import.meta.env.VITE_SERVER_URL,
})
