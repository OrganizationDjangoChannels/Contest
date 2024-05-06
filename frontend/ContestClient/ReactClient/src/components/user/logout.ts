// @ts-ignore
import {useCookies} from "react-cookie/cjs/useCookies";

export const logout = (removeCookie: useCookies) => {
    removeCookie('token');
    removeCookie('profile');
    console.log('logged out');
}