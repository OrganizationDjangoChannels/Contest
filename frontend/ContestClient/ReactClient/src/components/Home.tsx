import {useCookies} from "react-cookie";
import {useState} from "react";
import {axiosInstance} from "./AxiosInstance.ts";
import {logout} from "./logout.ts";
import {useNavigate} from "react-router-dom";

interface IUser{
    user_id: number,
    username: string
}

const Home = () => {
    const [cookie, , removeCookie] = useCookies(['token']);
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

    const navigate = useNavigate();

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
            <div>
                <button onClick={checkAuth} className={'submit-button'}>
                    check auth
                </button>
            </div>

            <div>
                <button className={'submit-button'} onClick={() => {
                    logout(removeCookie);
                    navigate('/');
                }}>
                    logout
                </button>
            </div>

        </>

    );
};

export default Home;