import React, { useState } from 'react';
import {axiosInstance} from "./AxiosInstance.ts";
import {useCookies} from "react-cookie";





interface User {
    username: string;
    password: string;
}

const LoginForm: React.FC = () => {
    const [cookies, setCookie] = useCookies(['token']);
    const [user, setUser] = useState<User>({
        username: '',
        password: ''
    });


    const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = event.target;
        setUser((prevUser) => ({
            ...prevUser,
            [name]: value,
        }))


    }

    const doAuth = async (event: React.FormEvent) => {
        event.preventDefault();

            const response  = await axiosInstance.post(
                'api/v1/login/',
                {
                    'username' : user.username,
                    'password' : user.password
                }
            );
            console.log(response);
            setCookie('token', response.data.token);
            axiosInstance.defaults.headers.post['Authorization'] = `Token ${cookies.token}`;




        }
        // Добавьте здесь логику отправки данных на сервер


    return (
        <form onSubmit={doAuth}>
            <div>
                <label htmlFor="username">Username:</label>
                <input
                    type="text"
                    id="login_username"
                    name="username"
                    value={user.username}
                    onChange={handleInputChange}
                />
            </div>
            <div>
                <label htmlFor="password">Password:</label>
                <input
                    type="password"
                    id="login_password"
                    name="password"
                    value={user.password}
                    onChange={handleInputChange}
                />
            </div>

            <button type="submit">Sign in</button>
        </form>
    );
};

export default LoginForm;
