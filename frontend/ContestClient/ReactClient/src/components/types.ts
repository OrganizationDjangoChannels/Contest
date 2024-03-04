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
    id: number | null,
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