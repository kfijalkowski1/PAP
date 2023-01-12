<script setup>
import { onMounted, watchEffect } from 'vue'
import { $ref } from 'vue/macros'
import {
    getClassrooms,
    getCourses,
    getFaculties,
    getLecturers,
    getGroups,
    getUserGroups,
    addUserGroup,
} from '@/api'
import { errorCatcher } from '@/utils'
import AddLecturerDialog from '@/components/GroupsDialogs/AddLecturerDialog.vue'
import AddCourseDialog from '@/components/GroupsDialogs/AddCourseDialog.vue'
import AddClassroomDialog from '@/components/GroupsDialogs/AddClassroomDialog.vue'
import AddGroupNrDialog from '@/components/GroupsDialogs/AddGroupNrDialog.vue'

let userGroups = $ref([])

let faculty = $ref(null)
let facultyOptions = $ref([])

let lecturer = $ref(null)
let lecturerOptions = $ref([])
let addLecturerDialog = $ref(false)

let course = $ref(null)
let courseOptions = $ref([])
let addCourseDialog = $ref(false)

let classroom = $ref(null)
let classroomOptions = $ref([])
let addClassroomDialog = $ref(false)

let groupNr = $ref(null)
let groupNrOptions = $ref([])
let addGroupNrDialog = $ref(false)

onMounted(
    errorCatcher(async () => {
        userGroups = await getUserGroups()

        facultyOptions = await getFaculties()
        lecturerOptions = await getLecturers()
    })
)

watchEffect(
    errorCatcher(async () => {
        if (faculty) {
            classroomOptions = await getClassrooms(faculty)
            courseOptions = await getCourses(faculty)
        } else {
            classroomOptions = []
            classroom = null
            courseOptions = []
            course = null
            groupNrOptions = []
            groupNr = null
        }
    })
)

watchEffect(
    errorCatcher(async () => {
        if (course) {
            groupNrOptions = await getGroups(course)
        } else {
            groupNrOptions = []
            groupNr = null
        }
    })
)

const joinGroup = errorCatcher(async () => {
    await addUserGroup(groupNr)
    userGroups = await getUserGroups()

    faculty = null
    lecturer = null
    classroomOptions = []
    classroom = null
    courseOptions = []
    course = null
    groupNrOptions = []
    groupNr = null
})
</script>

<template>
    <div class="wrapper">
        <v-sheet color="white" elevation="2" class="container">
            <h3>My groups</h3>
            <v-table>
                <thead>
                    <tr>
                        <th class="text-left">Faculty</th>
                        <th class="text-left">Course</th>
                        <th class="text-left">Group</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-if="userGroups.length === 0">
                        <td></td>
                        <td>No groups :(</td>
                        <td></td>
                    </tr>
                    <tr v-for="group in userGroups" :key="group.groupId">
                        <td>{{ group.facultyShortname }}</td>
                        <td>{{ group.courseCode }}</td>
                        <td>{{ group.groupNr }}</td>
                    </tr>
                </tbody>
            </v-table>
            <v-divider />
            <h3 style="margin-top: 16px">Add group</h3>
            <div class="line">
                <v-autocomplete
                    label="Faculty"
                    v-model="faculty"
                    :items="facultyOptions"
                    itemTitle="name"
                    itemValue="id"
                />
            </div>
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
                    :facultyId="faculty"
                />
            </div>
            <div class="line">
                <v-autocomplete
                    label="Course"
                    v-model="course"
                    :items="courseOptions"
                    itemTitle="code"
                    itemValue="courseId"
                />
                <v-btn
                    icon="mdi-plus"
                    size="small"
                    class="addButton"
                    @click="addCourseDialog = true"
                />
                <AddCourseDialog
                    v-model="addCourseDialog"
                    @setNewValue="course = $event"
                    @setNewOptions="courseOptions = $event"
                    :facultyId="faculty"
                />
            </div>
            <div class="line">
                <v-autocomplete
                    label="Group number"
                    v-model="groupNr"
                    :items="groupNrOptions"
                    itemTitle="groupNr"
                    itemValue="groupId"
                />
                <v-btn
                    icon="mdi-plus"
                    size="small"
                    class="addButton"
                    @click="addGroupNrDialog = true"
                />
                <AddGroupNrDialog
                    v-model="addGroupNrDialog"
                    @setNewValue="groupNr = $event"
                    @setNewOptions="groupNrOptions = $event"
                    :courseId="course"
                    :classroomId="classroom"
                    :lecturerId="lecturer"
                />
            </div>
            <v-btn color="primary" @click="joinGroup"> Join group </v-btn>
        </v-sheet>
    </div>
</template>

<style scoped>
.wrapper {
    max-width: 600px;
    margin: auto;
    padding-top: 32px;
}
.container {
    padding: 32px;
    margin: 16px;
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
