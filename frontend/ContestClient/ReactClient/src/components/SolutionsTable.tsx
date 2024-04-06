
import SolutionShow from "./SolutionShow.tsx";
import {SolutionShowType} from "./types.ts";


type SolutionShowPropTypes = {
    solutions: SolutionShowType[],
    showTaskId: boolean,
}

const SolutionsTable = ({solutions, showTaskId}: SolutionShowPropTypes) => {
    return (
        <table className={'solutions_table'}>
            <thead>
            <tr>

                {showTaskId ?
                    (<td>Task</td>)
                    :
                    <td>Profile</td>
                }

                <td>Sent at</td>
                <td>Lang</td>
                <td>Points</td>
            </tr>
            </thead>
            <tbody>
            {solutions ?
                solutions.map(solution =>
                    <SolutionShow solution={solution} showTaskId={showTaskId} key={solution.id}/>

                )
                : ''
            }

            </tbody>

        </table>
    );
};

export default SolutionsTable;