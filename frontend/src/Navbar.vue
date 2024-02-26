<template>
  <div class="common-layout" height="100%">
    <el-container>
      <el-header style="padding: 0" height="90px">
        <el-menu class="el-menu-demo" mode="horizontal" @select="" router style="
            --el-menu-bg-color: #141f2a;
            --el-menu-text-color: #c9c9c9;
            --el-menu-hover-text-color: #233547;
            --el-menu-active-color: #c9c9c9;
            --el-menu-hover-text-color: #ffd306;
            --el-menu-hover-bg-color: none;
            height: 90px;
          " :ellipsis="false">
          <el-menu-item index="/home" @click="currentRouterPath()" style="margin: 0; border: none; border-radius: 0">
            <template #title>
              <img src="../public/images/logo.png" style="width: 100px; padding-top: 10px" />
              <span style="
                  font-family: OCR A Std, monospace;
                  padding-top: 40px;
                  font-size: 20px;
                  text-shadow: 0 0 5px #ffd306;
                ">Root of Game</span>
            </template>
          </el-menu-item>
          <div style="
              flex: 1;
              width: 0;
              display: flex;
              align-items: center;
              justify-content: flex-end;
              margin-right: 25px;
            ">
            <el-popover placement="top-start" effect="dark" trigger="hover" :hide-after="0">
              <template #reference>
                <el-menu-item index="/home" @click="currentRouterPath()" class="centered-icon">
                  <template #title>
                    <el-icon size="40px">
                      <HomeFilled />
                    </el-icon>
                  </template>
                </el-menu-item>
              </template>
              <div style="text-align: center">前往首頁</div>
            </el-popover>

            <el-popover placement="top-start" effect="dark" trigger="hover" :hide-after="0">
              <template #reference>
                <el-menu-item :index="productRoute" @click="currentRouterPath(productRoute)" class="centered-icon">
                  <el-icon size="40px">
                    <Shop />
                  </el-icon>
                </el-menu-item>
              </template>
              <div style="text-align: center">前往遊戲商店</div>
            </el-popover>

            <el-popover placement="top-start" effect="dark" trigger="hover" :hide-after="0">
              <template #reference>
                <el-menu-item :index="forumRoute" @click="currentRouterPath(forumRoute)">
                  <el-icon size="40px">
                    <Platform />
                  </el-icon>
                </el-menu-item>
              </template>
              <div style="text-align: center">前往論壇</div>
            </el-popover>

            <el-popover placement="top-start" effect="dark" trigger="hover" :hide-after="0">
              <template #reference>
                <el-menu-item :index="shoppingcartRoute">
                  <el-icon size="40px">
                    <ShoppingCart />
                  </el-icon>
                </el-menu-item>
              </template>
              <div style="text-align: center">前往購物車</div>
            </el-popover>

            <el-menu-item :index="dashboardRoute" @click="currentRouterPath(dashboardRoute)"
              v-if="loginStore.isAdmin"><el-icon size="40px">
                <DataLine />
              </el-icon></el-menu-item>

            <el-menu-item index="/user/userlogin" v-if="!loginStore.isLogin && !loginStore.isAdmin"
              @click="currentRouterPath()">
              <template #title>登入 / 註冊</template>
            </el-menu-item>

            <el-dropdown v-if="loginStore.isLogin || loginStore.isAdmin" popper-class="dropDownStyle">
              <div style="display: flex; align-items: center; cursor: default">
                <el-avatar :src="userDetail.userphoto || userdefaultphoto" :size="60" shape="circle"
                  style="margin: 0 20px 0 20px" />
                <span style="font-size: 20px; margin-right: 15px; color: #baccde">{{ userDetail.username }}
                  <router-link class="router-link" to="/order/rootnode"><button class="rootnodebutton">
                      根節點:{{ userDetail.cash }}
                    </button></router-link></span>
              </div>

              <template #dropdown>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item @click="goprofile">
                    <el-icon>
                      <UserFilled />
                    </el-icon>
                    <span style="color: black !important"> 會員檔案</span>
                  </el-dropdown-item>
                  <el-dropdown-item @click="goorder">
                    <el-icon>
                      <Tickets />
                    </el-icon>
                    <span style="color: black !important"> 訂單明細</span>
                  </el-dropdown-item>
                  <el-dropdown-item @click="goproductwishlist">
                    <el-icon>
                      <Star />
                    </el-icon>
                    <span style="color: black !important"> 願望清單</span>
                  </el-dropdown-item>
                  <el-dropdown-item @click="goproductcoupon">
                    <el-icon>
                      <Ticket />
                    </el-icon>
                    <span style="color: black !important"> 優惠券</span>
                  </el-dropdown-item>
                  <el-dropdown-item @click="gousercollections">
                    <el-icon>
                      <Collection />
                    </el-icon>
                    <span style="color: black !important"> 文章收藏</span>
                  </el-dropdown-item>
                  <el-dropdown-item @click="gochangepassword" v-if="!isgoogleLogin">
                    <el-icon>
                      <Lock />
                    </el-icon>
                    <span style="color: black !important"> 修改密碼</span>
                  </el-dropdown-item>
                  <el-dropdown-item v-if="loginStore.isLogin || loginStore.isAdmin" @click="Logout">
                    <span style="color: black !important">登出</span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-menu>
      </el-header>
      <el-scrollbar height="100vh">
        <el-container style="background-color: #141f2a">
          <!-- <DashboardRouterlink v-if="isDashboard"></DashboardRouterlink> -->
          <el-main class="bodymain">
            <router-view v-slot="{ Component }">
              <transition>
                <component :is="Component" />
              </transition>
            </router-view>
            <!-- 貌似會因為ChatRoom會將所有的頁面尾端多了塊空白區塊 -->
            <ChatRoom v-if="loginStore.isLogin && !loginStore.isAdmin"></ChatRoom>
            <ChatOneToOne v-if="loginStore.showOneToOne && loginStore.isLogin" @show-chat="handleShowChat">
            </ChatOneToOne>
          </el-main>
        </el-container>
        <footer id="footer" class="footer-1">
          <div class="main-footer widgets-dark typo-light">
            <div class="container">
              <div class="row">
                <div class="col-xs-12 col-sm-6 col-md-3">
                  <div class="widget subscribe no-box">
                    <h5 class="widget-title">關於我們<span></span></h5>
                    <p>
                      About the company, little discription will goes here..
                    </p>
                  </div>
                </div>

                <div class="col-xs-12 col-sm-6 col-md-3">
                  <div class="widget no-box">
                    <h5 class="widget-title">快速導航<span></span></h5>
                    <ul class="thumbnail-widget">
                      <li>
                        <div class="thumb-content">
                          <a href="#.">Get Started</a>
                        </div>
                      </li>
                      <li>
                        <div class="thumb-content">
                          <a href="#.">Top Leaders</a>
                        </div>
                      </li>
                      <li>
                        <div class="thumb-content">
                          <a href="#.">Success Stories</a>
                        </div>
                      </li>
                      <li>
                        <div class="thumb-content">
                          <a href="#.">Event/Tickets</a>
                        </div>
                      </li>
                      <li>
                        <div class="thumb-content"><a href="#.">News</a></div>
                      </li>
                      <li>
                        <div class="thumb-content">
                          <a href="#.">Lifestyle</a>
                        </div>
                      </li>
                      <li>
                        <div class="thumb-content"><a href="#.">About</a></div>
                      </li>
                    </ul>
                  </div>
                </div>

                <div class="col-xs-12 col-sm-6 col-md-3">
                  <div class="widget no-box">
                    <h5 class="widget-title">Join us<span></span></h5>
                    <a class="btn" href="http://localhost:5173/home" target="_blank">加入我們</a>
                  </div>
                </div>

                <div class="col-xs-12 col-sm-6 col-md-3">
                  <div class="widget no-box">
                    <h5 class="widget-title">聯絡我們<span></span></h5>

                    <p>
                      <a href="mailto:eeit175together@gmail.com" title="glorythemes">eeit175together@gmail.com</a>
                    </p>
                    <!-- icon區 -->
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="footer-copyright">
            <div class="container">
              <div class="row">
                <div class="col-md-12 text-center">
                  <p>Copyright Company Name © 2024. All rights reserved.</p>
                </div>
              </div>
            </div>
          </div>
        </footer>
      </el-scrollbar>
    </el-container>
  </div>
