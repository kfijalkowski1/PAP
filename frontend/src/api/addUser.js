import { fetchApi } from './fetchApi'

export default async (login, password, name, surname, email) => {
    return await fetchApi('addUser', { login, password, name, surname, email })
}
