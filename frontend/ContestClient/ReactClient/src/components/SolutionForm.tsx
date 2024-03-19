import {ChangeEvent, Dispatch, FormEvent, SetStateAction} from "react";
import {SolutionCreate} from "./types.ts";
import {axiosFileUploadInstance} from "./AxiosInstance.ts";
import {useCookies} from "react-cookie";
import {parse_langs} from "./TestsDefault.ts";

type SolutionFormPropTypes = {
    solution: SetStateAction<SolutionCreate>,
    setSolution: Dispatch<SetStateAction<SolutionCreate>>,
    task_id: number | null,
    task_langs: string | null,

}

const SolutionForm = ({solution, setSolution, task_id, task_langs}: SolutionFormPropTypes) => {

    const [cookie] = useCookies(['token']);
    const langs = parse_langs(task_langs);

    const handleOnChangeFile = (e: ChangeEvent<HTMLInputElement>) => {
        setSolution((prev) => ({
            ...prev,
            file: e.target?.files ? e.target.files[0] : null,
        }))
    }

    const handleOnChangeLang = (e: ChangeEvent<HTMLSelectElement>) => {
        setSolution((prev) => ({
            ...prev,
            lang: e.target.value,
        }))
    }

    const handleOnSubmitSolutionForm = async (e: FormEvent) => {
        e.preventDefault();
        console.log(solution);

        axiosFileUploadInstance.defaults.headers.post['Authorization'] = `Token ${cookie.token}`;
        const response = await axiosFileUploadInstance.post(
            'api/v1/solution/',
            {
                ...solution,
                task_id: task_id,
            }
        );
        console.log(response);
    }

    return (
        <div className={'solution_form_container'}>
            <form encType="multipart/form-data" onSubmit={handleOnSubmitSolutionForm}>
                <div className={'solution_upload_container'}>
                    <div className={'solution_upload_item'} id={'file_choice'}>
                        <input type="file" name="file" id={"file_input"} onChange={handleOnChangeFile}/>
                    </div>
                    <div className={'solution_upload_item'} id={'lang_choice'}>

                        <label htmlFor="lang_select">Choose a lang:</label>
                        <select name="langs" id={"lang_select"} onChange={handleOnChangeLang}>
                            {langs?.C ? <option value="C">C</option> : <></>}
                            {langs?.["C++"] ? <option value="C++">C++</option> : <></>}
                            {langs?.Java ? <option value="Java">Java</option> : <></>}
                            {langs?.Python ? <option value="Python">Python</option> : <></>}
                        </select>

                    </div>
                    <div className={'solution_upload_item'}>
                        <button type="submit" className={"submit-button"}>Send solution</button>
                    </div>
                </div>

            </form>

        </div>
    );
};

export default SolutionForm;