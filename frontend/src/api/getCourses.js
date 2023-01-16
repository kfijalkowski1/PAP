import { fetchApi } from './fetchApi'
import { throwIf } from '@/utils'

export default async (facultyId) => {
    const result = await fetchApi('getCourses', { facultyId }).then(
        throwIf(
            (result) => !result.courses,
            500,
            'Invalid response from server'
        )
    )
    return result.courses
}
