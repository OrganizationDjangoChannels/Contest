import {SolutionShowType} from "./types.ts";
import {Link} from "react-router-dom";
import {useCookies} from "react-cookie";

type SolutionShowPropTypes = {
    solution: SolutionShowType,
    showTaskId: boolean,
}
const SolutionShow = ({solution, showTaskId}: SolutionShowPropTypes) => {
    const created_timestamp = Date.parse(solution.created_at);
    const created_date = new Date(created_timestamp);
    const [cookie,] = useCookies(['token', 'profile']);
    let row_style = {};
    if (cookie?.profile && !showTaskId && cookie.profile.id === solution.owner.id){
        row_style = {'background-color': '#444444'}
    }
    return (
        <tr style={row_style}>
            {showTaskId ?
                (<td>
                    <Link to={`../task/${solution.task.id}`} className={'custom_link'}>#{solution.task.id}</Link>
                </td>)
                :
                (<td>
                    <Link to={`../user/${solution.owner.id}`} className={'custom_link'}>{solution.owner.user.username}</Link>
                </td>)
            }
            <td>{created_date.toLocaleString()}</td>
            <td>{solution.lang}</td>
            <td>{solution.points}</td>
        </tr>
    );
};

export default SolutionShow;