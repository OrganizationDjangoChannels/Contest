import {useEffect, useState} from "react";
import {axiosInstance} from "./AxiosInstance.ts";
import {useCookies} from "react-cookie";
import {TaskShow as TaskShowType} from "./types.ts";
import TaskShow from "./TaskShow.tsx";

type TasksPropTypes = {
    by_myself: number,
}
// by_myself is 1 or 0

const Tasks = (props: TasksPropTypes) => {

    const [cookie] = useCookies(['token']);
    const [tasks, setTasks] = useState<Array<TaskShowType>>();



    useEffect( () => {
        axiosInstance.defaults.headers.get['Authorization'] = `Token ${cookie.token}`;
        axiosInstance.get(
            'api/v1/task/',
            {
                params: {...props},
            }
        )
            .then((response) => {
                setTasks(response.data);
            console.log(response);
        })
            .catch((error) => {console.log(error)})

    }, []);

    return (
        <>
            <div>Ids of every task</div>
            {tasks ? tasks.map((task: TaskShowType) => (
                <TaskShow task={task} key={task.id}/>
            )) : ''}
        </>

    );
};

export default Tasks;