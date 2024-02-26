<template>
  <div class="myback">
    <div class="info">
      <div>
        <OrderStep :stepInfo="stepInfo"></OrderStep>
      </div>
      <div class="card-XXX" style="width: 30rem;">
        <div class="card-body">
          <h5 class="card-title">詳細資訊 :</h5>
          <hr>
          <p class="card-text">
          <div>
            <td>{{ "訂購日期: " + formatDate(orders.orderDate) }}</td>
          </div>
          <div>
            <td>{{ "總金額: $" + orders.totalAmount }}</td>
          </div>
          <div>
            <td>{{ "付款方式: " + orders.paymentMethod }}</td>
          </div>
          <div>
            <td>{{ "付款日期: " + formatDate(orders.paymentDate) }}</td>
          </div>
          <div>
            <td>{{ "訂單狀態: " + orders.orderStatus }}</td>
          </div>
          <div>
            <td v-if="orders.consigneeUsername !== null">
              {{ "收件者: " + orders.consigneeUsername }}
            </td>
          </div>
          <div>
            <td v-if="orders.consigneeCity || orders.consigneeArea || orders.consigneeAddress">
              {{ "收件地址: " + (orders.consigneeCity || '') + (orders.consigneeArea || '') + (orders.consigneeAddress || '')
              }}
            </td>
          </div>
          <div>
            <td v-if="orders.consigneeTelephonenumber !== null">
              {{ "聯絡電話: " + orders.consigneeTelephonenumber }}
            </td>
          </div>
          </p>
        </div>
      </div>
    </div>
    <br>
    <div class="table-container">
      <table class="table table-hover" style="width: 900px;">
        <thead>
          <tr>
            <th scope="col"></th>
            <th scope="col">產品名稱</th>
            <!-- <th scope="col">數量</th> -->
            <th scope="col">價格</th>
            <th scope="col">評分狀態</th>
            <!-- <th scope="col">訂單狀態</th> -->
            <th scope="col"></th>
            <th scope="col"></th>
          </tr>
        </thead>

        <tbody class="table-group-divider">
          <tr v-for="(Orderdetail, index) in Orderdetail" :key="Orderdetail.orderId">
            <td style="width: 15px; vertical-align: middle">{{ (index + 1) + "." }}</td>
            <td style="width: 120px; vertical-align: middle" @click="SeeProductInfor(Orderdetail.productId)">{{
              Orderdetail.historyProductName }}</td>
            <td style="width: 70px; vertical-align: middle">{{ "$" + Orderdetail.historyPrice }}</td>
            <td style="width: 90px; vertical-align: middle">
              <OrderProductCommentButton
                v-if="Orderdetail.productCommentStatus === 'noComment' && new Date(Orderdetail.productCommentEndtime) >= new Date()"
                :userId="Orderdetail.userId" :productId="Orderdetail.productId"
                :orderdetailId="Orderdetail.orderdetailId">
              </OrderProductCommentButton>
              <span
                v-if="Orderdetail.productCommentStatus === 'noComment' && new Date(Orderdetail.productCommentEndtime) <= new Date()">未評論</span>
              <span v-if="Orderdetail.productCommentStatus === 'Commented'">已評論</span>
            </td>

            <!-- <td style="width: 80px; vertical-align: middle">{{ Orderdetail.orderStatus }}</td> -->
            <td style="width: 80px; vertical-align: middle">
              <button v-if="Orderdetail.gamekey !== null" type="button" class="btn btn-outline-primary"
                :gamekey="Orderdetail.gamekey"
                @click="openGameKey(Orderdetail.gamekey, Orderdetail.historyProductName)">查看金鑰</button>
            </td>

            <td style="width: 100px; vertical-align: middle">
              <button v-if="Orderdetail.refundStatus === null" type="button" class="btn btn-outline-danger"
                @click="gotoRedfund(Orderdetail.orderdetailId)">我要退貨</button>
              <button v-else-if="Orderdetail.refundStatus === 'pending'" type="button" disabled
                class="btn btn-outline-success">審核中</button>
              <button v-else-if="Orderdetail.refundStatus === 'approve'" type="button" disabled
                class="btn btn-warning">已退款</button>
              <button v-else="Orderdetail.refundStatus === 'reject'" type="button" disabled
                class="btn btn-outline-secondary">未通過</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

  </div>
</template>
  
