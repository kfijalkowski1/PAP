import { fetchApi } from './fetchApi'

export default async (firstname) => {
    return await fetchApi('addFirstname', { firstname })
}
