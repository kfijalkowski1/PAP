import { fetchApi } from './fetchApi'

export default async (login, password) => {
    return await fetchApi('login', { login, password })
}
