<script setup>
import { $ref } from 'vue/macros'
import { addUser, loginValid, authenticateUser } from '@/api'
import { useAuthStore } from '@/stores'
import { throwIf, errorCatcher } from '@/utils'

const auth = useAuthStore()

let mode = $ref('login')
let login = $ref('')
let password = $ref('')

let confirmPassword = $ref('')
let email = $ref('')
let firstname = $ref('')
let surname = $ref('')

const validateLogin = errorCatcher(async () => {
    throwIf(() => login.length < 6, 400, 'Login too short')()

    await loginValid(login)
})

const onLogin = errorCatcher(async () => {
    await validateLogin()

    const token = await authenticateUser(login, password)
    auth.login(login, token)
})

const onRegister = errorCatcher(async () => {
    await validateLogin()

    throwIf(() => password !== confirmPassword, 400, 'Password mismatch')()

    await addUser(login, password, firstname, surname, email)
    mode = 'login'
    password = ''
})
</script>

<template>
    <div class="wrapper">
        <div class="container">
            <v-sheet color="white" elevation="2" class="background">
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

                <v-text-field
                    v-if="mode === 'register'"
                    label="Confirm password"
                    color="primary"
                    fullWidth
                    variant="filled"
                    type="password"
                    v-model="confirmPassword"
                />
                <v-text-field
                    v-if="mode === 'register'"
                    label="Firstname"
                    placeholder="Jan"
                    color="primary"
                    fullWidth
                    variant="filled"
                    v-model="firstname"
                />
                <v-text-field
                    v-if="mode === 'register'"
                    label="Surname"
                    placeholder="Przyklad"
                    color="primary"
                    fullWidth
                    variant="filled"
                    v-model="surname"
                />
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

                <v-btn
                    v-if="mode === 'login'"
                    variant="flat"
                    color="primary"
                    class="button"
                    @click="onLogin"
                >
                    Login
                </v-btn>
                <v-btn
                    v-if="mode === 'register'"
                    variant="flat"
                    color="primary"
                    class="button"
                    @click="onRegister"
                >
                    Register
                </v-btn>
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
    width: 50%;
    max-width: 500px;
    margin: auto;
    justify-content: center;
    align-items: center;
    display: flex;
    flex-direction: column;
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
