import { fetchApi } from './fetchApi'

export default async (code, facultyId) => {
    return await fetchApi('addCourse', {
        type: 'lab',
        name: '',
        code,
        facultyId,
    })
}
