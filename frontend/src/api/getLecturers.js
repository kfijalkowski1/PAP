import { fetchApi } from './fetchApi'
import { throwIf } from '@/utils'

export default async () => {
    const result = await fetchApi('getLecturers', {}).then(
        throwIf(
            (result) => !result.lecturers,
            500,
            'Invalid response from server'
        )
    )
    return result.lecturers
}
