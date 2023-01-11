import { fetchApi } from './fetchApi'

export default async (name, surname, title) => {
    return await fetchApi('addLecturer', { name, surname, title, rating: -1 })
}
