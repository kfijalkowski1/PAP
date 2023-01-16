<script setup>
import { onMounted, watchEffect } from 'vue'
import { $ref } from 'vue/macros'
import {
    getCourses,
    getFaculties,
    getGroups,
    getUserGroups,
    addUserGroup,
} from '@/api'
import { errorCatcher, parseTime } from '@/utils'
import AddCourseDialog from '@/components/GroupsDialogs/AddCourseDialog.vue'
import AddGroupNrDialog from '@/components/GroupsDialogs/AddGroupNrDialog.vue'
import { dayStrings } from '@/assets'

let userGroups = $ref([])

let faculty = $ref(null)
let facultyOptions = $ref([])

let course = $ref(null)
let courseOptions = $ref([])
let addCourseDialog = $ref(false)

let groupNr = $ref(null)
let groupNrOptions = $ref([])
let addGroupNrDialog = $ref(false)

onMounted(
    errorCatcher(async () => {
        userGroups = await getUserGroups()

        facultyOptions = await getFaculties()
    })
)

watchEffect(
    errorCatcher(async () => {
        if (faculty) {
            courseOptions = await getCourses(faculty)
        } else {
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
            groupNrOptions = (await getGroups(course)).filter((group) =>
                userGroups.every(
                    (usrGroup) => group.groupId !== usrGroup.groupId
                )
            )
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
                        <th class="text-left">Group number</th>
                        <th class="text-left">Day</th>
                        <th class="text-left">Time of start</th>
                        <th class="text-left">Time of end</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-if="userGroups.length === 0">
                        <td></td>
                        <td></td>
                        <td>No groups :(</td>
                        <td></td>
                        <td></td>
                        <td></td>
                    </tr>
                    <tr v-for="group in userGroups" :key="group.groupId">
                        <td>{{ group.facultyShortname }}</td>
                        <td>{{ group.courseCode }}</td>
                        <td>{{ group.groupNr }}</td>
                        <td>{{ dayStrings[group.day] }}</td>
                        <td>{{ parseTime(group.timeStart) }}</td>
                        <td>{{ parseTime(group.timeEnd) }}</td>
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
                    itemTitle="previewName"
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
                    @setNewOptions="
                        groupNrOptions = $event.filter((group) =>
                            userGroups.every(
                                (usrGroup) => group.groupId !== usrGroup.groupId
                            )
                        )
                    "
                    :courseId="course"
                    :facultyId="faculty"
                />
            </div>
            <v-btn color="primary" @click="joinGroup"> Join group </v-btn>
        </v-sheet>
    </div>
</template>

<style scoped>
.wrapper {
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
