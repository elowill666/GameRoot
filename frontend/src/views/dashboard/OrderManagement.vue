<template>
  <div style="display: flex" class="layout">
    <DashboardRouterlink style="height: 100vh"></DashboardRouterlink>

    <main class="order_main">
      <div></div>

      <div class="order_searchbox" ref="scrollTarget">
        <div class="operation_icon_set" @click="showSearchBox">
          <el-icon size="24"><Operation /></el-icon>
        </div>
        <div v-show="isShowSearchBox" class="searchbox_out">
          <div class="searchbox">
            <label
              class="selectbox_label"
              for="userid_search"
              v-show="!isFromUserManagement"
              >會員編號:</label
            ><input
              type="text"
              class="form-control form-control-sm user_input"
              id="userid_search"
              v-model="selectCondition.userId"
              v-show="!isFromUserManagement"
            />
            <label class="selectbox_label" for="orderid_search">訂單編號:</label
            ><input
              type="text"
              class="form-control form-control-sm order_input"
              id="orderid_search"
              v-model="selectCondition.orderId"
            /><label class="selectbox_label" for="orderid_search"
              >訂單日期:</label
            >
            <el-date-picker
              v-model="selectOfDatePicker"
              type="daterange"
              range-separator="To"
              start-placeholder="Start date"
              end-placeholder="End date"
              class="orderDate_select"
              style="width: 45%"
            />
            <label class="selectbox_label" for="sortOption">排序方式:</label>
            <select
              class="form-select form-select-sm sort_select"
              v-model="pageControl.sort"
              id="sortOption"
            >
              <option
                v-for="opt of optionOfSort"
                :key="opt.value"
                :value="opt.value"
              >
                {{ opt.label }}
              </option>
            </select>

            <label class="selectbox_label" for="totalAmount">訂單總額:</label
            ><el-input-number
              class="totalAmount_select"
              v-model="selectCondition.totalAmountMin"
              :min="Number(0)"
              controls-position="right"
              id="totalAmount"
            ></el-input-number>
            <el-icon style="margin-left: 5px; margin-right: 5px"
              ><Minus
            /></el-icon>
            <el-input-number
              class="totalAmount_select"
              v-model="selectCondition.totalAmountMax"
              :min="Number(0)"
              controls-position="right"
            ></el-input-number>
            <label class="selectbox_label" for="orderStatus">訂單狀態:</label>
            <select
              class="form-select form-select-sm orderStatus_select"
              v-model="selectCondition.orderStatus"
              id="orderStatus"
            >
              <option v-for="opt of optionOfOrderStatus" :key="opt">
                {{ opt }}
              </option>
            </select>
            <label class="selectbox_label" for="paymentStatus">付款狀態:</label>
            <select
              class="form-select form-select-sm paymentStatus_select"
              id="paymentStatus"
              v-model="selectCondition.paymentStatus"
            >
              <option v-for="opt of optionOfPaymentStatus" :key="opt">
                {{ opt }}
              </option>
            </select>
          </div>
          <div class="selectbox_btn">
            <button class="btn btn-secondary ordermanag_btn" @click="goSearch">
              搜尋
            </button>
            <button
              class="btn btn-secondary ordermanag_btn"
              @click="clearSearchRequest"
            >
              清除
            </button>
          </div>
        </div>

        <span> </span>
      </div>

      <div class="table_fix_first">
        <!-- 商品資訊表格 -->
        <table class="table main_table table-dark">
          <thead class="table-dark main_table_head">
            <tr style="text-align: center">
              <th scope="col" style="width: 100px">訂單編號</th>
              <th scope="col" style="width: 150px">訂單狀態</th>
              <th scope="col" style="width: 150px">付款狀態</th>
              <th scope="col" style="width: 100px">付款方式</th>
              <th scope="col" style="width: 100px">訂單總額</th>
              <th scope="col" style="width: 150px">會員編號</th>
              <th scope="col" style="width: 170px">訂單成立時間</th>
              <th scope="col" style="width: 180px">最新更新時間</th>
              <th scope="col" style="width: 130px">編輯</th>
              <th scope="col" style="width: 120px">內容</th>
            </tr>
          </thead>

          <tbody>
            <tr class="nodata_show" v-show="isNoData">
              <td colspan="10">查無資料</td>
            </tr>
            <template v-for="item of orders" :key="item.orderId">
              <tr style="text-align: center" v-show="!isNoData">
                <td style="width: 100px">
                  {{ formatOrderId(item.ordersBean.orderId) }}
                </td>
                <td style="width: 100px">
                  <select
                    class="form-select form-select-sm"
                    v-show="ordersControl[`isEdit${item.ordersBean.orderId}`]"
                    v-model="
                      ordersControl[`tempOrderStatus${item.ordersBean.orderId}`]
                    "
                    @change="changeOrderStatus(item.ordersBean.orderId)"
                  >
                    <option v-for="opt of optionOfOrderStatus" :key="opt">
                      {{ opt }}
                    </option>
                  </select>
                  <span
                    v-show="!ordersControl[`isEdit${item.ordersBean.orderId}`]"
                  >
                    {{ formatOrderStatus(item.ordersBean.orderStatus) }}</span
                  >
                </td>
                <td style="width: 100px">
                  <select
                    class="form-select form-select-sm"
                    v-show="ordersControl[`isEdit${item.ordersBean.orderId}`]"
                    v-model="
                      ordersControl[
                        `tempPaymentStatus${item.ordersBean.orderId}`
                      ]
                    "
                    @change="changePayStatus(item.ordersBean.orderId)"
                  >
                    <option v-for="opt of optionOfPaymentStatus" :key="opt">
                      {{ opt }}
                    </option>
                  </select>
                  <span
                    v-show="!ordersControl[`isEdit${item.ordersBean.orderId}`]"
                  >
                    {{
                      formatPaymentStatus(item.ordersBean.paymentStatus)
                    }}</span
                  >
                </td>
                <td style="width: 100px">
                  {{ item.ordersBean.paymentMethod }}
                </td>
                <td style="width: 100px">
                  $NT{{ item.ordersBean.totalAmount }}
                </td>
                <td style="width: 100px">
                  {{ formatUserId(item.ordersBean.userId) }}
                </td>
                <td style="width: 100px">
                  {{ formatFullDate(item.ordersBean.orderDate) }}
                </td>
                <td style="width: 100px">
                  {{ formatFullDate(item.ordersBean.updateDate) }}
                </td>
                <td style="width: 60px">
                  <button
                    class="btn btn-secondary"
                    @click="doEdit(item.ordersBean, item.orderdetailsBean)"
                    v-show="!ordersControl[`isEdit${item.ordersBean.orderId}`]"
                  >
                    編輯
                  </button>

                  <button
                    class="btn btn-secondary"
                    @click="modifyOrderById(item)"
                    v-show="
                      ordersControl[
                        `isSomethingChange${item.ordersBean.orderId}`
                      ]
                    "
                    style="margin-right: 10px"
                  >
                    保存
                  </button>
                  <button
                    class="btn btn-secondary"
                    @click="doEdit(item.ordersBean, item.orderdetailsBean)"
                    v-show="ordersControl[`isEdit${item.ordersBean.orderId}`]"
                  >
                    返回
                  </button>
                </td>
                <td @click="openDetail(item.ordersBean, item.orderdetailsBean)">
                  <el-icon
                    v-if="!ordersControl[`isReading${item.ordersBean.orderId}`]"
                  >
                    <ArrowDownBold />
                  </el-icon>
                  <el-icon v-else>
                    <ArrowUpBold />
                  </el-icon>
                </td>
              </tr>

              <tr
                class="show_orderDetail"
                v-show="ordersControl[`isReading${item.ordersBean.orderId}`]"
              >
                <th colspan="10">訂單明細</th>
              </tr>
              <tr
                class="show_orderDetail"
                v-show="ordersControl[`isReading${item.ordersBean.orderId}`]"
              >
                <th class="show_orderDetailtd">收件人</th>
                <th class="show_orderDetailtd" colspan="2">收件寄送地址</th>
                <th class="show_orderDetailtd">收件人電話</th>
                <th class="show_orderDetailtd" colspan="">收件人信箱</th>
                <th class="show_orderDetailtd">帳單人姓名</th>
                <th class="show_orderDetailtd" colspan="">帳單寄送地址</th>
                <th class="show_orderDetailtd">訂單寄送日期</th>
                <th class="show_orderDetailtd" colspan="2">訂單完成日期</th>
              </tr>
              <tr
                class="show_orderDetail"
                v-show="ordersControl[`isReading${item.ordersBean.orderId}`]"
              >
                <td class="show_orderDetailtd">
                  {{ item.ordersBean.consigneeUsername }}
                </td>
                <td class="show_orderDetailtd" colspan="2">
                  {{ item.ordersBean.consigneeCity
                  }}{{ item.ordersBean.consigneeArea
                  }}{{ item.ordersBean.consigneeAddress }}
                </td>
                <td class="show_orderDetailtd">
                  {{ item.ordersBean.consigneePhonenumber }}
                </td>
                <td class="show_orderDetailtd">
                  {{ item.ordersBean.consigneeEmail }}
                </td>
                <td class="show_orderDetailtd">
                  {{ item.ordersBean.billingUsername }}
                </td>
                <td class="show_orderDetailtd" colspan="">
                  {{ item.ordersBean.billingCity
                  }}{{ item.ordersBean.billingArea
                  }}{{ item.ordersBean.billingAddress }}
                </td>
                <td class="show_orderDetailtd">
                  {{ formatFullDate(item.ordersBean.shipmentDate) }}
                </td>
                <td class="show_orderDetailtd" colspan="2">
                  {{ formatFullDate(item.ordersBean.finishDate) }}
                </td>
              </tr>
              <tr
                class="show_orderDetail"
                v-show="ordersControl[`isReading${item.ordersBean.orderId}`]"
              >
                <th class="show_orderDetailtd">順序</th>
                <th class="show_orderDetailtd" colspan="5">購買產品</th>
                <th class="show_orderDetailtd">購買數量</th>
                <th class="show_orderDetailtd">產品價格</th>
                <th class="show_orderDetailtd" colspan="2">產品狀態</th>
              </tr>

              <template
                v-for="(detail, index) of item.orderdetailsBean"
                :key="detail.orderdetailId"
              >
                <tr
                  class="show_orderDetail"
                  v-show="ordersControl[`isReading${item.ordersBean.orderId}`]"
                >
                  <td class="show_orderDetailtd">{{ index + 1 }}</td>
                  <td class="show_orderDetailtd" colspan="5">
                    {{ detail.historyProductName }}
                  </td>
                  <td class="show_orderDetailtd">{{ detail.historyPrice }}</td>
                  <td class="show_orderDetailtd">{{ detail.quantity }}</td>
                  <td class="show_orderDetailtd" colspan="2">
                    {{
                      formatRefund(
                        detail.refundStatus,
                        item.ordersBean.orderStatus
                      )
                    }}
                  </td>
                </tr>
                <tr
                  class="show_refundDetail"
                  v-show="
                    ordersControl[`isReadingRefund${detail.orderdetailId}`]
                  "
                >
                  <th class="show_refundDetailtd" colspan="2">退款原因</th>
                  <th class="show_refundDetailtd" colspan="4">
                    退款原因詳細描述
                  </th>
                  <th class="show_refundDetailtd" colspan="1">退款申請時間</th>
                  <th class="show_refundDetailtd" colspan="1">退款審核</th>
                  <th class="show_refundDetailtd" colspan="2">審核結果說明</th>
                </tr>
                <tr
                  class="show_refundDetail"
                  v-show="
                    ordersControl[`isReadingRefund${detail.orderdetailId}`]
                  "
                >
                  <td class="show_refundDetailtd" colspan="2">
                    {{ detail.reasonType }}
                  </td>
                  <td class="show_refundDetailtd" colspan="4">
                    {{ detail.reason }}
                  </td>
                  <td class="show_refundDetailtd" colspan="1">
                    {{ formatFullDate(detail.refundDate) }}
                  </td>
                  <td class="show_refundDetailtd" colspan="1">
                    <select
                      class="form-select form-select-sm refund_select"
                      v-model="
                        ordersControl[`tempRefund${detail.orderdetailId}`]
                      "
                      @change="changeRefundStatus(item, detail.orderdetailId)"
                    >
                      <option v-for="opt of optionOfRefund" :key="opt">
                        {{ opt }}
                      </option>
                    </select>
                  </td>
                  <td class="show_refundDetailtd" colspan="2">
                    <input
                      list="ReviewReason"
                      id="ice-cream-choice"
                      name="ice-cream-choice"
                      v-model="
                        ordersControl[`tempReviewReason${detail.orderdetailId}`]
                      "
                      @change="changeReviewReason(item, detail.orderdetailId)"
                    />

                    <datalist id="ReviewReason">
                      <option value="不符合退款條件"></option>
                      <option value="符合退款條件"></option>
                    </datalist>
                  </td>
                </tr>
              </template>
            </template>
          </tbody>
        </table>
        <div class="mt-4" style="display: flex; justify-content: flex-end">
          <el-pagination
            v-model:current-page="pageControl.currentPage"
            v-model:page-size="pageControl.size"
            :page-sizes="pageControl.pageSizes"
            background="true"
            layout="total,sizes, prev, pager, next"
            :total="pageControl.total"
            @size-change="goWhichPage"
            @current-change="goWhichPage"
            @prev-click="goWhichPage"
            @next-click="goWhichPage"
          />
        </div>
      </div>
    </main>
  </div>
