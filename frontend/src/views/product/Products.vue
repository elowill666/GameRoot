<template>
  <div class="common-layout">
    <el-container>
      <!-- <el-header>
        <router-link to="/product/productWishlist"><el-button type="primary" size="large" plain class="button"
            v-show="!isAdminLogin">願望清單</el-button></router-link>
            <router-link to="/order/shoppingcart"><button class="custom-btn btn-3" style="float: right;"
            v-show="!isAdminLogin" >
            <span>購物車</span>
          </button></router-link>
      </el-header> -->

      <el-container>
        <el-aside width="340px" style="padding-top: 300px;padding-left: 40px;">
          <ProductSearchBox @searchInput="searchHandler"></ProductSearchBox>
          <ProductAsideNav></ProductAsideNav>
        </el-aside>

        <el-main>
          <el-header style="width: 1200px; height: auto; margin: auto;">
            <ProductAds></ProductAds>
          </el-header>

          <div style="padding-left: 50px;height: 50px;text-align: left;">
            <input class="sortbutton" type="button" id="sortbyreleasedate" @click="sortby('releaseDate')" value="上架排序">
            <input class="sortbutton" type="button" id="sortbyprice" @click="sortby('price')" value="價格排序">
          </div>

          <el-row style="margin: auto; display: flex; justify-content: center;flex-wrap: wrap;"
            class="animate__animated animate__fadeInRight">

            <el-col v-for="(product, index) in pageProducts" :key="product.productId" :span="7"
              :offset="index > 0 ? 0 : 0" style="padding: 30px">
              <el-card :body-style="{ padding: '10px', height: '300px' }"
                style="background-color: #3a556acf;border: 0;color: aliceblue;cursor: pointer;">
                <img :src="product.coverImage" class="image" :alt="product.productName" :style="{ height: '155px' }"
                  @click="SeeProductInfor(product)" />
                <div style="padding: 10px">
                  <div style="text-align: center" @click="SeeProductInfor(product)">
                    <span style="text-align: center; font-size: 1em">{{
                      product.productName.length <= 20 ? product.productName : product.productName.substring(0, 20)
                    }}</span>
                  </div>

                  <div style="text-align: center" v-if="new Date(product.discountStarttime) <= new Date() &&
                    new Date(product.discountEndtime) > new Date(new Date().getTime() + 24 * 60 * 60 * 1000) &&
                    product.discountFactor !== null
                    ">
                    <span style="text-decoration: line-through; padding-right: 20px; font-size: 0.8em;">${{ product.price
                    }}</span><span style="font-size: 1.2em;color: red;">${{
  Math.floor(product.price * (1 - product.discountFactor / 100))
}}</span>
                  </div>
                  <div style="text-align: center" v-else>
                    ${{ product.price }}
                  </div>
                  <!-- 購物車跟願望清單按鈕 -->
                  <div class="bottom">
                    <InsertShoppingcart :productId="product?.productId" :productType="product?.productType"
                      :price="product?.price" :discount="product?.discount" :productStatus="product?.productStatus">
                    </InsertShoppingcart>
                    <InsertWishlist :productId="product?.productId"></InsertWishlist>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <el-footer style="display: flex; justify-content: center;">
            <ProductPagination :totalItem="pagedata.totalItem" :rows="pagedata.rows" @changePage="clickHandler">
            </ProductPagination>
          </el-footer>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import router from "@/router";
import axios from "axios";
import Swal from 'sweetalert2';
import { ref, onMounted } from "vue";
import ProductSearchBox from "./ProductSearchBox.vue";
import InsertWishlist from "./InsertWishlist.vue";
import ProductAsideNav from "./ProductAsideNav.vue";
import ProductAds from "./ProductAds.vue"
import ProductPagination from "./ProductPagination.vue";
import InsertShoppingcart from "./InsertShoppingcart.vue";
import { set } from "date-fns";

// const products = ref({});
//第一次載入用
// const loading = async () => {
//   const VITE_API_GAMESHOP_INITPRODUCT = `${
//     import.meta.env.VITE_JAVA_URL
//   }/gameshop/product`;
//   const response = await axios.get(VITE_API_GAMESHOP_INITPRODUCT);
//   console.log(response.data);
//   Object.assign(products.value, response.data);
//   // console.log(products);
// };
//掛載時載入全部產品資訊
onMounted(async () => {
  loading();
  checkAdminLogin();
});

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

//接收子元件傳送過來的資料
const searchHandler = async (data) => {
  console.log("data = ", data.beans);
  products.value = data.beans;
  pageProducts.value = products.value.slice(0, 9);
  pagedata.value.totalItem = products.value.length;

};

