import { fetchApi } from './fetchApi'
import { parseTime, throwIf } from '@/utils'
import { dayStrings } from '@/assets'

export default async (courseId) => {
    const result = await fetchApi('getGroups', { courseId }).then(
        throwIf((result) => !result.groups, 500, 'Invalid response from server')
    )
    return result.groups.map((group) => ({
        ...group,
        previewName: `${group.groupNr}, ${dayStrings[group.day]} ${parseTime(
            group.timeStart
        )}:${parseTime(group.timeEnd)}`,
    }))
}
