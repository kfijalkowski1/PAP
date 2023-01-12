<script setup>
import { addGroup, getGroups } from '@/api'
import { errorCatcher } from '@/utils'
import { defineProps, defineEmits } from 'vue'
import { $ref } from 'vue/macros'
import { dayStrings } from '@/assets'

const props = defineProps([
    'modelValue',
    'classroomId',
    'lecturerId',
    'courseId',
])
const emit = defineEmits(['update:modelValue', 'setNewValue', 'setNewOptions'])

let groupNr = $ref(null)
let day = $ref(null)
let timeStart = $ref(null)
let timeEnd = $ref(null)

const convertTime = (value) => {
    const [hours, minutes] = value.split(':').map((val) => parseInt(val))
    return 60 * hours + minutes
}

const cancel = () => {
    groupNr = null
    day = null
    timeStart = null
    timeEnd = null

    emit('update:modelValue', false)
}

const submit = errorCatcher(async () => {
    await addGroup(
        groupNr,
        props.courseId,
        props.lecturerId,
        props.classroomId,
        day,
        convertTime(timeStart),
        convertTime(timeEnd)
    )
    const newOptions = await getGroups(props.courseId)

    let id = newOptions.find((val) => val.groupNr === groupNr).groupId

    emit('setNewOptions', newOptions)
    emit('setNewValue', id)
    emit('update:modelValue', false)
})
</script>

<template>
    <v-dialog
        :model-value="props.modelValue"
        @update:model-value="emit('update:modelValue', $event)"
    >
        <div class="dialogWrapper">
            <v-card class="dialog">
                <v-text-field v-model="groupNr" label="Group number" />

                <v-select
                    v-model="day"
                    label="Day"
                    :items="dayStrings.map((name, value) => ({ name, value }))"
                    item-title="name"
                    item-value="value"
                />

                <v-text-field
                    v-model="timeStart"
                    label="Start time"
                    placeholder="HH:MM"
                />
                <v-text-field
                    v-model="timeEnd"
                    label="End time"
                    placeholder="HH:MM"
                />

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
