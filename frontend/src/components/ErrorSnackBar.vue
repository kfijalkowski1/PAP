<script setup>
import { computed } from 'vue'
import { useErrorDisplayStore } from '@/stores'

const errorDisplay = useErrorDisplayStore()

const message = computed(() => `${errorDisplay.code}: ${errorDisplay.message}`)
</script>
<template>
    <v-snackbar
        :modelValue="errorDisplay.snackbar"
        @update:modelValue="() => errorDisplay.clearMessage()"
        :timeout="2000"
        color="error"
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
