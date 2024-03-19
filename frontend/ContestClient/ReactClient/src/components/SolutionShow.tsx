import {SolutionShowType} from "./types.ts";

type SolutionShowPropTypes = {
    solution: SolutionShowType,
}
const SolutionShow = ({solution}: SolutionShowPropTypes) => {
    const created_timestamp = Date.parse(solution.created_at);
    const created_date = new Date(created_timestamp);

    return (
        <tr>
            <td>{solution.owner.user.username}</td>
            <td>{created_date.toLocaleString()}</td>
            <td>{solution.lang}</td>
            <td>{solution.points}</td>
        </tr>
    );
};

export default SolutionShow;