</template>
<script setup>
import router from "@/router";
import axios from "axios";
import Swal from "sweetalert2";
import { onMounted } from "vue";
import { ref, watch } from "vue";
import { useRoute } from "vue-router";
import { format } from "../../../node_modules/date-fns";
import DashboardRouterlink from "./DashboardRouterlink.vue";
import {
  formatDate,
  formatFullDate,
  formatFullAndFullDate,
} from "../../utils/someDateFormat.js";
import {
  formatOrderId,
  reformatOrderId,
  formatUserId,
  reformatUserId,
} from "../../utils/someIdFormat.js";
import { forShortSweetalert } from "@/utils/someSweetalert";

const route = useRoute();
const path = import.meta.env.VITE_JAVA_URL;
const config = {
  withCredentials: true,
};

//沒找到資料的時候顯示的
const isNoData = ref(false);

const orders = ref([]);
//用來打開搜尋
const isShowSearchBox = ref(false);

const isFromUserManagement = ref(false);

const ordersControl = ref({});

const optionOfOrderStatus = ref([
  "訂單已成立",
  "訂單已完成",
  "訂單已送出",
  "訂單已取消",
  "退款待審核",
  "退款已通過",
  "退款未通過",
  "申請過退款",
  "待確認",
]);

const optionOfPaymentStatus = ref(["已付款", "未付款", "已取消", "待確認"]);

