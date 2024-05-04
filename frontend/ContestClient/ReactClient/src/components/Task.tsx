import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {axiosInstance} from "./AxiosInstance.ts";
import {useCookies} from "react-cookie";
import {SolutionCreate, SolutionShowType, Task as TaskType} from "../types/types.ts";
import SolutionForm from "./SolutionForm.tsx";
import {level_to_string} from "../defaults/TestsDefault.ts";
import SolutionsTable from "./SolutionsTable.tsx";
import Header from "./Header.tsx";
import LoadingStatus from "./LoadingStatus.tsx";


const Task = () => {
    const [cookie] = useCookies(['token']);
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

    const getTask = () => {
        axiosInstance.defaults.headers.post['Authorization'] = `Token ${cookie.token}`;
        if (id){
            axiosInstance.get(
                `api/v1/task/${id}/`,
            )
                .then((response) => {
                    setTask(response.data);
                    console.log(response.data);
                })
                .catch((error) => {
                    setLoading(false);
                    console.log(error)
                })
        }
    }

    const getSolutions = () => {
        axiosInstance.defaults.headers.get['Authorization'] = `Token ${cookie.token}`;
        axiosInstance.get(
            'api/v1/solution/',
            {
                params: {
                    task_id: id,
                },
            }
        )
            .then((response) => {
                setSolutions(response.data);
                setLoading(false);
                console.log(response);
            })
            .catch((error) => {
                setLoading(false);
                console.log(error)
            })
    }

    useEffect( () => {
        if (cookie.token){
            setLoading(true);
            getTask();
            getSolutions();
        }
    }, []);

    return (
        <>
            <Header/>
            {loading ? <LoadingStatus/> :
            <>

                <div className={'task_show_container'}>
                    <div><strong>{task ? task.title: ''}</strong></div>
                    <div className={'task_description'}>{task ? task.description: ''}</div>
                    <div>level: {task ? level_to_string(task.level) : ''}</div>
                    <div>owner: {task ? task.owner?.user?.username: ''}</div>
                </div>

                <SolutionForm
                    solution={solution}
                    setSolution={setSolution}
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