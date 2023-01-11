import { fetchApi } from './fetchApi'
import { throwIf } from '@/utils'

export default async () => {
    const result = await fetchApi('getLecturerDegrees', {}).then(
        throwIf(
            (result) => !result.degrees,
            500,
            'Invalid response from server'
        )
    )
    return result.degrees
}
