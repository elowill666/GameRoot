<template>
  <div class="searchbox">
    遊戲名稱
    <el-input v-model="searchData.keyword" class="w-75 m-2" placeholder="搜尋想要的遊戲" />
    <br>
    金額區間
    <el-input v-model="searchData.maxPrice" class="w-75 m-2" placeholder="最大金額" />
    <br>
    <el-input v-model="searchData.minPrice" class="w-75 m-2" placeholder="最小金額" />
    <br>
    商品種類
    <el-select v-model="searchData.productType" class="m-2" placeholder="請選擇商品種類" size="large" style="width: 210px">
      <el-option v-for="mainType in Object.keys(productMainAndSubType)" :key="mainType"
        :label="productMainAndSubType[mainType].name" :value="mainType" />
    </el-select>

    <div v-if="searchData.productType">
      請選擇次分類
      <el-select v-model="searchData.productSubtype" class="m-2" placeholder="請選擇分類" size="large" style="width: 210px">
        <el-option v-for="subType in productMainAndSubType[searchData.productType].subtype" :key="subType"
          :label="Object.values(subType)[0]" :value="Object.keys(subType)[0]" />
      </el-select>

    </div>
    <el-button color="#FFD306" @click="searchHandler"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"
        fill="currentColor" class="bi bi-search" viewBox="0 0 16 16">
        <path
          d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001q.044.06.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1 1 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0" />
      </svg>Search</el-button>

  </div>
</template>
    
<script setup>

import axios from 'axios';
import { ref } from 'vue';

let products = ref([])

//定義要傳送至父元件的位置
const emit = defineEmits(["searchInput"])

const searchData = ref({
  "keyword": "",
  "productType": "",
  "productSubtype": "",
  "maxPrice": null,
  "minPrice": null,
})

const searchHandler = async () => {
  // console.log(searchData);

  console.log("searchData = ", searchData)
  const VITE_API_GAMESHOP_SEARCHPRODUCT = `${import.meta.env.VITE_JAVA_URL
    }/gameshop/product/find`;
  const response = await axios.post(VITE_API_GAMESHOP_SEARCHPRODUCT, searchData.value)
  products.value = response.data;
  console.log("ppppp = ", products.value.beans);
  for (let i = 0; i < products.value.beans.length; i++) {
    findProductImage(products.value.beans[i].productId).then(
      (response) => {
        products.value.beans[i].coverImage = response.data
      }
    );
  }
  emit("searchInput", response.data);
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

//下拉式選單資料
const productMainAndSubType = {
  "digitalGames": {
    name: "虛擬遊戲",
    subtype: [
      { "action": "動作" },
      { "simulation": "模擬" },
      { "adventure": "冒險" },
      { "racing": "競賽" },
      { "others": "其他" }
    ]
  },
  "physical": {
    name: "實體周邊",
    subtype: [
      { "Pokemon": "寶可夢" },
      { "Mario": "馬力歐" },
      { "Zelda": "薩爾達傳說" },
      { "others": "其他" }
    ]
  }

}



</script>
    
<style scoped>
.searchbox {
  background-color: #0b1722;
  padding: 10px;
  border-radius: 10px;
  border: 2px #ffdc42 solid;
  text-align: center;
}
</style>