import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {axiosInstance} from "./AxiosInstance.ts";
import {useCookies} from "react-cookie";
import {Task as TaskType, TestShow as TestType} from "./types.ts";
import Test from "./Test.tsx";


const Task = () => {
    const [cookie] = useCookies(['token']);
    const {id} = useParams();
    const [task, setTask] = useState<TaskType>();
    const [tests, setTests] = useState<TestType[]>();

    useEffect( () => {
        axiosInstance.defaults.headers.post['Authorization'] = `Token ${cookie.token}`;
        if (id){
            axiosInstance.get(
                `api/v1/task/${id}/`,
            )
                .then((response) => {
                    setTask(response.data);
                    console.log(response.data);
                })
                .catch((error) => {console.log(error)})

            axiosInstance.get(
                `api/v1/test/`,
                {
                    params: {task_id: id}
                }
            )
                .then((response) => {
                    setTests(response.data);
                    console.log(response.data);
                })
                .catch((error) => {console.log(error)})
        }

    }, []);

    return (
        <>
            <div>task_id = {task ? task.id: ''}</div>
            <div>description = {task ? task.description: ''}</div>
            <div>level = {task ? task.level: ''}</div>
            <div>langs = {task ? task.langs: ''}</div>
            <div>owner_id = {task ? task.owner?.id: ''}</div>
            <div>
                Tests:
            </div>
            {tests ?
                tests.map(test => <Test test={test} key={test.id}/>)
                : ''
            }
        </>

    );
};

export default Task;