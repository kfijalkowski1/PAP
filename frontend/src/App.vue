<script setup>
import { ErrorSnackBar, NavigationList } from '@/components'
import { $ref } from 'vue/macros'
import { useDisplay } from 'vuetify'
import ReportIssueDialog from './components/ReportIssueDialog.vue'
import { useAuthStore } from './stores'

const { lgAndUp } = useDisplay()

const auth = useAuthStore()

const drawer = $ref(false)
const reportIssue = $ref(false)
</script>
<template>
    <v-app>
        <v-app-bar color="primary" absolute>
            <v-app-bar-nav-icon
                icon="mdi-menu"
                @click="drawer = !drawer"
                v-if="!lgAndUp"
            />
            <span style="margin-left: 32px" v-if="auth.isSignedIn">
                {{ auth.login }}
            </span>
            <v-spacer />
            <v-btn
                variant="flat"
                rounded="pill"
                @click="reportIssue = true"
                v-if="auth.isSignedIn"
            >
                Report issue
            </v-btn>
            <ReportIssueDialog v-model="reportIssue" />
        </v-app-bar>
        <v-navigation-drawer v-model="drawer" v-if="!lgAndUp">
            <NavigationList />
        </v-navigation-drawer>
        <v-main class="container">
            <v-sheet v-if="lgAndUp" elevation="2" class="drawerList">
                <NavigationList />
            </v-sheet>
            <div class="content">
                <router-view />
            </div>
            <ErrorSnackBar />
        </v-main>
    </v-app>
</template>

<style scoped>
.container {
    display: flex;
    flex-direction: row;
}
.content {
    display: flex;
    flex-direction: column;
    flex-grow: 1;
}
.drawerList {
    margin: 16px;
    min-width: 200px;
}
</style>
