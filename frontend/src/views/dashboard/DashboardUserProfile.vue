<template>
  <div style="display: flex" class="layout">
    <DashboardRouterlink style="height: 100vh"></DashboardRouterlink>
    <main style="width: 100%; height: 100%; margin-bottom: 30px">
      <div class="container">
        <div style="height: 100%">
          <div id="logo">
            <h1 class="logo">
              <img
                src="../../../public/images/logo.png"
                style="width: 80px; height: 80px"
              />
            </h1>
          </div>
          <el-avatar
            :size="170"
            shape="circle"
            style="
              position: absolute;
              z-index: 1000;
              margin-top: 70px;
              margin-left: 20px;
            "
            :src="userDetail.userphoto"
          />
          <div class="leftbox">
            <nav>
              <a id="profile" class="active"><i class="fa fa-user"></i></a>
            </nav>
          </div>

          <div class="rightbox">
            <div class="profile">
              <div class="h1_dash" v-show="!isedit">
                <h1 style="width: 75%">會員檔案</h1>
                <span
                  ><button class="btn btn-primary" @click="goedit()">
                    編輯檔案
                  </button></span
                >
              </div>
              <div class="h1_dash" v-show="isedit">
                <h1 style="width: 61%">會員檔案</h1>
                <span
                  ><button
                    class="btn btn-primary user_info"
                    style="margin-right: 20px"
                    @click="edit"
                  >
                    送出編輯
                  </button></span
                ><span>
                  <button class="btn btn-primary user_info" @click="cancelEdit">
                    取消編輯
                  </button></span
                >
              </div>
              <table class="info_table">
                <thead>
                  <tr>
                    <th>
                      <h2 class="userProfile">會員編號</h2>
                    </th>
                    <th>
                      <h2 class="userProfile">創建日期</h2>
                    </th>
                  </tr>
                </thead>
              </table>
              <div style="display: flex">
                <p class="p_dash">{{ formatUserId(Number(userDetail.id)) }}</p>
                <p class="p_dash" style="margin-left: 160px">
                  {{ userDetail.created_at }}
                </p>
              </div>
              <div class="info_div">
                <span class="dash_span" style="margin-bottom: 5px"
                  >會員資料</span
                >
              </div>
              <table class="info_table">
                <thead>
                  <tr>
                    <th class="dash_th">會員權限</th>
                    <th class="dash_th">停權次數</th>
                    <!-- 停權開始時間及結束時間 待定 -->
                    <th class="dash_th" v-show="isBanReasonth">停權原因</th>
                    <th class="dash_th" colspan="2" v-show="isBanned">
                      停權時間
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td class="dash_td">
                      <span v-show="!isedit">
                        {{
                          formatUserPermissions(userDetail.permissions)
                        }}</span
                      >
                      <select
                        class="edit_dash form-select form-select-sm"
                        v-show="isedit"
                        style="width: 105px; font-size: 0.8em"
                        v-model="userDetailControl.tempPermission"
                        @change="
                          chanegePermission(userDetailControl.tempPermission)
                        "
                      >
                        <option
                          v-for="level in optionOfPermissions"
                          :key="level"
                        >
                          {{ level }}
                        </option>
                      </select>
                    </td>
                    <td class="dash_td">{{ userDetail.bantimecount }}次</td>
                    <td class="dash_td" v-show="isBanReasonth">
                      <span v-show="isBanReason">{{
                        userDetail.banReason
                      }}</span>
                      <input
                        type="text"
                        v-model="userDetailControl.tempBanReason"
                        v-show="isWantBan"
                        style="width: 100%"
                      />
                    </td>
                    <td class="dash_td" colspan="2" v-show="isBanned">
                      {{ userDetail.bantimestart }} -
                      {{ userDetail.bantimeend }}
                    </td>
                    <td v-show="!isBanned"></td>
                    <td v-show="!isBanned"></td>
                  </tr>
                </tbody>
              </table>
              <div class="info_div">
                <span class="dash_span">商城資料</span
                ><button
                  class="btn btn-primary user_info"
                  @click="goWatchOrderData"
                >
                  訂單總覽
                </button>
              </div>
              <table class="info_table">
                <thead>
                  <tr>
                    <th class="dash_th">儲值金餘額</th>
                    <th class="dash_th">消費總額</th>
                    <th class="dash_th">進行中訂單</th>
                    <th class="dash_th">歷史訂單</th>
                    <th class="dash_th">訂單總量</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td class="dash_td">{{ userDetail.cash }}元</td>
                    <td class="dash_td">{{ userDetail.totalAmount }}元</td>
                    <td class="dash_td">{{ userDetail.unconfirmedOrder }}筆</td>
                    <td class="dash_td">{{ userDetail.confirmedOrder }}筆</td>
                    <td class="dash_td">{{ userDetail.totalOrders }}筆</td>
                  </tr>
                </tbody>
              </table>
              <div class="info_div">
                <span class="dash_span">論壇資料</span
                ><button
                  class="btn btn-primary user_info"
                  @click="goWatchForumData"
                >
                  文章總覽
                </button>
              </div>
              <table class="info_table">
                <thead>
                  <tr>
                    <th class="dash_th" style="width: 110px">發文總數</th>
                    <th class="dash_th">留言總數</th>
                    <th class="dash_th"></th>
                    <th class="dash_th"></th>
                    <th class="dash_th"></th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td class="dash_td">
                      {{ userDetail.countUserArticles }}篇
                    </td>
                    <td class="dash_td">
                      {{ userDetail.countUserComments }}筆
                    </td>
                    <td class="dash_td"></td>
                    <td class="dash_td"></td>
                    <td class="dash_td"></td>
                  </tr>
                </tbody>
              </table>
              <span class="userProfile">帳號</span>
              <p class="p_origin">{{ userDetail.username }}</p>
              <h2 class="userProfile">密碼</h2>
              <p class="p_origin">
                {{ formatPassword(userDetail.password) }}......
              </p>
              <h2 class="userProfile">手機</h2>
              <p class="p_origin">{{ userDetail.phone_number }}</p>
              <h2 class="userProfile">性別</h2>
              <p class="p_origin">{{ userDetail.gender }}</p>
              <h2 class="userProfile">Email</h2>
              <p class="p_origin">{{ userDetail.email }}</p>
              <h2 class="userProfile">地址</h2>
              <p class="p_origin">{{ userDetail.address }}</p>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { useRoute } from "vue-router";
