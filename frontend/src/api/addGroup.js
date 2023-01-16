import { fetchApi } from './fetchApi'

export default async (
    groupNr,
    courseId,
    lecturerId,
    classroomId,
    day,
    timeStart,
    timeEnd
) => {
    return await fetchApi('addGroup', {
        groupNr,
        courseId,
        lecturerId,
        classroomId,
        day,
        timeStart,
        timeEnd,
    })
}
