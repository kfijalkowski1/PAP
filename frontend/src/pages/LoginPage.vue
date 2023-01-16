<script setup>
import { $ref } from 'vue/macros'
import { addUser, loginValid, authenticateUser } from '@/api'
import { useAuthStore } from '@/stores'
import { throwIf, errorCatcher } from '@/utils'
import router from '@/router'

const auth = useAuthStore()

let mode = $ref('login')
let login = $ref('')
let password = $ref('')

let confirmPassword = $ref('')
let email = $ref('')
let firstname = $ref('')
let surname = $ref('')

const validateLogin = errorCatcher(async () => {
    if (mode === 'register') await loginValid(login)
})

const onLogin = errorCatcher(async () => {
    const token = await authenticateUser(login, password)
    auth.signIn(login, token)

    console.log('Login succesful!')
    console.log(auth.login, auth.token)

    router.push('/dashboard')
})

const onRegister = errorCatcher(async () => {
    await validateLogin()

    throwIf(() => password !== confirmPassword, 400, 'Password mismatch')()

    await addUser(login, password, firstname, surname, email)
    mode = 'login'
    password = ''
})

const checkKey = (event) => {
    if (event.keyCode === 13) {
        if (mode === 'register') onRegister()
        else onLogin()
    }
}
</script>

<template>
    <div class="wrapper">
        <div class="container">
            <v-sheet
                color="white"
                elevation="2"
                class="background"
                :onkeydown="checkKey"
            >
                <div class="tabs">
                    <v-tabs v-model="mode" color="primary" fixed-tabs>
                        <v-tab value="login">Login</v-tab>
                        <v-tab value="register">Register</v-tab>
                    </v-tabs>
                </div>
                <v-text-field
                    label="Login"
                    color="primary"
                    fullWidth
                    variant="filled"
                    v-model="login"
                    @blur="validateLogin"
                />
                <v-text-field
                    label="Password"
                    color="primary"
                    fullWidth
                    variant="filled"
                    type="password"
                    v-model="password"
                />

                <v-expand-transition>
                    <v-text-field
                        v-if="mode === 'register'"
                        label="Confirm password"
                        color="primary"
                        fullWidth
                        variant="filled"
                        type="password"
                        v-model="confirmPassword"
                    />
                </v-expand-transition>
                <v-expand-transition>
                    <v-text-field
                        v-if="mode === 'register'"
                        label="Firstname"
                        placeholder="Jan"
                        color="primary"
                        fullWidth
                        variant="filled"
                        v-model="firstname"
                    />
                </v-expand-transition>
                <v-expand-transition>
                    <v-text-field
                        v-if="mode === 'register'"
                        label="Surname"
                        placeholder="Przyklad"
                        color="primary"
                        fullWidth
                        variant="filled"
                        v-model="surname"
                    />
                </v-expand-transition>
                <v-expand-transition>
                    <v-text-field
                        v-if="mode === 'register'"
                        label="Email"
                        type="email"
                        placeholder="jan.przyklad@gmail.com"
                        color="primary"
                        fullWidth
                        variant="filled"
                        v-model="email"
                    />
                </v-expand-transition>

                <v-expand-transition>
                    <div v-if="mode === 'login'">
                        <v-btn
                            variant="flat"
                            color="primary"
                            class="button"
                            @click="onLogin"
                        >
                            Login
                        </v-btn>
                    </div>
                    <div v-if="mode === 'register'">
                        <v-btn
                            variant="flat"
                            color="primary"
                            class="button"
                            @click="onRegister"
                        >
                            Register
                        </v-btn>
                    </div>
                </v-expand-transition>
            </v-sheet>
        </div>
    </div>
</template>

<style scoped>
.wrapper {
    display: flex;
    flex-grow: 1;
    min-height: calc(100vh - 64px);
}
.container {
    max-width: 500px;
    margin: auto;
    justify-content: center;
    align-items: center;
    display: flex;
    flex-direction: column;
    flex-grow: 1;
}
.background {
    width: 100%;
    padding: 10px 24px 24px 24px;
}
.tabs {
    width: 100%;
    padding-bottom: 16px;
}
.button {
    width: 100%;
}
</style>
