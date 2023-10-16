<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import Swal from 'sweetalert2/dist/sweetalert2.js';

const router = useRouter();
const email = ref('admin@example.com');
const password = ref('password');

const handleSubmit = () => {
  // 假設這裡是驗證登入的邏輯
  if (email.value === 'admin@example.com' && password.value === 'password') {
    localStorage.setItem('accessToken', 'some-token');
    router.push({ path: '/dashboard' });
  } else {
    Swal.fire({
      icon: 'error',
      title: '登入失敗',
      text: '用戶名或密碼不正確',
    });
  }
};
</script>

<template>
  <div class="container py-5 h-100">
    <div class="row d-flex justify-content-center align-items-center h-100">
      <div class="col-12 col-md-8 col-lg-6 col-xl-5">
        <div class="card bg-dark text-white" style="border-radius: 1rem">
          <div class="card-body p-5 text-center">
            <div class="md-5 mt-md-4 pb-5">
              <h2 class="fw-bold mb-2 text-uppercase">Login</h2>

              <form @submit.prevent="handleSubmit">
                <div class="form-outline form-white mb-4">
                  <input
                    type="email"
                    v-model="email"
                    id="email"
                    class="form-control form-control-lg text-light"
                    required
                  />
                  <label class="form-label" for="email">Email</label>
                </div>

                <div class="form-outline form-white mb-4">
                  <input
                    type="password"
                    v-model="password"
                    id="password"
                    class="form-control form-control-lg text-light"
                    required
                  />
                  <label class="form-label" for="password">Password</label>
                </div>

                <button class="btn btn-outline-light btn-lg px-5" type="submit">
                  Login
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
