import {TaskShow as TaskShowType} from "./types.ts";
import {level_to_string} from "./TestsDefault.ts";
import {Link} from "react-router-dom";

type TaskItemPropTypes = {
    task: TaskShowType,
}

// shows in tasks
const TaskShow = ({task} : TaskItemPropTypes) => {
    const task_level = level_to_string(task.level);
    return (
        <>
            <div className={'task_show_container'}>
                <div className={'task_header_container'}>
                    <div className={'task_header_item'}>
                        <Link to={`../task/${task.id}`} className={'custom_link'}>#{task.id}</Link>
                    </div>
                    <div className={'task_header_item task_title'}>{task.title}</div>
                </div>
                <div className={'task_description'}>
                    {task.description}
                </div>
                <div>
                    <div>{task_level} level</div>
                </div>
                <div>
                    {(task.sent_solutions === 1)
                        ? (<div>There were sent just 1 solution.</div>)
                        : (<div>There were sent {task.sent_solutions ? task.sent_solutions : '0'} solutions.</div>)
                    }
                </div>
            </div>

        </>

    );
};

export default TaskShow;