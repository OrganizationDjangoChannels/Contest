import React, { useState } from 'react';
import {axiosInstance} from "./AxiosInstance.ts";
import {useCookies} from 'react-cookie';
import {setToken} from "./Token.ts";
import {Link, useNavigate} from "react-router-dom";
import Header from "./Header.tsx";



interface User {
    username: string;
    password: string;
    repeat_password: string;
}

const RegisterForm: React.FC = () => {
    const [, setCookie] = useCookies(['token']);
    const [user, setUser] = useState<User>({
        username: '',
        password: '',
        repeat_password: ''
    });
    const navigate = useNavigate();

    const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = event.target;
            setUser((prevUser) => ({
                ...prevUser,
                [name]: value,
            }))


    }

    const handleSubmit = async (event: React.FormEvent) => {
        if (user.password == user.repeat_password) {
            event.preventDefault();

            const response  = await axiosInstance.post(
                'api/v1/register/',
                {
                    'username' : user.username,
                    'password' : user.password
                }
            );
            await setToken(setCookie,'token', response.data.token);
            axiosInstance.defaults.headers.post['Authorization'] = `Token ${response.data.token}`;
            navigate('/');


        }
        // Добавьте здесь логику отправки данных на сервер
    };

    return (
        <>
            <Header/>
            <form onSubmit={handleSubmit}>
                <div className={"input-container"}>
                    <label htmlFor="username" className={"input-label"}>Username:</label>
                    <input
                        className={"input-field"}
                        type="text"
                        id="username"
                        name="username"
                        value={user.username}
                        onChange={handleInputChange}
                    />
                </div>
                <div className={"input-container"}>
                    <label htmlFor="password" className={"input-label"}>Password:</label>
                    <input
                        className={"input-field"}
                        type="password"
                        id="password"
                        name="password"
                        value={user.password}
                        onChange={handleInputChange}
                    />
                </div>
                <div className={"input-container"}>
                    <label htmlFor="password" className={"input-label"}>Repeat password:</label>
                    <input
                        className={"input-field"}
                        type="password"
                        id="repeat_password"
                        name="repeat_password"
                        value={user.repeat_password}
                        onChange={handleInputChange}
                    />
                </div>
                <button type="submit" className={"submit-button"}>Register</button>
            </form>
            <div className={'login_form_footer'}>
                Already have an account? &nbsp; &nbsp;
                <Link to={`/login`} className={'custom_link'}>Log in</Link>
            </div>
        </>
    );
};

export default RegisterForm;
