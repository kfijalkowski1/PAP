import { createRouter, createWebHistory } from 'vue-router'
import {
    HomePage,
    DashboardPage,
    CreateExchangePage,
    UserInfoPage,
    MyGroupsPage,
    LoginPage,
    TestingPage,
} from '@/pages'

const routes = [
    {
        path: '/',
        name: 'home',
        component: HomePage,
    },
    {
        path: '/dashboard',
        name: 'dashboard',
        component: DashboardPage,
    },
    {
        path: '/createExchange',
        name: 'createExchange',
        component: CreateExchangePage,
    },
    {
        path: '/myGroups',
        name: 'myGroups',
        component: MyGroupsPage,
    },
    {
        path: '/userInfo',
        name: 'userInfo',
        component: UserInfoPage,
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
