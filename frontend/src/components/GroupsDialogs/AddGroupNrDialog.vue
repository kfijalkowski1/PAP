<script setup>
import { addGroup, getGroups, getClassrooms, getLecturers } from '@/api'
import { errorCatcher } from '@/utils'
import { defineProps, defineEmits, onMounted, watchEffect } from 'vue'
import { $ref } from 'vue/macros'
import { dayStrings } from '@/assets'
import AddLecturerDialog from '@/components/GroupsDialogs/AddLecturerDialog.vue'
import AddClassroomDialog from '@/components/GroupsDialogs/AddClassroomDialog.vue'

const props = defineProps(['modelValue', 'facultyId', 'courseId'])
const emit = defineEmits(['update:modelValue', 'setNewValue', 'setNewOptions'])

let groupNr = $ref(null)
let day = $ref(null)
let timeStart = $ref(null)
let timeEnd = $ref(null)

let classroom = $ref(null)
let classroomOptions = $ref([])
let addClassroomDialog = $ref(false)

let lecturer = $ref(null)
let lecturerOptions = $ref([])
let addLecturerDialog = $ref(false)

const convertTime = (value) => {
    const [hours, minutes] = value.split(':').map((val) => parseInt(val))
    return 60 * hours + minutes
}

const cancel = () => {
    groupNr = null
    day = null
    timeStart = null
    timeEnd = null

    classroom = null
    groupNr = null

    lecturer = null

    emit('update:modelValue', false)
}

const submit = errorCatcher(async () => {
    await addGroup(
        groupNr,
        props.courseId,
        lecturer,
        classroom,
        day,
        convertTime(timeStart),
        convertTime(timeEnd)
    )
    const newOptions = await getGroups(props.courseId)

    let id = newOptions.find((val) => val.groupNr === groupNr).groupId

    emit('setNewOptions', newOptions)
    emit('setNewValue', id)
    emit('update:modelValue', false)

    groupNr = null
    day = null
    timeStart = null
    timeEnd = null

    classroom = null
    groupNr = null

    lecturer = null
})

onMounted(
    errorCatcher(async () => {
        lecturerOptions = await getLecturers()
    })
)

watchEffect(
    errorCatcher(async () => {
        if (props.facultyId) {
            classroomOptions = await getClassrooms(props.facultyId)
        } else {
            classroomOptions = []
            groupNr = null
        }
        classroom = null
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

                <div class="line">
                    <v-autocomplete
                        label="Lecturer"
                        v-model="lecturer"
                        :items="lecturerOptions"
                        itemTitle="previewName"
                        itemValue="lecturerId"
                    />
                    <v-btn
                        icon="mdi-plus"
                        size="small"
                        class="addButton"
                        @click="addLecturerDialog = true"
                    />
                    <AddLecturerDialog
                        v-model="addLecturerDialog"
                        @setNewValue="lecturer = $event"
                        @setNewOptions="lecturerOptions = $event"
                    />
                </div>
                <div class="line">
                    <v-autocomplete
                        label="Classroom"
                        v-model="classroom"
                        :items="classroomOptions"
                        itemTitle="nr"
                        itemValue="id"
                    />
                    <v-btn
                        icon="mdi-plus"
                        size="small"
                        class="addButton"
                        @click="addClassroomDialog = true"
                    />
                    <AddClassroomDialog
                        v-model="addClassroomDialog"
                        @setNewValue="classroom = $event"
                        @setNewOptions="classroomOptions = $event"
                        :facultyId="props.facultyId"
                    />
                </div>

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
.line {
    display: flex;
    flex-direction: row;
    min-width: 300px;
}
.addButton {
    margin: 8px;
    margin-right: 0px;
}
</style>
