import { fetchApi } from './fetchApi'

export default async (message) => {
    return await fetchApi('userReport', { message })
}
