// import './assets/main.css'

import { createApp } from "vue";
import App from "./App.vue";

import router from "./router";
import "element-plus/dist/index.css";
import ElementPlus from "element-plus";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import { createPinia } from "pinia";
import VueLazyload from "vue-lazyload";

const app = createApp(App);
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

const pinia = createPinia();

app.use(router);
app.use(pinia);
app.use(VueLazyload);
app.use(ElementPlus);
app.mount("#app");
