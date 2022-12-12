<script setup>
import { ref } from 'vue'
import { addUser, listUsers } from '@/api'

const login = ref('')
const users = ref([{ id: -1, login: 'example' }])

const onAddUser = () => {
    addUser(login.value)
}
const onLoadUsers = async () => {
    users.value = await listUsers()
}
</script>

<template>
    <div class="wrapper">
        <h1>Testing page</h1>
        <v-sheet color="white" elevation="2" class="container">
            <v-text-field label="Login" v-model="login"></v-text-field>
            <v-btn color="primary" @click="onAddUser">Add to database</v-btn>
        </v-sheet>
        <v-sheet color="white" elevation="2" class="container">
            <v-btn color="primary" @click="onLoadUsers"
                >Read data from database</v-btn
            >
            <v-table>
                <thead>
                    <tr>
                        <th class="text-left">Id</th>
                        <th class="text-left">Login</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="user in users" :key="user.id">
                        <td>{{ user.id }}</td>
                        <td>{{ user.login }}</td>
                    </tr>
                </tbody>
            </v-table>
        </v-sheet>
    </div>
</template>

<style scoped>
.wrapper {
    max-width: 600px;
    margin: auto;
    padding-top: 32px;
}
.container {
    padding: 16px;
    margin: 16px;
}
</style>
