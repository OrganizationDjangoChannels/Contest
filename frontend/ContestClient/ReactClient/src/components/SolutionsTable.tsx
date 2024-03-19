
import SolutionShow from "./SolutionShow.tsx";
import {SolutionShowType} from "./types.ts";

type SolutionShowPropTypes = {
    solutions: SolutionShowType[],
}

const SolutionsTable = ({solutions}: SolutionShowPropTypes) => {
    return (
        <table className={'solutions_table'}>
            <thead>
            <tr>
                <td>Profile</td>
                <td>Sent at</td>
                <td>Lang</td>
                <td>Points</td>
            </tr>
            </thead>
            <tbody>
            {solutions ?
                solutions.map(solution =>
                    <SolutionShow solution={solution} key={solution.id}/>

                )
                : ''
            }

            </tbody>

        </table>
    );
};

export default SolutionsTable;