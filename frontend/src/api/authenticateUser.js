import { fetchApi } from './fetchApi'
import { throwIf } from '@/utils'

export default async (login, password) => {
    const result = await fetchApi('authenticateUser', {
        login,
        password,
    }).then(
        throwIf((result) => !result.token, 500, 'Invalid response from server')
    )
    return result.token
}
