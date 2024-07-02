<!-- src/components/Home.vue -->
<template>
    <div class="home">
      <h1 class="title">学生游学系统</h1>
      <!-- 只有当用户未登录时显示登录按钮 -->
      <div v-if="!isLoggedIn" class="button-group">
      <Button type="primary" size="large"  @click="goToLogin">登录</Button>
      <div class="button-gap"></div> <!-- 空白间隙 -->
      <Button type="primary" size="large"  @click="goToRegister">注册</Button>
      </div>
      <div v-else>
        <p>欢迎回来，{{ user.name }}</p>
        <Button type="primary" size="large"  @click="logout">切换账户</Button>
      <!-- 其他功能组件 -->
      </div>
    </div>
</template>
  
<script>
  import { mapState } from 'vuex';
  
  export default {
    name: 'HomeView',
    computed: {
      
      // 使用mapState来获取Vuex中的isLoggedIn状态
      ...mapState(['isLoggedIn', 'user'])
      //可以用mapGetter
      
    },
    methods: {
      goToLogin() {
        this.$router.push('/login');
        //跳转到登录界面
      },
      goToRegister() {
        this.$router.push('/register');
        //跳转到注册界面
      },
      logout(){
        this.$store.dispatch('logout').then(() => {
    // 可选：登出成功后的额外操作
    this.$router.push('/'); // 登出后重定向到根路由
  });
      }
    },
    created() {
    // 当组件创建时清空状态
    this.$store.dispatch('logout');
  }
  };
</script>
  
<style scoped>
  /* 仅作用于Home.vue组件的样式 */
.home {
    font-size: 30px;
    padding: 10px;
    text-align: center;
    margin-top: 180px; 
}
.title {
  margin-bottom: 40px; /* 增加标题和按钮之间的垂直距离 */
}
.button-group {
    display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
}
.button-gap {
  width: 40px;
}
</style>