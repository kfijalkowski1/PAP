import { fetchApi } from './fetchApi'
import { throwIf } from '@/utils'

export default async () => {
    const result = await fetchApi('getUserGroups', {}).then(
        throwIf((result) => !result.groups, 500, 'Invalid response from server')
    )
    return result.groups
}
