import {Dispatch, SetStateAction} from "react";
import {SolutionShowType} from "../types/types.ts";
import {axiosInstance} from "./AxiosInstance.ts";

type getSolutionsArgTypes = {
    task_id: number,
    token: string,
    setSolutions: Dispatch<SetStateAction<SolutionShowType[] | undefined>>,
    setLoading: Dispatch<SetStateAction<boolean>>,
}


export const getSolutions = async ({task_id, token, setSolutions, setLoading}: getSolutionsArgTypes) => {
    axiosInstance.defaults.headers.get['Authorization'] = `Token ${token}`;
    try{
        const response = await axiosInstance.get(
            'api/v1/solution/',
            {
                params: {
                    task_id: task_id,
                },
            }
        )
        if (response.status == 200){
            setSolutions(response.data);
        }
        return response;
    } catch (error) {
        setLoading(false);
        throw error;
    }
}