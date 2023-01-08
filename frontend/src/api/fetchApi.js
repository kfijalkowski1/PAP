import { useAuthStore } from '@/stores'
import { throwError } from '@/utils'

const address = 'http://localhost:8000/'

const fetchApi = async (endpoint, args) => {
    const auth = useAuthStore()
    let creds = { login: '', token: '' }
    if (auth.isSignedIn) {
        creds = {
            login: auth.login,
            token: auth.token,
        }
    }

    const res = await fetch(address + endpoint, {
        method: 'post',
        headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
        },

        body: JSON.stringify({
            ...creds,
            ...args,
        }),
    }).catch(throwError(500, 'Network Error'))

    const data = await res.json().catch(throwError(500, 'JSON Error'))

    if (data.code !== 200) {
        if (data.code === 401) {
            // session expired, logout and redirect to login page
        }
        throw data.error
    }

    return data
}

export { fetchApi }
