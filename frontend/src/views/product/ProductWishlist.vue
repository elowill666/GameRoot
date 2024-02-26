<template>
  <div class="myback">
    <br>
    <div class="table-container">
      <table class="table table-hover" style="width: 700px">
        <thead>
          <tr>
            <th scope="col"></th>
            <th scope="col">商品名稱</th>
            <th scope="col">價格</th>
            <th scope="col">加入時間</th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody class="table-group-divider">
          <tr v-for="{ productName, createAt, price, productId, coverImage } in Wishlist" :key="Wishlist.wishlistId">
            <td><img class="image" :src="coverImage" :alt="productName" :style="{ height: '155px' }"
                @click="SeeProductInfor(productId)"></td>
            <td style="width: 200px; vertical-align: middle; font-size: 19px;
              font-weight: bold" @click="SeeProductInfor(productId)">{{ productName }}</td>
            <td style="width: 70px; vertical-align: middle; font-size: 18px;
              font-weight: bold">{{ "$" + price }}</td>
            <td style="width: 150px; vertical-align: middle">{{ formatDate(createAt) }}</td>
            <td style="width: 70px; vertical-align: middle">
              <InsertWishlist :productId="productId"></InsertWishlist>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import axios from 'axios';
import { onMounted, ref } from 'vue';
import router from "@/router";
import InsertWishlist from "./InsertWishlist.vue";
import { useRouter } from "vue-router";

let Wishlist = ref([]);

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


onMounted(async () => {
  const API_URL = `${import.meta.env.VITE_JAVA_URL}/wishlist/findWishlistById`;
  let response = await axios.get(API_URL, { withCredentials: true });
  Wishlist.value = response.data;
  for (let temp = 0; temp < Wishlist.value.length; temp++) {
    //一定要有await等待findProductImage()找完圖片
    const coverImageUrls = await findProductImage(
      Wishlist.value[temp].productId
    );
    Wishlist.value[temp].coverImage = coverImageUrls.data;
  }
  try {
    const response = await axios.post(VITE_API_PRODUCTCOVERIMAGE);
    return response;
  } catch (error) {
    console.error(error);
  }
});

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





const formatDate = (value) => {
  const date = new Date(value);
  const formattedDate = `${date.getFullYear()}/${padZero(date.getMonth() + 1)}/${padZero(date.getDate())} ${padZero(date.getHours())}:${padZero(date.getMinutes())}`;
  return formattedDate;
};

const padZero = (num) => {
  return num.toString().padStart(2, '0');
};
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
  align-items: center;
  /* height: 100vh; 这会使表格在垂直方向上居中，视口的100%高度 */
}

.image {
  width: 100%;
  display: block;
  border: 1px #bebebe solid;
}
</style>