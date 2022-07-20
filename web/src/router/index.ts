import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import AboutView from '../views/AboutView.vue'
import EbookView from '../views/admin/EbookView.vue'
import CategorykView from '../views/admin/CategorykView.vue'
import DocView from "../views/admin/DocView.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    component: AboutView
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    // component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/admin/ebook',
    name: 'ebook',
    component: EbookView
  },
  {
    path: '/admin/category',
    name: 'category',
    component: CategorykView
  },
  {
    path: '/admin/doc',
    name: 'doc',
    component: DocView
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
