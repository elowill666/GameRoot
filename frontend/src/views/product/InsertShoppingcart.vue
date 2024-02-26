<!-- 複製orderthree.vue -->
<template>
  <span v-if="prop.productType === 'cash'">
    <el-icon class="cash" size="40" @click="gopay(prop.productId, prop.discount, prop.price, prop.productStatus)"
      style="cursor: pointer;">
      <Money :style="{ color: fillColor }" />
    </el-icon>
  </span>
  <svg v-else xmlns="http://www.w3.org/2000/svg" width="40" height="40" fill="currentColor" class="bi bi-cart-plus"
    viewBox="0 0 16 16" @click="doclick(prop.productId)" style="cursor: pointer;" :style="{ color: fillColor }">
    <path d="M9 5.5a.5.5 0 0 0-1 0V7H6.5a.5.5 0 0 0 0 1H8v1.5a.5.5 0 0 0 1 0V8h1.5a.5.5 0 0 0 0-1H9z" />
    <path
      d="M.5 1a.5.5 0 0 0 0 1h1.11l.401 1.607 1.498 7.985A.5.5 0 0 0 4 12h1a2 2 0 1 0 0 4 2 2 0 0 0 0-4h7a2 2 0 1 0 0 4 2 2 0 0 0 0-4h1a.5.5 0 0 0 .491-.408l1.5-8A.5.5 0 0 0 14.5 3H2.89l-.405-1.621A.5.5 0 0 0 2 1zm3.915 10L3.102 4h10.796l-1.313 7zM6 14a1 1 0 1 1-2 0 1 1 0 0 1 2 0m7 0a1 1 0 1 1-2 0 1 1 0 0 1 2 0" />
  </svg>
</template>

<script setup>
import axios from "axios";
import Swal from "sweetalert2";
import { useRouter } from "vue-router";
import { ref } from 'vue';

const prop = defineProps(["productId", "productType", "price", "discount", "productStatus"]);
const router = useRouter();
const productId = ref();

function doclick(pId) {

  productId.value = pId
  const API_URL = `${import.meta.env.VITE_JAVA_URL}`;
  axios
    .get(`${API_URL}/user/authenticate`, { withCredentials: true })
    .then((res) => {
      if (res.data == false) {
        Swal.fire({
          title: "Error!",
          text: "請先登入",
          icon: "error",
          confirmButtonText: "登入去",
        }).then((result) => {
          if (result.isConfirmed) {
            router.push("/user/userlogin");
            return;
          }
        });
      } else {
        // console.log('productId = ',productId.value)
        axios.get(API_URL + "/shoppingcart/addtocart/" + productId.value, {
          withCredentials: true,
        });
        Swal.fire({
          icon: "success",
          text: "成功加入購物車",
          allowOutsideClick: false,
          confirmButtonText: "確定",
        }).then(function (result) { });
      }
    })
    .catch((error) => {
      console.error(error);
    });
}

let discountprice = ref(0);
function gopay(pId, pDiscount, pPrice, pProductStatus) {
  const API_URL = `${import.meta.env.VITE_JAVA_URL}`;
  axios
    .get(`${API_URL}/user/authenticate`, { withCredentials: true })
    .then((res) => {
      if (res.data == false) {
        Swal.fire({
          title: "Error!",
          text: "請先登入",
          icon: "error",
          confirmButtonText: "登入去",
        }).then((result) => {
          if (result.isConfirmed) {
            router.push("/user/userlogin");
            return;
          }
        });
      } else {
        const API_URL = `${import.meta.env.VITE_JAVA_URL}/cash/getcash/`
        if (pProductStatus === 'discount') {
          discountprice.value = pDiscount;
        } else { discountprice.value = 0 }
        axios.get(API_URL + pPrice + "/" + pId + "/" + discountprice.value, { withCredentials: true }).then(function (response) {
          const div = document.createElement("div");
          div.innerHTML = response.data; // html code
          const newTab = window.open();
          newTab.document.body.appendChild(div);
          newTab.document.forms.allPayAPIForm.submit();
          setTimeout(() => {
            Swal.fire({
              icon: "success",
              text: "儲值成功",
              allowOutsideClick: false,
              showCancelButton: false,
              showDenyButton: true,
              confirmButtonText: "前往商店",
            }).then(function (result) {
              if (result.isConfirmed) {
                window.location.reload()
                window.location.href = '/product/products'
              } else {
                location.reload()
              }
            })
          }, 5000);
        })
      }

    })
}
</script>

<style scoped>
.bi-cart-plus:hover {
  color: #FFD306;
  /* 在鼠标悬停时填充颜色为黄色 */
}

.cash:hover {
  color: #FFD306;
}
</style>
