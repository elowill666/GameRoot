<template>
  <div class="myback">
    <div class="animate__animated animate__fadeInDown">
      <el-carousel indicator-position="outside" type="card" height="300px">
        <el-carousel-item v-for="item in items" :key="item">
          <img :src="item" style="object-fit: scale-down; width: 100%; height: 100%" />
        </el-carousel-item>
      </el-carousel>
      <el-container style="; text-align: center;padding: 30px;">
        <div>
        </div>
        <div class="ranking">
          <h1>遊戲熱銷榜</h1>
          <div class="row" >
            <div class="card" style="width: 18rem;height: 16rem ;" v-for="(phot, index) in producthotsale"
              @click="SeeProductInfor(phot[0])">
              <img :src="phot[0]?.coverImage" class="card-img-top" :alt="phot[0]?.productName" :key="index" />
              <div class="card-body">
                <p class="card-text">
                  {{ phot[0]?.productName.length <= 20?phot[0]?.productName : phot[0]?.productName.substring(0, 20) }}
                </p>
                <div style="text-align: center" v-if="new Date(phot[0]?.discountStarttime) <= new Date() &&
                  new Date(phot[0]?.discountEndtime) > new Date(new Date().getTime() + 24 * 60 * 60 * 1000) &&
                  phot[0]?.discountFactor !== null
                  ">
                  <span style="text-decoration: line-through; padding-right: 20px; font-size: 0.8em;">${{ phot[0]?.price
                  }}</span><span style="font-size: 1.2em;color: red;">${{phot[0]?.discount}}</span>
                </div>
                <div style="text-align: center" v-else>
                  ${{ phot[0]?.price }}
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="ranking">
          <h1>新上架遊戲</h1>
          <div class="row" >
            <div class="card" style="width: 18rem;height: 16rem ;" v-for="(pnew, index) in productnewrelease" :key="index"
              @click="SeeProductInfor(pnew[0])">
              <img :src="pnew[0]?.coverImage" class="card-img-top" :alt="pnew[0]?.productName" />
              <div class="card-body">
                <p class="card-text">
                  {{ pnew[0]?.productName.length <= 20 ? pnew[0]?.productName : pnew[0]?.productName.substring(0, 20) }}
                </p>
                <div style="text-align: center" v-if="new Date(pnew[0]?.discountStarttime) <= new Date() &&
                  new Date(pnew[0]?.discountEndtime) > new Date(new Date().getTime() + 24 * 60 * 60 * 1000) &&
                  pnew[0]?.discountFactor !== null
                  ">
                  <span style="text-decoration: line-through; padding-right: 20px; font-size: 0.8em;">${{ pnew[0]?.price
                  }}</span><span style="font-size: 1.2em;color: red;">${{pnew[0]?.discount}}</span>
                </div>
                <div style="text-align: center" v-else>
                  ${{ pnew[0]?.price }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-container>
      <div style="text-align: center;">
      <div  class="img-container" v-for="(game,index) in games" @click="pushToPage(game.text)">
            <img :src="game.image" :alt="game.text"  :key="index" class="imgtotype">
            <div class="caption">{{ game.text }}</div>
      </div>
    </div>
    </div>
  </div>
</template>

<script setup>
import axios from "axios";
import { ref, onMounted, nextTick } from "vue";
import router from "@/router";

const items = ref([
  "https://cdn.cloudflare.steamstatic.com/steam/apps/1623730/header.jpg?t=1705662211",
  "https://cdn.cloudflare.steamstatic.com/steam/apps/1172470/header.jpg?t=1702571386",
  "https://cdn.cloudflare.steamstatic.com/steam/apps/414340/header.jpg?t=1702318087",
  "https://cdn.cloudflare.steamstatic.com/steam/apps/230410/header.jpg?t=1705509351",
  "https://cdn.cloudflare.steamstatic.com/steam/apps/1086940/header.jpg?t=1705604554",
]);

const games = ref([
  {text:"冒險",image:"https://cdn.akamai.steamstatic.com/steam/spotlights/b4d1e3612e9417c6f9ec94f2/spotlight_image_english.jpg?t=1705789537"},
  {text:"動作",image:"https://cdn.akamai.steamstatic.com/steam/apps/578080/header.jpg?t=1701911655"},
  {text:"模擬",image:"https://cdn.akamai.steamstatic.com/steam/apps/1462040/extras/about01_616x347.jpg?t=1696383548"},
  {text:"競賽",image:"https://cdn.akamai.steamstatic.com/steam/apps/2108330/hero_capsule.jpg?t=1702567747"},
  {text:"其他",image:"https://cdn.akamai.steamstatic.com/steam/apps/813780/extras/plymouth-key-art-logo-main-capsule-616x35.png?t=1702497119"}
])

const products = ref([]);
const producthotsale = ref([]);
const productnewrelease = ref([]);
onMounted(async () => {
  const VITE_API_GAMESHOP_PRODUCTWITHMONTHLYSALES = `${import.meta.env.VITE_JAVA_URL
    }/gameshop/product/monthlysales`
  const VITE_API_PRODUCTCOVERIMAGE = `${import.meta.env.VITE_JAVA_URL
    }/dashboard/productimage/`
  const response = await axios.get(VITE_API_GAMESHOP_PRODUCTWITHMONTHLYSALES);

  products.value = response.data
  // console.log(products.value[0][0].productId);
  
  console.log("product = ", products.value);
  if (products.value != null) {
    products.value.sort((a, b) => {
      const saleA = a[1];
      const saleB = b[1];
      // console.log("saleA",saleA);
      // console.log("saleB",saleB);
      return saleB - saleA;
    })
    producthotsale.value = products.value.slice(0, 3);
    // console.log("producthotsale = ", producthotsale.value);
    for (let i = 0; i < producthotsale.value.length; i++) {
    const getImg = await axios.post(VITE_API_PRODUCTCOVERIMAGE + producthotsale.value[i][0].productId);
    producthotsale.value[i][0].coverImage = getImg.data;
  }
  }
  if (products.value != null) {
    products.value.sort((a, b) => {
      const newA = new Date(a[0].releaseDate);
      const newB = new Date(b[0].releaseDate);
      // console.log("saleA",saleA);
      // console.log("saleB",saleB);
      return newB - newA;
    })
    productnewrelease.value = products.value.slice(0, 3);
    // console.log("productnewrelease = ", productnewrelease.value);
    for (let i = 0; i < productnewrelease.value.length; i++) {
    const getImg = await axios.post(VITE_API_PRODUCTCOVERIMAGE + productnewrelease.value[i][0].productId);
    productnewrelease.value[i][0].coverImage = getImg.data;
  }
  }
})
//點選進入產品詳細內容頁
const SeeProductInfor = async (product) => {
  console.log(product.productId);

  router.push({
    name: "ProductDetail",
    params: {
      id: product.productId,
    },
  });
};
const subType = ref(null);
function pushToPage(value){
  if(value==='動作'){
    subType.value = 'action';
  } else if(value==='冒險'){
    subType.value = 'adventure';
  } else if(value==='模擬'){
    subType.value = 'simulation';
  } else if(value==='競賽'){
    subType.value = 'racing';
  } else if(value==='其他'){
    subType.value = 'others';
  }
  console.log("subtype = ",subType.value);
    router.push({
      name:"ProductsFindSubType",
      params:{
        subtype:subType.value
      },
    })
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

.el-carousel,
.el-carousel--horizontal {
  width: 1350px;
  margin: auto !important;
}

.el-carousel__item,
.is-active,
.is-in-stage,
.el-carousel__item--card :deep(.el-carousel__mask) {
  height: auto;
}

.ranking {
  /* background-color: #d0d0d0; */
  width: 50%;
  height: 400px;
  border-radius: 10px;
  margin: 10px;
  padding: 20px;
  text-align: center;
  /* border: 2px solid #FFD306; */
  
}



.card {
  background-color: #3a556acf;
  padding: 10px;
  margin: 5px;
  cursor: pointer;
  color: white;
}

.card-img-top {
  height: 125px;
  object-fit: contain;
}
.imgtotype{
  width:250px;
  height:250px;
  object-fit: cover;
  margin: 20px;
}
.img-container{
  position: relative;
  display: inline-block;
  margin: 10px;
  cursor: pointer;
}
.caption{
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  padding: 10px;
  box-sizing: border-box;
  
}
</style>