import DashboardRouterlink from "./DashboardRouterlink.vue";
import { ref, reactive } from "vue";
import { onBeforeMount, onMounted } from "vue";
import axios from "axios";
import Swal from "sweetalert2";
import { useRouter } from "vue-router";
import { formatUserId } from "@/utils/someIdFormat";
import { forShortSweetalert } from "@/utils/someSweetalert";
const route = useRoute();
const router = useRouter();
const url = import.meta.env.VITE_JAVA_URL;
let userDetail = ref({});
let isedit = ref(false);
let isgoogleLogin = ref(false);
const isBanned = ref(false);
const isBanReason = ref(false);
const isBanReasonth = ref(false);
const isWantBan = ref(false);
const axiosObj = axios.create({
  timeout: 5000,
  withCredentials: true,
});

const userDetailControl = ref({
  tempPermission: 0,
  tempMembership: "",
  tempBanReason: "",
});

const optionOfPermissions = ref({
  0: "一般會員",
  "-1": "停權會員",
});

const optionOfMembership = ref(["lv1", "lv2", "lv3", "lv4", "lv5", "lv6"]);

onMounted(() => {
  getUserInfo();
});

function goedit() {
  isedit.value = !isedit.value;
  if (isedit.value) {
    if (userDetail.value.permissions === -1) {
      isBanReason.value = true;
    }
  } else if (!isedit.value) {
    isWantBan.value = false;
    userDetailControl.value.tempBanReason = null;
    if (userDetail.value.permissions === 0) {
      isBanReasonth.value = false;
    }
  }
}

function edit() {
  let user = {
    id: userDetail.value.id,
    permissions: formatTempPermissions(userDetailControl.value.tempPermission),
    membership: userDetail.value.membership,
    banReason: userDetailControl.value.tempBanReason,
  };
  if (user.permissions == -1) {
    if (user.banReason == null || user.banReason.length == 0) {
      forShortSweetalert("error", "禁用原因為必填項目", 500, 2000);
      return;
    }
  }
  Swal.fire({
    title: "確認更改權限嗎？",
    text: "您的更改會對會員產生嚴重影響！",
    icon: "warning",
    allowOutsideClick: false,
    showCancelButton: true,
    confirmButtonColor: "#3085d6",
    confirmButtonText: "確定更改",
    cancelButtonText: "返回",
  }).then((result) => {
    console.log("qwe", user);
    if (result.isConfirmed) {
      axiosObj
        .post(`${url}/dashboard/user/updateUserprofile`, user)
        .then((res) => {
          getUserInfo();
          isedit.value = !isedit.value;
          isWantBan.value = false;
          forShortSweetalert("success", "更新成功");
        })
        .catch((err) => {
          console.log(`output->err`, err);
        });
    }
  });
}
//取消編輯
function cancelEdit() {
  isedit.value = !isedit.value;
  userDetailControl.value.tempPermission = formatUserPermissions(
    userDetail.value.permissions
  );
  userDetailControl.value.tempMembership = userDetail.value.membership;
  isWantBan.value = false;
  userDetailControl.value.tempBanReason = null;
  if (userDetail.value.permissions === 0) {
    isBanReasonth.value = false;
  } else if (userDetail.value.permissions === -1) {
    isBanReason.value = true;
  }
}
//改權限
function chanegePermission(status) {
  if ("停權會員" === status) {
    isWantBan.value = true;
    isBanReason.value = false;
    isBanReasonth.value = true;
  } else if ("一般會員" === status) {
    isWantBan.value = false;
    isBanReason.value = true;
    if (userDetail.value.permissions === 0) {
      isBanReasonth.value = false;
    }
  }
}

