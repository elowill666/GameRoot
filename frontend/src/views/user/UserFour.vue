<template>
  <div class="myback">
    <br>
    <!-- <div class="table-container">
      <table class="table table-hover" style="width: 700px">
        <thead>
          <tr>
            <th scope="col"></th>
            <th scope="col">折扣</th>
            <th scope="col">開始時間</th>
            <th scope="col">結束時間</th>
          </tr>
        </thead>

        <tbody class="table-group-divider">
          <tr v-for="(coupon, index) in couponlist" :key="coupon.couponId">
            <td style="width: 15px; vertical-align: middle; font-size: 19px">{{ (index + 1) + "." }}</td>
            <td style="width: 70px; vertical-align: middle ; color:red; font-size: 25px;
              font-weight: bold">{{ (coupon.discount) * 100 - 100 + "%" }}</td>
            <td style="width: 70px; vertical-align: middle">{{ formatDate(coupon.begin_of_Valid_date) }}</td>
            <td style="width: 70px; vertical-align: middle">{{ formatDate(coupon.end_of_valid_date) }}
              <el-button text @click="open">使用說明</el-button>

              <router-link to="/order/orderfour"><button type="button" class="btn btn-primary" @click="doclick">使用</button></router-link>
            </td>

          </tr>
        </tbody>
      </table>
    </div> -->

    <div class="mycop">
      <div class="coupons">
        <div class="coupon " v-for="(coupon, index) in couponlist" :key="coupon.couponId" @click="usecoupon">
          <div class="coupon-intro">
            <h4>{{ coupon.info }}</h4>
            <ul style="color:#3a556a;">
              <li>有效日期 : </li>
              <li>{{ formatDate(coupon.beginDate) }} 至 {{ formatDate(coupon.endDate) }} 前使用</li>
              <!-- <br> -->
              <el-button type="primary" plain @click="open">使用說明</el-button>
              

              <!-- <RouterLink :to="{name:'Shoppingcart',params:{id:coupon.coupon_id , userid:coupon.userId}}"><el-button type="danger" plain>使用優惠券</el-button></RouterLink> -->
            </ul>
          </div>
          <div class="coupon-value">
            {{ (coupon.discount) * 10 + "折" }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import axios from 'axios';
import { onMounted, ref, h,watch } from 'vue';
import { ElMessageBox } from 'element-plus'
import { useRoute } from 'vue-router';

const url = import.meta.env.VITE_JAVA_URL;
const couponlist = ref([]);
const route = useRoute();

const fetchData  =async () => {
  try {
    const API_URL = `${import.meta.env.VITE_JAVA_URL}/coupon/findById`
    let response = await axios.get(API_URL, { withCredentials: true });
    Object.assign(couponlist.value, response.data)
    console.log("couponlist",couponlist.value)
    couponlist.value = response.data;
  } catch (error) {
    console.error(error);
  }
};

onMounted( () => {
  fetchData ();
});







const formatDate = (value) => {
  const date = new Date(value);
  const formattedDate = `${date.getFullYear()}/${padZero(date.getMonth() + 1)}/${padZero(date.getDate())} ${padZero(date.getHours())}:${padZero(date.getMinutes())}`;
  return formattedDate;
};
const padZero = (num) => {
  return num.toString().padStart(2, '0');
};

const open = (event) => {
  ElMessageBox({
    title: '使用說明:',
    message: h('p', null, [
      h('span', null, '一次只能使用一張!'),
    ]),
  })
  // event.stopPropagation();
}

function usecoupon(event) {
  console.log("使用優惠券方法")
}
</script>

<style scoped lang="scss">
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
  align-items: center;
  /* height: 100vh; 这会使表格在垂直方向上居中，视口的100%高度 */
}

h4 {
  color: #3a556a;
}

.mycop {
  display: flex;
  justify-content: center;
  align-items: center;
}

$background-color: #39526a;
$radius-size: 12px;

.coupons {
  background: $background-color;
  padding: 50px;
  width: 700px;
  height: 100%;
  border-radius: 10px;
  box-shadow: 0px 0px 10px 3px #171717;

  .coupon {
    margin-bottom: 20px;
    display: flex;
    align-items: stretch;
    justify-content: stretch;
    cursor: pointer;

    >div {
      padding: 10px 20px;
      border-radius: $radius-size;
    }

    .coupon-intro {
      position: relative;
      background: #fff;
      flex: 3;

      ul {
        color: #aaa;
        padding-left: 20px;
      }

      &:after {
        content: ' ';
        border: 2px dashed $background-color;
        height: 100%;
        position: absolute;
        top: 0;
        right: -2px;
        z-index: 2;
      }
    }

    .coupon-value {
      display: flex;
      flex: 1;
      position: relative;
      background: #efac37;
      color: #fff;
      font-size: 38px;
      align-items: center;
      justify-content: center;

      &:before {
        content: ' ';
        width: $radius-size * 2;
        height: $radius-size * 2;
        position: absolute;
        top: -$radius-size;
        left: -$radius-size;
        border-radius: 100%;
        background: $background-color;
      }

      &:after {
        content: ' ';
        width: $radius-size * 2;
        height: $radius-size * 2;
        position: absolute;
        bottom: -$radius-size;
        left: -$radius-size;
        border-radius: 100%;
        background: $background-color;
      }
    }
  }
}
</style>

