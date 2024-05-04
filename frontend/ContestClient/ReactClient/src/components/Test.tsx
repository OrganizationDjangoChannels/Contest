import {TestShow} from "../types/types.ts";

type TestPropTypes = {
    test: TestShow
}

const Test = ({test}: TestPropTypes) => {
    return (
        <div>
            Test #{test.test_number} with input {test.input} and output {test.output}
        </div>
    );
};

export default Test;