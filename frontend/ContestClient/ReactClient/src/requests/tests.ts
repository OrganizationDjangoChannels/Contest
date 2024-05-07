import {Dispatch, SetStateAction} from "react";
import {Test, Test as TestType} from "../types/types.ts";
import {axiosInstance} from "./AxiosInstance.ts";

type getTestsArgTypes = {
    task_id: number,
    token: string,
    setTests: Dispatch<SetStateAction<TestType[] | undefined>>,
    setLoading: Dispatch<SetStateAction<boolean>>,
}

export const getTests = async ({task_id, token, setTests, setLoading}: getTestsArgTypes) => {
    axiosInstance.defaults.headers.get['Authorization'] = `Token ${token}`;
    try{
        const response = await axiosInstance.get(
            'api/v1/test/',
            {
                params: {
                    task_id: task_id,
                },
            }
        )
        if (response.status == 200){
            setTests(response.data);
        }
        return response;
    } catch (error) {
        setLoading(false);
        throw error;
    }
}


export const updateTests = async (task_id: number, token: string, testsFormData: Test[]) => {
    axiosInstance.defaults.headers.put['Authorization'] = `Token ${token}`;
    try{
        console.log(testsFormData);
        return await axiosInstance.put(
            `api/v1/test/${task_id}/`,
            [
                ...testsFormData,
            ]
        );

    } catch (error) {
        throw error;
    }
}