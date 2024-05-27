import {Link, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {useCookies} from "react-cookie";
import {SolutionCreate, SolutionShowType, Task as TaskType} from "../../types/types.ts";
import SolutionForm from "../solution/SolutionForm.tsx";
import {level_to_string} from "../../defaults/TestsDefault.ts";
import SolutionsTable from "../solution/SolutionsTable.tsx";
import Header from "../Header.tsx";
import LoadingStatus from "../statuses/LoadingStatus.tsx";
import {getTask} from "../../requests/tasks.ts";
import {getSolutions} from "../../requests/solutions.ts";


const Task = () => {
    const [cookie] = useCookies(['token', 'profile']);
    const {id} = useParams();
    const [task, setTask] = useState<TaskType>();
    const [loading, setLoading] = useState<boolean>(false);

    const [solution,
        setSolution] = useState<SolutionCreate>({
        file: null,
        lang: 'C',   // C language is default in select
    });

    const [solutions,
        setSolutions] = useState<SolutionShowType[]>();


    useEffect( () => {
        if (cookie.token && id){
            setLoading(true);
            const token = cookie.token.toString();

            getTask({id, token, setTask, setLoading})
                .then(response => console.log(response));

            const task_id = Number(id);
            getSolutions({task_id, token, setSolutions, setLoading})
                .then((response) => {
                    console.log(response)
                    setLoading(false);
                });
        }
    }, []);

    return (
        <>
            <Header/>
            {loading ? <LoadingStatus/> :
            <>
                <div className={'edit_task_link_container'}>
                    {cookie.profile.id === task?.owner?.id &&
                        <Link to={`/task/${id}/edit`}
                              reloadDocument={true}
                              className={'profile_link'}>{'Edit task'}</Link>
                    }
                </div>

                <div className={'task_show_container'}>
                    <div><strong>{task ? task.title: ''}</strong></div>
                    <div className={'task_description'}>{task ? task.description: ''}</div>
                    <div>level: {task ? level_to_string(task.level) : ''}</div>
                    <div>owner: {task ? task.owner?.user?.username: ''}</div>
                </div>

                <SolutionForm
                    solution={solution}
                    setSolution={setSolution}
                    setSolutions={setSolutions}
                    task_id={task ? task.id : null}
                    task_langs={task ? task.langs : null}
                />

                <div className={'solutions_table_container'}>
                    {solutions ? (<SolutionsTable solutions={solutions} showTaskId={false}/>) : ''}

                </div>
            </>
            }

        </>

    );
};

export default Task;