const optionOfRefund = ref(["退款待審核", "核准退款", "拒絕退款"]);

//排序方式
const optionOfSort = ref([
  { value: "createdDESC", label: "訂單時間近>遠" },
  { value: "createdASC", label: "訂單時間遠>近" },
  { value: "updateDESC", label: "更新時間近>遠" },
  { value: "updateASC", label: "更新時間遠>近" },
  { value: "totalDESC", label: "訂單總價多>少" },
  { value: "totalASC", label: "訂單總價少>多" },
]);
const pageControl = ref({
  pageSizes: [5, 10, 20, 50],
  size: 10,
  currentPage: Number(route.params.page),
  sort: "createdDESC",
  total: 0,
});

const selectOfDatePicker = ref([]);

const selectCondition = ref({
  userId: null,
  //訂單ID
  orderId: null,
  //訂單狀態
  orderStatus: null,
  //退款狀態
  refundStatus: null,
  //付款狀態
  paymentStatus: null,
  //訂單成立時間
  orderDateStart: null,
  orderDateEnd: null,
  //付款方式
  paymentMethod: null,
  //總價
  totalAmountMin: null,
  totalAmountMax: null,
});

const scrollTarget = ref(null);

function scrollToElement() {
  // document.documentElement.scrollTop = 0;
  scrollTarget.value.scrollIntoView({
    behavior: "smooth",
    block: "start",
    inline: "nearest",
  });
}

