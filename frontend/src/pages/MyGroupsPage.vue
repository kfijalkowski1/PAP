<script setup>
import { onMounted } from 'vue'
import { $ref } from 'vue/macros'
import { getFaculties, getLecturers } from '@/api'
import { errorCatcher } from '@/utils'
import AddLecturerDialog from '@/components/GroupsDialogs/AddLecturerDialog.vue'

let faculty = $ref(null)
let facultyOptions = $ref([
    { name: 'WEITI', id: 1 },
    { name: 'SIMR', id: 2 },
    { name: 'MEL', id: 3 },
])

let lecturer = $ref(null)
let lecturerOptions = $ref([
    { name: 'WEITI', id: 1 },
    { name: 'SIMR', id: 2 },
    { name: 'MEL', id: 3 },
])
let addLecturerDialog = $ref(false)

/*

let classroom = $ref(null)
let classroomOptions = $ref([
    { name: 'WEITI', id: 1 },
    { name: 'SIMR', id: 2 },
    { name: 'MEL', id: 3 },
])

let course = $ref(null)
let courseOptions = $ref([
    { name: 'WEITI', id: 1 },
    { name: 'SIMR', id: 2 },
    { name: 'MEL', id: 3 },
])

let groupNr = $ref(null)
let groupNrOptions = $ref([
    { name: 'WEITI', id: 1 },
    { name: 'SIMR', id: 2 },
    { name: 'MEL', id: 3 },
])
*/

onMounted(
    errorCatcher(async () => {
        facultyOptions = await getFaculties()
        lecturerOptions = await getLecturers()
        /*
        classroomOptions = await getClassrooms()
        courseOptions = await getCourses()
        groupNrOptions = await getGroups
        */
    })
)
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
                    <tr>
                        <td>WEITI</td>
                        <td>AISDI</td>
                        <td>104</td>
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
