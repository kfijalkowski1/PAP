import { fetchApi } from './fetchApi'
import { throwIf } from '@/utils'

export default async () => {
    const result = await fetchApi('getExchanges', {}).then(
        throwIf(
            (result) => !result.exchanges,
            500,
            'Invalid response from server'
        )
    )
    return result.exchanges
}
