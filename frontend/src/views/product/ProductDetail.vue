<template>
  <div class="back">
    <DashboardRouterlink v-show="isFromDashboard"></DashboardRouterlink>
    <el-container style="padding-left: 40px">
      <el-aside v-show="!isFromDashboard" width="300px" style="padding-top: 100px">
        <ProductAsideNav></ProductAsideNav>
      </el-aside>

      <el-main>
        <table>
          <el-container style="text-align: center">
            <el-aside width="700px" style="padding-top: 20px">
              <!-- 產品照片 -->
              <ProductImage :productImg="product?.productImages" :productId="product?.product?.productId"></ProductImage>
              <!-- 商家測評及留言評論 -->
              <div>
                <div v-if="product &&
                  product.gameInfor &&
                  product.gameInfor.length !== 0
                  ">
                  商家評論：{{
                    product && product.gameInfor && product.gameInfor[0]?.rating
                  }}
                </div>
                <div>
                  客戶購買評論(共{{ product?.productComments?.length }}筆)：
                  <el-rate v-model="productCommentRate" disabled show-score text-color="#ff9900"
                    score-template="{value} 顆星" />
                </div>
              </div>
            </el-aside>
            <el-main style="padding-top: 20px">
              <div style="text-align: center">
                <!-- 產品封面照片 -->
                <el-image style="
                    /* width: 600px; */
                    height: 320px;
                    border:none;
                    background-color:none;
                    object-fit: scale-down;
                  " :src="img" />
                <!-- 產品介紹 -->
                <div class="outlined">
                  <div style="font-size: 2em; padding-top: 20px">
                    {{ product?.product?.productName }}
                  </div>
                  <hr />
                  <div style="font-size: 1em">
                    {{ product?.product?.outlined }}
                  </div>
                  <!-- 打折時的價格狀況 -->
                  <div style="padding: 20px" v-if="new Date(product?.product?.discountStarttime) <=
                    new Date() &&
                    new Date(product?.product?.discountEndtime) >
                    new Date() &&
                    product.discountFactor !== null
                    ">
                    <el-text tag="del" style="padding: 10px; font-size: 1em">原價：${{ product?.product?.price }}</el-text>
                    <el-text tag="ins" style="font-size: 1.5em; color: #ff2d2d">
                      特價：${{
                        Math.floor(
                          product?.product?.price *
                          (1 - product?.product?.discountFactor / 100)
                        )
                      }}</el-text>

                    <el-text tag="sub" style="color: #ff2d2d">({{ product?.product?.discountFactor }}%off)</el-text>
                  </div>
                  <!-- 沒打折時的狀況 -->
                  <div style="padding: 20px" v-else>
                    <el-text tag="ins" style="padding: 10px; font-size: 1.5em; color: white">原價：${{
                      product?.product?.price }}</el-text>
                  </div>

                  <!--  周邊才有的欄位 -->
                  <div v-if="product?.merchandises?.length !== 0">
                    <li style="font-size: 1.2em; padding-bottom: 10px">
                      <span>
                        顏色：
                        <el-button size="large" v-for="merchandises in product?.merchandises" type="primary" disabled>{{
                          merchandises.color }}</el-button>
                      </span>
                      <span style="padding-left: 20px">
                        尺寸：
                        <el-button size="large" v-for="merchandises in product?.merchandises" type="primary" disabled>{{
                          merchandises.size }}</el-button>
                      </span>
                    </li>
                    <!-- <li style="font-size: 1.2em; padding-bottom: 10px">
                    數量：
                    <el-input-number
                      v-model="num"
                      :min="1"
                      :max="product?.merchandises?.iventory"
                    />
                  </li> -->
                  </div>
                  <div>
                    <!-- 購物車跟願望清單按鈕 -->
                    <span style="padding-right: 30px">
                      <InsertShoppingcart :productId="product?.product?.productId"
                        :productType="product?.product?.productType" :price="product?.product?.price"
                        :discount="product?.product?.discount" :productStatus="product?.product?.productStatus">
                      </InsertShoppingcart>
                    </span>
                    <span style="padding-left: 30px">
                      <InsertWishlist :productId="product?.product?.productId"></InsertWishlist>
                    </span>
                  </div>
                </div>
              </div>
            </el-main>
          </el-container>

          <!-- 產品標籤頁面 -->
          <el-tabs type="border-card" class="demo-tabs">
            <el-tab-pane label="產品介紹">
              <ProductInterview :productInter="{
                product: product?.product,
                descriptionImages: product?.descriptionImages,
              }"></ProductInterview>
            </el-tab-pane>
            <el-tab-pane label="客戶評論">
              <ProductComment :productCom="product?.productComments"></ProductComment>
            </el-tab-pane>
            <el-tab-pane label="相關文章">
              <ProductArticle :productArt="product?.productArticles"></ProductArticle>
            </el-tab-pane>
            <!-- 確保在 product 完全準備好之前，不要嘗試訪問它的屬性 -->
            <el-tab-pane label="遊戲規格" v-if="product && product.gameInfor && product.gameInfor.length !== 0
              ">
              <GameDemands :gameInfor="product?.gameInfor"></GameDemands>
            </el-tab-pane>
          </el-tabs>
        </table>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref } from "vue";