</template>
<script setup>
import { ref, onBeforeMount, onMounted } from "vue";
import axios from "axios";
import { useLoginStore } from "../src/store/navbar";
import Swal from "sweetalert2";
import { useRouter, useRoute } from "vue-router";
import ChatRoom from "./views/ChatRoom.vue";
import DashboardRouterlink from "./views/dashboard/DashboardRouterlink.vue";
import ChatOneToOne from "./views/user/ChatOneToOne.vue";
import userdefaultphoto from "./assets/defaultUser.png";
import { storeToRefs } from "pinia";
const router = useRouter();
const route = useRoute();
const url = import.meta.env.VITE_JAVA_URL;
const windowPath = window.location.pathname;
const loginStore = useLoginStore();
const { userDetail, isgoogleLogin } = storeToRefs(loginStore);
const shoppingcartRoute = ref("/order/shoppingcart");
const productRoute = ref("/product/products");
const forumRoute = ref("/forum/forumone");
const userRoute = ref("/user/usersignup");
const dashboardRoute = ref("/dashboard/datamanagement");
const isDashboard = ref(false);
const defaultact = ref("");
// let isgoogleLogin = ref(false);
const vuebaseUrl = window.location.origin;
//default
let handleShowChat = (value) => {
  loginStore.showOneToOne = value;
};
//登出
const Logout = async () => {
  try {
    const { data } = await axios.get(`${url}/user/logout`, {
      withCredentials: true,
    });
    if (data == true) {
      currentRouterPath();
      Swal.fire({
        // text: '已登出',
        html: '<h4 style="display: flex; justify-content: center; align-items: center; height: 100%; font-family= "Arial", sans-serif;">已登出</h4>',
        icon: "success",
        showConfirmButton: false,
        width: 300,
        timer: 1200,
        toast: true,

        showClass: {
          popup: `
        animate__animated
        animate__fadeInUp
        animate__faster
        `,
        },
        hideClass: {
          popup: `
        animate__animated
        animate__fadeOutDown
        animate__faster
        `,
        },
      }).then(() => {
        // router.push('/home')
        loginStore.isLogin = false;
        loginStore.isAdmin = false;
        loginStore.showOneToOne = false;
        localStorage.removeItem("userId");
        location.href = `${vuebaseUrl}/home`;
      });
    }
  } catch (error) {
    console.error("Logout failed", error);
  }
};
async function authenticate() {
  try {
    const { data } = await axios.get(`${url}/user/authenticate`, {
      withCredentials: true,
    });
    if (data === true) {
      const userId = localStorage.getItem("userId");
      const res = await axios.get(`${url}/user/getuser/${userId}`, {
        withCredentials: true,
      });
      userDetail.value = res.data;
      loginStore.isLogin = true;
    }
    if (data == "管理員") {
      const userId = localStorage.getItem("userId");
      const res = await axios.get(`${url}/user/getuser/${userId}`, {
        withCredentials: true,
      });
      userDetail.value = res.data;
      loginStore.isAdmin = true;
    }

    // if (localStorage.getItem("userId")) {
    //   const userId = localStorage.getItem("userId");
    //   const res = await axios.get(`${url}/user/getuser/${userId}`, {
    //     withCredentials: true,
    //   });
    //   userDetail.value = res.data;
    //   loginStore.isLogin = true;
    //   isgoogleLogin.value = true;
    //   console.log(`347googlelogin`);
    // }
  } catch (error) {
    console.error("Error during authentication request:", error);
  }
}
//載入完成前進行登入驗證
onBeforeMount(async () => {
  await authenticate();
});

