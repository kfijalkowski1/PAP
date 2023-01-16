<script setup>
import { addLecturer, getLecturerDegrees, getLecturers } from '@/api'
import { errorCatcher } from '@/utils'
import { defineProps, defineEmits, watch } from 'vue'
import { $ref } from 'vue/macros'

const props = defineProps(['modelValue'])
const emit = defineEmits(['update:modelValue', 'setNewValue', 'setNewOptions'])

let lecturerDegrees = $ref([])

let degree = $ref(null)
let firstname = $ref(null)
let surname = $ref(null)

const cancel = () => {
    degree = null
    firstname = null
    surname = null

    emit('update:modelValue', false)
}
const submit = errorCatcher(async () => {
    await addLecturer(firstname, surname, degree)
    const newOptions = await getLecturers()

    let id = newOptions.find(
        (val) =>
            val.name === firstname &&
            val.surname === surname &&
            val.title === degree
    ).lecturerId

    emit('setNewOptions', newOptions)
    emit('setNewValue', id)
    emit('update:modelValue', false)

    degree = null
    firstname = null
    surname = null
})

watch(
    props,
    errorCatcher(async (newProps) => {
        if (newProps.modelValue) {
            lecturerDegrees = await getLecturerDegrees()
        }
    })
)
</script>

<template>
    <v-dialog
        :model-value="props.modelValue"
        @update:model-value="emit('update:modelValue', $event)"
    >
        <div class="dialogWrapper">
            <v-card class="dialog">
                <v-select
                    v-model="degree"
                    label="Degree"
                    :items="lecturerDegrees"
                />

                <v-text-field v-model="firstname" label="Firstname" />
                <v-text-field v-model="surname" label="Surname" />

                <div style="float: right">
                    <v-btn style="margin: 4px" @click="cancel">Cancel</v-btn>
                    <v-btn style="margin: 4px" color="primary" @click="submit">
                        Add
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
