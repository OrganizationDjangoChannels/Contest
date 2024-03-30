import {Profile} from "./types.ts";
import {Link} from "react-router-dom";


type RatingsItemPropTypes = {
    profile: Profile,
    index: number,
}

const RatingsItem = ({profile, index}: RatingsItemPropTypes) => {
    return (
        <tr>
            <td>{index + 1}</td>
            <td><Link to={`../user/${profile.id}`} className={'custom_link'}>{profile.user.username}</Link></td>
            <td>{profile.points}</td>
        </tr>
    );
};

export default RatingsItem;