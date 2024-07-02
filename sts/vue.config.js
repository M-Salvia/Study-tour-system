const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false, // 关闭语法检测
  devServer: {
    open: true,
    port: 8088,
    proxy: {
      '/api': {
        target: `http://localhost:8080`,
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    },
  },
});
