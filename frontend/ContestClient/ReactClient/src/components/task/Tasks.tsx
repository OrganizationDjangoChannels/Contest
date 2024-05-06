import {ChangeEvent, useEffect, useState} from "react";
import {axiosInstance} from "../../requests/AxiosInstance.ts";
import {useCookies} from "react-cookie";
import {TaskShow as TaskShowType} from "../../types/types.ts";
import {level_to_string} from "../../defaults/TestsDefault.ts";
import Header from "../Header.tsx";
import {Link} from "react-router-dom";
import TaskShow from "./TaskShow.tsx";
import Pagination from "../Pagination.tsx";
import LoadingStatus from "../statuses/LoadingStatus.tsx";

type TasksPropTypes = {
    by_myself: number,
}
// by_myself is 1 or 0

const Tasks = (props: TasksPropTypes) => {

    const [cookie] = useCookies(['token']);
    const [tasks, setTasks] = useState<Array<TaskShowType>>();
    const [filteredTasks, setFilteredTasks] = useState<Array<TaskShowType>>();
    const [showLevels, setShowLevels] =
        useState({
        1: true,
        2: true,
        3: true,
    });
    const [page, setPage] = useState<number>(0);
    const [loading, setLoading] = useState<boolean>(false);
    let filtered_levels = {'1': true, '2': true, '3': true};

    const handleOnChangeLevel = (e: ChangeEvent<HTMLInputElement>) => {
        setShowLevels({
            ...showLevels,
            [e.target.name]: e.target.checked,
        })
        // @ts-ignore
        filtered_levels[e.target.name] = e.target.checked;

        if (tasks){
            setFilteredTasks([...tasks.filter((task) => {
                if (task.level === 1 && filtered_levels["1"]){
                    return task
                }
                if (task.level === 2 && filtered_levels["2"]){
                    return task
                }
                if (task.level === 3 && filtered_levels["3"]){
                    return task
                }
            })]);
        }

    }

    const getTasks = async() => {
        axiosInstance.defaults.headers.get['Authorization'] = `Token ${cookie.token}`;
        setLoading(true);
        try {
            const response = await axiosInstance.get('api/v1/task/',
                {params: {...props, page: page},})
            setTasks(response.data);
            setFilteredTasks(response.data);
            setLoading(false);
            return response;
        } catch (error) {
            setLoading(false);
            console.log(error);
        }

    }

    useEffect(() =>{
        if (cookie.token){
            getTasks().then(response => console.log(response));
        }
    }, [page])

    return (
        <>
            <Header/>
            <h2>Tasks</h2>
            <div>
                <Link to={`../create-task`} className={'custom_link'}>
                    <button className={'logout-button'}>
                        Create a new task
                    </button>
                </Link>
            </div>

            <span className={'langs_selection'}>
                        <input type={'checkbox'} id={'easy_level'} name={'1'}
                               checked={showLevels['1']} onChange={handleOnChangeLevel}/>
                        <label htmlFor={'easy_level'} className={"input-label checkbox-label"}>
                            {level_to_string(1)}
                        </label>
            </span>

            <span className={'langs_selection'}>
                        <input type={'checkbox'} id={'medium_level'} name={'2'}
                               checked={showLevels['2']} onChange={handleOnChangeLevel}/>
                        <label htmlFor={'medium_level'} className={"input-label checkbox-label"}>
                            {level_to_string(2)}
                        </label>
            </span>

            <span className={'langs_selection'}>
                        <input type={'checkbox'} id={'checkbox_java'} name={'3'}
                               checked={showLevels['3']} onChange={handleOnChangeLevel}/>
                        <label htmlFor={'medium_level'} className={"input-label checkbox-label"}>
                            {level_to_string(3)}
                        </label>
            </span>

            {filteredTasks && filteredTasks.map((task: TaskShowType) => (
                <TaskShow task={task} show_description={false} key={task.id}/>
            ))}

            <Pagination page={page} setPage={setPage}/>

            {loading && <LoadingStatus/>}

        </>

    );
};

export default Tasks;