<script setup >
import axios from 'axios';
import { onMounted, ref, h } from 'vue';
import router from "@/router";
import { RouterLink, useRoute } from 'vue-router';
import OrderProductCommentButton from './OrderProductCommentButton.vue';
import OrderStep from '@/components/OrderStep.vue';
import { format } from "../../../node_modules/date-fns";
import { ElMessageBox } from 'element-plus'

const Orderdetail = ref([]);
const orders = ref([]);
const stepInfo = ref({
  list: [],
  step: 0
});


//點選進入產品詳細內容頁
const SeeProductInfor = async (productId) => {
  console.log(productId);

  router.push({
    name: "ProductDetail",
    params: {
      id: productId,
    },
  });
};

const openGameKey = (params, name) => {
  ElMessageBox({
    title: `${name}的金鑰:`,
    message: h('p', null, [
      h('span', null, `${params}`),
    ]),
  })
  // event.stopPropagation();
}



const formatDate = (value) => {
  const date = new Date(value);
  const formattedDate = `${date.getFullYear()}/${padZero(date.getMonth() + 1)}/${padZero(date.getDate())} ${padZero(date.getHours())}:${padZero(date.getMinutes())}`;
  return formattedDate;
};
const padZero = (num) => {
  return num.toString().padStart(2, '0');
};

onMounted(async () => {
  const route = useRoute();
  const orderId = route.params.id
  const VITE_API_GAMESHOP_ORDERDETAIL = `${import.meta.env.VITE_JAVA_URL}/order/findOrderdetail/`
  const response01 = await axios.get(VITE_API_GAMESHOP_ORDERDETAIL + orderId)
  Orderdetail.value = response01.data;
  console.log("Orderdetail = ", response01.data);
  orders.value = response01.data[0]
  console.log("orders = ", response01.data[0]);
  orders.value.orderDate = formatordersDate(orders.value.orderDate)

  switch (orders.value.orderStatus) {
    case 'confirmed':
      stepInfo.value.list.push({ status: '確認訂單', statusTime: orders.value.orderDate });
      stepInfo.value.list.push({ status: '已出貨', statusTime: '' });
      stepInfo.value.list.push({ status: '已完成', statusTime: '' });
      stepInfo.value.step = 1;
      break;

    case 'sent':
      orders.value.shipmentDate = formatordersDate(orders.value.shipmentDate);
      stepInfo.value.list.push({ status: '確認訂單', statusTime: orders.value.orderDate });
      stepInfo.value.list.push({ status: '已出貨', statusTime: orders.value.shipmentDate });
      stepInfo.value.list.push({ status: '已完成', statusTime: '' });
      stepInfo.value.step = 2;
      break;

    case 'finish':
      orders.value.shipmentDate = formatordersDate(orders.value.shipmentDate);
      orders.value.finishDate = formatordersDate(orders.value.finishDate);
      stepInfo.value.list.push({ status: '確認訂單', statusTime: orders.value.orderDate });
      stepInfo.value.list.push({ status: '已出貨', statusTime: orders.value.shipmentDate });
      stepInfo.value.list.push({ status: '已完成', statusTime: orders.value.finishDate });
      stepInfo.value.step = 3;
      break;

    default:
      stepInfo.value.list.push({ status: '確認訂單', statusTime: '' });
      stepInfo.value.list.push({ status: '已出貨', statusTime: '' });
      stepInfo.value.list.push({ status: '已完成', statusTime: '' });
      break;
  }

});
function formatordersDate(date) {
  console.log(date);
  if (date === null) {
    return '';
  }
  return format(date, "yyyy-MM-dd HH:mm:ss");
}

function gotoRedfund(orderdetailId) {
  if (orderdetailId != null) {
    router.push('/order/orderRedfund/' + orderdetailId)
  }

}
</script>
  
<style scoped>
.myback {
  background: url("../../../public/images/homeimg.jpg") center/cover fixed;
  margin: 0;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.2);
  background-blend-mode: multiply;
}

.table-container {
  display: flex;
  justify-content: center;
}

.card-XXX {
  display: flex;
  justify-content: center;
  margin-left: auto;
  margin-right: auto;
}

.info {
  /* background-size: cover;  */
  background-position: center;
  background-color: black;
  width: 900px;
  /* display: flex; */
  justify-content: center;
  /* margin-left: 510px; */
  margin-left: auto;
  margin-right: auto;

}
</style>