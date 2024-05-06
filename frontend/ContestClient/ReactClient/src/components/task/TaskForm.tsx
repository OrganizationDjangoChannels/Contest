import {ChangeEvent, FormEvent, useState} from "react";
import {Langs, Task, Test} from "../../types/types.ts";
import {useCookies} from "react-cookie";
import TestCreate from "../test/TestCreate.tsx";
import {empty_tests, string2langs} from "../../defaults/TestsDefault.ts";
import Header from "../Header.tsx";
import LoadingStatus from "../statuses/LoadingStatus.tsx";
import SuccessfulStatus from "../statuses/SuccessfulStatus.tsx";
import {createTask, updateTask} from "../../requests/tasks.ts";

type TaskFormPropTypes = {
    task2edit?: Task,
    tests2show?: Test[],
}

const TaskForm = ({task2edit, tests2show}: TaskFormPropTypes) => {
    const [cookie] = useCookies(['token']);
    const [taskFormData, setTaskFormData] = useState<Task>(
        task2edit ?
            task2edit :
            {id: 1, title: '', description: '', level: 1, langs: 'C|C++|Python|Java', owner: null,}
    );
    const [langs, setLangs] = useState<Langs>(
        task2edit ?
            string2langs(task2edit.langs) :
            {C: true, 'C++': true, Java: true, Python: true,}
    );

    const [tests, setTests] = useState<Array<Test>>(
        tests2show ? tests2show : empty_tests
    );

    const [loading, setLoading] = useState<boolean>(false);
    const [successfulStatus, setSuccessfulStatus] =
        useState<boolean>(false);


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

    const handleOnChangeFile = (e: ChangeEvent<HTMLInputElement>) => {
        let tests_from_files = empty_tests;
        console.log(e.target.files);
        if (e.target.files){
            for (let file of e.target.files){
                let filename_split = file.name.slice(0, -4).split('-');
                let reader = new FileReader();
                let test_index = Number(filename_split[1]) - 1;
                reader.readAsText(file);
                reader.onload = function() {
                    if (filename_split[0] === 'input' && reader.result){
                        tests_from_files[test_index].input = reader.result.toString();
                    }
                    if (filename_split[0] === 'output' && reader.result){
                        tests_from_files[test_index].output = reader.result.toString();
                    }

                };
            }
        }
        setTests(tests_from_files);
    }

    const handleOnSubmitTaskForm = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setLoading(true);
        if (task2edit){
            await updateTask(task2edit.id, String(cookie.token), taskFormData, setLoading, setSuccessfulStatus)
        } else {
            await createTask(String(cookie.token), taskFormData, tests, setLoading, setSuccessfulStatus)
        }


    }

    return (
        <div className={'main_container'}>
            <Header/>
            <h2>{task2edit ? 'Update' : 'Create'} your task</h2>
            <form onSubmit={handleOnSubmitTaskForm}>
                <div className={"flex_container_vertical"}>
                    <div className={'title_label_container'}>
                        <label className={"input-label"}>
                            Title
                        </label>
                    </div>
                    <div>
                        {task2edit ?
                            <input
                                className={'title_input_field'}
                                type="text"
                                name='title'
                                placeholder={'title'}
                                value={taskFormData.title}
                                onChange={handleOnChangeTaskFormData}
                            />
                            : <input
                                className={'title_input_field'}
                                type="text"
                                name='title'
                                placeholder={'title'}
                                onChange={handleOnChangeTaskFormData}
                            />
                        }

                    </div>
                    <div className={'description_label_container'}>

                        <label htmlFor={'description_area'} className={"input-label"} >
                            Description
                        </label>
                    </div>
                    {task2edit ?
                        <textarea rows={10} cols={54} id={'description_area'} name={'description'}
                                  value={taskFormData.description}
                                  placeholder={'description'} onChange={handleOnChangeTaskFormData}>

                        </textarea>
                        : <textarea rows={10} cols={54} id={'description_area'} name={'description'}
                                    placeholder={'description'} onChange={handleOnChangeTaskFormData}>

                        </textarea> }

                </div>
                <div>
                    <label htmlFor={'level_select'} className={"input-label"}>
                        Choose a difficulty level
                    </label>
                    <select name="level" id={'level_select'} defaultValue={taskFormData.level}
                            onChange={handleOnChangeTaskFormData}>
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
                    <div className={'solution_upload_item flex_container_horizontal'} id={'file_choice'}>
                        <input type="file" name="file" id={"file_input"} multiple={true}
                               onChange={handleOnChangeFile}/>
                    </div>
                </div>

                <div className={'flex_container_horizontal'}>
                    <div className={'tests_container'}>
                        {
                            tests2show ?
                                [...tests2show].map((elem, i) => (
                                    <TestCreate test_number={elem.test_number} test={elem}
                                                setTests={setTests} readonly={true} key={i}/>
                                ))
                            : [...empty_tests].map((elem, i) => (
                                <TestCreate test_number={elem.test_number}
                                            setTests={setTests} key={i}/>
                            ))
                        }
                    </div>

                </div>


                <button type="submit" className={"submit-button"}>{task2edit ? 'Update' : 'Create'}</button>
            </form>
            {loading && <LoadingStatus/>}
            {successfulStatus && <SuccessfulStatus message={
                task2edit ? 'Your task was updated' : 'Your task was created'
            }/>}
        </div>
    );
};

export default TaskForm;