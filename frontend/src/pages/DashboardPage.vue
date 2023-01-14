<script setup>
import { onMounted } from 'vue'
import { $ref } from 'vue/macros'
import { getExchanges } from '@/api'
import { errorCatcher } from '@/utils'
import { timeString } from '@/utils'
let exchanges = $ref([])

onMounted(
    errorCatcher(async () => {
        exchanges = await getExchanges()
    })
)
</script>

<template>
    <div class="wrapper">
        <v-expand-transition
            v-for="(exchange, index) in exchanges"
            :key="index"
        >
            <v-sheet
                elevation="2"
                :class="
                    'exchange ' +
                    (exchange.complete ? 'exchangeComplete' : 'exchangePending')
                "
                rounded
            >
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
