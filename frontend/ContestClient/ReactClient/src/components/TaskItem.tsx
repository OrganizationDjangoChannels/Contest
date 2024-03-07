
type TaskItemPropTypes = {
    task_id: number,
}

// shows in tasks
const TaskItem = ({task_id} : TaskItemPropTypes) => {
    return (
        <div>
            {task_id}
        </div>
    );
};

export default TaskItem;