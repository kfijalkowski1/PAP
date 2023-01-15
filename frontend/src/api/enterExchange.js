import { fetchApi } from './fetchApi'

export default async (sellGroupId, buyGroupIds) => {
    return await fetchApi('enterExchange', { sellGroupId, buyGroupIds })
}