import axios from "axios";
import { useRoute } from "vue-router";
import { RouterLink, RouterView } from "vue-router";

import ProductInterview from "./ProductInterview.vue";
import ProductComment from "./ProductComment.vue";
import ProductArticle from "./ProductArticle.vue";
import GameDemands from "./GameDemands.vue";
import InsertWishlist from "./InsertWishlist.vue";
import ProductImage from "./ProductImage.vue";
import ProductAsideNav from "./ProductAsideNav.vue";
import ProductSearchBox from "./ProductSearchBox.vue";
import InsertShoppingcart from "./InsertShoppingcart.vue";
import { constructFrom } from "date-fns";
import DashboardRouterlink from "../dashboard/DashboardRouterlink.vue";

const product = ref({});
const productCommentRate = ref(0);
const img = ref();
const num = ref(1);
const route = useRoute();

//
const isFromDashboard = ref(false);

const loadingDetail = async () => {
  if (route.params.from) {
    isFromDashboard.value = true;
  }

  const id = route.params.id;
  const VITE_API_GAMESHOP_PRODUCTDETAIL = `${import.meta.env.VITE_JAVA_URL
    }/gameshop/product/detail/`;
  // console.log(VITE_API_GAMESHOP_PRODUCTDETAIL + id);
  const response = await axios.get(VITE_API_GAMESHOP_PRODUCTDETAIL + id);
  console.log(response.data);
  Object.assign(product.value, response.data);
  //計算客戶留言評分
  for (let i = 0; i < product.value.productComments.length; i++) {
    productCommentRate.value += product.value.productComments[i][0].score;
    // console.log("productCommentRate = ", product.value.productComments[i][0].score);
    // console.log("final productCommentRate = ",productCommentRate.value )
  }
  if (productCommentRate.value !== 0) {
    productCommentRate.value = parseFloat(
      (productCommentRate.value / product.value.productComments.length).toFixed(
        1
      )
    );
  }
  // console.log(productCommentRate.value)
  // console.log(product.value.productComments.length)

  //另外載入圖片
  const VITE_API_PRODUCTCOVERIMAGE = `${import.meta.env.VITE_JAVA_URL
    }/dashboard/productimage/${id}`;
  const getimg = await axios.post(VITE_API_PRODUCTCOVERIMAGE);
  // console.log("product = ", product.value);
  img.value = getimg.data;
  // console.log("img=", img);
};
onMounted(async () => {
  loadingDetail();
});

// async function findProductImage(productId) {
//   const VITE_API_PRODUCTCOVERIMAGE = `${
//     import.meta.env.VITE_JAVA_URL
//   }/dashboard/productimage/${productId}`;
//   try {
//     const response = await axios.post(VITE_API_PRODUCTCOVERIMAGE);
//     return response;
//   } catch (error) {
//     console.log(error);
//   }
// }
// onUnmounted(
//   async () => {
//   console.log(product.value.gameInfor)
//   }
// )
</script>

<style scoped>
.back {
  background: url("../../../public/images/homeimg.jpg") center/cover fixed;
  margin: 0;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.2);
  background-blend-mode: multiply;
}

table {
  width: 1400px;
  height: 400px;
  padding-right: 0px;
  background-color: #141f2adf;
  box-shadow: 0px 0px 10px 3px #171717;
}

li {
  font-size: 1.2em;
  padding-bottom: 10px;
  list-style-type: none;
}

.el-tab-pane {
  background-color: none;
  /* padding: 20px; */
}

.demo-tabs {
  width: 1400px;
  height: auto;
  border: 0px grey solid;
  background-color: white

}

.outlined {
  width: 600px;
  margin: auto;
}




</style>
<style lang="scss">

.el-tabs--border-card >.el-tabs__header .el-tabs__item.is-active{
  color: #ffdc42 !important;
}
.el-tabs__item:hover {
  color: #ffdc42 !important;
}
</style>
