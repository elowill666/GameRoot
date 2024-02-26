<template>
  <div style="display: flex" class="layout">
      <DashboardRouterlink style="height: 100vh"></DashboardRouterlink>
      <main class="insertcoupon">
        <div class="table-container">
          <table class="table" style="width: 500px">
            <thead>
              <tr>
                <th scope="col" >新增優惠券</th>
                <th scope="col" class="text-right">
                  <RouterLink :to="{ name: 'CouponManagement' }"
                    ><button type="button" class="btn btn-outline-primary">
                      查看所有優惠券
                    </button></RouterLink
                  >
                </th>
              </tr>
            </thead>
            <tbody class="table-group-divider">
              <tr>
                <td>優惠券名稱:</td>
                <td><input type="text" v-model="info" id="info" /></td>
              </tr>
              <tr>
                <td>折扣:</td>
                <td><input type="text" v-model="discount" id="discount" /></td>
              </tr>
              <tr>
                <td>折扣開始時間:</td>
                <td>
                  <div class="blocktime">
                    <span class="demonstration"></span>
                    <el-date-picker
                      v-model="value1"
                      id="begindate"
                      type="datetime"
                      placeholder="Select date and time"
                      @change="begindate(value1)"
                    />
                  </div>
                </td>
              </tr>
              <tr>
                <td>折扣結束時間:</td>
                <td>
                  <div class="blocktime">
                    <span class="demonstration"></span>
                    <el-date-picker
                      v-model="value2"
                      id="enddate"
                      type="datetime"
                      placeholder="Select date and time"
                      @change="enddate(value2)"
                    />
                  </div>
                </td>
              </tr>
            </tbody>
            <tfoot>
              <tr>
                <th scope="col"></th>
                <th scope="col" class="text-right">
                  <button
                    type="button"
                    class="btn btn-primary"
                    @click="addCoupon"
                  >
                    新增
                  </button>
                </th>
              </tr>
            </tfoot>
          </table>
        </div>
      </main>
  </div>
</template>

<script setup>
import { ref } from "vue";
import axios from "axios";
import Swal from "sweetalert2";
import { RouterLink } from "vue-router";
import DashboardRouterlink from "./DashboardRouterlink.vue";

const url = import.meta.env.VITE_JAVA_URL;
const value1 = ref("");
const value2 = ref("");
const info = ref(""); // Ref for the info input element
const discount = ref(""); // Ref for the discount input element
let globalBeginDate = null;
let globalEndDate = null;

const formatDate = (date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");
  const seconds = String(date.getSeconds()).padStart(2, "0");

  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};

const begindate = (params) => {
  globalBeginDate = formatDate(params);
};

const enddate = (temp) => {
  globalEndDate = formatDate(temp);
};

const addCoupon = () => {
  const discountValue = discount.value; // Accessing input value via ref
  const infoValue = info.value; // Accessing input value via ref

  console.log("info=" + infoValue);
  console.log("discount=" + discountValue);
  // 检查输入数据是否为空
  if (!discountValue || !infoValue || !globalBeginDate || !globalEndDate) {
    Swal.fire({
      title: "新增失敗",
      text: "空格不得為空",
      icon: "error",
      showConfirmButton: true,
      confirmButtonText: "OK",
    });
    return;
  }
  if (globalBeginDate > globalEndDate) {
  Swal.fire({
    title: "新增失敗",
    text: "開始日期不能晚於結束日期",
    icon: "error",
    showConfirmButton: true,
    confirmButtonText: "OK",
  });
  return;
}

  const used = "unused";
  const couponData = {
    discount: discountValue,
    info: infoValue,
    used: used,
    beginDate: globalBeginDate,
    endDate: globalEndDate,
  };

  axios
    .post(`${url}/coupon/createCoupon`, couponData)
    .then((response) => {
      Swal.fire({
        title: response.data.text,
        icon: "success",
        showConfirmButton: true,
        confirmButtonText: "OK",
      });

      // 清空输入框的数据
      value1.value = "";
      value2.value = "";
      info.value = ""; // Clearing input value via ref
      discount.value = ""; // Clearing input value via ref
    })
    .catch((error) => {
      console.error("Error adding coupon:", error);
      Swal.fire({
        title: "Error",
        text: "Failed to add coupon",
        icon: "error",
        showConfirmButton: true,
        confirmButtonText: "OK",
      });
    });
};
</script>

<style scoped>
.table-container{
  display: flex;
  justify-content: center;
}
.insertcoupon {
  width: 100%;
  margin-left: 100px;
  margin-right: 250px;
  margin-top: 150px;
  margin-bottom: 30px;
}
.layout {
  background: url("../../../public/images/loginImg.jpg") center/cover fixed;
  margin: 0;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.6);
  background-blend-mode: multiply;
}

</style>
