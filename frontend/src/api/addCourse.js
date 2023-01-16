import { fetchApi } from './fetchApi'

export default async (code, name, type, facultyId) => {
    return await fetchApi('addCourse', {
        type,
        name,
        code,
        facultyId,
    })
}
