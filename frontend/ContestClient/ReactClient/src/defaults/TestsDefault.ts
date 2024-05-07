import {DifficultyTable, Langs, Test} from "../types/types.ts";

export const tests_max_number = 100;

export const empty_tests: Array<Test> = Array.from({length: tests_max_number}, (_, i) => {
    return {
        input: null,
        output: null,
        test_number: i + 1,
    }
});

export const build_empty_tests = (tests: Test[]): Test[] => {
    let arr = empty_tests.slice();
    for (let test of tests){
        arr[test.test_number - 1] = test;
    }
    return arr;
}

export const parse_langs = (str_langs: string | null): Langs | null => {
    if (str_langs === null){
        return null;
    }

    let langs_array = str_langs.split('|');
    let langs: Langs = {
        C: false,
        "C++": false,
        Java: false,
        Python: false,
    };
    for (let lang of langs_array){
        if (lang === 'C'){
            langs.C = true;
        }
        if (lang === 'C++'){
            langs['C++'] = true;
        }
        if (lang === 'Java'){
            langs.Java = true;
        }
        if (lang === 'Python'){
            langs.Python = true;
        }
    }
    return langs;
}

export const difficulty_table: DifficultyTable = {
    1: 'easy',
    2: 'medium',
    3: 'hard',
}

export const level_to_string = (level: number): string | null => {
    if (level in difficulty_table){
        if (level === 1){
            return difficulty_table[1];
        }
        if (level === 2){
            return difficulty_table[2];
        }
        if (level === 3){
            return difficulty_table[3];
        }
    }
    return null;
}

export const string2langs = (s: string): Langs => {
    let arr = s.split('|');
    return {
        C: arr.includes('C'),
        'C++': arr.includes('C++'),
        Java: arr.includes('Java'),
        Python: arr.includes('Python'),
    };
}

export const RATINGS_LIMIT = 8;  // should be the same as on server

