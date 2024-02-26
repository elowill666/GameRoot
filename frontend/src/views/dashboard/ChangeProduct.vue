<template>
  <div style="display: flex" class="layout">
    <DashboardRouterlink></DashboardRouterlink>
    <main style="width: 100%; height: 100%; margin-left: 50px; margin-bottom: 30px">
      <div class="table-container" style="margin-top: 30px">
        <form>
          <table style="background-color: #141f2a">
            <thead>
              <tr>
                <td class="tabletitle" colspan="3">商品上架</td>
                <td style="background-color: #ffd306; color: black">
                  上架日期：{{
                    product.product.releaseDate
                    ? product.product.releaseDate.substring(0, 10)
                    : null
                  }}
                </td>
              </tr>
            </thead>
            <tbody>
              <!-- table Product -->
              <tr>
                <td class="required">商品名稱</td>
                <td>
                  <input class="form-control" type="text" id="productName" v-model="product.product.productName"
                    name="productName" placeholder="請輸入產品名稱" required />
                </td>
                <td>商品版本</td>
                <td>
                  <input class="form-control" type="text" id="spec" v-model="product.product.spec" name="spec"
                    placeholder="請輸入商品版本" />
                </td>
              </tr>
              <tr>
                <td class="required">封面照(擇一)</td>
                <td>
                  <input v-model="coverImageSelect" class="form-check-input" type="radio" name="imgurl" id="imgurl"
                    value="imgurl" @change="changeUploadImgMethod" required />
                  <label class="form-check-label" for="flexRadioDefault1">
                    網址(建議)
                  </label>
                  <br />
                  <input v-model="coverImageSelect" class="form-check-input" type="radio" name="imgfile" id="imgfile"
                    value="imgfile" @change="changeUploadImgMethod" />
                  <label class="form-check-label" for="flexRadioDefault2">
                    檔案(限jpg或png)
                  </label>
                </td>
                <td>請輸入</td>
                <td>
                  <div v-if="coverImageSelect === 'imgurl'">
                    <input class="form-control" type="text" id="inputimgurl" v-model="product.product.coverImage"
                      name="coverImage" placeholder="https://" @blur="checkimgurl('coverImage')" />
                  </div>
                  <div v-if="coverImageSelect === 'imgfile'">
                    <input class="form-control" type="file" id="inputimgfile" @input="changeBase64('inputimgfile')"
                      name="coverImage" />
                  </div>
                </td>
              </tr>
              <!-- 其他照片 -->
              <tr>
                <td rowspan="5">其他照片(請依照順序)</td>
                <td>1</td>
                <td colspan="2">
                  <div v-if="coverImageSelect === 'imgurl'">
                    <input class="form-control" type="text" id="productimgurl1"
                      v-model="product.productImages[0].productImage" placeholder="https://"
                      @blur="checkimgurl('productimgurl1')" />
                  </div>
                  <div v-if="coverImageSelect === 'imgfile'">
                    <input class="form-control" type="file" id="productimgfile1"
                      @input="changeBase64('productimgfile1')" />
                  </div>
                </td>
              </tr>
              <tr>
                <td>2</td>
                <td colspan="2">
                  <div v-if="coverImageSelect === 'imgurl'">
                    <input class="form-control" type="text" id="productimgurl2"
                      v-model="product.productImages[1].productImage" placeholder="https://"
                      @blur="checkimgurl('productimgurl2')" />
                  </div>
                  <div v-if="coverImageSelect === 'imgfile'">
                    <input class="form-control" type="file" id="productimgfile2"
                      @input="changeBase64('productimgfile2')" />
                  </div>
                </td>
              </tr>
              <tr>
                <td>3</td>
                <td colspan="2">
                  <div v-if="coverImageSelect === 'imgurl'">
                    <input class="form-control" type="text" id="productimgurl3"
                      v-model="product.productImages[2].productImage" placeholder="https://"
                      @blur="checkimgurl('productimgurl3')" />
                  </div>
                  <div v-if="coverImageSelect === 'imgfile'">
                    <input class="form-control" type="file" id="productimgfile3"
                      @input="changeBase64('productimgfile3')" />
                  </div>
                </td>
              </tr>
              <tr>
                <td>4</td>
                <td colspan="2">
                  <div v-if="coverImageSelect === 'imgurl'">
                    <input class="form-control" type="text" id="productimgurl4"
                      v-model="product.productImages[3].productImage" name="coverImage04" placeholder="https://"
                      @blur="checkimgurl('productimgurl4')" />
                  </div>
                  <div v-if="coverImageSelect === 'imgfile'">
                    <input class="form-control" type="file" id="productimgfile4"
                      @input="changeBase64('productimgfile4')" />
                  </div>
                </td>
              </tr>
              <tr>
                <td>5</td>
                <td colspan="2">
                  <div v-if="coverImageSelect === 'imgurl'">
                    <input class="form-control" type="text" id="productimgurl5"
                      v-model="product.productImages[4].productImage" placeholder="https://"
                      @blur="checkimgurl('productimgurl5')" />
                  </div>
                  <div v-if="coverImageSelect === 'imgfile'">
                    <input class="form-control" type="file" id="productimgfile5" @input="changeBase64('productimgfile5')"
                      name="coverImage" />
                  </div>
                </td>
              </tr>

              <tr>
                <td class="required">商品大綱</td>
                <td colspan="3">
                  <textarea class="form-control" id="outlined" v-model="product.product.outlined" name="outlined"
                    placeholder="請輸入商品大綱(限50字)" rows="3" maxlength="50" required></textarea>
                </td>
              </tr>
              <tr>
                <td class="required">商品描述</td>
                <td colspan="3">
                  <main id="sample">
                    <Editor :init="tinyMCEConfig" v-model="product.product.description" />
                  </main>
                </td>
              </tr>
              <tr>
                <td class="required">商品價格</td>
                <td>
                  <input class="form-control" type="number" id="price" v-model="product.product.price" name="price"
                    placeholder="請輸入商品價格" min="1" @input="checkPriceValue" required />
                </td>
                <td class="required">供應商</td>
                <td>
                  <input class="form-control" type="text" id="supplier" v-model="product.product.supplier" name="supplier"
                    placeholder="請輸入商品供應商" required />
                </td>
              </tr>
              <tr>
                <td class="required">商品主分類</td>
                <td>
                  <select v-model="product.product.productType" class="form-select" style="width: 210px"
                    aria-label="Default select example" @change="changeRequired(product?.product?.productType)" required>
                    <option selected value="">請選擇</option>
                    <option v-for="mainType in Object.keys(productMainAndSubType)" :key="mainType" :value="mainType">
                      {{ productMainAndSubType[mainType].name }}
                    </option>
                  </select>
                </td>
                <td class="required">商品次分類</td>
                <td>
                  <select v-model="product.product.productSubtype" class="form-select" style="width: 210px"
                    aria-label="Default select example" required>
                    <option selected value="">請選擇</option>
                    <option v-if="product.product.productType" v-for="subtype in productMainAndSubType[
                      product.product.productType
                    ].subtype" :key="subtype" :value="Object.keys(subtype)[0]">
                      {{ Object.values(subtype)[0] }}
                    </option>
                  </select>
                </td>
              </tr>
              <tr>
                <td class="tabletitle" colspan="4">設定折扣狀態</td>
              </tr>
              <tr>
                <td>折扣趴數(%off)</td>
                <td>
                  <input class="form-control" type="number" id="discountfactor" v-model="product.product.discountFactor"
                    name="discountfactor" placeholder="請輸入折扣趴數(??%off)" min="0" max="100" step="1"
                    @blur="checkDiscountFactorValue" required />
                </td>
                <td>折扣後價格(自動計算)</td>
                <td>{{ product.product.discount }}</td>
              </tr>
              <tr>
                <td>折扣起訖時間</td>
                <td colspan="3">
                  <el-date-picker v-model="discountdate" type="daterange" range-separator="To"
                    start-placeholder="Start date" end-placeholder="End date" size="large" @change="changeDiscountDate" />
                </td>
              </tr>
            </tbody>
          </table>
          <!-- table gameInfor -->
          <table v-if="product.product.productType === 'digitalGames'" style="background-color: #141f2a" id="dtable">
            <tbody>
              <tr>
                <td class="tabletitle" colspan="4">遊戲最低需求</td>
              </tr>
              <tr>
                <td class="required">電腦系統</td>
                <td>
                  <select v-model="product.gameInfor.os" class="form-select" style="width: 210px"
                    aria-label="Default select example" name="os">
                    <option selected value="">請選擇</option>
                    <option v-for="os in computeros['windows']" :key="os" :value="os">
                      {{ os }}
                    </option>
                  </select>
                </td>
                <td class="required">記憶體</td>
                <td>
                  <select v-model="product.gameInfor.memory" class="form-select" style="width: 210px"
                    aria-label="Default select example" name="memory">
                    <option selected value="">請選擇</option>
                    <option v-for="memory in computermemory['memory']" :key="memory" :value="memory">
                      {{ memory }}
                    </option>
                  </select>
                </td>
              </tr>
              <tr>
                <td class="required">顯卡</td>
                <td>
                  <select v-model="product.gameInfor.graphics" class="form-select" style="width: 210px"
                    aria-label="Default select example" name="graphics">
                    <option selected value="">請選擇</option>
                    <option v-for="graphics in computergraphics['graphics']" :key="graphics" :value="graphics">
                      {{ graphics }}
                    </option>
                  </select>
                </td>
                <td class="required">佔用空間</td>
                <td>
                  <input class="form-control" type="text" id="storage" v-model="product.gameInfor.storage" name="storage"
                    placeholder="ex: 30GB" />
                </td>
              </tr>
              <tr>
                <td class="required">處理器</td>
                <td>
                  <select v-model="product.gameInfor.processor" class="form-select" style="width: 210px"
                    aria-label="Default select example" name="processor">
                    <option selected value="">請選擇</option>
                    <option v-for="processor in computerprocessor['processor']" :key="processor" :value="processor">
                      {{ processor }}
                    </option>
                  </select>
                </td>
              </tr>
              <tr>
                <td class="tabletitle" colspan="4">商家實測評分</td>
              </tr>
              <tr>
                <td>評分</td>
                <td>
                  <input class="form-control" type="number" id="rating" v-model="product.gameInfor.rating" name="rating"
                    placeholder="請輸入商品評分" min="0" max="10" step="0.1" @blur="checkRatingValue" required />
                </td>
              </tr>
            </tbody>
          </table>
          <!-- table merchandise -->
          <table v-if="product.product.productType === 'physical'" id="mtable" style="background-color: #141f2a">
            <Tbody>
              <tr>
                <td class="tabletitle" colspan="4">產品規格</td>
              </tr>
              <tr>
                <td class="required">商品數量</td>
                <td>
                  <input class="form-control" type="number" id="inventoryQuantity"
                    v-model="product.merchandise.inventoryQuantity" name="price" placeholder="請輸入商品數量" min="1"
                    @input="checkInventoryValue" />
                </td>
                <td class="required">商品遊戲</td>
                <td>
                  <select v-model="product.merchandise.gameName" class="form-select" style="width: 210px"
                    aria-label="Default select example">
                    <option selected value="">請選擇</option>
                    <option v-for="gameName in gamemerchandise['game']" :key="gameName" :value="gameName">
                      {{ gameName }}
                    </option>
                  </select>
                </td>
              </tr>
              <tr>
                <td class="required">商品顏色</td>
                <td>
                  <select v-model="product.merchandise.color" class="form-select" style="width: 210px"
                    aria-label="Default select example">
                    <option selected value="">請選擇</option>
                    <option v-for="color in colormerchandise['color']" :key="color" :value="color">
                      {{ color }}
                    </option>
                  </select>
                </td>
              </tr>
              <tr>
                <td class="required">商品尺寸</td>
                <td>
                  <input class="form-control" type="text" id="size" v-model="product.merchandise.size" name="size"
                    placeholder="請輸入商品尺寸" />
                </td>
              </tr>
            </Tbody>
          </table>
          <div style="padding-top: 20px">
            <el-button color="#FFD306" @click="updateProduct">更新商品</el-button>
          </div>
        </form>
      </div>
    </main>
  </div>