//從router傳過來的username拿使用者名字去資料庫找新的資料
function getUserInfo() {
  const userId = localStorage.getItem("userId");
  console.log(userId);
  axiosObj
    .get(`${url}/dashboard/user/${route.params.id}`)
    .then((res) => {
      userDetail.value = res.data;
      console.log(`output->res.data`, res.data);
      userDetailControl.value.tempPermission = formatUserPermissions(
        userDetail.value.permissions
      );
      userDetailControl.value.tempMembership = userDetail.value.membership;
      userDetailControl.value.tempBanReason = userDetail.value.banReason;
      if (
        userDetail.value.permissions != null &&
        userDetail.value.permissions == -1
      ) {
        isBanned.value = true;
        isBanReason.value = true;
        isBanReasonth.value = true;
      } else {
        isBanned.value = false;
        isBanReason.value = false;
        isBanReasonth.value = false;
      }
    })
    .catch((err) => {
      console.log(err);
    });
}
//把權限的數字轉文字
function formatUserPermissions(permissions) {
  if (permissions === -1) {
    return "停權會員";
  } else if (permissions === 0) {
    return "一般會員";
  } else if (permissions === 1) {
    return "無情管理員";
  }
}

//把文字轉回來
function formatTempPermissions(permissions) {
  if (permissions === "停權會員") {
    return -1;
  } else if (permissions === "一般會員") {
    return 0;
  } else if (permissions === "無情管理員") {
    return 1;
  }
}

async function goWatchOrderData() {
  router.push({
    name: "ordermanagementforuser",
    params: {
      userId: userDetail.value.id,
      page: 1,
    },
  });
}

async function goWatchForumData() {
  router.push({
    name: "articlemanagementforuser",
    params: {
      userId: userDetail.value.id,
      page: 1,
    },
  });
}

function formatPassword(password) {
  let pass = "";
  pass = pass + password;
  pass = pass.substring(0, 45);
  return pass;
}
</script>

<style scoped lang="scss">
.layout {
  background: url("../../../public/images/loginImg.jpg") center/cover fixed;
  margin: 0;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.6);
  background-blend-mode: multiply;
}
.edit_dash {
  width: 450px;
  margin-top: 0;
}

.info_table {
  width: 95%;
  font-size: 0.5em;
  color: black;
  margin-bottom: 15px;
}

.info_div {
  display: flex;
  align-items: center;

  > span {
    float: left;
    width: 65%;
  }
}

.user_info {
  font-size: 0.4em;
  margin: auto;
  margin-bottom: 8px;
  float: right;
}

.dash_th {
  width: 110px;
}

:deep(.el-input__wrapper) {
  background-color: rgb(250, 250, 250);
  border-radius: 95px;
  border: 0;
  box-shadow: 0px 0px 10px rgba(154, 154, 154, 0.5);
}

$logo: #141f2a;
$gray: #777777;
$black: #070707;
$green: #637384;
$aqua: #3fb6a8;
$white: #ffffff;

@import url("https://fonts.googleapis.com/css?family=Nunito:400,900|Montserrat|Roboto");

$hulu: "Nunito Sans", sans-serif;
$heading: "Montserrat", sans-serif;
$body: "Roboto", sans-serif;

body {
  background: linear-gradient(to right, $aqua, $green);
}

.container {
  background-color: rgb(227, 232, 236);
  width: 940px;
  height: 1150px;
  margin: 0 auto;
  position: relative;
  margin-top: 2%;
  margin-bottom: 20px;
  box-shadow: 2px 5px 20px rgba($gray, 0.5);
  border-radius: 12px;
}

.logo {
  float: right;
  margin-right: 12px;
  margin-top: 12px;
  font-family: $hulu;
  color: $logo;
  font-weight: 900;
  font-size: 1.5em;
  letter-spacing: 1px;
}

.leftbox {
  float: left;
  margin-top: -20px;

  left: 5%;
  position: absolute;
  width: 15%;
  height: 1200px;
  background: $green;
  box-shadow: 3px 3px 10px rgba($gray, 0.5);
  z-index: 1;
}

.active {
  color: $aqua;
}

.rightbox {
  float: right;
  width: 60%;
  height: 100%;
}

.profile {
  position: absolute;
  width: 70%;
  font-size: 35px;
}

h1 {
  font-family: $heading;
  color: $green;
  font-size: 1.2em;
  margin-top: 40px;
  margin-bottom: 35px;
}

.userProfile {
  color: $gray;
  font-family: $body;
  width: 80%;
  text-transform: uppercase;
  font-size: 15px;
  letter-spacing: 1px;
  margin-left: 2px;
}

.h1_dash {
  display: flex;
  align-items: center;
  margin-top: 50px;
}

.p_origin {
  border-width: 1px;
  border-style: solid;
  border-image: linear-gradient(to right, $aqua, rgba($green, 0.5)) 1 0%;
  border-top: 0;
  width: 85%;
  font-family: $heading;
  font-size: 0.5em;
  padding: 5px 0;
  color: $black;
}

.p_dash {
  color: $black;
}

.dash_span {
  font-size: 0.5em;
  color: $gray;
}
</style>
