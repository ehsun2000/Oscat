import { createRouter, createWebHistory } from 'vue-router';
import Swal from 'sweetalert2';

const routes = [
  {
    path: '/adminlogin',
    name: 'Login',
    component: () => import('@/views/AdminLogin.vue'),
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/AdminDashboard.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '/member',
        name: 'Member',
        component: import('@/views/MemberAdmin.vue'),
      },
      {
        path: '/member/insert',
        name: 'Insert',
        component: import('@/views/MemberAdInsert.vue'),
      },
      {
        path: '/member/update/:memberId',
        name: 'Update',
        component: import('@/views/MemberAdUpdate.vue'),
      },
      {
        path: '/member-report',
        name: 'MAnalysis',
        component: import('@/views/MemberAnalysis.vue'),
      },
      {
        path: '/transorder',
        name: 'TransOrder',
        component: import('@/views/OrderAdmin.vue'),
      },
      {
        path: '/transorder-report',
        name: 'OAnalysis',
        component: import('@/views/OrderAnalysis.vue'),
      },
      {
        path: '/cinema-select',
        name: 'CinemaSelect',
        component: import('@/views/CinemaSelect.vue'),
      },
      {
        path: '/cinema-detail/:cinemaId',
        name: 'CinemaDetail',
        component: import('@/views/CinemaDetail.vue'),
      },
      {
        path: '/showtime',
        name: 'ShowTimeManager',
        component: import('@/views/ShowTimeManager.vue'),
      },
      {
        path: '/cinema-product',
        name: 'ProductManager',
        component: import('@/views/ProductController.vue'),
      },
      {
        path: '/product/add',
        name: 'ProductAdd',
        component: import('@/views/AddProduct.vue'),
      },
      {
        path: '/product/update/:productName',
        name: 'ProductUpdate',
        component: import('@/views/UpdateProduct.vue'),
      },
      {
        path: '/cinema-seat',
        name: 'SeatManager',
        component: import('@/views/SeatController.vue'),
      },
      {
        path: '/movie',
        name: 'Movie',
        component: import('@/views/AdminMovie.vue'),
      },
      {
        path: '/movie/add',
        name: 'Add',
        component: import('@/views/AdminAddMovie.vue'),
      },
      {
        path: '/movie/edit/:movieId',
        name: 'Edit',
        component: import('@/views/AdminEditMovie.vue'),
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach(async (to) => {
  const requiresAuth = to.meta.requiresAuth;

  if (requiresAuth) {
    try {
      // 發送 API 請求來檢查用戶身份
      const response = await fetch(
        `${import.meta.env.VITE_CHECK_API_ENDPOINT}`,
        {
          method: 'GET',
          credentials: 'include',
        },
      );

      if (response.status != 200) {
        const { isConfirmed } = await Swal.fire({
          title: '驗證失效',
          text: '您的驗證已失效，請重新登入',
          icon: 'info',
          confirmButtonText: '回到登入頁面',
        });
        if (isConfirmed) {
          return { name: 'Login' }; // 身份驗證失敗，導向登入頁
        }
      }
    } catch (error) {
      const { isConfirmed } = await Swal.fire({
        title: '驗證失效',
        text: '您的驗證已失效，請重新登入',
        icon: 'info',
        confirmButtonText: '回到登入頁面',
      });
      if (isConfirmed) {
        return { name: 'Login' }; // API 請求失敗，導向登入頁
      }
    }
  }
});

export default router;
