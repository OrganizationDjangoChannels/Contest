// @ts-ignore
import {useCookies} from "react-cookie/cjs/useCookies";

export const logout = (removeCookie: useCookies) => {
    removeCookie('token', {path: '/', expires: 'Thu, 01 Jan 1970 00:00:00 GMT'});
    removeCookie('profile', {path: '/', expires: 'Thu, 01 Jan 1970 00:00:00 GMT'});
    console.log('logged out');
}