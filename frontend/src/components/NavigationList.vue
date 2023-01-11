<script setup>
import { computed } from 'vue'
import { useAuthStore } from '@/stores'
const auth = useAuthStore()

const links = computed(() =>
    auth.isSignedIn
        ? [
              { to: '/', text: 'Home', icon: 'mdi-home' },
              {
                  to: '/dashboard',
                  text: 'Dashboard',
                  icon: 'mdi-view-dashboard',
              },
              { to: '/browse', text: 'Browse exchanges', icon: 'mdi-magnify' },
              {
                  to: '/createExchange',
                  text: 'Create exchange',
                  icon: 'mdi-plus',
              },
              {
                  to: '/myGroups',
                  text: 'My groups',
                  icon: 'mdi-account-multiple',
              },
              {
                  to: '/userInfo',
                  text: 'User information',
                  icon: 'mdi-account',
              },
          ]
        : [
              { to: '/', text: 'Home', icon: 'mdi-home' },
              { to: '/login', text: 'Login', icon: 'mdi-login' },
              { to: '/testing', text: 'Testing', icon: 'mdi-test-tube' },
          ]
)
</script>
<template>
    <v-list nav>
        <v-list-item v-for="{ to, text, icon } in links" :key="to" :to="to">
            <template v-slot:prepend v-if="!!icon">
                <v-icon :icon="icon"></v-icon>
            </template>

            <v-list-item-title>{{ text }}</v-list-item-title>
        </v-list-item>
    </v-list>
</template>
