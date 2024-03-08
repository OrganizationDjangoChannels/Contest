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
    description: string,
    level: number,
    langs: string,   // not type Langs
    owner: Profile | null,
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