</template>

<script setup>
import Swal from "sweetalert2";
import axios from "axios";
import { ref, onMounted } from "vue";
import {
  productMainAndSubType,
  computeros,
  computermemory,
  computergraphics,
  computerprocessor,
  gamemerchandise,
  colormerchandise,
} from "./ProductCategoryList.js";
import Editor from "@tinymce/tinymce-vue";
import router from "@/router";
import { useRoute } from "vue-router";
import DashboardRouterlink from "./DashboardRouterlink.vue";
const url = import.meta.env.VITE_JAVA_URL;
const product = ref({
  product: {
    productName: "",
    price: 0,
    outlined: "",
    description: "",
    spec: "",
    supplier: "",

    productStatus: "onsale",
    productType: "",
    productSubtype: "",
    coverImage: "",

    releaseDate: null,
    discount: null,
    discountFactor: null,
    discountStarttime: null,
    discountEndtime: null,
  },
  gameInfor: {
    gameInforId: "",
    rating: null,
    os: "",
    memory: "",
    graphics: "",
    storage: "",
    processor: "",
  },
  merchandise: {
    merchandiseId: "",
    productId: "",
    inventoryQuantity: null,
    gameName: "",
    color: "",
    size: "",
  },
  productImages: [
    { productImage: "", imageType: null, imageGetFirst: 1, productId: 0 },
    { productImage: "", imageType: null, imageGetFirst: 2, productId: 0 },
    { productImage: "", imageType: null, imageGetFirst: 3, productId: 0 },
    { productImage: "", imageType: null, imageGetFirst: 4, productId: 0 },
    { productImage: "", imageType: null, imageGetFirst: 5, productId: 0 },
  ],
});

