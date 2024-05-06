import {useCookies} from "react-cookie";
import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {getTests} from "../../requests/tests.ts";
import {Task as TaskType, TaskShow as TaskShowType, Test} from "../../types/types.ts";
import LoadingStatus from "../statuses/LoadingStatus.tsx";
import {getTask} from "../../requests/tasks.ts";
import TaskForm from "./TaskForm.tsx";
import Header from "../Header.tsx";

type TaskEditProps = {
    task2edit?: TaskShowType,
}

const TaskEdit = ({task2edit}: TaskEditProps) => {
    const [cookie] = useCookies(['token', 'profile']);
    const {id} = useParams();
    const [task, setTask] = useState<TaskType>();
    const [tests, setTests] = useState<Array<Test>>();
    const [loading, setLoading] = useState<boolean>(false);
    const [wrongUser, setWrongUser] = useState<boolean>();

    useEffect(() => {

        if (cookie.token && id){
            const token = cookie.token.toString();
            if (!task2edit){
                getTask({id, token, setTask, setLoading})
                    .then(response => {
                        console.log(response);
                        task2edit = response.data;
                        if (task2edit?.owner?.id === cookie.profile.id){
                            setWrongUser(false);
                        } else {
                            setWrongUser(true);
                        }
                    })

            }

        }

    }, [])

    useEffect(() => {
        const token = cookie.token.toString();
        const task_id = Number(id);
        if (!wrongUser && !tests){
            setLoading(true);
            getTests({task_id, token, setTests, setLoading})
                .then(response => {
                    setLoading(false);
                    console.log(response);
                })
        } else {
            setLoading(false);
        }
    }, [wrongUser])
    return (
        <div>

            {loading && <LoadingStatus/>}
            {
                wrongUser ?
                <>
                    <Header/>
                    <h2>
                        You can't edit this task.
                    </h2>
                </>

                :
                    task &&
                    <TaskForm task2edit={
                        {id: task.id, title: task.title, description: task.description,
                            level: task.level, langs: task.langs, owner: task.owner,}
                    } tests2show={tests}/>
            }
        </div>
    );
};

export default TaskEdit;