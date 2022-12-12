import { fetchApi } from './fetchApi'

export default async (login) => {
    return await fetchApi('addUser', { login })
}
