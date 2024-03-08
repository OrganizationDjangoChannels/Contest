import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {axiosInstance} from "./AxiosInstance.ts";
import {useCookies} from "react-cookie";
import {SolutionCreate, Task as TaskType, TestShow as TestType} from "./types.ts";
import Test from "./Test.tsx";
import SolutionForm from "./SolutionForm.tsx";


const Task = () => {
    const [cookie] = useCookies(['token']);
    const {id} = useParams();
    const [task, setTask] = useState<TaskType>();
    const [tests, setTests] = useState<TestType[]>();

    const [solution, setSolution] = useState<SolutionCreate>({
        file: null,
        lang: 'C',   // C language is default in select
    });

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
            <div>title = {task ? task.title: ''}</div>
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
            <SolutionForm
                solution={solution}
                setSolution={setSolution}
                task_id={task ? task.id : null}
                task_langs={task ? task.langs : null}
            />
        </>

    );
};

export default Task;