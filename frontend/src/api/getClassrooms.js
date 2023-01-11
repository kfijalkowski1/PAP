import { fetchApi } from './fetchApi'
import { throwIf } from '@/utils'

export default async (facultyId) => {
    const result = await fetchApi('getClassrooms', { facultyId }).then(
        throwIf(
            (result) => !result.classrooms,
            500,
            'Invalid response from server'
        )
    )
    return result.classrooms
}
