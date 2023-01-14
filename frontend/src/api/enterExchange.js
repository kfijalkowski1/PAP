import { fetchApi } from './fetchApi'

export default async (sellGroupId, buyGroupIds) => {
    return await fetchApi('addSurname', { sellGroupId, buyGroupIds })
}
