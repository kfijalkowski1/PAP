import { fetchApi, throwIf } from './fetchApi'

export default async (login, password) => {
    const result = await fetchApi('authenticateUser', {
        login,
        password,
    }).then(
        throwIf((result) => !result.token, 500, 'Invalid response from server')
    )
    return result.token
}
