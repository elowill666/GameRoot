<template>
  <div style="display: flex" class="layout">
    <DashboardRouterlink></DashboardRouterlink>

    <main style="width: 100%; margin-left: 30px">
      <div ref="scrollTarget"></div>

      <div class="order_searchbox">
        <div class="operation_icon_set" @click="showSearchBox">
          <el-icon size="24"><Operation /></el-icon>
        </div>
        <div v-show="isShowSearchBox" class="searchbox_out">
          <div class="searchbox">
            <label
              class="selectbox_label"
              style="width: 11%"
              for="userid_search"
              >會員編號:</label
            ><input
              type="text"
              class="form-control form-control-sm userid_input"
              id="userid_search"
              v-model="userSearchBox.id"
            />
            <label
              class="selectbox_label"
              style="width: 11%"
              for="username_search"
              >會員姓名:</label
            ><input
              type="text"
              class="form-control form-control-sm username_input"
              id="username_search"
              v-model="userSearchBox.username"
            /><label
              class="selectbox_label"
              style="width: 11%"
              for="permissions_search"
              >會員權限:</label
            >
            <select
              class="form-select form-select-sm permissions_input"
              v-model="userSearchBox.permissions"
              id="permissions_search"
            >
              <option
                v-for="opt of optionOfPermission"
                :key="opt.value"
                :value="opt.value"
              >
                {{ opt.label }}
              </option>
            </select>
            <label class="selectbox_label" style="width: 18%" for="cash_search"
              >儲值金餘額大於:</label
            >
            <el-input-number
              class="cash_input"
              v-model="userSearchBox.cash"
              :min="Number(0)"
              controls-position="right"
              id="cash_search"
            ></el-input-number>
            <label class="selectbox_label" for="totalAmount_search"
              >消費總額大於:</label
            ><el-input-number
              class="totalAmount_input"
              v-model="userSearchBox.totalAmount"
              :min="Number(0)"
              controls-position="right"
              id="totalAmount_search"
            ></el-input-number>
            <label class="selectbox_label" for="totalOrders_search"
              >訂單總數大於:</label
            ><el-input-number
              class="totalOrders_input"
              v-model="userSearchBox.totalOrders"
              :min="Number(0)"
              controls-position="right"
              id="totalOrders_search"
            ></el-input-number>
            <label class="selectbox_label" for="bantimecount_input"
              >停權次數大於:</label
            ><el-input-number
              class="bantimecount_input"
              v-model="userSearchBox.bantimecount"
              :min="Number(0)"
              controls-position="right"
              id="bantimecount_search"
            ></el-input-number>
          </div>
          <div class="selectbox_btn_bottom">
            <label class="selectbox_label" style="width: 5%" for="sort_input"
              >排序方式:</label
            >
            <select
              class="form-select form-select-sm sort_input"
              v-model="userSearchBox.sort"
              id="sort_input"
            >
              <option
                v-for="opt of optionOfSort"
                :key="opt.value"
                :value="opt.value"
              >
                {{ opt.label }}
              </option>
            </select>
            <button class="btn btn-secondary opera_btn" @click="goSearch">
              搜尋
            </button>
            <button
              class="btn btn-secondary opera_btn"
              @click="clearSearchRequest"
            >
              清除
            </button>
          </div>
        </div>
      </div>

      <div
        class="table-container x_scroll table_fix_first"
        style="margin: 0; padding: 0; width: 98%"
      >
        <!-- 商品資訊表格 -->
        <table
          class="table main_table table-dark table-responsive"
          style="width: 100%"
        >
          <thead class="table-dark">
            <tr style="text-align: center">
              <th scope="col" style="width: 70px">會員編號</th>
              <th scope="col" style="width: 120px">大頭照</th>
              <th scope="col" style="width: 60px">姓名</th>
              <th scope="col" style="width: 100px">信箱</th>
              <th scope="col" style="width: 70px">會員權限</th>
              <!-- <th scope="col" style="width: 70px">論壇等級</th> -->
              <th scope="col" style="width: 80px">消費總額</th>
              <th scope="col" style="width: 90px">儲值金餘額</th>
              <th scope="col" style="width: 80px">發文次數</th>
              <th scope="col" style="width: 80px">停權次數</th>
              <th scope="col" style="width: 80px">停權原因</th>
              <th scope="col" style="width: 200px">停權時間</th>
            </tr>
          </thead>

          <tbody v-show="!isNoData">
            <tr style="text-align: center" v-for="item of usersinfo">
              <td class="col_userid" @click="goTOUserProfile(item.id)">
                {{ formatUserId(item.id) }}
              </td>

              <td>
                <img
                  v-lazy="item.userphoto"
                  style="object-fit: cover; width: 90px; height: 80px"
                />
              </td>
              <td>{{ item.username }}</td>
              <td style="width: 20px">{{ item.email }}</td>
              <td>{{ formatPermission(item.permissions) }}</td>
              <!-- <td>{{ item.membership }}</td> -->
              <td>{{ item.totalAmount }}</td>
              <td>{{ item.cash }}</td>
              <td>{{ item.countUserArticles }}</td>
              <td>{{ item.bantimecount }}</td>
              <td>{{ item.banReason }}</td>
              <td>
                {{ formatDate(item.bantimestart) }} -
                {{ formatDate(item.bantimeend) }}
              </td>
            </tr>
          </tbody>
          <tbody v-show="isNoData">
            <td colspan="11" class="search_nodata">查無資料</td>
          </tbody>
        </table>
      </div>
      <div
        class="mt-4"
        style="
          display: flex;
          justify-content: flex-end;
          width: 98%;
          margin-bottom: 30px;
        "
      >
        <el-pagination
          v-model:current-page="pageControl.currentPage"
          v-model:page-size="pageControl.size"
          :page-sizes="pageControl.sizeOfPage"
          background="true"
          layout="total,sizes, prev, pager, next"
          :total="pageControl.total"
          @size-change="goWhichPage"
          @current-change="goWhichPage"
          @prev-click="goWhichPage"
          @next-click="goWhichPage"
        />
      </div>
    </main>
  </div>
