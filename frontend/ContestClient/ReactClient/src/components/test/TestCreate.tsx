
import {ChangeEvent, Dispatch, SetStateAction} from "react";
import {Test} from "../../types/types.ts";

type TestCreateProps = {
    test_number: number,
    setTests: Dispatch<SetStateAction<Test[]>>,
    readonly?: boolean,
    test2update?: Test,
}
const TestCreate = ({test_number, setTests, readonly, test2update} : TestCreateProps) => {

    const handleTestOnChange = function (e : ChangeEvent<HTMLTextAreaElement>, stream: string){
        setTests((tests) => {
            let updated_tests = [...tests];
            let updated_test = {...updated_tests[test_number - 1]};   // index
            if (stream === 'input'){
                if (e.target.value === ''){
                    updated_test.input = null;
                } else {
                    updated_test.input = e.target.value;
                }
            }
            if (stream === 'output'){
                if (e.target.value === ''){
                    updated_test.output = null;
                } else {
                    updated_test.output = e.target.value;
                }

            }
            updated_test.test_number = test_number;
            updated_tests[test_number - 1] = updated_test;   // index
            return updated_tests;
        });
    }

    return (
        <div className={'test_input_container'}>
            <div className={'test_number'}>
                {`${test_number}`}
            </div>
            {test2update ?
                <>
                    <textarea rows={4} cols={24} readOnly={readonly}
                              defaultValue={test2update?.input ? test2update.input : ''}
                              placeholder={'input'} onChange={(e) =>
                        handleTestOnChange(e, 'input')}>
                    </textarea>
                    <textarea rows={4} cols={24} readOnly={readonly}
                              defaultValue={test2update?.output ? test2update.output : ''}
                              placeholder={'output'} onChange={(e) =>
                        handleTestOnChange(e, 'output')}>
                    </textarea>
                </>
                :
                <>
                    <textarea rows={4} cols={24} readOnly={readonly}
                              placeholder={'input'} onChange={(e) =>
                        handleTestOnChange(e, 'input')}>
                    </textarea>
                    <textarea rows={4} cols={24} readOnly={readonly}
                              placeholder={'output'} onChange={(e) =>
                        handleTestOnChange(e, 'output')}>
                    </textarea>
                </>

            }

        </div>
    );
};

export default TestCreate;