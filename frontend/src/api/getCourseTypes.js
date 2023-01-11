import { fetchApi } from './fetchApi'
import { throwIf } from '@/utils'

export default async () => {
    const result = await fetchApi('getCourseTypes', {}).then(
        throwIf(
            (result) => !result.courses,
            500,
            'Invalid response from server'
        )
    )
    return result.courses
}
