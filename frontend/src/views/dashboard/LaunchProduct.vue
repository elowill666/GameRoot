<template>
  <div style="display: flex" class="layout">
    <DashboardRouterlink style="height: 100vh"></DashboardRouterlink>
    <main style="width: 100%; height: 100%; margin-left: 30px; margin-bottom: 30px">
      <div class="table-container" style="margin-top: 30px">
        <form>
          <table style="background-color: #141f2a">
            <thead>
              <tr>
                <td class="tabletitle" colspan="4">商品上架</td>
              </tr>
            </thead>
            <tbody>
              <!-- table Product -->
              <tr>
                <td class="required">商品名稱</td>
                <td>
                  <input class="form-control" type="text" id="productName" v-model="request.productName"
                    name="productName" placeholder="請輸入產品名稱" required />
                </td>
                <td>商品版本</td>
                <td>
                  <input class="form-control" type="text" id="spec" v-model="request.spec" name="spec"
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
                    <input class="form-control" type="text" id="inputimgurl" v-model="request.coverImage"
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
                    <input class="form-control" type="text" id="productimgurl1" v-model="productimg01.productImage"
                      placeholder="https://" @blur="checkimgurl('productimgurl1')" />
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
                    <input class="form-control" type="text" id="productimgurl2" v-model="productimg02.productImage"
                      placeholder="https://" @blur="checkimgurl('productimgurl2')" />
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
                    <input class="form-control" type="text" id="productimgurl3" v-model="productimg03.productImage"
                      placeholder="https://" @blur="checkimgurl('productimgurl3')" />
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
                    <input class="form-control" type="text" id="productimgurl4" v-model="productimg04.productImage"
                      name="coverImage04" placeholder="https://" @blur="checkimgurl('productimgurl4')" />
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
                    <input class="form-control" type="text" id="productimgurl5" v-model="productimg05.productImage"
                      placeholder="https://" @blur="checkimgurl('productimgurl5')" />
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
                  <textarea class="form-control" id="outlined" v-model="request.outlined" name="outlined"
                    placeholder="請輸入商品大綱(限50字)" rows="3" maxlength="50" required></textarea>
                </td>
              </tr>
              <tr>
                <td class="required">商品描述</td>
                <td colspan="3">
                  <main id="sample">
                    <Editor :init="tinyMCEConfig" v-model="request.description" />
                  </main>
                </td>
              </tr>
              <tr>
                <td class="required">商品價格</td>
                <td>
                  <input class="form-control" type="number" id="price" v-model="request.price" name="price"
                    placeholder="請輸入商品價格" min="1" @input="checkPriceValue" required />
                </td>
                <td class="required">供應商</td>
                <td>
                  <input class="form-control" type="text" id="supplier" v-model="request.supplier" name="supplier"
                    placeholder="請輸入商品供應商" required />
                </td>
              </tr>
              <tr>
                <td class="required">商品主分類</td>
                <td>
                  <select v-model="request.productType" class="form-select" style="width: 210px"
                    aria-label="Default select example" @change="changeRequired(request.productType)" required>
                    <option selected value="">請選擇</option>
                    <option v-for="mainType in Object.keys(productMainAndSubType)" :key="mainType" :value="mainType">
                      {{ productMainAndSubType[mainType].name }}
                    </option>
                  </select>
                </td>
                <td class="required">商品次分類</td>
                <td>
                  <select v-model="request.productSubtype" class="form-select" style="width: 210px"
                    aria-label="Default select example" required>
                    <option selected value="">請選擇</option>
                    <option v-if="request.productType" v-for="subtype in productMainAndSubType[
                      request.productType
                    ].subtype" :key="subtype" :value="Object.keys(subtype)[0]">
                      {{ Object.values(subtype)[0] }}
                    </option>
                  </select>
                </td>
              </tr>
            </tbody>
          </table>
          <!-- table gameInfor -->
          <table v-if="request.productType === 'digitalGames'" id="dtable" style="background-color: #141f2a">
            <tbody>
              <tr>
                <td class="tabletitle" colspan="4">遊戲最低需求</td>
              </tr>
              <tr>
                <td class="required">電腦系統</td>
                <td>
                  <select v-model="request.os" class="form-select" style="width: 210px"
                    aria-label="Default select example" name="os">
                    <option selected value="">請選擇</option>
                    <option v-for="os in computeros['windows']" :key="os" :value="os">
                      {{ os }}
                    </option>
                  </select>
                </td>
                <td class="required">記憶體</td>
                <td>
                  <select v-model="request.memory" class="form-select" style="width: 210px"
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
                  <select v-model="request.graphics" class="form-select" style="width: 210px"
                    aria-label="Default select example" name="graphics">
                    <option selected value="">請選擇</option>
                    <option v-for="graphics in computergraphics['graphics']" :key="graphics" :value="graphics">
                      {{ graphics }}
                    </option>
                  </select>
                </td>
                <td class="required">佔用空間</td>
                <td>
                  <input class="form-control" type="text" id="storage" v-model="request.storage" name="storage"
                    placeholder="ex: 30GB" />
                </td>
              </tr>
              <tr>
                <td class="required">處理器</td>
                <td>
                  <select v-model="request.processor" class="form-select" style="width: 210px"
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
                  <input class="form-control" type="number" id="rating" v-model="request.rating" name="rating"
                    placeholder="請輸入商品評分" min="0" max="10" step="0.1" @blur="checkRatingValue" required />
                </td>
              </tr>
            </tbody>
          </table>
          <!-- table merchandise -->
          <table v-if="request.productType === 'physical'" id="mtable" style="background-color: #141f2a">
            <Tbody>
              <tr>
                <td class="tabletitle" colspan="4">產品規格</td>
              </tr>
              <tr>
                <td class="required">商品數量</td>
                <td>
                  <input class="form-control" type="number" id="inventoryQuantity" v-model="request.inventoryQuantity"
                    name="price" placeholder="請輸入商品數量" min="1" @input="checkInventoryValue" />
                </td>
                <td class="required">商品遊戲</td>
                <td>
                  <select v-model="request.gameName" class="form-select" style="width: 210px"
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
                  <select v-model="request.color" class="form-select" style="width: 210px"
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
                  <input class="form-control" type="text" id="size" v-model="request.size" name="size"
                    placeholder="請輸入商品尺寸" />
                </td>
              </tr>
            </Tbody>
          </table>
          <div style="padding-top: 20px; margin-bottom: 20px">
            <el-button color="#FFD306" @click="insertProduct">上架商品</el-button>
          </div>
          <div>
            <input type="button" @click="insertMerchandisetEx" value="周邊一鍵上車" />
            <input type="button" @click="insertGameEx" value="遊戲一鍵上車" />
          </div>
        </form>
      </div>
    </main>
  </div>
