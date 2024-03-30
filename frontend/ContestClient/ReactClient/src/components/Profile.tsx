import {Profile} from "./types.ts";
import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {axiosInstance} from "./AxiosInstance.ts";
import {useCookies} from "react-cookie";


const ProfileShow = () => {
    const [cookie] = useCookies(['token']);
    const {id} = useParams();
    const [profile, setProfile] = useState<Profile>();

    useEffect( () => {
        axiosInstance.defaults.headers.get['Authorization'] = `Token ${cookie.token}`;
        axiosInstance.get(
            `api/v1/profile/${id}/`,
        )
            .then((response) => {
                setProfile(response.data);
                console.log(response);
            })
            .catch((error) => {console.log(error)})

    }, []);
    return (
        <div>
            <h2>UserView</h2>
            <div>
                id = {profile?.id}
            </div>
            <div>
                {profile?.user.username}
            </div>
            <div>
                {profile?.points} total points
            </div>
        </div>
    );
};

export default ProfileShow;