let coverImageSelect = ref("imgurl");
const discountdate = ref([]);

onMounted(async () => {
  const route = useRoute();
  const VITE_API_FINDPRODUCTDETAILFORUPDATE = `${import.meta.env.VITE_JAVA_URL
    }/gameshop/product/detailforupdate/`;
  const response = await axios.get(
    VITE_API_FINDPRODUCTDETAILFORUPDATE + route.params.id
  );
  console.log("response = ", response.data);
  product.value.product = response.data.product;
  if (product.value.product.discountStarttime != null) {
    discountdate.value[0] = product.value.product.discountStarttime.substring(
      0,
      10
    );
  }
  if (product.value.product.discountEndtime != null) {
    discountdate.value[1] = product.value.product.discountEndtime.substring(
      0,
      10
    );
  }
  if (response.data.merchandise != null) {
    product.value.merchandise = response.data.merchandise;
    // if(response.data.merchandise.inventoryQuantity!=NaN){
    //   product.value.merchandise.inventoryQuantity =  parseInt(response.data.merchandise.inventoryQuantity);
    // }
  }
  if (response.data.gameInfor != null) {
    product.value.gameInfor = response.data.gameInfor;
  }
  if (response.data.productImages.length > 0) {
    for (let i = 0; i < response.data.productImages.length; i++) {
      product.value.productImages[i].productImage =
        response.data.productImages[i].productImage;
      product.value.productImages[i].imageType = "productimg";
      product.value.productImages[i].productId =
        response.data.productImages[i].productId;
    }
  }
  console.log("product = ", product.value);
});

