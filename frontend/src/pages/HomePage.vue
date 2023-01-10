<script setup></script>

<template>
    <div class="wrapper">
        <v-sheet color="white" elevation="2" class="container">
            <h1>Welcome to pap22z-z27!</h1>
            <span
                >This software is supposed to help with group exchanges between
                students<br />
                <br />
                Please sign in to continue</span
            >
            <br />
            <v-btn color="primary" to="/login">Sign in</v-btn>
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
</style>
