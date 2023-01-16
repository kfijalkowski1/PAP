<script setup>
import { onMounted, watchEffect } from 'vue'
import { $ref } from 'vue/macros'
import {
    getCourses,
    getAllExchanges,
    getUserGroups,
    getFaculties,
    enterExchange,
} from '@/api'
import { errorCatcher, throwIf } from '@/utils'
import { timeString } from '@/utils'
import router from '@/router'

let mode = $ref('my')

let facultyOptions = $ref([])
let courseOptions = $ref([])
let userGroups = $ref([])
let exchanges = $ref([])

let faculty = $ref(null)
let courses = $ref([])

onMounted(
    errorCatcher(async () => {
        facultyOptions = await getFaculties()
        userGroups = await getUserGroups()
    })
)

watchEffect(
    errorCatcher(async () => {
        if (faculty) {
            courseOptions = await getCourses(faculty)
        } else {
            courseOptions = []
        }
        courses = []
    })
)

watchEffect(
    errorCatcher(async () => {
        if (mode === 'my') {
            exchanges = await getAllExchanges([], true, -1)
        } else {
            if (courses.length > 0) {
                exchanges = await getAllExchanges(courses, false, faculty)
            } else {
                exchanges = []
            }
        }
    })
)

const completeExchange = errorCatcher(async (exchange) => {
    const sellGroup = userGroups.find((usrGroup) =>
        exchange.buyGroups.some(
            (group) => parseInt(usrGroup.groupId) === group.groupId
        )
    )

    throwIf(() => !sellGroup, 400, 'Cannot complete exchange')()

    await enterExchange(sellGroup.groupId, [exchange.sellGroup.groupId])

    router.push('/dashboard')
})
</script>

<template>
    <div class="wrapper">
        <v-sheet elevation="2" class="exchange" rounded>
            <div class="tabs">
                <v-tabs v-model="mode" color="primary" fixed-tabs>
                    <v-tab value="my">My courses</v-tab>
                    <v-tab value="all">All courses</v-tab>
                </v-tabs>
            </div>
            <v-expand-transition appear>
                <div v-if="mode === 'my'"></div>
            </v-expand-transition>
            <v-expand-transition appear>
                <div v-if="mode === 'all'">
                    <v-autocomplete
                        label="Faculty"
                        v-model="faculty"
                        :items="facultyOptions"
                        itemTitle="name"
                        itemValue="id"
                    />
                    <v-select
                        v-model="courses"
                        :items="courseOptions"
                        label="Courses"
                        chips
                        multiple
                        hide-details="auto"
                        itemTitle="code"
                        itemValue="courseId"
                    />
                </div>
            </v-expand-transition>
        </v-sheet>
        <v-sheet
            v-if="exchanges.length === 0"
            elevation="2"
            class="exchange"
            rounded
        >
            No matching exchanges. You can create one
            <router-link to="/createExchange">here</router-link>
        </v-sheet>
        <v-expand-transition
            v-for="(exchange, index) in exchanges"
            :key="index"
            appear
        >
            <v-sheet elevation="2" class="exchange" rounded>
                <h2 style="display: inline">
                    {{ exchange.sellGroup.code }}
                </h2>
                <div class="exchangeDetails">
                    <div style="padding: 8px">
                        <span>
                            <h3 style="display: inline; margin-right: 4px">
                                {{ exchange.sellGroup.groupNr }}
                            </h3>
                            <span
                                class="text-subtitle1"
                                style="display: inline"
                            >
                                {{ timeString(exchange.sellGroup) }}
                            </span>
                        </span>
                    </div>
                    <v-divider vertical />
                    <div style="padding: 8px">
                        <span
                            v-for="(group, index) in exchange.buyGroups"
                            :key="index"
                        >
                            <h3 style="display: inline; margin-right: 4px">
                                {{ group.groupNr }}
                            </h3>
                            <span
                                class="text-subtitle1"
                                style="display: inline"
                            >
                                {{ timeString(group) }}
                            </span>
                            <br />
                        </span>
                    </div>
                </div>
                <v-btn
                    v-if="
                        userGroups.some((usrGroup) =>
                            exchange.buyGroups.some(
                                (group) =>
                                    parseInt(usrGroup.groupId) === group.groupId
                            )
                        )
                    "
                    style="margin: 4px"
                    color="primary"
                    @click="completeExchange(exchange)"
                >
                    Exchange
                </v-btn>
                <span
                    v-else-if="
                        userGroups.some(
                            (usrGroup) =>
                                parseInt(usrGroup.courseId) ===
                                exchange.sellGroup.courseId
                        )
                    "
                    style="margin: 4px"
                >
                    Can't exchange, your are in a group that doesn't match the
                    request
                </span>
                <span v-else style="margin: 4px">
                    Can't exchange, you need to be in this course. You can join
                    <router-link to="/myGroups">here</router-link>
                </span>
            </v-sheet>
        </v-expand-transition>
    </div>
</template>

<style scoped>
.wrapper {
    min-width: 200px;
    margin: auto;
}
.exchange {
    margin: auto;
    margin-top: 16px;
    margin-bottom: 16px;
    padding: 16px;
}
.exchangeDetails {
    display: flex;
    flex-direction: row;
    flex-grow: 1;
    justify-content: space-between;
}
</style>
