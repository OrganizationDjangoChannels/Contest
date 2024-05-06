import {Profile} from "../../types/types.ts";
import {Link} from "react-router-dom";
import {SetStateAction, useEffect, useState} from "react";
import {RATINGS_LIMIT} from "../../defaults/TestsDefault.ts";


type RatingsItemPropTypes = {
    profile: Profile,
    index: number,
    ratings: SetStateAction<Profile[]>,
    page: SetStateAction<number>,
}

const RatingsItem = ({profile, index, ratings, page}: RatingsItemPropTypes) => {
    const [tableIndex, setTableIndex] = useState<number>(index + 1);
    useEffect(() =>{
        setTableIndex(prevState => Number(page) * RATINGS_LIMIT + prevState )
    }, [ratings])

    return (
        <tr>
            <td>{tableIndex}</td>
            <td><Link to={`../user/${profile.id}`} className={'profile_link'}>{profile.user.username}</Link></td>
            <td>{(new Date(profile.created_at)).toLocaleDateString('ru')}</td>
            <td>{profile.points}</td>

        </tr>
    );
};

export default RatingsItem;