export type User = {
    id: number,
    username: string,
    email: string,
}

export type Profile = {
    id: number,
    user: User,
    points: number,
    solved_tasks: number,
}

export type Task = {
    id: number,
    title: string,
    description: string,
    level: number,
    langs: string,   // not type Langs
    owner: Profile | null,
}

export type TaskShow = {
    id: number,
    title: string,
    description: string,
    level: number,
    langs: string,   // not type Langs
    owner: Profile | null,
    sent_solutions: number,
}

export type Langs = {
    C: boolean,
    'C++': boolean,
    Java: boolean,
    Python: boolean,
}

export type Test = {
    input: string | null,
    output: string | null,
    test_number: number,
}

export type TestShow = {
    id: number,
    input: string,
    output: string,
    test_number: number,
    task: Task,
    status: string | null,
}

export type SolutionCreate = {
    file: File | null,
    lang: string | null,
}

export type DifficultyTable = {
    1: string,
    2: string,
    3: string,
}