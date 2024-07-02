// src/router.js
import { createRouter, createWebHistory } from 'vue-router';
import UserLogin from './components/UserLogin.vue'; // 导入UserLogin组件
import HomeView from './components/HomeView.vue';// 如果有其他视图组件，也在这里导入
import UserRegister from './components/UserRegister.vue';
import HomeService from './components/HomeService.vue';
import InnermapContainer from './components/InnermapContainer.vue';

// 定义路由配置
const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomeView
  },
  {
    path: '/register', // 如果有注册页面，可以在这里添加对应的路由对象
    name: 'Register',
    component: UserRegister
  },
  {
    path: '/login', // UserLogin组件对应的路由
    name: 'Login',
    component: UserLogin
  },
  {
    path: '/service',
    name: 'Service',
    component: HomeService,
    meta: { requiresAuth: true }, // 添加路由元信息，表示需要登录才能访问
  },
  {
    path: '/map',
    name: 'Map',
    component: InnermapContainer

  },
  // 可以在这里添加更多的路由对象
];

// 创建路由器实例
const router = createRouter({
  history: createWebHistory(),
  routes: routes
});

// 添加路由守卫，检查用户是否登录
// router.beforeEach((to, from, next) => {
//   if (to.matched.some(record => record.meta.requiresAuth)) {
//     // 判断用户是否已经登录，这里假设你在存储中存储了用户登录状态
//     const isAuthenticated = localStorage.getItem('user-token');
//     if (!isAuthenticated) {
//       // 如果用户未登录，则重定向到登录页面
//       next('/');
//     } else {
//       next();
//     }
//   } else {
//     next();
//   }
// });
// 导出路由器实例
export default router;