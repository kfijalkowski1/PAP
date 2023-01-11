import { fetchApi } from './fetchApi'

export default async (name) => {
    return await fetchApi('addLecturer', { name, surname: '', rating: -1 })
}