// -----------------我是分隔線----------------------
// 改寫的程式
const products = ref([]);
const loading = async () => {
  const VITE_API_GAMESHOP_INITPRODUCT = `${import.meta.env.VITE_JAVA_URL
    }/gameshop/product`;
  const response = await axios.get(VITE_API_GAMESHOP_INITPRODUCT);
  console.log(response.data);
  products.value = response.data;
  // console.log(products);
  //傳送產品總筆數
  pagedata.value.totalItem = products.value.length;
  //每頁顯示筆數
  pageProducts.value = products.value.slice(startIndex.value, lastIndex.value);
  // console.log("pageProducts",pageProducts);

  for (let temp = 0; temp < products.value.length; temp++) {
    //一定要有await等待findProductImage()找完圖片
    const coverImageUrls = await findProductImage(
      products.value[temp].productId
    );
    products.value[temp].coverImage = coverImageUrls.data;
  }
};

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

//分頁製作
const pagedata = ref({
  totalItem: 0,
  currentPage: 1,
  rows: 9
})

function clickHandler(value) {
  pagedata.value.currentPage = value;
  // console.log("currentPage",pagedata.value.currentPage)
  if (pagedata.value.currentPage !== 1) {
    startIndex.value = (pagedata.value.currentPage - 1) * pagedata.value.rows;
    // console.log("startIndex",startIndex.value)
    lastIndex.value = startIndex.value + pagedata.value.rows;
    // console.log("lastIndex",startIndex.value)
    pageProducts.value = products.value.slice(startIndex.value, lastIndex.value);
    // console.log("pageProducts",pageProducts.value);

  } else {
    pageProducts.value = products.value.slice(0, 9);

  }


}
//擷取每頁頭INDEX到尾INDEX
const pageProducts = ref(products.value.slice(0, 9));
//每頁起始INDEX
const startIndex = ref(0);
//每頁尾端INDEX
const lastIndex = ref(9);


//製作產品排序
const pricesort = ref('none');
const datesort = ref('none');

function sortby(value) {
  const datebutton = document.getElementById('sortbyreleasedate')
  const pricebutton = document.getElementById('sortbyprice')
  if (value === 'releaseDate') {
    products.value.sort((a, b) => {
      const dataA = new Date(a.releaseDate);
      const dataB = new Date(b.releaseDate);
      // console.log("dataA",dataA)
      // console.log("dataB",dataB)
      if (datesort.value === 'none' || datesort.value === 'oldtonew') {
        return dataB - dataA;
      } else if (datesort.value === 'newtoold') {
        return dataA - dataB;
      }

    });
    if (datesort.value === 'none' || datesort.value === 'oldtonew') {
      datebutton.setAttribute('value', "商品新至舊")
      datesort.value = 'newtoold'
      console.log(datesort.value);
    } else if (datesort.value === 'newtoold') {
      datebutton.setAttribute('value', "商品舊至新")
      datesort.value = 'oldtonew'
      console.log(datesort.value);
    }
    pricebutton.setAttribute('value', "價格排序")
    pricesort.value = 'none';
    // products.value.sort((a,b)=>{
    //   const dataA = new Date(a.releaseDate);
    //   const dataB = new Date(b.releaseDate);
    //   return dataB-dataA;
    // });
  } else if (value === 'price') {

    const currentDate = new Date();
    products.value.sort((a, b) => {
      const priceA = (a.discount != null && new Date(a.discountStarttime) <= currentDate && currentDate <= new Date(a.discountEndtime)) ? a.discount : a.price;
      const priceB = (b.discount != null && new Date(b.discountStarttime) <= currentDate && currentDate <= new Date(b.discountEndtime)) ? b.discount : b.price;




      if (pricesort.value === 'none' || pricesort.value === 'lowtohigh') {
        return priceB - priceA;
      } else if (pricesort.value === 'hightolow') {
        return priceA - priceB;
      }
    })
    if (pricesort.value === 'none' || pricesort.value === 'lowtohigh') {
      pricebutton.setAttribute('value', "價格高至低")
      pricesort.value = 'hightolow';
      console.log(pricesort.value);
    } else if (pricesort.value === 'hightolow') {
      pricebutton.setAttribute('value', "價格低至高")
      pricesort.value = 'lowtohigh';
      console.log(pricesort.value);
    }
    datebutton.setAttribute('value', "上架排序")
    datesort.value = 'none'

  }
  clickHandler(1);//回到第一頁
}
//管理者登入
const isAdminLogin = ref(true)
async function checkAdminLogin() {
  try {
    const response = await axios.get(`${import.meta.env.VITE_JAVA_URL}/user/adminAuthenticate`, {
      withCredentials: true,
    });
    console.log(response.data);
    console.log(typeof response.data);
    if (true === response.data) {
      isAdminLogin.value = true
    } else {
      isAdminLogin.value = false
    }
  } catch (error) {
    console.log(error)
  }

}



</script>

<style scoped>
.time {
  font-size: 12px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.button {
  padding: 5px;
  min-height: auto;
}

.image {
  width: 100%;
  display: block;
  border: none;
  object-fit: scale-down;

}

el-container {
  height: auto;
}

.el-card.is-always-shadow {
  box-shadow: 5px 5px 20px 3px #292929;
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
  width: 130px;
  height: 40px;
  line-height: 42px;
  padding: 0;
  border: none;
}

.sortbutton {
  color: white;
  background-color: #0b1722;
  border: 2px #FFD306 solid;
  border-radius: 10px;
  padding: 5px;
  margin: 5px;
}
</style>
