<script setup>
import { computed } from 'vue'
import { useErrorDisplayStore } from '@/stores'

const errorDisplay = useErrorDisplayStore()

const message = computed(() => `${errorDisplay.code}: ${errorDisplay.message}`)

const type = computed(() => (errorDisplay.code === 200 ? 'info' : 'error'))
</script>
<template>
    <v-snackbar
        :modelValue="errorDisplay.snackbar"
        @update:modelValue="() => errorDisplay.clearMessage()"
        :timeout="5000"
        :color="type === 'error' ? 'error' : 'primary'"
        variant="elevated"
    >
        {{ message }}

        <template v-slot:actions>
            <v-btn variant="text" @click="() => errorDisplay.clearMessage()">
                Close
            </v-btn>
        </template>
    </v-snackbar>
</template>
<style scoped></style>
