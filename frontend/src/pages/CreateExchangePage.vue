<script setup>
import { getGroups, getUserGroups } from '@/api'
import { errorCatcher, parseTime } from '@/utils'
import { onMounted, watchEffect, computed } from 'vue'
import { $ref } from 'vue/macros'
import { enterExchange } from '@/api'
import { dayStrings } from '@/assets'
import router from '@/router'

let userGroups = $ref([
    {
        groupId: 123,
        groupNr: '012',
        courseCode: 'PIPR',
        facultyShortname: 'WEiTI',
        day: 3,
        timeStart: 500,
        timeEnd: 640,
    },
])
let matchingGroups = $ref([
    {
        groupId: 100,
        groupNr: '011',
        courseCode: 'PIPR',
        facultyShortname: 'WEiTI',
        day: 3,
        timeStart: 500,
        timeEnd: 640,
    },
    {
        groupId: 101,
        groupNr: '013',
        courseCode: 'PIPR',
        facultyShortname: 'WEiTI',
        day: 3,
        timeStart: 500,
        timeEnd: 640,
    },
])

let sellGroup = $ref(null)
let buyGroups = $ref([])

onMounted(
    errorCatcher(async () => {
        userGroups = await getUserGroups()
    })
)

watchEffect(
    errorCatcher(async () => {
        if (sellGroup) {
            matchingGroups = (
                await getGroups(
                    userGroups.find((elem) => elem.groupId === sellGroup)
                        .courseId
                )
            ).filter((group) => group.groupId !== sellGroup)

            buyGroups = []
        }
    })
)

const sellGroupObject = computed(() =>
    userGroups.find((elem) => elem.groupId === sellGroup)
)

const addNewExchange = errorCatcher(async () => {
    await enterExchange(sellGroup, buyGroups)

    sellGroup = null
    buyGroups = []
    matchingGroups = []

    router.push('/dashboard')
})
</script>

<template>
    <div class="wrapper">
        <v-sheet color="white" elevation="2" class="container">
            <h3>Course to trade</h3>
            <v-autocomplete
                v-model="sellGroup"
                label="Course to trade"
                :items="userGroups"
                item-title="courseCode"
                item-value="groupId"
            />
            <div v-if="sellGroup">
                <h3>Current group</h3>
                <h5>Group nr: {{ sellGroupObject.groupNr }}</h5>
                <h5>Day: {{ dayStrings[sellGroupObject.day] }}</h5>
                <h5>Start time: {{ parseTime(sellGroupObject.timeStart) }}</h5>
                <h5>End time: {{ parseTime(sellGroupObject.timeEnd) }}</h5>
            </div>

            <v-divider />

            <h3 style="margin-top: 16px">Groups to buy</h3>
            <v-table style="margin: 8px">
                <thead>
                    <tr>
                        <th class="text-left">Include</th>
                        <th class="text-left">Group number</th>
                        <th class="text-left">Day</th>
                        <th class="text-left">Time of start</th>
                        <th class="text-left">Time of end</th>
                        <th class="text-left">Priority</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-if="matchingGroups.length === 0">
                        <td>No matching groups :(</td>
                    </tr>
                    <tr v-for="group in matchingGroups" :key="group.groupId">
                        <td>
                            <v-checkbox
                                density="compact"
                                hide-details="auto"
                                color="primary"
                                :modelValue="
                                    buyGroups.some(
                                        (elem) => elem === group.groupId
                                    )
                                "
                                @update:modelValue="
                                    $event
                                        ? buyGroups.push(group.groupId)
                                        : (buyGroups = buyGroups.filter(
                                              (elem) => elem !== group.groupId
                                          ))
                                "
                            />
                        </td>
                        <td>{{ group.groupNr }}</td>
                        <td>{{ dayStrings[group.day] }}</td>
                        <td>{{ parseTime(group.timeStart) }}</td>
                        <td>{{ parseTime(group.timeEnd) }}</td>
                        <td>
                            {{
                                buyGroups.findIndex(
                                    (elem) => elem === group.groupId
                                ) + 1 || null
                            }}
                        </td>
                    </tr>
                </tbody>
            </v-table>

            <div>
                <span>
                    Missing a group? Add it
                    <router-link to="/myGroups">here</router-link>
                </span>

                <div style="float: right">
                    <v-btn color="primary" @click="addNewExchange"
                        >Add exchange</v-btn
                    >
                </div>
            </div>
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
</style>
