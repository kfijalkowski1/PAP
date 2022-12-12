const address = 'http://localhost:5000/api/'

const throwError = (code, message) => () => {
    throw { code, message }
}

const throwIf = (func, code, message) => (res) => {
    if (!func(res)) {
        return res
    } else {
        return throwError(code, message)()
    }
}

const fetchApi = async (endpoint, args) => {
    const res = await fetch(address + endpoint, {
        method: 'post',
        headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
        },

        body: JSON.stringify({
            ...args,
            // add authorization data
        }),
    }).catch(throwError(500, 'Network Error'))

    const data = await res.json().catch(throwError(500, 'JSON Error'))

    if (data.error) {
        if (data.error.code === 401) {
            // session expired, logout and redirect to login page
        }
        throw data.error
    }

    return data
}

export { fetchApi, throwIf, throwError }
