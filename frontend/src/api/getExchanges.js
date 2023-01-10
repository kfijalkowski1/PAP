import { fetchApi } from './fetchApi'

export default async () => {
    return await fetchApi('getExchanges', {})
}