</template>

<script setup>
import Swal from "sweetalert2";
import { ref } from "vue";
import {
  productMainAndSubType,
  computeros,
  computermemory,
  computergraphics,
  computerprocessor,
  gamemerchandise,
  colormerchandise,
} from "./ProductCategoryList.js";
import axios from "axios";
import Editor from "@tinymce/tinymce-vue";
import router from "@/router";
import DashboardRouterlink from "./DashboardRouterlink.vue";
const url = import.meta.env.VITE_JAVA_URL;
// let productCategory = ref();
let coverImageSelect = ref("imgurl");
let request = ref({
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
  rating: null,
  os: "",
  memory: "",
  graphics: "",
  storage: "",
  processor: "",
  inventoryQuantity: 0,
  gameName: "",
  color: "",
  size: "",
});
let productimg01 = ref({
  productImage: "",
  imageGetFirst: 1,
});
let productimg02 = ref({
  productImage: "",
  imageGetFirst: 2,
});
let productimg03 = ref({
  productImage: "",
  imageGetFirst: 3,
});
let productimg04 = ref({
  productImage: "",
  imageGetFirst: 4,
});
let productimg05 = ref({
  productImage: "",
  imageGetFirst: 5,
});
const newProductId = ref(0);

function checkPriceValue() {
  if (request.value.price <= 0) {
    request.value.price = 0;
  } else {
    request.value.price = request.value.price.toString().replace(/\D/g, "");
  }
}
function checkInventoryValue() {
  if (request.value.inventoryQuantity <= 1) {
    request.value.inventoryQuantity = 1;
  } else {
    request.value.inventoryQuantity = request.value.inventoryQuantity
      .toString()
      .replace(/\D/g, "");
  }
}
function checkRatingValue() {
  if (request.value.rating <= 0) {
    request.value.rating = 0;
  } else {
    request.value.rating = request.value.rating.toFixed(1);
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
    request.value.inventoryQuantity = null;
    request.value.gameName = "";
    request.value.color = "";
    request.value.size = "";
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
    request.value.rating = null;
    request.value.os = "";
    request.value.memory = "";
    request.value.graphics = "";
    request.value.storage = "";
    request.value.processor = "";
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
    request.value.inventoryQuantity = null;
    request.value.gameName = "";
    request.value.color = "";
    request.value.size = "";
    request.value.rating = null;
    request.value.os = "";
    request.value.memory = "";
    request.value.graphics = "";
    request.value.storage = "";
    request.value.processor = "";
  }
}
//檢查imgurl
function checkimgurl(id) {
  if (coverImageSelect.value === "imgurl") {
    if (id === "coverImage") {
      if (!request.value.coverImage.includes("https://")) {
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
        request.value.coverImage = base64Data;
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
            productimg01.value.productImage = base64Data;
            break;
          case "productimgfile2":
            productimg02.value.productImage = base64Data;
            break;
          case "productimgfile3":
            productimg03.value.productImage = base64Data;
            break;
          case "productimgfile4":
            productimg04.value.productImage = base64Data;
            break;
          case "productimgfile5":
            productimg05.value.productImage = base64Data;
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
  request.value.coverImage = "";
  productimg01.value.productImage = "";
  productimg02.value.productImage = "";
  productimg03.value.productImage = "";
  productimg04.value.productImage = "";
  productimg05.value.productImage = "";
}
const insertProduct = async () => {
  console.log(request.value);
  if (
    request.value.productName.length === 0 ||
    request.value.outlined.length === 0 ||
    request.value.description.length === 0 ||
    request.value.supplier.length === 0 ||
    request.value.productType.length === 0 ||
    request.value.productSubtype.length === 0 ||
    request.value.coverImage.length === 0
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
    const VITE_API_LAUNCHPRODUCT = `${import.meta.env.VITE_JAVA_URL
      }/gameshop/product/create`;
    const VITE_API_INSERTPRODUCTIMG = `${import.meta.env.VITE_JAVA_URL
      }/gameshop/product/createproductimg`;
    const VITE_API_FINDNEWPRODUCTID = `${import.meta.env.VITE_JAVA_URL
      }/gameshop/product/lastproductid`;
    //產品資訊塞入資料庫
    const response = await axios.post(VITE_API_LAUNCHPRODUCT, request.value);
    console.log(response.data);
    //確認產品照片檔案不為空後塞入資料庫
    if (
      productimg01.value.productImage.length != 0 ||
      productimg01.value.productImage !== ""
    ) {
      const response = await axios.post(
        VITE_API_INSERTPRODUCTIMG,
        productimg01.value
      );
      console.log("productimg01 = ", response.data.text);
    }
    if (
      productimg02.value.productImage.length != 0 ||
      productimg02.value.productImage !== ""
    ) {
      const response = await axios.post(
        VITE_API_INSERTPRODUCTIMG,
        productimg02.value
      );
      console.log("productimg02 = ", response.data.text);
    }
    if (
      productimg03.value.productImage.length != 0 ||
      productimg03.value.productImage !== ""
    ) {
      const response = await axios.post(
        VITE_API_INSERTPRODUCTIMG,
        productimg03.value
      );
      console.log("productimg03 = ", response.data.text);
    }
    if (
      productimg04.value.productImage.length != 0 ||
      productimg04.value.productImage !== ""
    ) {
      const response = await axios.post(
        VITE_API_INSERTPRODUCTIMG,
        productimg04.value
      );
      console.log("productimg04 = ", response.data.text);
    }
    if (
      productimg05.value.productImage.length != 0 ||
      productimg05.value.productImage !== ""
    ) {
      const response = await axios.post(
        VITE_API_INSERTPRODUCTIMG,
        productimg05.value
      );
      console.log("productimg05 = ", response.data.text);
    }

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
          await axios.get(VITE_API_FINDNEWPRODUCTID).then((response) => {
            console.log("response.data", response.data);
            newProductId.value = response.data;
            console.log("newproductId = ", newProductId.value);
          });
          router.push({
            name: "ProductDetailFromDashboard",
            params: {
              id: newProductId.value,
              from: "dashboard",
            },
          });
        } else {
          router.push({
            name: "productmanagement",
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
};
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

function insertGameEx() {
  request.value.productName = "The Sims 4";
  request.value.price = 269;
  request.value.outlined =
    "在一個無拘無束的虛擬世界中，享受創造和控制生命的奇妙力量。得到力量和自由，享受快樂，遊戲人生！";
  request.value.description =
    "免費下載— 《The Sims™ 4》主遊戲可免費下載。您有多到用不完的選項，可用於建造家園、打扮模擬市民和自訂他們的個性。創作他們的生命故事，探索充滿活力的世界，發掘更多活出自我的方式。";
  request.value.spec = "Digital Deluxe Upgrade";
  request.value.supplier = "Electronic Arts";
  request.value.productStatus = "onsale";
  request.value.productType = "digitalGames";
  request.value.productSubtype = "simulation";
  request.value.coverImage =
    "https://cdn.akamai.steamstatic.com/steam/apps/1222670/extras/TS4_04_610x343_ZH-CHS.gif?t=1705600873";
  request.value.rating = 4.2;
  request.value.os = "Windows 11";
  request.value.memory = "8 GB RAM";
  request.value.graphics = "GeForce RTX 3060";
  request.value.storage = "30GB";
  request.value.processor = "Intel Core i5";
}
function insertMerchandisetEx() {
  request.value.productName =
    "皮卡丘 神奇寶貝球 水晶立體拼圖 益智 日本正版 寶可夢 61片 tp702";
  request.value.price = 937;
  request.value.outlined = "日本正版 寶可夢 61片";
  request.value.description =
    "全部日本當地採購, 全部現貨可馬上出貨不用等！如訂購時數量不足, 請告訴我您要的數量, 我再加足讓您下單";
  request.value.spec = "一般";
  request.value.supplier = "Pokemon";
  request.value.productStatus = "onsale";
  request.value.productType = "physical";
  request.value.productSubtype = "Pokemon";
  request.value.coverImage = "";
  request.value.inventoryQuantity = 2;
  request.value.gameName = "寶可夢";
  request.value.color = "單一";
  request.value.size = "單一";
}
</script>

<style scoped>
.layout {
  background: url("../../../public/images/loginImg.jpg") center/cover fixed;
  margin: 0;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.6);
  background-blend-mode: multiply;
}

.table-container {
  display: flex;
  justify-content: center;
}

/* td {
    width: 300px;
} */
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
