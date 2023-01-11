import { fetchApi } from './fetchApi'

export default async (classNr, facultyId) => {
    return await fetchApi('addClassroom', { classNr, facultyId })
}