onMounted(() => {
  findAllOrder();
  countAllOrder();
});
//監看是從哪邊進入此網頁
watch(
  () => route.params.userId,
  (newParam, oldParam) => {
    goWhichPage();
  }
);

function goWhichPage() {
  if (route.params.userId) {
    router.push({
      name: "ordermanagementforuser",
      params: {
        userId: route.params.userId,
        page: pageControl.value.currentPage,
      },
    });
    isFromUserManagement.value = true;
  } else {
    router.push({
      name: "ordermanagement",
      params: {
        page: pageControl.value.currentPage,
      },
    });
    isFromUserManagement.value = false;
  }
  findAllOrder();
  countAllOrder();
  scrollToElement();
}

function goSearch(howToSort) {
  pageControl.value.currentPage = 1;
  goWhichPage();
}

//向後端請求更新
async function findAllOrder() {
  let request = searchRequest();
  console.log("myreq", request);
  Swal.fire({
    title: "資料搜尋中",
    showConfirmButton: false,
    timer: 2000,
  });
  try {
    const response = await axios.post(
      path + "/dashboard/order/find",
      request,
      config
    );
    forShortSweetalert("success", "搜尋完畢");
    console.log("all", response.data);
    findOrderInitObj(response.data);
  } catch (error) {
    console.log(error);
  }
}