const updateProduct = async () => {
  console.log(product.value.product);
  if (
    product.value.product.productName.length === 0 ||
    product.value.product.outlined.length === 0 ||
    product.value.product.description.length === 0 ||
    product.value.product.supplier.length === 0 ||
    product.value.product.productType.length === 0 ||
    product.value.product.productSubtype.length === 0 ||
    product.value.product.coverImage.length === 0
  ) {
    Swal.fire({
      title: "發現錯誤",
      text: "必填欄位為空",
      icon: "error",
      showConfirmButton: true,
      confirmButtonText: "確認",
    });
    return;
  } else {
    if (
      product.value.product.discount !== null ||
      product.value.product.discountStarttime !== null ||
      product.value.product.discountEndtime !== null ||
      product.value.product.discountFactor !== null
    ) {
      if (
        product.value.product.discount !== null &&
        product.value.product.discountStarttime !== null &&
        product.value.product.discountEndtime !== null &&
        product.value.product.discountFactor !== null
      ) {
        if (new Date(product.value.product.discountEndtime) <= new Date()) {
          Swal.fire({
            title: "折扣結束日期不得小於今天",
            text: "請確認",
            icon: "error",
            showConfirmButton: true,
            confirmButtonText: "確認",
          });
          return;
        } else {
          // 判斷折扣日期
          if (
            new Date(product.value.product.discountStarttime) <= new Date() &&
            new Date(product.value.product.discountEndtime) >= new Date()
          ) {
            product.value.product.productStatus = "discount";
          }
          const VITE_API_UPRODUCT = `${import.meta.env.VITE_JAVA_URL
            }/gameshop/product/create`;
          const VITE_API_UPDATEPRODUCTIMG = `${import.meta.env.VITE_JAVA_URL
            }/gameshop/product/update`;

          //產品資訊塞入資料庫
          const response = await axios.put(
            VITE_API_UPDATEPRODUCTIMG,
            product.value
          );
          console.log("response = ", response.data);

          if (response.data.success) {
            Swal.fire({
              title: response.data.text,
              icon: "success",
              showConfirmButton: true,
              confirmButtonText: "前往商品頁查看",
              confirmButtonColor: "#FFD306",
              showCancelButton: true,
              cancelButtonText: "留在後台",
            }).then(async (response) => {
              if (response.isConfirmed) {
                router.push({
                  name: "ProductDetailFromDashboard",
                  params: {
                    id: product.value.product.productId,
                    from: "dashboard",
                  },
                });
              } else {
                router.push({
                  name: "productmanagement",
                  params: {
                    page: 1,
                  },
                });
              }
            });
          } else {
            Swal.fire({
              title: response.data.text,
              icon: "error",
              showConfirmButton: true,
              confirmButtonText: "重新上架",
            });
          }
          setTimeout(() => {
            Swal.close();
          }, 3000);
        }
      } else {
        Swal.fire({
          title: "請確認折扣是否為空",
          text: "若有一欄不為空，則其他欄位為必填",
          icon: "error",
          showConfirmButton: true,
          confirmButtonText: "確認",
        });
      }
    }
  }
};

