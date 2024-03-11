import {ChangeEvent, FormEvent, useState} from "react";
import {Langs, Task, Test} from "./types.ts";
import {axiosInstance} from "./AxiosInstance.ts";
import {useCookies} from "react-cookie";
import TestCreate from "./TestCreate.tsx";
import {empty_tests} from "./TestsDefault.ts";

const TaskForm = () => {
    const [cookie] = useCookies(['token']);
    const [taskFormData, setTaskFormData] = useState<Task>({
        id: 1,
        title: '',
        description: '',
        level: 1,
        langs: 'C|C++|Python|Java',
        owner: null,
    });
    const [langs, setLangs] = useState<Langs>({
        C: true,
        'C++': true,
        Java: true,
        Python: true,
    });

    const [tests, setTests] = useState<Array<Test>>(empty_tests);



    const handleOnChangeLangs = (e: ChangeEvent<HTMLInputElement>) => {
        setLangs(langs => ({
            ...langs,
            [e.target.name]: e.target.checked,
        }));
    };

    const handleOnChangeTaskFormData =
        (e: ChangeEvent<HTMLSelectElement>
            | ChangeEvent<HTMLTextAreaElement>
            | ChangeEvent<HTMLInputElement>) => {
        setTaskFormData(taskFormData => ({
            ...taskFormData,
            [e.target.name]: e.target.value,
        }));
    }

    const handleOnSubmitTaskForm = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        axiosInstance.defaults.headers.post['Authorization'] = `Token ${cookie.token}`;
        const response_task  = await axiosInstance.post(
            'api/v1/task/',
            {
                ...taskFormData,
                langs: `${langs.C ? 'C' : ''}${langs["C++"] ? '|C++' : ''}${langs.Java ? '|Java' : ''}${langs.Python ? '|Python' : ''}`,
            }
        );
        const task_id = response_task.data.id;
        console.log(tests);
        const response_test  = await axiosInstance.post(
            'api/v1/test/',
            {
                tests,
                task_id: task_id,
            }
        );
        console.log(response_test.data);
    }

    return (
        <>
            <h3>Create your task</h3>
            <form onSubmit={handleOnSubmitTaskForm}>
                <div className={"flex_container_vertical"}>
                    <div className={'title_label_container'}>
                        <label className={"input-label"}>
                            Title
                        </label>
                    </div>
                    <div>
                        <input
                            className={'title_input_field'}
                            type="text"
                            name='title'
                            placeholder={'title'}
                            onChange={handleOnChangeTaskFormData}
                        />
                    </div>
                    <div className={'description_label_container'}>
                        <label htmlFor={'description_area'} className={"input-label"} >
                            Description
                        </label>
                    </div>
                    <textarea rows={10} cols={54} id={'description_area'} name={'description'}
                              placeholder={'description'} onChange={handleOnChangeTaskFormData}>

                    </textarea>
                </div>
                <div>
                    <label htmlFor={'level_select'} className={"input-label"}>
                        Choose a difficulty level
                    </label>
                    <select name="level" id={'level_select'} onChange={handleOnChangeTaskFormData}>
                        <option value={1}>easy</option>
                        <option value={2}>medium</option>
                        <option value={3}>hard</option>
                    </select>
                </div>

                <div className={'flex_container_horizontal'}>
                    <label className={"input-label"}>
                        Available programming languages
                    </label>
                </div>

                <div className={'flex_container_horizontal'}>
                    <span className={'langs_selection'}>
                        <input type={'checkbox'} id={'checkbox_c'} name={'C'}
                               checked={langs.C} onChange={handleOnChangeLangs}/>
                        <label htmlFor={'checkbox_c'} className={"input-label checkbox-label"}>C</label>
                    </span>

                    <span className={'langs_selection'}>
                        <input type={'checkbox'} id={'checkbox_c++'} name={'C++'}
                               checked={langs['C++']} onChange={handleOnChangeLangs}/>
                        <label htmlFor={'checkbox_c++'} className={"input-label checkbox-label"}>C++</label>
                    </span>

                    <span className={'langs_selection'}>
                        <input type={'checkbox'} id={'checkbox_java'} name={'Java'}
                               checked={langs.Java} onChange={handleOnChangeLangs}/>
                        <label htmlFor={'checkbox_java'} className={"input-label checkbox-label"}>Java</label>
                    </span>

                    <span className={'langs_selection'}>
                        <input type={'checkbox'} id={'checkbox_python'} name={'Python'}
                               checked={langs.Python} onChange={handleOnChangeLangs}/>
                        <label htmlFor={'checkbox_python'} className={"input-label checkbox-label"}>Python</label>
                    </span>
                </div>

                <div className={'flex_container_horizontal'}>
                    <label id={'tests_label'}>
                        Task tests
                    </label>
                </div>
                <div className={'flex_container_horizontal'}>
                    <div className={'tests_container'}>
                        {
                            [...empty_tests].map((elem, i) => (
                                <TestCreate test_number={elem.test_number} setTests={setTests} key={i}/>
                            ))
                        }
                    </div>

                </div>


                <button type="submit" className={"submit-button"}>Create</button>
            </form>
        </>
    );
};

export default TaskForm;