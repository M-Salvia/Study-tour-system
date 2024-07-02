<template>
    <div class="register">
      <h2>注册页面</h2>
      <form @submit.prevent="handleSubmit">
        <div>
          <label for="username">名字：</label>
          <input type="text" id="username" v-model="username" required>
        </div>
        <div>
          <label for="password">密码：</label>
          <input type="password" id="password" v-model="password" required>
        </div>
        <div>
          <label for="email">邮箱：</label>
          <input type="email" id="email" v-model="email" required>
        </div>
        <div>
          <label for="age">年龄：</label>
          <input type="age" id="age" v-model="age" required>
        </div>
        <button type="submit">注册</button>
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
        email: '',
        age: ''
      };
    },
    methods: {
      handleSubmit() {
        if(!this.username || !this.password || !this.email || !this.age)
        {
          this.$Modal.info({
            title: '注册失败',
            content: '请填写完整信息',
          });
        }
        api.post('/user/register', {
          "username": this.username,
          "password": this.password,
          "email": this.email,
          "age": this.age
        }, {
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
          }
        })
        .then(response => {
          // 注册成功
          if(response.data) {
              this.$Modal.info({
                title: '注册成功',
                content: '请登录',
              });
              this.$router.push('/login');
          } 
        })
        .catch(error => {
          if (error.response && error.response.status === 409) {
    // 处理401状态码
    this.$Modal.error({
      title: '注册失败',
      content: '409:用户名已存在,请重新输入 ',
    });
  }
        })
      }
    }
  }
  </script>
  
<style scoped>
.register {
  max-width: 400px;
  margin: 2rem auto;
  padding: 1.5rem;
  background: #f7f7f7;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.15);
}

.register h2 {
  margin-bottom: 1rem;
  text-align: center;
  color: #333;
}

.form-group {
  margin-bottom: 1rem;
  display: flex;
  align-items: center;
}

.form-group label {
  flex: 1;
  margin-right: 1rem;
  white-space: nowrap;
  color: #555;
}
.register form div {
  display: flex; /* 使用Flexbox布局 */
  align-items: center; /* 垂直居中 */
  margin-bottom: 10px; /* 一致的外边距 */
}

.register form label {
  margin-right: 10px; /* 保持标签和输入框之间的间距一致 */
  white-space: nowrap; /* 防止标签换行 */
  font-family: 'Arial', sans-serif; /* 设置字体族 */
  font-size: 16px; /* 设置字体大小 */
}

.register form input {
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

.register form input:focus {
  outline: none;
  border-color: #555555;
}

.register form button {
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
  background-color: #504c4c;
}

</style>
  