import {ChangeEvent, useEffect, useState} from "react";
import {axiosInstance} from "./AxiosInstance.ts";
import {useCookies} from "react-cookie";
import {TaskShow as TaskShowType} from "./types.ts";
import TaskShow from "./TaskShow.tsx";
import {level_to_string} from "./TestsDefault.ts";

type TasksPropTypes = {
    by_myself: number,
}
// by_myself is 1 or 0

const Tasks = (props: TasksPropTypes) => {

    const [cookie] = useCookies(['token']);
    const [tasks, setTasks] = useState<Array<TaskShowType>>();
    const [allTasks, setAllTasks] = useState<Array<TaskShowType>>();
    const [showLevels, setShowLevels] =
        useState({
        1: true,
        2: true,
        3: true,
    });
    let filtered_levels = {'1': true, '2': true, '3': true};

    const handleOnChangeLevel = (e: ChangeEvent<HTMLInputElement>) => {
        setShowLevels({
            ...showLevels,
            [e.target.name]: e.target.checked,
        })
        // @ts-ignore
        filtered_levels[e.target.name] = e.target.checked;
        if (allTasks){
            let filtered_tasks
                = [...allTasks.filter((task) => {
                    if (task.level === 1){
                        return filtered_levels["1"]
                    }
                if (task.level === 2){
                    return filtered_levels["2"]
                }
                if (task.level === 3){
                    return filtered_levels["3"]
                }
                })];
            setTasks(filtered_tasks);
        }

        console.log(tasks);

    }

    const getTasks = () => {
        axiosInstance.defaults.headers.get['Authorization'] = `Token ${cookie.token}`;
        axiosInstance.get(
            'api/v1/task/',
            {
                params: {...props},
            }
        )
            .then((response) => {
                setTasks(response.data);
                setAllTasks(response.data);
                console.log(response);
            })
            .catch((error) => {console.log(error)})
    }


    useEffect( () => {
        getTasks();

    }, []);

    return (
        <>
            <h2>Tasks</h2>
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

            {tasks ? tasks.map((task: TaskShowType) => (
                <TaskShow task={task} key={task.id}/>
            )) : <div>No tasks</div>}
        </>

    );
};

export default Tasks;