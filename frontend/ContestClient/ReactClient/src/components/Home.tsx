import {useCookies} from "react-cookie";
import {useState} from "react";
import {axiosInstance} from "./AxiosInstance.ts";

interface IUser{
    user_id: number,
    username: string
}

const Home = () => {
    const [cookie] = useCookies(['token']);
    console.log(cookie);
    const [user, setUser] = useState<IUser>();
    const checkAuth = async () => {
        axiosInstance.defaults.headers.post['Authorization'] = `Token ${cookie.token}`;
        const response =  await axiosInstance.post(
            'api/v1/check_auth/'
        );
        console.log(response);
        setUser(response.data);
    }

    return (
        <>
            <div>
                Home Page
            </div>
            <div>
                user_id = {user ? user.user_id : ''}
            </div>
            <div>
                username = {user ? user.username : ''}
            </div>
            <button onClick={checkAuth}>
                check auth
            </button>
        </>

    );
};

export default Home;