async function countAllOrder() {
  let request = searchRequest();
  console.log("reqcount", request);
  try {
    const response = await axios.post(
      `${path}/dashboard/order/ordercount`,
      request,
      config
    );
    if (response.data.length != 0) {
      pageControl.value.total = response.data;
    }
  } catch (error) {
    console.log(error);
  }
}

//用來初始化一些物件的
function findOrderInitObj(data) {
  if (data.length == 0 || data === "nodata") {
    isNoData.value = true;
  } else if (data.length != 0) {
    isNoData.value = false;
    orders.value = data;
    for (let temp of orders.value) {
      for (let temp2 of temp.orderdetailsBean) {
        let tempdetail = {
          [`isReadingRefund${temp2.orderdetailId}`]: false,
          [`tempRefund${temp2.orderdetailId}`]: formatRefund(
            temp2.refundStatus
          ),
          [`tempReviewReason${temp2.orderdetailId}`]: temp2.reviewReason,
        };
        Object.assign(ordersControl.value, tempdetail);
      }
      let control = {
        [`isEdit${temp.ordersBean.orderId}`]: false,
        [`isReading${temp.ordersBean.orderId}`]: false,
        [`tempPaymentStatus${temp.ordersBean.orderId}`]: formatPaymentStatus(
          temp.ordersBean.paymentStatus
        ),
        [`tempOrderStatus${temp.ordersBean.orderId}`]: formatOrderStatus(
          temp.ordersBean.orderStatus
        ),
        [`isSomethingChange${temp.ordersBean.orderId}`]: false,
      };

      Object.assign(ordersControl.value, control);
    }
  }
}

//抓參數
function searchRequest() {
  let request = {
    orderPage: {
      start: pageControl.value.currentPage,
      rows: pageControl.value.size,
      sort: pageControl.value.sort,
    },
    ...selectCondition.value,
  };
  if (request.paymentStatus) {
    request.paymentStatus = formatTempPaymentStatus(request.paymentStatus);
  }

  if (request.orderStatus) {
    if (request.orderStatus === "退款待審核") {
      request.refundStatus = "pending";
      request.orderStatus = null;
    } else if (request.orderStatus === "退款已通過") {
      request.refundStatus = "approve";
      request.orderStatus = null;
    } else if (request.orderStatus === "退款未通過") {
      request.refundStatus = "reject";
      request.orderStatus = null;
    } else if (request.orderStatus === "申請過退款") {
      request.refundStatus = "allRefund";
      request.orderStatus = null;
    } else {
      request.orderStatus = formatTempOrderStatus(request.orderStatus);
    }
  }
  if (route.params.userId) {
    request.userId = route.params.userId;
  }
  if (
    selectOfDatePicker.value != null &&
    selectOfDatePicker.value.length != 0
  ) {
    request.orderDateStart = selectOfDatePicker.value[0];
    request.orderDateEnd = selectOfDatePicker.value[1];
  }
  if (request.orderId) {
    request.orderId = reformatOrderId(request.orderId);
  }
  if (request.userId) {
    request.userId = reformatUserId(request.userId);
  }
  return request;
}

function clearSearchRequest() {
  pageControl.value.currentPage = 1;
  pageControl.value.size = 10;
  selectCondition.value.userId = null;
  pageControl.value.sort = "createdDESC";
  selectCondition.value.orderId = null;
  selectCondition.value.orderStatus = null;
  selectCondition.value.paymentStatus = null;
  selectCondition.value.orderDateStart = null;
  selectCondition.value.orderDateEnd = null;
  selectCondition.value.paymentMethod = null;
  selectCondition.value.totalAmountMin = null;
  selectCondition.value.totalAmountMax = null;
  selectOfDatePicker.value = [];
  goWhichPage();
}

//更新某訂單
async function modifyOrderById(item) {
  Swal.fire({
    title: "是否更新訂單？",
    icon: "warning",
    showCancelButton: true,
    allowOutsideClick: false,
    confirmButtonColor: "#3085d6",
    cancelButtonColor: "#d33",
    confirmButtonText: "確認更新",
  }).then((result) => {
    if (result.isConfirmed) {
      forOrderModify(item);
    }
  });
}

