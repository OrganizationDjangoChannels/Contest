export const setToken = async(setCookie: Function, token_title: string, token_value: string) => {
    await setCookie(token_title, token_value, {
        'path': '/',
        'maxAge': 3600 * 24 * 7,
        'secure': true,
    });
}