function checkPriceValue() {
  if (product.value.product.price <= 0) {
    product.value.product.price = 0;
  } else {
    product.value.product.price = product.value.product.price
      .toString()
      .replace(/\D/g, "");
  }
}
function checkInventoryValue() {
  if (product.value.merchandise.inventoryQuantity <= 1) {
    product.value.merchandise.inventoryQuantity = 1;
  } else {
    product.value.merchandise.inventoryQuantity =
      product.value.merchandise.inventoryQuantity.toString().replace(/\D/g, "");
  }
}
function checkRatingValue() {
  if (product.value.gameInfor.rating <= 0) {
    product.value.gameInfor.rating = 0.0;
  } else if (product.value.gameInfor.rating > 10) {
    product.value.gameInfor.rating = 10.0;
  } else {
    product.value.gameInfor.rating = product.value.gameInfor.rating.toFixed(1);
  }
}
function checkDiscountFactorValue() {
  if (product.value.product.discountFactor <= 0) {
    product.value.product.discountFactor = 0;
    product.value.product.discount = product.value.price;
  } else if (
    product.value.product.discountFactor >= 0 &&
    product.value.product.discountFactor <= 99
  ) {
    product.value.product.discountFactor = product.value.product.discountFactor
      .toString()
      .replace(/\D/g, "");
    product.value.product.discount = Math.floor(
      product.value.product.price *
      (1 - product.value.product.discountFactor / 100)
    );
  } else {
    product.value.product.discountFactor = 100;
    product.value.product.discount = 0;
  }
}

