import { useAuthStore } from '@/stores'
import { throwError } from '@/utils'
import router from '@/router'
import { useErrorDisplayStore } from '@/stores'

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

    console.log('Fetching', endpoint, args)
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

    console.log('Got', data)

    if (data.code !== 200) {
        console.error('api error', data)
        if (data.code === 401) {
            console.info('logging out', data)
            router.push('/login')
            auth.signOut()
        }
        throw data
    }
    if (data.message) {
        const errorDisplay = useErrorDisplayStore()
        errorDisplay.setMessage(data)
    }

    return data
}

export { fetchApi }
