<template>
  <!-- <h5>hihi</h5> -->
  <div style="text-align: center;">
    <!-- v-if是避免載入時圖片還沒載入造成網頁掛掉 -->
    <el-image style="width: 540px; height: 320px; background-color: none;border:none;object-fit: scale-down;"
      :src="mainProductImage" :alt="mainProductImageAlt" fit="contain" />
    <br>
    <el-image v-for="images in productImg"
      style="width: 80px; height: 50px; border:none; background-color:none; margin:5px;object-fit: scale-down;cursor: pointer;"
      :src="images?.productImage" :alt="images?.productImageId"
      @click="changeMainImg(images?.productImage, images?.productImageId)" />
  </div>
</template>
    
<script setup>
import axios from "axios";
import { ref, onMounted, watchEffect } from "vue"

const prop = defineProps(["productImg", "productId"]);
const mainProductImage = ref('');
const mainProductImageAlt = ref('');

watchEffect(async () => {
  if (prop.productImg && Array.isArray(prop.productImg) && prop.productImg.length > 0) {
    mainProductImage.value = prop.productImg[0].productImage;
    // console.log(2222222222222, prop.productImg[0].productImage);
    mainProductImageAlt.value = prop.productImg[0].productId;
  }
})

function changeMainImg(productImage, productImageId) {
  mainProductImage.value = productImage;
  mainProductImageAlt.value = productImageId;
}
</script>
    
<style scoped></style>