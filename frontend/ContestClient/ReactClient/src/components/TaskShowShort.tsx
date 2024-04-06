import {TaskShow as TaskShowType} from "./types.ts";
import {Link} from "react-router-dom";

type TaskItemPropTypes = {
    task: TaskShowType,
}
const TaskShowShort = ({task} : TaskItemPropTypes) => {
    return (
        <>
            <div className={'task_show_short_container'}>
                <div className={'task_header_container'}>
                    <div className={'task_header_item'}>
                        <Link to={`../task/${task.id}`} className={'custom_link'}>#{task.id}</Link>
                    </div>
                    <div className={'task_short_header_item task_title'}>{task.title}</div>
                </div>
            </div>

        </>

    );
};

export default TaskShowShort;