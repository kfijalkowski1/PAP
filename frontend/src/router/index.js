import { createRouter, createWebHistory } from 'vue-router'
import { HomePage, LoginPage, TestingPage } from '@/pages'

const routes = [
    {
        path: '/',
        name: 'home',
        component: HomePage,
    },
    {
        path: '/login',
        name: 'login',
        component: LoginPage,
    },
    {
        path: '/testing',
        name: 'testing',
        component: TestingPage,
    },
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes,
})

export default router
