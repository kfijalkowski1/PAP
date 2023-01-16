<script setup>
import { $ref } from 'vue/macros'
import { defineProps, defineEmits } from 'vue'

const props = defineProps(['name', 'value'])
const emit = defineEmits(['submit'])

let editMode = $ref(false)
let tempValue = $ref('')

const startEdit = () => {
    editMode = true
    tempValue = props.value
}

const submit = () => {
    emit('submit', tempValue)
    editMode = false
}
</script>

<template>
    <tr>
        <td>{{ props.name }}</td>
        <td>
            <v-text-field
                v-if="editMode"
                color="primary"
                variant="underlined"
                v-model="tempValue"
                class="textField"
            />
            <span v-if="!editMode">{{ props.value }}</span>
        </td>
        <td>
            <v-btn
                v-if="!editMode"
                icon="mdi-pencil"
                size="small"
                color="primary"
                @click="startEdit()"
            />
            <v-btn
                v-if="editMode"
                icon="mdi-close"
                size="small"
                color="error"
                @click="editMode = false"
            />
            <v-btn
                v-if="editMode"
                icon="mdi-content-save"
                size="small"
                color="success"
                @click="submit"
            />
        </td>
    </tr>
</template>
<style scoped>
.textField {
    min-width: 250px;
}
</style>
