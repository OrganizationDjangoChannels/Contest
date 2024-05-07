import {axiosInstance} from "./AxiosInstance.ts";
import {Dispatch, SetStateAction} from "react";
import {Task, Task as TaskType, Test} from "../types/types.ts";
import {updateTests} from "./tests.ts";

type getTaskArgTypes = {
    id: string,
    token: string,
    setTask: Dispatch<SetStateAction<TaskType | undefined>>,
    setLoading: Dispatch<SetStateAction<boolean>>,
}

export const getTask = async ({id, token, setTask, setLoading}: getTaskArgTypes) => {
    axiosInstance.defaults.headers.get['Authorization'] = `Token ${token}`;
    try{
        const response = await axiosInstance.get(
            `api/v1/task/${id}/`,
        )
        if (response.status == 200){
            setTask(response.data);
        }
        return response;
    } catch (error) {
        setLoading(false);
        throw error;
    }

}


export const createTask = async (token: string,
                                 taskFormData: object,
                                 testsFormData: object,
                                 setLoading: Dispatch<SetStateAction<boolean>>,
                                 setSuccessfulStatus: Dispatch<SetStateAction<boolean>>,
                                 ) => {
    axiosInstance.defaults.headers.post['Authorization'] = `Token ${token}`;
    try{
        const response_task  = await axiosInstance.post(
            'api/v1/task/',
            {
                ...taskFormData,
            }
        );
        const task_id = response_task.data.id;
        const response_tests = await axiosInstance.post(
            'api/v1/test/',
            {
                ...testsFormData,
                task_id: task_id,
            }
        );
        if (response_task.status == 201 && response_tests.status == 201){
            setLoading(false);
            setSuccessfulStatus(true);
        }
        return response_tests;
    } catch (error) {
        setLoading(false);
        throw error;
    }
}


export const updateTask = async (task_id: number,
                                 token: string,
                                 taskFormData: Task,
                                 testsFormData: Test[],
                                 setLoading: Dispatch<SetStateAction<boolean>>,
                                 setSuccessfulStatus: Dispatch<SetStateAction<boolean>>,
                                 ) => {

    axiosInstance.defaults.headers.put['Authorization'] = `Token ${token}`;
    try{
        const response_task = await axiosInstance.put(
            `api/v1/task/${task_id}/`,
            {
                ...taskFormData,
            }
        )

        if (response_task.status === 200){
            const response_tests = await updateTests(task_id, token, testsFormData)
            if (response_tests.status === 204){
                setLoading(false);
                setSuccessfulStatus(true);
                return response_tests;
            }

        }
        return response_task;


    } catch (error) {
        setLoading(false);
        throw error;
    }
}