onMounted(() => {
  currentWindowPath();
});

function goprofile() {
  currentRouterPath();
  router.push("/user/userprofile");
}
function gochangepassword() {
  currentRouterPath();
  router.push("/user/userEmailCheck");
}
function currentRouterPath(routerPathData) {
  console.log(routerPathData);
  if (dashboardRoute.value == routerPathData) {
    isDashboard.value = true;
  } else {
    isDashboard.value = false;
  }
}

function currentWindowPath() {
  console.log(windowPath);
  if ("/" == windowPath) {
    router.push("/home");
  } else if (windowPath.startsWith("/home")) {
    console.log(windowPath);
    defaultact.value = "/home";
    isDashboard.value = false;
  } else if (windowPath.startsWith("/product")) {
    defaultact.value = productRoute.value;
    isDashboard.value = false;
  } else if (windowPath.startsWith("/forum")) {
    defaultact.value = forumRoute.value;
    isDashboard.value = false;
  } else if (windowPath.startsWith("/user")) {
    defaultact.value = userRoute.value;
    isDashboard.value = false;
  } else if (windowPath.startsWith("/dashboard")) {
    defaultact.value = dashboardRoute.value;
    isDashboard.value = true;
  } else {
    defaultact.value = "";
    isDashboard.value = false;
  }
}

function gousercollections() {
  currentRouterPath();
  router.push({
    name: "UserCollections",
    params: {
      page: 1,
    },
  });
}

function goproductwishlist() {
  router.push({
    name: "ProductWishlist",
  });
}

function goproductcoupon() {
  router.push({
    name: "userfour",
  });
}
function goorder() {
  router.push({
    name: "orderDetails",
    params: {
      id: userDetail.value.id,
    },
  });
};
</script>
<style scoped>
.common-layout {
  background: url("../public/images/homeimg.jpg") center/cover fixed;
  margin: 0;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.2);
  background-blend-mode: multiply;
}

