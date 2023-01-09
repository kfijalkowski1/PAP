<script setup>
import ErrorSnackBar from '@/components/ErrorSnackBar.vue'
</script>
<template>
    <v-app>
        <v-app-bar color="primary" absolute>
            <v-app-bar-nav-icon icon="mdi-menu"></v-app-bar-nav-icon>
        </v-app-bar>
        <v-navigation-drawer>
            <v-list>
                <v-list-item>
                    <router-link to="/">Home</router-link>
                </v-list-item>
                <v-list-item>
                    <router-link to="/login">Login</router-link>
                </v-list-item>
                <v-list-item>
                    <router-link to="/testing">Testing</router-link>
                </v-list-item>
            </v-list>
        </v-navigation-drawer>
        <v-main>
            <router-view />
            <ErrorSnackBar />
        </v-main>
    </v-app>
</template>

<style></style>
