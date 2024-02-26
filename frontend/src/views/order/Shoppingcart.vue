<template>
  <div class="layout">
    <main>
      <div class="table-responsive">
        <table class="table align-middle table-hover custom-table">
          <thead>
            <tr class="table-secondary">
              <th scope="col"></th>
              <th scope="col">商品名稱</th>
              <th scope="col">數量</th>
              <th scope="col">價格</th>
              <th scope="col"></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="{
              productName, quantity, totalprice, coverImage, cartmiddleId, discountPrice, productStatus, inventoryQuantity
            }, index in   shoppingcart  " :key="cartmiddleId" :class="{ 'not-sale': productStatus === 'notsale' }">
              <td><img :src="coverImage" :alt="productName"></td>
              <td>{{ productName }}</td>
              <td v-if="productStatus === 'notsale'"><img src="/images/soldout.png" style="width: 50%;" alt="123"></td>
              <td v-else><button class="btn btn-sm btn-outline-primary" type="button" style="margin: 8px;"
                  @click="doclickDecrease(cartmiddleId, quantity, index)">-</button>{{ quantity
                  }}<button type="button" style="margin: 8px" class="btn btn-sm btn-outline-primary"
                  @click="doclickIncrease(cartmiddleId, quantity, index, inventoryQuantity)">+</button></td>
              <td v-if="productStatus === 'discount'">
                <div class="org-price">NT${{ totalprice }}</div>
                <div style="color: rgb(255, 17, 0); font-weight: bold;">NT${{ discountPrice }}</div>
              </td>
              <td v-else-if="productStatus === 'notsale'">
                NT${{ totalprice / quantity }}
              </td>
              <td v-else>NT${{ totalprice }}</td>
              <td><button @click="doclickRemove(cartmiddleId, index)" class="custom-button">
                  <img src="/images/trashcan.png" alt="delete" class="custom-img">
                </button></td>
            </tr>
          </tbody>
          <tfoot>
            <tr>
              <td></td>
              <td><button class="custom-btn btn-3" style="float: right;" @click="chooseCoupon">
                  <span>{{ couponcolumn }}</span>
                </button></td>
              <td>
                <div v-show="couponshow" class="discounttext">折扣-NT${{ discountTotal }}</div>
              </td>
              <td>
                <div>總額</div>NT${{ totalPrices }}
              </td>
              <td>
                <button type="button" class="custom-btn btn-3" @click="gopay"><span>結帳去</span></button>
              </td>
            </tr>
          </tfoot>
        </table>
      </div>
    </main>
  </div>
</template>

<script setup>
import axios from 'axios';
import Swal from 'sweetalert2';
import { onMounted, computed, ref } from 'vue'
import { useRouter } from "vue-router"

const router = useRouter()
const discountTotal = ref(0)
let totalPrices = ref(0);
let shoppingcart = ref([]);
let x = 0;
let org = 0;
let loadShoppingcart = async () => {
  const API_URL = `${import.meta.env.VITE_JAVA_URL}/shoppingcart/findcart`
  let response = await axios.get(API_URL, { withCredentials: true });
  shoppingcart.value = response.data
  console.log(shoppingcart.value);
  for (let temp = 0; temp < shoppingcart.value.length; temp++) {
    //一定要有await等待findProductImage()找完圖片
    const coverImageUrls = await findProductImage(
      shoppingcart.value[temp].productId
    );
    shoppingcart.value[temp].coverImage = coverImageUrls.data;
  }
  chacklength();
}
async function findProductImage(productId) {
  const VITE_API_PRODUCTCOVERIMAGE = `${import.meta.env.VITE_JAVA_URL
    }/dashboard/productimage/${productId}`;
  try {
    const response = await axios.post(VITE_API_PRODUCTCOVERIMAGE);
    return response;
  } catch (error) {
    console.log(error);
  }
}
onMounted(async () => {
  loadShoppingcart();
})

function chacklength() {
  if (shoppingcart.value.length === 0) {
    Swal.fire({
      icon: "info",
      title: "購物車沒有商品",
      text: "前往商店",
      allowOutsideClick: false,
      confirmButtonText: "確定",
    }).then(async function (result) {
      if (result.isConfirmed) {
        router.push("/product/products")
      }
    })
  }
}
function gopay() {
  if (totalPrices.value.value === 0) {
    Swal.fire({
      icon: "info",
      title: "購物車沒有有效商品",
      text: "前往商店",
      allowOutsideClick: false,
      confirmButtonText: "確定",
    }).then(async function (result) {
      if (result.isConfirmed) {
        router.push("/product/products")
      }
    })
  } else {
    router.push("/order/paydata")
  }
}