function changeDiscountDate() {
  product.value.product.discountStarttime = discountdate.value[0];
  if (discountdate.value[0] >= new Date()) {
    product.value.product.discountEndtime = discountdate.value[1];
  } else {
    Swal.fire({
      title: "折扣結束日期不得小於今天",
      text: "請確認",
      icon: "error",
      showConfirmButton: true,
      confirmButtonText: "確認",
    });
  }
}

//設定如果是選擇周邊或虛擬遊戲那些欄位要required
function changeRequired(productType) {
  const dinputEle = document.querySelectorAll(`#dtable input`);
  const minputEle = document.querySelectorAll(`#mtable input`);
  const dselectEle = document.querySelectorAll(`#dtable select`);
  const mselectEle = document.querySelectorAll(`#mtable select`);
  if (productType === "digitalGames") {
    dinputEle.forEach(function (input) {
      input.required = true;
    });
    dselectEle.forEach(function (select) {
      select.required = true;
    });
    minputEle.forEach(function (input) {
      input.required = false;
    });
    mselectEle.forEach(function (select) {
      select.required = false;
    });
    product.value.merchandise.inventoryQuantity = null;
    product.value.merchandise.gameName = "";
    product.value.merchandise.color = "";
    product.value.merchandise.size = "";
  } else if (productType === "physical") {
    minputEle.forEach(function (input) {
      input.required = true;
    });
    mselectEle.forEach(function (select) {
      select.required = true;
    });
    dinputEle.forEach(function (input) {
      input.required = false;
    });
    dselectEle.forEach(function (select) {
      select.required = false;
    });
    product.value.gameInfor.rating = null;
    product.value.gameInfor.os = "";
    product.value.gameInfor.memory = "";
    product.value.gameInfor.graphics = "";
    product.value.gameInfor.storage = "";
    product.value.gameInfor.processor = "";
  } else {
    minputEle.forEach(function (input) {
      input.required = false;
    });
    mselectEle.forEach(function (select) {
      select.required = false;
    });
    dinputEle.forEach(function (input) {
      input.required = false;
    });
    dselectEle.forEach(function (select) {
      select.required = false;
    });
    product.value.merchandise.inventoryQuantity = null;
    product.value.merchandise.gameName = "";
    product.value.merchandise.color = "";
    product.value.merchandise.size = "";
    product.value.gameInfor.rating = null;
    product.value.gameInfor.os = "";
    product.value.gameInfor.memory = "";
    product.value.gameInfor.graphics = "";
    product.value.gameInfor.storage = "";
    product.value.gameInfor.processor = "";
  }
}
//umgfile轉base64
function changeBase64(imgId) {
  if (imgId === "inputimgfile") {
    //獲取文件輸入框
    const imgfile = document.getElementById(imgId);
    //確保用戶獲取了文件
    if (imgfile?.files?.length > 0) {
      //獲取選擇的第一個文件
      const file = imgfile?.files[0];
      //使用fileReader讀取文件
      const reader = new FileReader();

      reader.onload = function (event) {
        //獲取Base64編碼的文件內容
        const base64Data = event.target.result;
        console.log("coverimg base64Data = ", base64Data);
        product.value.product.coverImage = base64Data;
      };
      // 以DataURL形式讀去取文件內容得到BASE64字符串
      reader.readAsDataURL(file);
      console.log("????? = ", reader.readAsDataURL(file));
    }
  } else {
    console.log(imgId);
    //獲取文件輸入框
    const imgfile = document.getElementById(imgId);
    //確保用戶獲取了文件
    if (imgfile?.files?.length > 0) {
      //獲取選擇的第一個文件
      const file = imgfile?.files[0];
      //使用fileReader讀取文件
      const reader = new FileReader();

      reader.onload = function (event) {
        //獲取Base64編碼的文件內容
        const base64Data = event.target.result;
        console.log("base64Data = ", base64Data);
        switch (imgId) {
          case "productimgfile1":
            console.log("base64Data = ", base64Data);
            product.value.productImages[0].productImage = base64Data;
            break;
          case "productimgfile2":
            product.value.productImages[1].productImage = base64Data;
            break;
          case "productimgfile3":
            pproduct.value.productImages[2].productImage = base64Data;
            break;
          case "productimgfile4":
            product.value.productImages[3].productImage = base64Data;
            break;
          case "productimgfile5":
            product.value.productImages[4].productImage = base64Data;
            break;
        }
      };
      // 以DataURL形式讀去取文件內容得到BASE64字符串
      reader.readAsDataURL(file);
      console.log("????? = ", reader.readAsDataURL(file));
    }
  }
}
function changeUploadImgMethod() {
  product.value.product.coverImage = "";
  product.value.productImages[0].productImage = "";
  product.value.productImages[1].productImage = "";
  product.value.productImages[2].productImage = "";
  product.value.productImages[3].productImage = "";
  product.value.productImages[4].productImage = "";
}

