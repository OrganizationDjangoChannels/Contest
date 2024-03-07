import {Test} from "./types.ts";

export const tests_max_number = 100;

export const empty_tests: Array<Test> = Array.from({length: tests_max_number}, (_, i) => {
    return {
        input: null,
        output: null,
        test_number: i + 1,
    }
});

