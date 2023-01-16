<script setup>
import { onMounted, watchEffect } from 'vue'
import { $ref } from 'vue/macros'
import { getCourses, getAllExchanges, getUserGroups, getFaculties } from '@/api'
import { errorCatcher } from '@/utils'
import { timeString } from '@/utils'

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
            courses = []
        }
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
</script>

<template>
    <div class="wrapper">
        <v-sheet
            v-if="exchanges.length === 0"
            elevation="2"
            class="exchange"
            rounded
        >
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
        <v-expand-transition
            v-for="(exchange, index) in exchanges"
            :key="index"
            appear
        >
            <v-sheet
                elevation="2"
                :class="
                    'exchange ' +
                    (exchange.complete ? 'exchangeComplete' : 'exchangePending')
                "
                rounded
            >
                {{ userGroups }}
                <h2 style="display: inline">
                    {{ exchange.sellGroup.courseCode }}
                </h2>
                <span class="text-subtitle1" style="float: right">{{
                    exchange.complete ? 'complete' : 'pending'
                }}</span>
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
.exchangePending {
    background-color: lightblue;
}
.exchangeComplete {
    background-color: lightgreen;
}
.exchangeDetails {
    display: flex;
    flex-direction: row;
    flex-grow: 1;
    justify-content: space-between;
}
</style>
