<template>
  <div class="myback">
    <br />
    <br />
    <!-- 時間查詢 -->
    <div style="width: 900px; margin: auto; margin-bottom: 20px">
      <div style="justify-content: flex-start">
        <el-date-picker
          v-model="value2"
          type="monthrange"
          unlink-panels
          range-separator="To"
          start-placeholder="Start month"
          end-placeholder="End month"
          @change="dosearch(value2)"
        />
      </div>

      <!-- <h3>查詢結果</h3> -->
      <br />

      <div class="table-container" style="justify-content: flex-start">
        <table class="table table-hover" style="width: 900px">
          <thead>
            <tr>
              <th scope="col"></th>
              <th scope="col">商品名稱</th>
              <th scope="col">訂單編號</th>
              <th scope="col">總金額</th>
              <th scope="col">訂購日期</th>
              <th scope="col"></th>
            </tr>
          </thead>
          <tbody class="table-group-divider">
            <tr v-for="(order, index) in paginatedOrders" :key="order.userId">
              <td style="width: 15px">{{ index + 1 + "." }}</td>
              <td style="width: 80px">
                {{ order.orderdetailsBean[0]?.historyProductName }}
              </td>
              <td style="width: 50px">{{ order.ordersBean.orderId }}</td>
              <td style="width: 60px">
                {{ "$" + order.ordersBean.totalAmount }}
              </td>
              <td style="width: 15px">
                {{ formatDate(order.ordersBean.orderDate) }}
              </td>
              <td style="width: 65px">
                <RouterLink
                  :to="{
                    name: 'orderDetailsCard',
                    params: {
                      id: order.ordersBean.orderId,
                      userid: order.ordersBean.userId,
                    },
                  }"
                >
                  <button type="button" class="btn btn-primary">
                    查看更多
                  </button>
                </RouterLink>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <!-- Pagination Controls -->

      <el-pagination
        style="justify-content: end"
        layout="prev, pager, next"
        :total="totalItems"
        v-if="totalItems > 0"
        :current-page.sync="currentPage"
        :page-size="pageSize"
        :background="background"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup>
import axios from "axios";
import { onMounted, ref, computed } from "vue";
import { RouterLink, useRoute } from "vue-router";

const url = import.meta.env.VITE_JAVA_URL;
const orders = ref([]);
const ordersTemp = ref([]);

onMounted(async () => {
  const route = useRoute();
  const userId = route.params.id;
  const VITE_API_GAMESHOP_USERORDER = `${
    import.meta.env.VITE_JAVA_URL
  }/order/userordertest/`;
  const response = await axios.post(VITE_API_GAMESHOP_USERORDER + userId);
  orders.value = response.data;
  ordersTemp.value = response.data;
});

const formatDate = (value) => {
  const date = new Date(value);
  const formattedDate = `${date.getFullYear()}/${padZero(
    date.getMonth() + 1
  )}/${padZero(date.getDate())} ${padZero(date.getHours())}:${padZero(
    date.getMinutes()
  )}`;
  return formattedDate;
};
const padZero = (num) => {
  return num.toString().padStart(2, "0");
};

//時間查詢
const value2 = ref("");

function dosearch(params) {
  orders.value = ordersTemp.value.filter(
    (order) =>
      new Date(order.ordersBean.orderDate) >= new Date(params[0]) &&
      new Date(order.ordersBean.orderDate) <= new Date(params[1])
  );
}

// 分页相关
const currentPage = ref(1);
const pageSize = ref(10);
const background = ref(true);

const totalItems = computed(() => orders.value.length);

const paginatedOrders = computed(() => {
  const startIndex = (currentPage.value - 1) * pageSize.value;
  const endIndex = startIndex + pageSize.value;
  return orders.value.slice(startIndex, endIndex);
});

const handleCurrentChange = (val) => {
  currentPage.value = val;
};
</script>

<style scoped>
.myback {
  background: url("../../../public/images/homeimg.jpg") center/cover fixed;
  margin: 0;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.2);
}

.table-container {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
