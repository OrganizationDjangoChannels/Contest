import {useEffect, useState} from "react";
import {axiosInstance} from "./AxiosInstance.ts";
import {useCookies} from "react-cookie";
import {Task as TaskType} from "./types.ts";
import TaskItem from "./TaskItem.tsx";



const Tasks = () => {

    const [cookie] = useCookies(['token']);
    const [tasks, setTasks] = useState<Array<TaskType>>();



    useEffect( () => {
        axiosInstance.defaults.headers.post['Authorization'] = `Token ${cookie.token}`;
        axiosInstance.get(
            'api/v1/task/',
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
            {tasks ? tasks.map((item: TaskType) => (
                <TaskItem task_id={item.id}/>
            )) : ''}
        </>

    );
};

export default Tasks;