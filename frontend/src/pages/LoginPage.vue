<script setup>
import { ref } from 'vue'
import {} from '@/api'

const mode = ref('login')
const login = ref('')
const password = ref('')

const confirmPassword = ref('')
const email = ref('')
const firstname = ref('')
const surname = ref('')

const onLogin = () => {}
const onRegister = () => {}
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
                    fullWidth
                    variant="filled"
                    v-model="login"
                />
                <v-text-field
                    label="Password"
                    fullWidth
                    variant="filled"
                    type="password"
                    v-model="password"
                />

                <v-text-field
                    v-if="mode === 'register'"
                    label="Confirm password"
                    fullWidth
                    variant="filled"
                    type="password"
                    v-model="confirmPassword"
                />
                <v-text-field
                    v-if="mode === 'register'"
                    label="Firstname"
                    placeholder="Jan"
                    fullWidth
                    variant="filled"
                    v-model="firstname"
                />
                <v-text-field
                    v-if="mode === 'register'"
                    label="Surname"
                    placeholder="Przyklad"
                    fullWidth
                    variant="filled"
                    v-model="surname"
                />
                <v-text-field
                    v-if="mode === 'register'"
                    label="Email"
                    type="email"
                    placeholder="jan.przyklad@gmail.com"
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
