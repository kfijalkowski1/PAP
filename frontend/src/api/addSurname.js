import { fetchApi } from './fetchApi'

export default async (surname) => {
    return await fetchApi('addSurname', { surname })
}
