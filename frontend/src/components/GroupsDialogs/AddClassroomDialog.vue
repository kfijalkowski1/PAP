<script setup>
import { addClassroom, getClassrooms } from '@/api'
import { errorCatcher } from '@/utils'
import { defineProps, defineEmits } from 'vue'
import { $ref } from 'vue/macros'

const props = defineProps(['modelValue', 'facultyId'])
const emit = defineEmits(['update:modelValue', 'setNewValue', 'setNewOptions'])

let classNr = $ref(null)

const cancel = () => {
    classNr = null

    emit('update:modelValue', false)
}
const submit = errorCatcher(async () => {
    await addClassroom(classNr, props.facultyId)
    const newOptions = await getClassrooms(props.facultyId)

    let id = newOptions.find((val) => val.nr === classNr).id

    emit('setNewOptions', newOptions)
    emit('setNewValue', id)
    emit('update:modelValue', false)

    classNr = null
})
</script>

<template>
    <v-dialog
        :model-value="props.modelValue"
        @update:model-value="emit('update:modelValue', $event)"
    >
        <div class="dialogWrapper">
            <v-card class="dialog">
                <v-text-field v-model="classNr" label="Number" />

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
