import {useCookies} from "react-cookie";
import {useEffect, useState} from "react";
import {axiosInstance} from "./AxiosInstance.ts";
import {Profile} from "../types/types.ts";
import RatingsItem from "./RatingsItem.tsx";
import Header from "./Header.tsx";
import Pagination from "./Pagination.tsx";
import LoadingStatus from "./LoadingStatus.tsx";

const Ratings = () => {
    const [cookie] = useCookies(['token']);
    const [ratings, setRatings] = useState<Profile[]>();
    const [page, setPage] = useState<number>(0);
    const [loading, setLoading] = useState<boolean>(false);

    const getRatings = async() => {
        axiosInstance.defaults.headers.get['Authorization'] = `Token ${cookie.token}`;
        setLoading(true);
        try {
            const response = await axiosInstance.get('api/v1/ratings/',
                {params: {page: page},})
            setRatings(response.data);
            setLoading(false);
            return response;
        } catch (error) {
            setLoading(false);
            console.log(error);
        }


    }

    useEffect(() =>{
        if (cookie.token){
            getRatings().then(response => console.log(response));
        }
    }, [page])
    return (
        <div className={'main_container'}>
            <Header/>
            <h2>Ratings</h2>
            {ratings &&
            <table className={'solutions_table'}>
                <thead>
                <tr>
                    <td>Position</td>
                    <td>Username</td>
                    <td>Created at</td>
                    <td>Total points</td>
                </tr>
                </thead>
                <tbody>
                {ratings.map((profile: Profile, index) => (
                    <RatingsItem profile={profile} index={index}
                                 ratings={ratings} page={page} key={profile.id}/>
                ))}

                </tbody>

            </table>
            }
            <Pagination page={page} setPage={setPage}/>

            {loading && <LoadingStatus/>}

        </div>
    );
};

export default Ratings;