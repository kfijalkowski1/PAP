import { fetchApi } from './fetchApi'
import { throwIf } from '@/utils'

export default async (courses, forUser, facultyId) => {
    const result = await fetchApi('getAllExchanges', {
        courses,
        forUser,
        facultyId,
    }).then(
        throwIf(
            (result) => !result.exchanges,
            500,
            'Invalid response from server'
        )
    )
    return result.exchanges
}
