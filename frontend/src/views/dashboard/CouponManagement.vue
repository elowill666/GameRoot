<template>
  <div style="display: flex" class="layout">
    <DashboardRouterlink></DashboardRouterlink>
    <main class="couponmang_main">
      <div class="table-container">
        <table class="table table-hover" style="width: 1000px">
          <thead>
            <tr>
              <th scope="col"></th>
              <th scope="col">活動名稱</th>
              <th scope="col">折扣</th>
              <th scope="col">User Id</th>
              <th scope="col">Coupon Id</th>
              <th scope="col">使用狀態</th>
              <th scope="col">開始時間</th>
              <th scope="col">結束時間 </th>
              <th scope="col"><button type="button" class="btn btn-warning" @click="showdelete">編輯</button></th>
            </tr>
          </thead>
          <tbody class="table-group-divider">
            <tr v-for="(coupon, index) in paginatedCouponList" :key="coupon.couponId">
              <td style="width: 15px; vertical-align: middle; font-size: 19px">{{ (index + 1) + "." }}</td>
              <td style="width: 90px; height: 80px; vertical-align: middle; font-size: 19px">{{ coupon.info }}</td>
              <td style="width: 70px; vertical-align: middle">{{ (coupon.discount) * 10 + "折" }}</td>
              <td style="width: 80px; vertical-align: middle">{{ coupon.userId }}</td>
              <td style="width: 70px; vertical-align: middle">{{ coupon.couponId }}</td>
              <td style="width: 70px; vertical-align: middle">{{ coupon.used }}</td>
              <td style="width: 100px; vertical-align: middle">{{ formatDate(coupon.beginDate) }}</td>
              <td style="width: 100px; vertical-align: middle">{{ formatDate(coupon.endDate) }}</td>
              <td style="width: 40px; vertical-align: middle"><button type="button" class="btn btn-outline-danger"
                  v-show="isdelete" @click="deleteCoupon(coupon.couponId)">刪除</button></td>
            </tr>
          </tbody>
        </table>

      </div>
      <div class="example-pagination-block">
        <el-pagination layout="prev, pager, next" :total="totalItems" v-if="totalItems > 0"
          :current-page.sync="currentPage" :page-size="pageSize" :background="background"
          @current-change="handleCurrentChange" style="justify-content: center;" />
      </div>
    </main>

  </div>
  <!-- 分页控件 -->
</template>

<script setup>
import axios from 'axios';
import { onMounted, ref, computed } from 'vue';
import Swal from "sweetalert2";
import DashboardRouterlink from "./DashboardRouterlink.vue";

const url = import.meta.env.VITE_JAVA_URL;
const couponlist = ref([]);
const request = ref({
  couponId: "",
});
const isdelete = ref(false);
const currentPage = ref(1);
const pageSize = ref(10);
const background = ref(true);

onMounted(async () => {
  try {
    const response = await axios.get(`${url}/coupon/findall`);
    couponlist.value = response.data.reverse();
  } catch (error) {
    console.error(error);
  }
});

function showdelete() {
  isdelete.value = !isdelete.value;
}

async function deleteCoupon(couponId) {
  console.log("couponid" + couponId);
  Swal.fire({
    title: "請確認",
    text: "是否移除此優惠券？",
    icon: "question",
    showConfirmButton: true,
    confirmButtonText: "刪除",
    confirmButtonColor: "red",
    showCancelButton: true,
    cancelButtonText: "取消"
  }).then(async result => {
    if (result.isConfirmed) {
      request.value.couponId = couponId;
      const response = await axios.delete(`${url}/coupon/delete/${couponId}`);
      if (response.data.success) {
        Swal.fire({
          title: "刪除成功",
          text: response.data.text,
          icon: "success",
          showConfirmButton: true,
        });
        location.reload();
      } else {
        Swal.fire({
          title: "刪除失敗",
          text: response.data.text,
          icon: "error",
          showConfirmButton: true,
        });
      }
    }
  });
}

const formatDate = (value) => {
  const date = new Date(value);
  const formattedDate = `${date.getFullYear()}/${padZero(date.getMonth() + 1)}/${padZero(date.getDate())} ${padZero(date.getHours())}:${padZero(date.getMinutes())}`;
  return formattedDate;
};
const padZero = (num) => {
  return num.toString().padStart(2, '0');
};

const totalItems = computed(() => couponlist.value.length);

const paginatedCouponList = computed(() => {
  const startIndex = (currentPage.value - 1) * pageSize.value;
  const endIndex = startIndex + pageSize.value;
  return couponlist.value.slice(startIndex, endIndex);
});

const handleCurrentChange = (val) => {
  currentPage.value = val;
};
</script>

<style scoped>
.table-container{
  display: flex;
  justify-content: center;
}
.couponmang_main {
  width: 100%;
  margin-left: 10px;
  margin-right: 50px;
  margin-top: 30px;
  margin-bottom: 30px;
}

.layout {
  background: url("../../../public/images/loginImg.jpg") center/cover fixed;
  margin: 0;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.6);
  background-blend-mode: multiply;
}

/* .example-pagination-block {
  margin-top: 10px;
  margin-left: auto;
  margin-bottom: 10px;
} */
</style>
