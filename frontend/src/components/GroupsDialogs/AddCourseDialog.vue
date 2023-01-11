<script setup>
import { addCourse, getCourseTypes, getCourses } from '@/api'
import { errorCatcher } from '@/utils'
import { defineProps, defineEmits, watch } from 'vue'
import { $ref } from 'vue/macros'

const props = defineProps(['modelValue', 'facultyId'])
const emit = defineEmits(['update:modelValue', 'setNewValue', 'setNewOptions'])

let courseTypes = $ref(['aaa'])

let type = $ref(null)
let name = $ref(null)
let code = $ref(null)

const cancel = () => {
    type = null
    name = null
    code = null

    emit('update:modelValue', false)
}
const submit = errorCatcher(async () => {
    await addCourse(code, name, type, props.facultyId)
    const newOptions = await getCourses(props.facultyId)

    let id = newOptions.find((val) => val.code === code).lecturerId

    emit('setNewOptions', newOptions)
    emit('setNewValue', id)
    emit('update:modelValue', false)
})

watch(
    props,
    errorCatcher(async (newProps) => {
        if (newProps.modelValue) {
            courseTypes = await getCourseTypes()
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
                    v-model="type"
                    label="Course type"
                    :items="courseTypes"
                />

                <v-text-field v-model="name" label="Name" />
                <v-text-field v-model="code" label="Code" />

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