//檢查imgurl
function checkimgurl(id) {
  if (coverImageSelect.value === "imgurl") {
    if (id === "coverImage") {
      if (
        !product.value.product.coverImage.includes("https://") &&
        !product.value.product.coverImage.includes("base64")
      ) {
        Swal.fire({
          text: "封面照必須包含「https://」",
          icon: "error",
        });
        setTimeout(() => {
          Swal.close();
        }, 2000);
      }
    }
    // switch(id){
    //   case
    // }
  }
}

//文本編譯器上傳圖片
const handleImageUpload = (blobInfo, progress) =>
  new Promise((resolve, reject) => {
    const file = blobInfo.blob();
    const formData = new FormData();
    formData.append("file", file, file.name);
    axios
      .post(`${url}/upload`, formData)
      .then((response) => {
        console.log(response.data);
        if (response.length != 0) {
          const imageUrl = response.data;
          // 將圖片的 URL 插入到編輯器內容中
          // const imgElement = `<img src="${imageUrl}" alt="uploaded image">`;
          resolve(imageUrl);
          // return;
        }
      })
      .catch(() => {
        reject("出包了");
      });
  });

const tinyMCEConfig = {
  selector: "#tinydemo",
  skin: "oxide-dark",
  language: "zh_TW",
  language_url:
    "https://cdn.tiny.cloud/1/no-api-key/tinymce/6.8.2/langs/zh_TW.js",
  plugins:
    "preview searchreplace autolink directionality visualblocks visualchars fullscreen image link media template code codesample help autosave autoresize",
  toolbar:
    "code undo redo restoredraft  | forecolor backcolor bold italic underline strikethrough link anchor | alignleft aligncenter alignright alignjustify outdent indent | \
        styleselect formatselect fontselect fontsizeselect | bullist numlist | \
        table image media charmap emoticons hr pagebreak insertdatetime| fullscreen",
  height: 650, //編輯器高度
  width: 650,
  min_height: 400,
  images_upload_handler: handleImageUpload,
};
</script>

<style scoped>
.layout {
  background: url("../../../public/images/loginImg.jpg") center/cover fixed;
  margin: 0;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.6);
  background-blend-mode: multiply;
}

table {
  width: 1000px;
  border: black solid;
}

td {
  width: 25%;
  border: black solid;
}

.tabletitle {
  background-color: #ffd306;
  color: black;
  font-size: 1.5em;
}

.required::after {
  content: " *";
  color: red;
}

@media (min-width: 1024px) {
  #sample {
    display: flex;
    flex-direction: column;
    place-items: center;
    width: 100%;
  }
}
</style>
