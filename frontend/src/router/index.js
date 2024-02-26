import { createRouter, createWebHistory } from "vue-router";

import Home from "../views/HomePage.vue";
import ChatRoom from "../views/ChatRoom.vue";
import user from "../views/user/userrouter.js";
import forum from "../views/forum/forumrouter.js";
import product from "../views/product/productrouter.js";
import dashboard from "../views/dashboard/dashboardrouter.js";
import order from "../views/order/orderrouter.js";
import axios from "axios";
import Swal from "sweetalert2";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: "/home", name: "home", component: Home },
    { path: "/chatRoom", name: "chatRoom", component: ChatRoom },
    ...user,
    ...forum,
    ...product,
    ...dashboard,
    ...order,
  ],
});

router.beforeEach(async (to, from, next) => {
  const isDashboardRoute = to.path.startsWith("/dashboard");
  const isLoginRoute = to.path.startsWith("/user/userlogin");
  const API_URL = `${import.meta.env.VITE_JAVA_URL}`;

  if (isDashboardRoute) {
    const response = await axios.get(`${API_URL}/user/adminAuthenticate`, {
      withCredentials: true,
    });
    console.log(response.data);
    console.log(typeof response.data);
    if (false === response.data) {
      next("/home");
      return; // 在這裡加上 return，確保只有一個分支被執行
    }
  } else if (isLoginRoute) {
    const response = await axios.get(`${API_URL}/user/authenticate`, {
      withCredentials: true,
    });
    console.log(response.data);
    if (true === response.data || 'googleuser' === response.data) {
      next("/home");
      return; // 在這裡加上 return，確保只有一個分支被執行
    }
  }
  next();
});


export default router;
