<template>
  <div class="demo-login">
    <h2>登录页面</h2>
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="username">名字：</label>
        <input type="text" id="username" v-model="username" required class="form-control">
      </div>
      <div class="form-group">
        <label for="password">密码：</label>
        <input type="password" id="password" v-model="password" required class="form-control">
      </div>
      <div class="auto-login">
        <Checkbox v-model="autoLogin" size="large">自动登录</Checkbox>
        <a href="javascript:void(0)" @click="showForgetPassword">忘记密码</a>
      </div>
      <button type="submit" class="submit-button">登录</button>
    </form>
  </div>
</template>

<script>
import api from '@/api';
export default {
  data() {
    return {
      username: '',
      password: '',
      autoLogin: false
    };
  },
  methods: {
    handleSubmit() {
      api.post('/user/login', {
        "username": this.username,
        "password": this.password,
        // "autoLogin": this.autoLogin
      }, {
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        }
      })
      .then(response => {
        
        // 登录成功
        if (response.data) {
          this.$store.dispatch('login', {
            username: this.username,
          })
          .then(() => {
          // 登录成功后的逻辑
          alert("登录成功");
          this.$router.push('/service');
        })
        } 
      })
      .catch(error => {
  if (error.response && error.response.status === 401) {
    // 处理401状态码
    this.$Modal.error({
      title: '登录失败',
      content: '401:用户名或密码错误,请重新输入 ',
    });
  } else {
    // 其他错误的通用处理
    this.$Modal.error({
      title: '登录失败',
      content: '发生未知网络错误，请稍后再试',
    });
  }
});

    },
    showForgetPassword() {
      // 显示忘记密码的逻辑
      // 这里只是一个示例，你需要根据实际情况实现忘记密码的功能
      this.$Modal.info({
        title: '忘记密码',
        content: '请联系管理员重置密码',
      });
    },
  },
};
</script>

<style scoped>
.demo-login {
  width: 400px;
  margin: 50px auto;
  padding: 30px;
  background: #f7f7f7;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  color: #333;
}
.demo-login form div {
  display: flex; /* 使用Flexbox布局 */
  align-items: center; /* 垂直居中 */
  margin-bottom: 10px; /* 一致的外边距 */
}
.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  white-space: nowrap; /* 防止标签换行 */
  margin-bottom: 5px;
  font-family: 'Arial', sans-serif; /* 设置字体族 */
  font-size: 16px; /* 设置字体大小 */
  color: #555;
}

/* 修改输入框样式，去除边框，增加灰色背景 */
.form-control {
  width: 100%;
  flex-grow: 1;
  padding: 0.5rem;
  margin-right: 10px; 
  max-width: 300px;
  margin-bottom: 10px;
  box-sizing: border-box;
  border: 1px solid #ccc;
  border-radius: 4px;
  background: #fff;
  color: #333;
}

/* 输入框聚焦效果 */
.form-control:focus {
  outline: none;
  border-color: #555555;
}

.auto-login {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

/* 修改按钮样式为灰色系，悬停效果为更深的灰色 */
.submit-button {
  padding: 5px 20px; /* 增加内边距 */
  font-size: 18px; /* 增大字体大小 */
  color: white; /* 设置文字颜色 */
  background-color: #6c6c6c; /* 设置背景颜色 */
  border: none; /* 移除边框 */
  border-radius: 4px; /* 设置圆角 */
  cursor: pointer; /* 鼠标悬停时显示手形图标 */
  transition: background-color 0.3s ease; /* 平滑过渡背景颜色变化 */
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2); /* 添加阴影效果 */
}

.submit-button:hover {
  background-color: #555555;
}

/* 链接样式，去除下划线，点击时显示下划线 */
a {
  color: #1d8ae3;
  text-decoration: none;
  transition: text-decoration 0.3s ease;
}

a:hover {
  text-decoration: underline;
}

/* 勾选框样式调整，以适应灰色系主题 */
.checkbox-group {
  margin-right: 10px;
}
</style>