</template>
<script setup>
import axios from "axios";
import Swal from "sweetalert2";
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import DashboardRouterlink from "./DashboardRouterlink.vue";
import { formatDate } from "../../utils/someDateFormat.js";
import { formatUserId, reformatUserId } from "../../utils/someIdFormat.js";
import { formatPermission } from "../../utils/someFormat.js";
import { forShortSweetalert } from "@/utils/someSweetalert";
const path = import.meta.env.VITE_JAVA_URL;
const router = useRouter();
const route = useRoute();
const config = {
  withCredentials: true,
};
const isNoData = ref(false);

//接收後端的參數
const usersinfo = ref([]);

//用來搜尋的參數
const userSearchBox = ref({
  id: null,
  username: null,
  permissions: null,
  cash: null,
  totalOrders: null,
  totalAmount: null,
  bantimecount: null,
  sort: "idasc",
});
//permission選項
const optionOfPermission = [
  { value: 0, label: "一般會員" },
  { value: -1, label: "停權會員" },
];
//排序選項
const optionOfSort = [
  { value: "idasc", label: "會員編號小>大" },
  { value: "iddesc", label: "會員編號大>小" },
  { value: "totalamountdesc", label: "消費總額多>少" },
  { value: "totalamountasc", label: "消費總額少>多" },
  { value: "articlesamountdesc", label: "發文次數多>少" },
  { value: "articlesamountasc", label: "發文次數少>多" },
];

//搜尋工具開關
const isShowSearchBox = ref(false);
//分頁工具參數
const pageControl = ref({
  pageSizes: [5, 10, 20, 50],
  size: 10,
  currentPage: Number(route.params.page),
  total: 0,
});

