import { fetchApi } from './fetchApi'
import { throwIf } from '@/utils'

export default async () => {
    const result = await fetchApi('getFaculties', {}).then(
        throwIf(
            (result) => !result.faculties,
            500,
            'Invalid response from server'
        )
    )
    return result.faculties
}
