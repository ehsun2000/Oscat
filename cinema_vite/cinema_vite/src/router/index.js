import { createRouter, createWebHistory } from 'vue-router';
import MemberAdmin from '../views/yuho/MemberAdmin.vue';
import MemberAdInsert from '../views/yuho/MemberAdInsert.vue';
import MemberAdUpdate from '../views/yuho/MemberAdUpdate.vue';
import HelloWorld from '../components/HelloWorld.vue';

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HelloWorld,
  },
  {
    path: '/member',
    name: 'Member',
    component: MemberAdmin,
  },
  {
    path: '/member/insert',
    name: 'Insert',
    component: MemberAdInsert,
  },
  {
    path: '/member/update/:memberId',
    name: 'Update',
    component: MemberAdUpdate,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