function lastprice(newTotalPrices) {
  const API_URL = `${import.meta.env.VITE_JAVA_URL}/shoppingcart/lastprice/`
  axios.get(API_URL + newTotalPrices + '/' + couponId.value, { withCredentials: true })
}

totalPrices.value = computed(() => {
  x = 0
  for (let i = 0; i < Object.keys(shoppingcart.value).length; i++) {
    if (shoppingcart.value[i].productStatus === 'discount') {
      x = x + shoppingcart.value[i].discountPrice
    } else if (shoppingcart.value[i].productStatus === 'notsale') {
      x = x
    } else {
      x = x + shoppingcart.value[i].totalprice
    }
  }

  if (coupondiscount.value != 0) {
    org = x;
    x = Math.ceil(x * coupondiscount.value / 10);
    discountTotal.value = org - x;
  }

  lastprice(x);
  return x
});

async function doclickRemove(cartmiddleId, index) {
  Swal.fire({
    icon: "question",
    text: "確定要刪除嗎？",
    allowOutsideClick: false,
    confirmButtonText: "確定",
    showCancelButton: true,
    cancelButtonText: "取消"
  }).then(async function (result) {
    if (result.isConfirmed) {
      Swal.fire({
        text: "Loading.......",
        allowOutsideClick: false,
        showConfirmButton: false
      });
      const API_URL = `${import.meta.env.VITE_JAVA_URL}/shoppingcart/removecart/`
      await axios.delete(API_URL + cartmiddleId, { withCredentials: true })
      Swal.fire({
        icon: "success",
        text: "刪除成功",
        confirmButtonText: "確定"
      });
      shoppingcart.value.splice(index, 1);
      chacklength();
    }
  })
}


async function doclickDecrease(cartmiddleId, quantity, index) {
  if (quantity === 1) {
    Swal.fire({
      icon: "question",
      text: "確定要刪除嗎？",
      allowOutsideClick: false,
      confirmButtonText: "確定",
      showCancelButton: true,
      cancelButtonText: "取消"
    }).then(async function (result) {
      if (result.isConfirmed) {
        Swal.fire({
          text: "Loading.......",
          allowOutsideClick: false,
          showConfirmButton: false
        });
        const API_URL = `${import.meta.env.VITE_JAVA_URL}/shoppingcart/removecart/`
        await axios.delete(API_URL + cartmiddleId, { withCredentials: true })
        Swal.fire({
          icon: "success",
          text: "刪除成功",
          confirmButtonText: "確定"
        });
        shoppingcart.value.splice(index, 1);
        chacklength();
      }
    })
  } else {
    const API_URL = `${import.meta.env.VITE_JAVA_URL}/shoppingcart/decrease/`
    await axios.put(API_URL + cartmiddleId, { withCredentials: true });
    let orgDiscountPrice = shoppingcart.value[index].discountPrice / quantity
    shoppingcart.value[index].quantity = quantity - 1;
    let newQuantity = shoppingcart.value[index].quantity;
    shoppingcart.value[index].totalprice = newQuantity * shoppingcart.value[index].price
    shoppingcart.value[index].discountPrice = newQuantity * orgDiscountPrice
  }
}

async function doclickIncrease(cartmiddleId, quantity, index, inventoryQuantity) {
  if (inventoryQuantity != null) {
    if (quantity === inventoryQuantity) {
      Swal.fire({
        icon: "warning",
        text: "商品已達庫存數量",
        allowOutsideClick: false,
        confirmButtonText: "確定",
        showCancelButton: true,
        cancelButtonText: "取消"
      })
      return;
    }
  }
  if (quantity === 5) {
    Swal.fire({
      icon: "warning",
      text: "已達最大數量",
      allowOutsideClick: false,
      showCloseButton: true,
      confirmButtonText: "確定",
    })
  } else {
    const API_URL = `${import.meta.env.VITE_JAVA_URL}/shoppingcart/increase/`
    await axios.put(API_URL + cartmiddleId, { withCredentials: true });
    let orgDiscountPrice = shoppingcart.value[index].discountPrice / quantity
    shoppingcart.value[index].quantity = quantity + 1;
    let newQuantity = shoppingcart.value[index].quantity;
    shoppingcart.value[index].totalprice = newQuantity * shoppingcart.value[index].price
    shoppingcart.value[index].discountPrice = newQuantity * orgDiscountPrice

  }
}
const couponcolumn = ref('優惠券');
const coupondiscount = ref(0)
const couponId = ref(0);
const couponshow = ref(false)
const couponlist = ref([]);
const couponlistId = ref({})
function formatdiscount(x) {
  x = x * 10
  return x;
}

