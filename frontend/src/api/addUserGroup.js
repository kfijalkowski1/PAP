import { fetchApi } from './fetchApi'

export default async (groupId) => {
    return await fetchApi('addUserGroup', { groupId })
}
