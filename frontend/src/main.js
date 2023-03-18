import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap";

axios.defaults.baseURL = "/api";
axios.defaults.headers.common.Accept = "application/json";

const app = createApp(App);
app.use(store).use(router);
app.config.globalProperties.$axios = axios;
app.mount("#app");
