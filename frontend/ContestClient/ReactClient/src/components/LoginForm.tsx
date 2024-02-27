import React, { useState } from 'react';
import {axiosInstance} from "./AxiosInstance.ts";
import {useCookies} from "react-cookie";
import {useNavigate} from "react-router-dom";
import {setToken} from "./Token.ts";


interface User {
    username: string;
    password: string;
}



const LoginForm: React.FC = () => {
    const [cookie, setCookie] = useCookies(['token']);
    const [user, setUser] = useState<User>({
        username: '',
        password: ''
    });
    const navigate = useNavigate();


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

            await setToken(setCookie,'token', response.data.token);

            axiosInstance.defaults.headers.post['Authorization'] = `Token ${response.data.token}`;

            navigate('/');


        }
        // Добавьте здесь логику отправки данных на сервер


    return (
        <form onSubmit={doAuth}>
            <div className={"input-container"}>
                <label className={"input-label"} htmlFor="username">Username:</label>
                <input
                    className={"input-field"}
                    type="text"
                    id="login_username"
                    name="username"
                    value={user.username}
                    onChange={handleInputChange}
                />
            </div>
            <div className={"input-container"}>
                <label className={"input-label"} htmlFor="password">Password:</label>
                <input
                    className={"input-field"}
                    type="password"
                    id="login_password"
                    name="password"
                    value={user.password}
                    onChange={handleInputChange}
                />
            </div>

            <button type="submit" className={"submit-button"}>Sign in</button>
            <div>
                {cookie.token}
            </div>
        </form>
    );
};

export default LoginForm;
