import { useErrorDisplayStore } from '@/stores'

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

const errorCatcher =
    (func) =>
    async (...args) => {
        const errorDisplay = useErrorDisplayStore()
        const showError = errorDisplay.setMessage
        try {
            await func(...args)
        } catch (error) {
            showError(error)
        }
    }

export { throwIf, throwError, errorCatcher }
