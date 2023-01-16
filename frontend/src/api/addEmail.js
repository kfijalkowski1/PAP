import { fetchApi } from './fetchApi'

export default async (email) => {
    return await fetchApi('addEmail', { email })
}
