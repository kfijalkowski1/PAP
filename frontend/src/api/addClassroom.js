import { fetchApi } from './fetchApi'

export default async (nr, facultyId) => {
    return await fetchApi('addClassroom', { nr, facultyId })
}