.router-link {
  text-decoration: none;
  color: inherit;
  /* 保持颜色和默认样式一致 */
}

.rootnodebutton {
  font-size: 12px;
  margin: 0;
  padding: 5px 0 0 0;
  color: #baccde;
  display: block;
  border: none;
  background-color: transparent;
  text-decoration: none;
}

.v-enter-active,
.v-leave-active {
  transition: opacity 0.3s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
}

.bodymain {
  /* background-color: #203243; */
  background-image: linear-gradient(to top right, #152534, #24313c);
  font-family: "Arial", sans-serif;
  color: rgb(211, 211, 211);
  margin: 90px 0 0 0;
  padding: 0;
}

.el-menu-vertical-demo {
  height: 100%;
}

li.el-menu-item.is-active {
  margin: 0 10px 0 10px;
  border-radius: 8px;
  color: #ffd306;
}

li.el-menu-item {
  margin: 0 10px 0 10px;
  border-radius: 8px;
  font-size: 15px;
}

/*==================== 
	Footer 
====================== */

/* Main Footer */
footer .main-footer {
  padding: 20px 0;
  height: 270px;
  background: #252525;
}

footer ul {
  padding-left: 0;
  list-style: none;
}

/* Copy Right Footer */
.footer-copyright {
  background: #222;
  padding: 5px 0;
}

.footer-copyright .logo {
  display: inherit;
}

.footer-copyright nav {
  float: right;
  margin-top: 5px;
}

.footer-copyright nav ul {
  list-style: none;
  margin: 0;
  padding: 0;
}

.footer-copyright nav ul li {
  border-left: 1px solid #505050;
  display: inline-block;
  line-height: 12px;
  margin: 0;
  padding: 0 8px;
}

.footer-copyright nav ul li a {
  color: #969696;
}

.footer-copyright nav ul li:first-child {
  border: medium none;
  padding-left: 0;
}

.footer-copyright p {
  color: #969696;
  margin: 2px 0 0;
}

footer.transparent .footer-copyright {
  background: none repeat scroll 0 0 rgba(0, 0, 0, 0.3);
}

footer.light .footer-copyright {
  background: none repeat scroll 0 0 rgba(255, 255, 255, 0.3);
}

/* Footer 4 */
.footer- .logo {
  display: inline-block;
}

/*==================== 
	Widgets 
====================== */
.widget {
  padding: 20px;
  margin-bottom: 40px;
}

.widget.widget-last {
  margin-bottom: 0px;
}

.widget.no-box {
  padding: 0;
  background-color: transparent;
  margin-bottom: 40px;
  box-shadow: none;
  -webkit-box-shadow: none;
  -moz-box-shadow: none;
  -ms-box-shadow: none;
  -o-box-shadow: none;
}

.widget.subscribe p {
  margin-bottom: 18px;
}

.widget li a {
  color: #6da3d1;
}

.widget li a:hover {
  color: #4b92dc;
}

.widget-title {
  margin-bottom: 20px;
}

.widget-title span {
  background: #839fad none repeat scroll 0 0;
  display: block;
  height: 1px;
  margin-top: 25px;
  position: relative;
  width: 20%;
}

.widget-title span::after {
  background: inherit;
  content: "";
  height: inherit;
  position: absolute;
  top: -4px;
  width: 50%;
}

.widget-title.text-center span,
.widget-title.text-center span::after {
  margin-left: auto;
  margin-right: auto;
  left: 0;
  right: 0;
}

.widget .badge {
  float: right;
  background: #7f7f7f;
}

.typo-light h1,
.typo-light h2,
.typo-light h3,
.typo-light h4,
.typo-light h5,
.typo-light h6,
.typo-light p,
.typo-light div,
.typo-light span,
.typo-light small {
  color: #fff;
}

.btn {
  background-color: #4a7092;
  color: #fff;
}

.btn:hover,
.btn:focus,
.btn.active {
  background: #4b92dc;
  color: #fff;
}

@media (max-width: 1024px) {
  .el-menu--horizontal>.el-menu-item {
    width: 80px;
  }
}

.centered-icon {
  display: flex;
  justify-content: center;
  align-items: center;
}

.el-icon {
  margin: 4px;
}

.el-header {
  position: fixed;
  width: 100%;
  z-index: 1100;
}

:global(.dropDownStyle .el-dropdown-menu__item) {
  line-height: 36px;
  padding: 6px 22px;
  width: 150px;
  display: flex;
  justify-content: center;
}
</style>