async function forOrderModify(item) {
  let request = item;
  for (let temp in item.orderdetailsBean) {
    let tempOrderdetailsId = item.orderdetailsBean[temp].orderdetailId;
    if (
      formatTempRefund(ordersControl.value[`tempRefund${tempOrderdetailsId}`])
    ) {
      if (
        formatTempRefund(
          ordersControl.value[`tempRefund${tempOrderdetailsId}`]
        ) !== "pending" &&
        ordersControl.value[`tempReviewReason${tempOrderdetailsId}`].length ===
          0
      ) {
        forSweetalert("error", "審核原因為必填項目", 500);
        return;
      }

      request.orderdetailsBean[temp].refundStatus = formatTempRefund(
        ordersControl.value[`tempRefund${tempOrderdetailsId}`]
      );
      request.orderdetailsBean[temp].reviewReason =
        ordersControl.value[`tempReviewReason${tempOrderdetailsId}`];
    }
  }
  (request.ordersBean.paymentStatus = formatTempPaymentStatus(
    ordersControl.value[`tempPaymentStatus${item.ordersBean.orderId}`]
  )),
    (request.ordersBean.orderStatus = formatTempOrderStatus(
      ordersControl.value[`tempOrderStatus${item.ordersBean.orderId}`]
    )),
    console.log("beforeupdate", request);
  try {
    const response = await axios.put(
      `${path}/dashboard/order/${request.ordersBean.orderId}`,
      request,
      config
    );

    console.log("aft", response.data);
    if (response.data.length != 0) {
      forSweetalert("success", "更新成功");
      item = response.data;
      for (let temp in orders.value) {
        if (
          orders.value[temp].ordersBean.orderId ===
          response.data.ordersBean.orderId
        ) {
          orders.value[temp] = response.data;
        }
      }

      //此開關適用
      for (let temp of item.orderdetailsBean) {
        ordersControl.value[`isReadingRefund${temp.orderdetailId}`] = false;
      }
      ordersControl.value[
        `isSomethingChange${item.ordersBean.orderId}`
      ] = false;
      ordersControl.value[`isEdit${item.ordersBean.orderId}`] = false;
      ordersControl.value[`isReading${item.ordersBean.orderId}`] = false;
    }
  } catch (error) {
    console.log(error);
  }
}

async function findOrderDetailByOrderId(orderId) {
  try {
    const response = await axios.get(
      `${path}/dashboard/order/orderdetail/${orderId}`,
      config
    );
    console.log(response.data);
    ordersControl.value[`orderdetail${orderId}`] = response.data;
    console.log("sdads", ordersControl.value[`orderdetail${orderId}`]);
  } catch (error) {
    console.log(error);
  }
}

//打開詳情
function openDetail(ordersBean, orderdetailsBean) {
  if (!ordersControl.value[`isReading${ordersBean.orderId}`]) {
    ordersControl.value[`isReading${ordersBean.orderId}`] = true;
  } else if (ordersControl.value[`isReading${ordersBean.orderId}`]) {
    //如果開起detail狀態下又有變更
    if (ordersControl.value[`isSomethingChange${ordersBean.orderId}`]) {
      Swal.fire({
        title: "是否放棄？",
        icon: "warning",
        showCancelButton: true,
        allowOutsideClick: false,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "放棄更新",
      }).then((result) => {
        if (result.isConfirmed) {
          cancalEdit(ordersBean, orderdetailsBean);
          ordersControl.value[`isReading${ordersBean.orderId}`] = false;
        }
      });
    } else {
      cancalEdit(ordersBean, orderdetailsBean);
      ordersControl.value[`isReading${ordersBean.orderId}`] = false;
    }
  }
}
//按下編輯
function doEdit(ordersBean, orderdetailsBean) {
  console.log(typeof ordersBean.orderStatus);
  //編輯狀態開起來的情況，也就是按了要關
  if (ordersControl.value[`isEdit${ordersBean.orderId}`]) {
    //如果開起detail狀態下又有變更
    if (ordersControl.value[`isSomethingChange${ordersBean.orderId}`]) {
      Swal.fire({
        title: "是否放棄？",
        icon: "warning",
        showCancelButton: true,
        allowOutsideClick: false,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "放棄更新",
      }).then((result) => {
        if (result.isConfirmed) {
          cancalEdit(ordersBean, orderdetailsBean);
        }
      });
    } else {
      cancalEdit(ordersBean, orderdetailsBean);
    }
  } else if (!ordersControl.value[`isEdit${ordersBean.orderId}`]) {
    ordersControl.value[`isReading${ordersBean.orderId}`] = true;
    ordersControl.value[`isEdit${ordersBean.orderId}`] = true;
    //如果有退款開退款
    if (1 === ordersBean.paymentStatus) {
      for (let temp of orderdetailsBean) {
        if (temp.refundStatus) {
          ordersControl.value[`isReadingRefund${temp.orderdetailId}`] = true;
        }
      }
    }
  }
}

