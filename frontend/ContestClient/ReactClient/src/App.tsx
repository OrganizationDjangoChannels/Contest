import './App.css'
import {
    BrowserRouter, Route, Routes,
} from "react-router-dom";

import LoginForm from "./components/auth/LoginForm.tsx";
import RegisterForm from "./components/auth/RegisterForm.tsx";
import Home from "./components/Home.tsx";
import TaskForm from "./components/task/TaskForm.tsx";
import Tasks from "./components/task/Tasks.tsx";
import Task from "./components/task/Task.tsx";
import Ratings from "./components/user/Ratings.tsx";
import ProfileShow from "./components/user/Profile.tsx";
import TaskEdit from "./components/task/TaskEdit.tsx";


function App() {
    return (
        <>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Home />}></Route>
                    <Route path="/register" element={<RegisterForm/>}></Route>
                    <Route path="/login" element={<LoginForm/>}></Route>
                    <Route path="/create-task" element={<TaskForm/>}></Route>
                    <Route path="/tasks" element={<Tasks by_myself={0}/>}></Route>
                    <Route path="/my-tasks" element={<Tasks by_myself={1}/>}></Route>
                    <Route path="/task/:id" element={<Task/>}></Route>
                    <Route path="/task/:id/edit" element={<TaskEdit/>}></Route>
                    <Route path="/ratings" element={<Ratings/>}></Route>
                    <Route path="/user/:id" element={<ProfileShow/>}></Route>
                </Routes>
            </BrowserRouter>

        </>

    )
}

export default App;
