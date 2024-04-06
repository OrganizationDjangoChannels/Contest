import {useCookies} from "react-cookie";
import {useEffect, useState} from "react";
import {axiosInstance} from "./AxiosInstance.ts";
import {Profile} from "./types.ts";
import RatingsItem from "./RatingsItem.tsx";

const Ratings = () => {
    const [cookie] = useCookies(['token']);
    const [ratings, setRatings] = useState<Profile[]>();

    useEffect( () => {
        axiosInstance.defaults.headers.get['Authorization'] = `Token ${cookie.token}`;
        axiosInstance.get(
            'api/v1/ratings/',
        )
            .then((response) => {
                setRatings(response.data);
                console.log(response);
            })
            .catch((error) => {console.log(error)})

    }, []);
    return (
        <>
            <h2>Ratings</h2>
            <table className={'solutions_table'}>
                <thead>
                <tr>
                    <td>Position</td>
                    <td>Username</td>
                    <td>Total points</td>
                </tr>
                </thead>
                <tbody>
                {ratings ? ratings.map((profile: Profile, index) => (
                    <RatingsItem profile={profile} index={index} key={profile.id}/>
                )) : ''}

                </tbody>

            </table>

        </>
    );
};

export default Ratings;