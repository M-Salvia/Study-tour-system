
<template>
  <div id="app">
    <!-- 使用 router-view 来显示当前路由对应的组件 -->
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
};
</script>

<style>
@tailwind base;
@tailwind components;
@tailwind utilities;
/* 全局样式 */
 body, html {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
}

#app {
  text-align: center;
  font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
}
</style> 