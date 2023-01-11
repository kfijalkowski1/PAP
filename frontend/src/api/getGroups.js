import { fetchApi } from './fetchApi'
import { throwIf } from '@/utils'

export default async (courseId) => {
    const result = await fetchApi('getGroups', { courseId }).then(
        throwIf((result) => !result.groups, 500, 'Invalid response from server')
    )
    return result.groups
}
