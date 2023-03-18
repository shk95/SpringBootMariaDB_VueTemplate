const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 3000,
    proxy: {
      "/api/notice/*": {
        target: "http://localhost:11000",
        // changeOrigin: true,
      },
    },
  },
  configureWebpack: {
    entry: {
      app: "./src/main.js",
    },
  },
});
