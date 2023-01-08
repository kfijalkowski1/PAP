import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useErrorDisplayStore = defineStore('errorDisplay', () => {
    const code = ref(0)
    const message = ref('')

    const snackbar = computed(() => !!code.value)

    const setMessage = (error) => {
        console.log(error)

        code.value = error.code
        message.value = error.message
    }
    const clearMessage = () => {
        code.value = 0
        message.value = ''
    }

    return {
        snackbar,
        code,
        message,
        setMessage,
        clearMessage,
    }
})