//取消編輯事件
function cancalEdit(ordersBean, orderdetailsBean) {
  for (let temp of orderdetailsBean) {
    ordersControl.value[`isReadingRefund${temp.orderdetailId}`] = false;
    ordersControl.value[`tempRefund${temp.orderdetailId}`] = formatRefund(
      temp.refundStatus
    );
    ordersControl.value[`tempReviewReason${temp.orderdetailId}`] =
      temp.reviewReason;
  }
  ordersControl.value[`isSomethingChange${ordersBean.orderId}`] = false;
  ordersControl.value[`isEdit${ordersBean.orderId}`] = false;
  ordersControl.value[`tempPaymentStatus${ordersBean.orderId}`] =
    formatPaymentStatus(ordersBean.paymentStatus);
  ordersControl.value[`tempOrderStatus${ordersBean.orderId}`] =
    formatOrderStatus(ordersBean.orderStatus);
}

//改變退款資訊
function changeRefundStatus(item, orderdetailId) {
  if (item.ordersBean.paymentStatus === 1) {
    ordersControl.value[`isSomethingChange${item.ordersBean.orderId}`] = true;
    if (ordersControl.value[`tempRefund${orderdetailId}`] === "核准退款") {
      ordersControl.value[`tempReviewReason${orderdetailId}`] = "符合退款條件";
    } else if (
      ordersControl.value[`tempRefund${orderdetailId}`] === "拒絕退款"
    ) {
      ordersControl.value[`tempReviewReason${orderdetailId}`] =
        "不符合退款條件";
    }
  } else {
    forSweetalert("warning", "未付款不得調整退款");
    for (let temp of item.orderdetailsBean) {
      ordersControl.value[`isReadingRefund${temp.orderdetailId}`] = false;
      ordersControl.value[`tempRefund${temp.orderdetailId}`] = formatRefund(
        temp.refundStatus
      );
    }
  }
}

//sweetalert通知
function forSweetalert(responsestatus, text, width) {
  if (width == null) {
    width = 300;
  }
  Swal.fire({
    html: `<h4 style="display: flex; justify-content: center; align-items: center; height: 100%; font-family= "Arial", sans-serif;">${text}</h4>`,
    icon: `${responsestatus}`,
    showConfirmButton: false,
    width: width,
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
  });
}

function changePayStatus(params) {
  ordersControl.value[`isSomethingChange${params}`] = true;
}

function changeOrderStatus(params) {
  ordersControl.value[`isSomethingChange${params}`] = true;
}

function changeReviewReason(item, orderdetailId, text) {
  ordersControl.value[`isSomethingChange${item.ordersBean.orderId}`] = true;

  if (
    ordersControl.value[`tempReviewReason${orderdetailId}`] === "符合退款條件"
  ) {
    ordersControl.value[`tempRefund${orderdetailId}`] = "核准退款";
  } else if (
    ordersControl.value[`tempReviewReason${orderdetailId}`] === "不符合退款條件"
  ) {
    ordersControl.value[`tempRefund${orderdetailId}`] = "拒絕退款";
  }
}

//用來顯示產品狀態
function formatRefund(refundStatus, item) {
  if ("cancel" === item) {
    return "已取消";
  }
  if (refundStatus === "pending") {
    return "退款待審核";
  } else if (refundStatus === "approve") {
    return "核准退款";
  } else if (refundStatus === "reject") {
    return "拒絕退款";
  }
  if ("finish" === item) {
    return "已送達";
  } else if ("sent" === item) {
    return "已送出";
  }

  return "準備中";
}

function formatTempRefund(refundStatus) {
  if (refundStatus === "退款待審核") {
    return "pending";
  } else if (refundStatus === "核准退款") {
    return "approve";
  } else if (refundStatus === "拒絕退款") {
    return "reject";
  }
  return;
}

//用來調整訂單狀態顯示內容
function formatOrderStatus(status) {
  if ("confirmed" === status) {
    return "訂單已成立";
  } else if ("finish" === status) {
    return "訂單已完成";
  } else if ("sent" === status) {
    return "訂單已送出";
  } else if ("cancel" === status) {
    return "訂單已取消";
  }
  return "待確認";
}
//用來將訂單狀態轉換成後端的資料名稱
function formatTempOrderStatus(status) {
  if ("訂單已成立" === status) {
    return "confirmed";
  } else if ("訂單已完成" === status) {
    return "finish";
  } else if ("訂單已送出" === status) {
    return "sent";
  } else if ("訂單已取消" === status) {
    return "cancel";
  }
  return "ToBeConfirmed";
}

