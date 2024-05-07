import {useEffect} from "react";
import {axiosInstance} from "../requests/AxiosInstance.ts";
import {useCookies} from "react-cookie";
import {Link, useLocation, useNavigate} from "react-router-dom";


const Header = () => {

    const [cookie, setCookie] = useCookies(['token', 'profile']);
    const location = useLocation();
    const navigate = useNavigate();


    const checkAuth = () => {
        axiosInstance.defaults.headers.post['Authorization'] = `Token ${cookie.token}`;
        axiosInstance.post(
            'api/v1/check_auth/',
        )
            .then((response) => {
                setCookie('profile', response.data.profile);
                console.log(response);
            })
            .catch((error) => {console.log(error)})
    }

    useEffect(() => {
        if (!cookie.profile && cookie.token){
            checkAuth();
        }
        if (location.pathname !== '/login' && location.pathname !== '/register' && !cookie.token){
            setTimeout(() => {navigate('/login')}, 2000);
        }
    }, []);

    return (
        <div className={'header_container'}>
            <div className={'header_container_item'}>
                <Link to={`/`}
                      reloadDocument={true}
                      className={'custom_link'}>{'Home'}</Link>
            </div>
            <div className={'header_container_item'}>
                <Link to={`/tasks`}
                      reloadDocument={true}
                      className={'custom_link'}>{'Tasks'}</Link>
            </div>
            <div className={'header_container_item'}>
                <Link to={`/ratings`}
                      reloadDocument={true}
                      className={'custom_link'}>{'Ratings'}</Link>
            </div>
            {cookie.profile ?
                (<div className={'header_container_item'}>
                    <Link to={`../user/${cookie.profile.id}`}
                          reloadDocument={true}
                          className={'custom_link'}>{cookie.profile.user.username}</Link>
                </div>)
                :
                (<div className={'header_container_item'}>
                    <Link to={`/login`} className={'custom_link'}>Login</Link>
                </div>)
            }
        </div>
    );
};

export default Header;