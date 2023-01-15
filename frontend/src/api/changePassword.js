import { fetchApi } from './fetchApi'

export default async (oldPass, newPass) => {
    return await fetchApi('changePassword', { old: oldPass, new: newPass })
}
