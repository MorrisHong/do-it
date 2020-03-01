import Vue from 'vue'
import Router from 'vue-router'
import MainPage from '@/views/MainPage'
import LoginPage from '@/views/LoginPage'
import RegisterPage from '@/views/RegisterPage'
import DetailPage from '@/views/DetailPage'

Vue.use(Router)

export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'MainPage',
      component: MainPage
    },
    {
      path: '/login',
      name: 'LoginPage',
      component: LoginPage
    },
    {
      path: '/register',
      name: 'RegisterPage',
      component: RegisterPage
    },
    {
      path: '/detail',
      name: 'DetailPage',
      component: DetailPage
    }
  ]
})
