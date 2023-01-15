<script setup>
//import { addCourse, getCourseTypes, getCourses } from '@/api'
import { userReport } from '@/api'
import { errorCatcher } from '@/utils'
import { defineProps, defineEmits } from 'vue'
import { $ref } from 'vue/macros'

const props = defineProps(['modelValue'])
const emit = defineEmits(['update:modelValue'])

let message = $ref(null)

const cancel = () => {
    message = null

    emit('update:modelValue', false)
}
const submit = errorCatcher(async () => {
    await userReport(message)

    emit('update:modelValue', false)

    message = null
})
</script>

<template>
    <v-dialog
        :model-value="props.modelValue"
        @update:model-value="emit('update:modelValue', $event)"
    >
        <div class="dialogWrapper">
            <v-card class="dialog">
                <h1>Report issue</h1>
                <v-textarea v-model="message" label="Message" />

                <div style="float: right">
                    <v-btn style="margin: 4px" @click="cancel">Cancel</v-btn>
                    <v-btn style="margin: 4px" color="primary" @click="submit">
                        Submit
                    </v-btn>
                </div>
            </v-card>
        </div>
    </v-dialog>
</template>

<style scoped>
.dialogWrapper {
    display: flex;
    align-items: center;
    justify-content: center;
}
.dialog {
    flex-grow: 1;
    max-width: 600px;
    padding: 32px;
}
</style>
