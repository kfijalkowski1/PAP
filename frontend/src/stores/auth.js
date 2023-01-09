import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAuthStore = defineStore('auth', () => {
    const login = ref(null)
    const token = ref(null)

    const signIn = (newLogin, newToken) => {
        login.value = newLogin
        token.value = newToken
    }

    const signOut = () => {
        login.value = null
        token.value = null
    }

    const isSignedIn = computed(() => token.value !== null)

    return {
        login,
        token,
        signIn,
        signOut,
        isSignedIn,
    }
})
