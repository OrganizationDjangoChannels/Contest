import {Profile, SolutionShowType} from "./types.ts";
import {TaskShow as TaskShowType} from "./types.ts";
import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {axiosInstance} from "./AxiosInstance.ts";
import {useCookies} from "react-cookie";
import SolutionsTable from "./SolutionsTable.tsx";
import TaskShowShort from "./TaskShowShort.tsx";
import {logout} from "./logout.ts";
import Header from "./Header.tsx";


const ProfileShow = () => {
    const [cookie, ,removeCookie] = useCookies(['token', 'profile']);
    const navigate = useNavigate();
    const {id} = useParams();
    const [profile, setProfile] = useState<Profile>();
    const [mySolutions, setMySolutions] =
        useState<SolutionShowType[]>();
    const [solvedTasks, setSolvedTasks] =
        useState<TaskShowType[]>();

    const getProfile = () => {
        axiosInstance.defaults.headers.get['Authorization'] = `Token ${cookie.token}`;
        axiosInstance.get(
            `api/v1/profile/${id}/`,
        )
            .then((response) => {
                setProfile(response.data.profile);
                setMySolutions(response.data.my_solutions);
                setSolvedTasks(response.data.solved_tasks);
                console.log(response);
            })
            .catch((error) => {console.log(error)})
    }

    useEffect( () => {
        if (cookie.token){
            getProfile();
        }
        console.log(cookie.profile);
    }, []);
    return (
        <>
            <Header/>
            <div className={'flex_container_vertical'}>

                {cookie?.profile?.id === profile?.id ?
                    (<h2>My successful solutions</h2>) :
                    (<h2>{profile?.user.username}'s successful solutions</h2>)
                }
                {
                    mySolutions ? <SolutionsTable solutions={mySolutions} showTaskId={true}/> : ''
                }
                <h2>Solved Tasks</h2>
                {
                    solvedTasks ?
                        solvedTasks.map(task => (
                            <TaskShowShort task={task} key={task.id}/>
                        ))
                        : ''
                }

                <div>
                    <button className={'logout-button'} onClick={() => {
                        logout(removeCookie);
                        setTimeout(() => {navigate('/')}, 2000);
                    }}>
                        logout
                    </button>
                </div>

            </div>
        </>
    );
};

export default ProfileShow;