async function chooseCoupon() {
  const API_URL = `${import.meta.env.VITE_JAVA_URL}/coupon/findById`
  let response = await axios.get(API_URL, { withCredentials: true });
  Object.assign(couponlist.value, response.data)
  console.log(couponlist.value);
  for (let i = 0; i < couponlist.value.length; i++) {
    const discount = formatdiscount(couponlist.value[i].discount)
    couponlistId.value[i] = couponlist.value[i].info +"："+ discount + "折券";

  }
  console.log(couponlistId.value);
  const { value: fruit } = await Swal.fire({
    title: "選擇你的優惠券",
    input: "select",
    inputOptions: couponlistId.value,
    inputPlaceholder: "要使用的優惠券",
    showCancelButton: true,
    inputValidator: (value) => {
    }
  });
  if (fruit) {
    couponshow.value = true;
    couponId.value = couponlist.value[fruit].couponId;
    console.log(couponId);
    couponcolumn.value = `${couponlist.value[fruit].discount * 10}折券`;
    coupondiscount.value = (couponlist.value[fruit].discount * 10)
    // Swal.fire(`You selected: ${fruit}`);
  }
}
</script>
<style scoped lang="css" src="../../assets/shoppingcart.css"></style>
<style scoped>
.layout {
  background: url('../../../public/images/homeimg.jpg') center/cover fixed;
  margin: 0;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.6);
  background-blend-mode: multiply;
}

main {
  margin-top: 80px;
  margin-bottom: 50px;
  min-height: 500px;
}

.discounttext {
  color: red;
  /* 將文字顏色設置為紅色 */
  font-weight: bold;
  /* 將文字設置為粗體 */
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
  font-size: 16px;
  /* 設置文字大小 */
  letter-spacing: 1px;
  /* 設置文字間距 */

}

.custom-img {
  padding: 0;
  margin: 0;
  width: 50%;
}

.custom-btn {
  width: 130px;
  height: 40px;
  color: #fff;
  border-radius: 5px;
  padding: 10px 25px;
  font-family: "Lato", sans-serif;
  font-weight: 500;
  background: transparent;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  display: inline-block;
  box-shadow: inset 2px 2px 2px 0px rgba(255, 255, 255, 0.5),
    7px 7px 20px 0px rgba(0, 0, 0, 0.1), 4px 4px 5px 0px rgba(0, 0, 0, 0.1);
  outline: none;
}

.btn-3 {
  background: rgb(0, 172, 238);
  background: linear-gradient(0deg,
      rgba(0, 172, 238, 1) 0%,
      rgba(2, 126, 251, 1) 100%);
  width: 80px;
  height: 40px;
  line-height: 42px;
  padding: 0;
  border: none;
}

.btn-3 span {
  position: relative;
  display: block;
  width: 100%;
  height: 100%;
}

.btn-3:before,
.btn-3:after {
  position: absolute;
  content: "";
  right: 0;
  top: 0;
  background: rgba(2, 126, 251, 1);
  transition: all 0.3s ease;
}

.btn-3:before {
  height: 0%;
  width: 2px;
}

.btn-3:after {
  width: 0%;
  height: 2px;
}

.btn-3:hover {
  background: transparent;
  box-shadow: none;
}

.btn-3:hover:before {
  height: 100%;
}

.btn-3:hover:after {
  width: 100%;
}

.btn-3 span:hover {
  color: rgba(2, 126, 251, 1);
}

.btn-3 span:before,
.btn-3 span:after {
  position: absolute;
  content: "";
  left: 0;
  bottom: 0;
  background: rgba(2, 126, 251, 1);
  transition: all 0.3s ease;
}

.btn-3 span:before {
  width: 2px;
  height: 0%;
}

.btn-3 span:after {
  width: 0%;
  height: 2px;
}

.btn-3 span:hover:before {
  height: 100%;
}

.btn-3 span:hover:after {
  width: 100%;
}

.custom-button {
  border: none;
  /* 移除邊框 */
  background-color: transparent;
  /* 設置背景為透明 */
  padding: 0;
  /* 移除內邊距，使按鈕緊貼內容 */
  cursor: pointer;
  /* 讓鼠標變成手型 */
  outline: none;
  /* 移除點擊時的輪廓效果 */
}

:deep(.not-sale) td {
  background-color: #BEBEBE;
  /* 灰色背景颜色，你可以根据需要修改 */
  /* 其他样式 */
}

.org-price {
  color: gray;
  font-size: 80%;
  text-decoration: line-through;
}
</style>