onMounted(() => {
  findAllUser();
});

function goWhichPage() {
  router
    .push({
      name: "usermanagement",
      params: {
        page: pageControl.value.currentPage,
      },
    })
    .then(() => {
      findAllUser();
    });
}

//後端請求資料用
async function findAllUser() {
  let request = searchRequest();
  console.log("beffind", request);
  Swal.fire({
    title: "資料搜尋中",
    showConfirmButton: false,
  });
  try {
    const response = await axios.post(
      `${path}/dashboard/user/find`,
      request,
      config
    );
    forShortSweetalert("success", "搜尋完畢");
    console.log("alluser", response);
    if (response.data.totalcount != 0 || response.data.data != "nodata") {
      usersinfo.value = response.data.data;
      pageControl.value.total = response.data.totalcount;
      isNoData.value = false;
    } else {
      pageControl.value.total = response.data.totalcount;
      isNoData.value = true;
    }
    scrollToElement();
  } catch (error) {
    console.log(error);
  }
}

//搜集參數用的
function searchRequest() {
  let request = {
    start: 1,
    rows: 10,
    ...userSearchBox.value,
  };
  request.start = pageControl.value.currentPage;
  request.rows = pageControl.value.size;
  if (request.id) {
    request.id = reformatUserId(request.id);
  }
  return request;
}

function showSearchBox() {
  isShowSearchBox.value = !isShowSearchBox.value;
}

function goSearch() {
  findAllUser();
}

//清除搜尋項
function clearSearchRequest() {
  pageControl.value.currentPage = 1;
  pageControl.value.size = 10;
  for (let temp in userSearchBox.value) {
    userSearchBox.value[temp] = null;
  }
  userSearchBox.value.sort = "idasc";
  findAllUser();
}
//跳轉到個人頁面
async function goTOUserProfile(id) {
  router.push({
    name: "DashboardUserProfile",
    params: {
      id: id,
    },
  });
}

const scrollTarget = ref(null);

async function scrollToElement() {
  // document.documentElement.scrollTop = 0;
  try {
    scrollTarget.value.scrollIntoView({
      block: "start",
      inline: "nearest",
    });
  } catch (error) {
    console.log(error);
  }
}
</script>

<style scoped>
.layout {
  background: url("../../../public/images/loginImg.jpg") center/cover fixed;
  margin: 0;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.6);
  background-blend-mode: multiply;
}

.col_userid {
  cursor: pointer;
}

.order_searchbox {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-bottom: 10px;
  margin-right: 20px;
  width: 98%;
  margin-top: 20px;
}

.operation_icon_set {
  display: flex;
  justify-content: center;
  margin-right: 2px;
  margin-top: 5px;
  align-self: flex-start;
  padding: 7px;
  background-color: #354551;
  border-radius: 5px;
}

.searchbox_out {
  background-color: #354551;
  padding: 0;
  padding: 10px;
  border: 0;
  color: aliceblue;
  cursor: pointer;
  border-radius: 10px;
  margin: 5px;
}

.searchbox {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  justify-content: flex-end;
}

.selectbox_label {
  width: 16%;
  margin-left: 10px;
  padding: 0;
}

.userid_input {
  width: 15%;
}

.username_input {
  width: 15%;
}

.cash_input {
  width: 18%;
}
.totalAmount_input {
  width: 18%;
}
.totalOrders_input {
  width: 15%;
}
.bantimecount_input {
  width: 15%;
}

.selectbox_btn_bottom {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 0;
  align-items: center;
}
.opera_btn {
  margin-left: 20px;
}
.permissions_input {
  width: 10%;
}
.sort_input {
  height: 80%;
  width: 10%;
}
.search_nodata {
  background-color: #222121;
  text-align: center;
  font-size: 1.5em;
  height: 5em;
  vertical-align: middle;
}
</style>
