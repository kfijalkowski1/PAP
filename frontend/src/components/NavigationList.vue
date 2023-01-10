<script setup>
import { computed } from 'vue'
import { useAuthStore } from '@/stores'
const auth = useAuthStore()

const links = computed(() =>
    !auth.isSignedIn
        ? [
              { to: '/', text: 'Dashboard' },
              { to: '/browse', text: 'Browse exchanges' },
              { to: '/addExchange', text: 'Create exchange' },
          ]
        : [
              { to: '/login', text: 'Login' },
              { to: '/testing', text: 'Testing' },
          ]
)
</script>
<template>
    <v-list nav>
        <v-list-item v-for="{ to, text } in links" :key="to" :to="to">{{
            text
        }}</v-list-item>
    </v-list>
</template>
