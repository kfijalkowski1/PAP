<script setup>
import { onMounted } from 'vue'
import { $ref } from 'vue/macros'
import { useAuthStore } from '@/stores'
import UserInfoRow from '@/components/UserInfoRow.vue'
import { errorCatcher } from '@/utils'
import { getUserInfo, addFirstname, addSurname, addEmail } from '@/api'

const auth = useAuthStore()

let userInfo = $ref({
    firstname: '',
    surname: '',
    email: '',
})

onMounted(
    errorCatcher(async () => {
        userInfo = await getUserInfo()
    })
)

const updateFirstname = errorCatcher(async (newValue) => {
    await addFirstname(newValue)
    userInfo = await getUserInfo()
})
const updateSurname = errorCatcher(async (newValue) => {
    await addSurname(newValue)
    userInfo = await getUserInfo()
})
const updateEmail = errorCatcher(async (newValue) => {
    await addEmail(newValue)
    userInfo = await getUserInfo()
})
</script>

<template>
    <div class="wrapper">
        <v-sheet color="white" elevation="2" class="container">
            <h1>Profile details</h1>
            <v-table>
                <thead>
                    <tr>
                        <th class="text-left">Name</th>
                        <th class="text-left">Value</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>login</td>
                        <td>{{ auth.login }}</td>
                        <td></td>
                    </tr>
                    <UserInfoRow
                        name="Firstname"
                        :value="userInfo.firstname"
                        @submit="updateFirstname"
                    />
                    <UserInfoRow
                        name="Surname"
                        :value="userInfo.surname"
                        @submit="updateSurname"
                    />
                    <UserInfoRow
                        name="Email"
                        :value="userInfo.email"
                        @submit="updateEmail"
                    />
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
    padding: 32px;
    margin: 16px;
}
</style>