//用來調整付款狀態顯示內容
function formatPaymentStatus(status) {
  if (0 === status) {
    return "未付款";
  } else if (1 === status) {
    return "已付款";
  } else if (-1 === status) {
    return "已取消";
  }
  return "待確認";
}
function formatTempPaymentStatus(status) {
  if ("未付款" === status) {
    return 0;
  } else if ("已付款" === status) {
    return 1;
  } else if ("已取消" === status) {
    return -1;
  }
  return 999;
}

function showSearchBox() {
  isShowSearchBox.value = !isShowSearchBox.value;
}

// //用來確認用什麼方法找訂單
// function useWhichFindOrder() {
//   if (route.params.userId) {
//     findOrderByuserId();
//   } else {
//     findAllOrder();
//   }
// }

// function useWhichCount() {
//   if (route.params.userId) {
//     countOrderById();
//   } else {
//     countAllOrder();
//   }
// }

// async function countOrderById() {
//   try {
//     const response = await axios.get(
//       `${path}/dashboard/order/ordercount/${route.params.userId}`,
//       config
//     );
//     if (response.data.length != 0) {
//       pageControl.value.total = response.data;
//     }
//   } catch (error) {
//     console.log(error);
//   }
// }

// //找出某個使用者的訂單
// async function findOrderByuserId() {
//   let request = searchRequest();
//   try {
//     const response = await axios.post(
//       `${path}/dashboard/order/find`,
//       request,
//       config
//     );
//     findOrderInitObj(response.data);
//     console.log("userid", response.data);
//   } catch (error) {
//     console.log(error);
//   }
// }
</script>

<style scoped>
.layout {
  background: url("../../../public/images/loginImg.jpg") center/cover fixed;
  margin: 0;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.6);
  background-blend-mode: multiply;
}

.order_main {
  width: 95%;
  margin-left: 30px;
  margin-top: 30px;
  margin-bottom: 30px;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.25s;
}
.fade-enter ,.fade-leave-to /* .fade-leave-active in<2.1.8*/ {
  opacity: 0.01s;
}

@keyframes fade-in {
  0% {
    transform: scale(0);
  }
  50% {
    transform: scale(1.25);
  }
  100% {
    transform: scale(1);
  }
}
.selectbox_btn {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 0;
}
.paymentStatus_select {
  margin: 0;
  width: 12%;
}

.orderStatus_select {
  margin: 0;
  width: 17%;
}

.sort_select {
  width: 23%;
}
.totalAmount_select {
  width: 22%;
}
.orderDate_select {
  width: 100%;
}

.searchbox {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  justify-content: flex-end;
}

.selectbox_label_top {
  width: 10%;
  margin-left: 10px;
  padding: 0;
}

.selectbox_label {
  width: 15%;
  margin-left: 10px;
  padding: 0;
}

.order_searchbox {
  justify-content: flex-end;
  display: flex;
  align-items: center;
  margin-bottom: 5px;
  margin-right: 20px;
}

.user_input {
  width: 15%;
}

.order_input {
  width: 15%;
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

/* .order_searchbox_set {
} */

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

.main_table {
  border-radius: 10px;
}

.dropdown_management {
  text-align: center;
  cursor: pointer;
  padding-top: 5px;
  padding-bottom: 5px;
}

.dropdown_management_menu {
  width: 100%;
}

.dropdown_management_btn {
  width: 100%;
}
.show_orderDetail {
  text-align: center;
}

.show_orderDetailtd {
  background-color: rgb(91, 89, 89);
}

.show_refundDetail {
  text-align: center;
}

.show_refundDetailtd {
  text-align: center;
  align-items: center;
  justify-content: center;
  background-color: rgb(148, 148, 154);
}

.table_fix_first {
  margin-right: 20px;
  border-radius: 10px;
}

.nodata_show {
  text-align: center;
}
.ordermanag_btn {
  border: none;
  cursor: pointer;
  margin-left: 10px;

  text-align: center;
}

.refund_select {
  width: 60%;
  margin: auto;
}
.refundreason_select {
  width: 40%;
  margin: auto;
}
</style>

<style scoped></style>
