import {axiosInstance} from "./AxiosInstance.ts";
import {useState} from "react";

const TestComponent = () => {
    async function get_test_data():Promise<void>{
        const response = await axiosInstance.get('api/v1/testview/')
        console.log(response)
        setResponse(response?.data?.test_message)
    }

    const [response, setResponse] = useState("");

    return (
        <div>
            <button onClick={get_test_data}>get request</button>
            <div>{response}</div>
        </div>
    );
};

export default TestComponent;