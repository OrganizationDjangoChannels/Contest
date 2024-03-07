import './App.css'
import {
    BrowserRouter, Route, Routes,
} from "react-router-dom";

import LoginForm from "./components/LoginForm.tsx";
import RegisterForm from "./components/RegisterForm.tsx";
import Home from "./components/Home.tsx";
import TaskForm from "./components/TaskForm.tsx";
import Tasks from "./components/Tasks.tsx";
import Task from "./components/Task.tsx";


function App() {
    return (
        <>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Home />}></Route>
                    <Route path="/register" element={<RegisterForm/>}></Route>
                    <Route path="/login" element={<LoginForm/>}></Route>
                    <Route path="/create-task" element={<TaskForm/>}></Route>
                    <Route path="/tasks" element={<Tasks/>}></Route>
                    <Route path="/task/:id" element={<Task/>}></Route>
                </Routes>
            </BrowserRouter>

        </>

    )
}

export default App;
