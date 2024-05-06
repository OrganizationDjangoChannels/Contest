import {axiosInstance, axiosFileUploadInstance} from "../../requests/AxiosInstance.ts";
import {FormEvent, useRef, useState} from "react";

const TestComponent = () => {
    async function get_test_data():Promise<void>{
        const response = await axiosInstance.get('api/v1/testview/')
        console.log(response)
        setResponse(response?.data?.test_message)
    }

    // let formData = new FormData();
    // formData.append("file", selectedFile);

    const handleOnSubmit = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (file_input_ref.current && lang_select_ref.current){
            let file_container = file_input_ref.current;
            let lang_container = lang_select_ref.current;
            let formData = new FormData();
            if (file_container.files){
                formData.append('file', file_container.files['0'])
                formData.append('lang', lang_container.value)

                let response = await axiosFileUploadInstance.post(
                    'api/v1/uploadfile/',
                    formData,
                );
                console.log(response)
            }


        }

    }

    const [response, setResponse] = useState("");

    const file_input_ref = useRef<HTMLInputElement | null>(null);
    const lang_select_ref = useRef<HTMLSelectElement | null>(null);


    return (
        <div>
            <button onClick={get_test_data}>get request</button>
            <div>{response}</div>
            <div>
                <form encType="multipart/form-data" onSubmit={handleOnSubmit}>

                    <div>
                        <label>Add file: </label><br/>
                        <input type="file" name="file" id={"file_input"} ref={file_input_ref}/>
                    </div>
                    <div>
                        <label htmlFor="lang_select">Choose a lang:</label>
                        <select name="langs" id={"lang_select"} ref={lang_select_ref}>
                            <option value="C">C</option>
                            <option value="C++">C++</option>
                            <option value="Java">Java</option>
                            <option value="Python">Python</option>
                        </select>

                    </div>
                    <div>
                        <input type="submit"/>
                    </div>
                </form>

            </div>
        </div>
    );
};

export default TestComponent;