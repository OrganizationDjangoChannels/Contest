
import {ChangeEvent, Dispatch, SetStateAction} from "react";
import {Test} from "./types.ts";

type TestCreateProps = {
    test_number: number,
    setTests: Dispatch<SetStateAction<Test[]>>,
}
const TestCreate = ({test_number, setTests} : TestCreateProps) => {

    const handleTestOnChange = function (e : ChangeEvent<HTMLTextAreaElement>, stream: string){
        setTests((tests) => {
            let updated_tests = [...tests];
            let updated_test = {...updated_tests[test_number - 1]};   // index
            if (stream === 'input'){
                updated_test.input = e.target.value;
            }
            if (stream === 'output'){
                updated_test.output = e.target.value;
            }

            updated_tests[test_number - 1] = updated_test;   // index
            return updated_tests;
        });
    }

    return (
        <div className={'test_input_container'}>
            <div className={'test_number'}>
                {`${test_number}`}
            </div>
            <textarea rows={4} cols={24}
                      placeholder={'input'} onChange={(e) =>
                handleTestOnChange(e, 'input')}>

            </textarea>
            <textarea rows={4} cols={24}
                      placeholder={'output'} onChange={(e) =>
                handleTestOnChange(e, 'output')}>

            </textarea>
        </div>
    );
};

export default TestCreate;