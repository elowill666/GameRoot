<template>
  <div style="display: flex" class="layout">
    <DashboardRouterlink style="height: 100vh"></DashboardRouterlink>
    <main
      style="
        width: 98%;
        margin: 0;
        padding: 0;
        margin-left: 30px;
        margin-bottom: 30px;
      "
    >
      <div ref="scrollTarget"></div>

      <div class="order_searchbox">
        <div class="operation_icon_set" @click="showSearchBox">
          <el-icon size="24"><Operation /></el-icon>
        </div>
        <div
          style="
            display: flex;
            justify-content: flex-end;
            width: 5%;
            margin-top: 5px;
          "
          v-show="!isShowSearchBox"
        >
          <button
            class="btn btn-secondary btn_bc"
            style="background-color: #354551; border: #354551"
            @click="modifyAllProduct"
          >
            saveAll
          </button>
        </div>
        <div v-show="isShowSearchBox" class="searchbox_out">
          <div class="searchbox">
            <label class="selectbox_label" for="productid_search"
              >商品編號:</label
            ><input
              type="text"
              class="form-control form-control-sm product_input"
              id="productid_search"
              v-model="searchData.productId"
            />
            <label class="selectbox_label" for="productName_search"
              >商品名稱:</label
            ><input
              type="text"
              class="form-control form-control-sm productName_input"
              id="productName_search"
              v-model="searchData.productName"
            /><label class="selectbox_label" for="orderid_search"
              >折扣日期:</label
            >
            <el-date-picker
              v-model="selectOfDatePicker"
              type="daterange"
              range-separator="To"
              start-placeholder="Start date"
              end-placeholder="End date"
              class="discountDate_select"
              style="width: 45%"
            />

            <label class="selectbox_label" for="productStatus">商品狀態:</label>
            <select
              class="form-select form-select-sm productStatus_input"
              v-model="searchData.productStatus"
              id="productStatus"
            >
              <option
                v-for="opt of options"
                :key="opt.value"
                :value="opt.value"
              >
                {{ opt.label }}
              </option>
            </select>
            <label class="selectbox_label" for="productType">商品類別:</label>
            <select
              class="form-select form-select-sm productType_input"
              id="productType"
              v-model="searchData.productType"
              @change="findproductsubType(searchData.productType)"
            >
              <option v-for="opt of optionOfProductType" :key="opt.value">
                {{ opt.label }}
              </option>
            </select>
            <label class="selectbox_label" for="productSubtype"
              >商品子類別:</label
            >
            <select
              class="form-select form-select-sm productType_input"
              id="productSubtype"
              v-model="searchData.productSubtype"
              @change=""
            >
              <option v-for="opt of optionOfProductSubType" :key="opt.value">
                {{ opt.label }}
              </option>
            </select>
          </div>
          <!-- 搜尋列第二行 -->
          <div class="searchbox">
            <!-- <div style="width: 30%"></div> -->
            <label class="selectbox_label_top" for="price">商品定價:</label
            ><el-input-number
              class="price_input"
              v-model="searchData.minPrice"
              :min="Number(0)"
              controls-position="right"
              id="price"
            ></el-input-number>
            <el-icon style="margin-left: 5px; margin-right: 5px"
              ><Minus
            /></el-icon>
            <el-input-number
              class="price_input"
              v-model="searchData.maxPrice"
              :min="Number(0)"
              controls-position="right"
            ></el-input-number>
            <label class="selectbox_label_top" for="discount">特價價格:</label
            ><el-input-number
              class="price_input"
              v-model="searchData.minDiscount"
              :min="Number(0)"
              controls-position="right"
              id="discount"
            ></el-input-number>
            <el-icon style="margin-left: 5px; margin-right: 5px"
              ><Minus
            /></el-icon>
            <el-input-number
              class="price_input"
              v-model="searchData.maxDiscount"
              :min="Number(0)"
              controls-position="right"
            ></el-input-number>
            <label class="selectbox_label_top" for="discountFactor"
              >折扣趴數:</label
            ><el-input-number
              class="discountFactor_input"
              v-model="searchData.minDiscountFactor"
              :min="Number(0)"
              controls-position="right"
              id="discountFactor"
            ></el-input-number>
            <el-icon style="margin-left: 5px; margin-right: 5px"
              ><Minus
            /></el-icon>
            <el-input-number
              class="discountFactor_input"
              v-model="searchData.maxDiscountFactor"
              :min="Number(0)"
              controls-position="right"
            ></el-input-number>
            <label class="selectbox_label_top" for="sortOption"
              >排序方式:</label
            >
            <select
              class="form-select form-select-sm sort_input"
              v-model="searchData.sort"
              id="sortOption"
            >
              <option
                v-for="opt of optionOfSort"
                :key="opt.value"
                :value="opt.value"
              >
                {{ opt.label }}
              </option>
            </select>
            <button class="btn btn-secondary ordermanag_btn" @click="goSearch">
              搜尋
            </button>
            <button
              class="btn btn-secondary ordermanag_btn"
              @click="clearSearchRequest"
            >
              清除
            </button>
          </div>
          <div class="saveAll_btn">
            <button
              class="btn btn-secondary saveAll_btn"
              @click="modifyAllProduct"
            >
              saveAll
            </button>
          </div>
        </div>
      </div>
      <div style="width: 98%">
        <div>
          <div>
            <table style="width: 1666px" v-show="isNoData">
              <tr style="height: 2.2em">
                <th class="ondata_table_th" style="width: 85px">商品編號</th>
                <th class="ondata_table_th" style="width: 100px">封面照片</th>
                <th class="ondata_table_th" style="width: 160px">產品名稱</th>
                <th class="ondata_table_th" style="width: 100px">規格</th>
                <th class="ondata_table_th" style="width: 110px">商品定價</th>
                <th class="ondata_table_th" style="width: 100px">庫存</th>
                <th class="ondata_table_th" style="width: 120px">商品狀態</th>
                <th class="ondata_table_th" style="width: 100px">特價價格</th>
                <th class="ondata_table_th" style="width: 120px">折扣趴數</th>
                <th class="ondata_table_th" style="width: 200px">折扣日期</th>
                <th
                  style="
                    width: 150px;
                    padding-right: 25px;
                    background-color: #272727;
                  "
                  class="ondata_table_th"
                >
                  操作
                </th>
              </tr>
              <tr style="height: 80px">
                <td
                  class="ondata_table_td"
                  style="text-align: center"
                  colspan="11"
                >
                  查無資料
                </td>
              </tr>
            </table>

            <el-table
              v-show="!isNoData"
              class="usertable"
              :data="product"
              style="width: 1700px; margin: 0; padding: 0; cursor: pointer"
              :header-row-style="headerRowStyle"
              :show-header="true"
              :header-cell-style="{
                'background-color': '#303133',
                color: '#fff',
              }"
              :cell-style="{
                'background-color': '#606266',
                color: '#fff',
              }"
            >
              <!-- ProductID -->
              <el-table-column
                fixed="left"
                label="產品編號"
                width="100"
                header-align="center"
                align="center"
              >
                <template v-slot="scope">
                  <div
                    alt=""
                    style="width: 100%"
                    @click="goToProductPage(scope.row.productId)"
                  >
                    {{
                      formatProductId(
                        scope.row.productId,
                        scope.row.productType
                      )
                    }}
                  </div>
                </template>
              </el-table-column>
              <!-- PHOTO -->
              <el-table-column
                label="封面照片"
                width="120"
                header-align="center"
              >
                <template v-slot="scope">
                  <img
                    v-lazy="scope.row.coverImage"
                    :alt="product.productName"
                    :style="{ height: '80px' }"
                    class="dash_image"
                  />
                </template>
              </el-table-column>
              <!-- NAME -->
              <el-table-column
                label="商品名稱"
                width="200"
                header-align="center"
                align="center"
              >
                <template #default="{ row }">
                  <el-input
                    type="textarea"
                    resize="none"
                    rows="3"
                    v-if="editBotton[row.productId]"
                    v-model="row.productName"
                    @change="changeData(row.productId)"
                  ></el-input>
                  <span v-else>{{ row.productName }}</span>
                </template>
              </el-table-column>
              <!-- spec -->
              <el-table-column
                label="商品規格"
                width="120"
                header-align="center"
                align="center"
              >
                <template #default="{ row }">
                  <el-input
                    v-if="editBotton[row.productId]"
                    v-model="row.spec"
                    @change="changeData(row.productId)"
                  ></el-input>
                  <span v-else>{{ row.spec }}</span>
                </template>
              </el-table-column>
              <!-- Price -->
              <el-table-column
                label="商品定價"
                width="140"
                header-align="center"
                align="center"
              >
                <template #default="{ row }">
                  <el-input-number
                    class="mx-4"
                    v-if="editBotton[row.productId]"
                    v-model="row.price"
                    :min="Number(0)"
                    style="width: 90%"
                    controls-position="right"
                    @change="changePrice(row)"
                  />
                  <span v-else>{{ row.price }}</span>
                </template>
              </el-table-column>
              <!-- InventoryQuantity -->
              <el-table-column
                label="庫存"
                width="150"
                header-align="center"
                align="center"
              >
                <template #default="{ row }">
                  <el-input-number
                    class="mx-4"
                    v-if="editBotton[row.productId]"
                    v-model="row.inventoryQuantity"
                    :min="Number(0)"
                    style="width: 80%"
                    controls-position="right"
                    @change="changeInventoryQuantity(row)"
                  />
                  <span v-else>{{ formatInventoryQuantity(row) }}</span>
                </template>
              </el-table-column>
              <!-- STATUS -->
              <el-table-column
                label="商品狀態"
                width="140"
                header-align="center"
                align="center"
              >
                <template #default="{ row }">
                  <el-select
                    v-if="editBotton[row.productId]"
                    v-model="row.productStatus"
                    class="m-2"
                    style="width: 100%"
                    @change="changeData(row.productId)"
                  >
                    <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                  <span v-else>{{ formatStatus(row.productStatus) }}</span>
                </template>
              </el-table-column>
              <!-- Discount -->
              <el-table-column
                label="特價售價"
                width="130"
                header-align="center"
                align="center"
              >
                <template #default="{ row }">
                  <el-input-number
                    class="mx-4"
                    v-if="editBotton[row.productId]"
                    v-model="row.discount"
                    :min="Number(0)"
                    style="width: 90%"
                    controls-position="right"
                    @change="changeDiscount(row)"
                  />
                  <span v-else>{{ row.discount }}</span>
                </template>
              </el-table-column>
              <!-- DiscountFactor -->
              <el-table-column label="折扣趴數" width="140" align="center">
                <template #default="{ row }">
                  <el-input-number
                    v-if="editBotton[row.productId]"
                    v-model="row.discountFactor"
                    style="width: 80%"
                    :min="Number(0)"
                    :max="Number(99)"
                    controls-position="right"
                    @change="changeDiscountFactor(row)"
                    >%Off</el-input-number
                  >
                  <span v-else style="margin: auto"
                    >{{ row.discountFactor }}%off</span
                  >
                </template>
              </el-table-column>
              <!-- DiscountStarttime -->
              <el-table-column
                label="折扣日期"
                width="250"
                header-align="center"
                align="center"
              >
                <template #default="{ row }">
                  <el-date-picker
                    v-if="editBotton[row.productId]"
                    v-model="tempDiscountTime[row.productId]"
                    type="daterange"
                    range-separator="To"
                    :start-placeholder="formatDate(row.discountStarttime)"
                    :end-placeholder="formatDate(row.discountEndtime)"
                    @change="changeData(row.productId)"
                    style="width: 100%"
                  />
                  <span v-else
                    >{{ formatDate(row.discountStarttime) }} -
                    {{ formatDate(row.discountEndtime) }}</span
                  >
                </template>
              </el-table-column>

              <!-- ProductType -->
              <el-table-column
                label="商品類別"
                width="150"
                header-align="center"
                align="center"
              >
                <template #default="{ row }">
                  <el-input
                    v-if="editBotton[row.productId]"
                    v-model="row.productType"
                    @change="changeData(row.productId)"
                  >
                  </el-input>
                  <span v-else>{{ row.productType }}</span>
                </template>
              </el-table-column>
              <!-- ProductSubType -->
              <el-table-column
                label="商品子類別"
                width="120"
                header-align="center"
                lign="center"
              >
                <template #default="{ row }">
                  <el-input
                    v-if="editBotton[row.productId]"
                    v-model="row.productSubtype"
                    @change="changeData(row.productId)"
                  >
                  </el-input>
                  <span v-else>{{ row.productSubtype }}</span>
                </template>
              </el-table-column>
              <!-- Supplier -->
              <el-table-column
                label="商品製造商"
                width="120"
                header-align="center"
              >
                <template #default="{ row }">
                  <el-input
                    v-if="editBotton[row.productId]"
                    v-model="row.supplier"
                    @change="changeData(row.productId)"
                  >
                  </el-input>
                  <span v-else>{{ row.supplier }}</span>
                </template>
              </el-table-column>
              <!-- 編輯 -->
              <el-table-column
                fixed="right"
                label="操作"
                width="220"
                header-align="center"
                align="center"
              >
                <template #default="{ row }">
                  <el-button
                    v-show="!tryChanged[row.productId]"
                    @click="doedit(row)"
                    >編輯</el-button
                  >
                  <el-button
                    @click="dodetailedit(row.productId)"
                    v-show="!tryChanged[row.productId]"
                    >詳細編輯</el-button
                  >
                  <el-button
                    v-show="tryChanged[row.productId]"
                    @click="dosave(row)"
                    >保存</el-button
                  >
                  <el-button
                    v-show="tryChanged[row.productId]"
                    @click="notChangeData(row)"
                    >放棄</el-button
                  >
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div class="mt-4" style="display: flex; justify-content: flex-end">
            <el-pagination
              v-model:current-page="paginationParam.currentPage"
              v-model:page-size="paginationParam.pageSize"
              :page-sizes="paginationParam.sizeOfPage"
              background="true"
              layout="total,sizes, prev, pager, next"
              :total="totalNumberOfProduct"
              @size-change="goWhichPage"
              @current-change="goWhichPage"
              @prev-click="goWhichPage"
              @next-click="goWhichPage"
            />
          </div>
        </div>
      </div>
    </main>
  </div>
</template>
<script setup>
import axios from "axios";
import Swal from "sweetalert2";
import { onMounted } from "vue";
import { ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import { format } from "../../../node_modules/date-fns";
import DashboardRouterlink from "./DashboardRouterlink.vue";
import {
  formatDate,
  formatFullAndFullDate,
} from "../../utils/someDateFormat.js";
import {
  formatProductId,
  reformatProductId,
} from "../../utils/someIdFormat.js";
import { forShortSweetalert } from "../../utils/someSweetalert.js";

const route = useRoute();
const config = { withCredentials: true };
const path = import.meta.env.VITE_JAVA_URL;
const tryChanged = ref({});
const changeDetail = ref({});
const editBotton = ref({});
const product = ref([]);
const tempProductInfo = ref({});
const tempDiscountTime = ref({});

const headerRowStyle = ref({
  position: "sticky",
  top: "0",
});
const operationWidth = ref(120);
const options = [
  {
    value: "onsale",
    label: "上架商品",
  },
  {
    value: "notsale",
    label: "下架商品",
  },
  {
    value: "discount",
    label: "特價商品",
  },
];
const optionOfProductType = ref([]);
const optionOfProductSubType = ref([]);
const optionOfSort = [
  {
    value: "idacs",
    label: "商品編號小>大",
  },
  {
    value: "iddesc",
    label: "商品編號大>小",
  },
  {
    value: "priceasc",
    label: "商品定價低>高",
  },
  {
    value: "pricedesc",
    label: "商品定價高>低",
  },
  {
    value: "discountasc",
    label: "特價售價低>高",
  },
  {
    value: "discountdesc",
    label: "特價售價高>低",
  },
  {
    value: "discountFactorasc",
    label: "折扣趴數高>低",
  },
  {
    value: "discountFactordesc",
    label: "折扣趴數高>低",
  },
];
const router = useRouter();
//用來控制沒有data的那張表格
const isNoData = ref(false);
//分頁器的參數
const paginationParam = ref({
  sizeOfPage: [40, 30, 20, 10, 5], //設定每頁幾筆資料數
  pageSize: 10, //目前每頁幾筆資料
  currentPage: Number(route.params.page),
});

const scrollTarget = ref(null);

async function scrollToElement() {
  // document.documentElement.scrollTop = 0;
  try {
    scrollTarget.value.scrollIntoView({
      inline: "nearest",
    });
  } catch (error) {
    console.log(error);
  }
}
//用來接收後端count後的產品數量
const totalNumberOfProduct = ref();

const isShowSearchBox = ref(false);

const selectOfDatePicker = ref([]);
const selectOfDiscount = ref([]);
const selectOfDiscountFactor = ref([]);

//用來給後端找資料用的
const searchData = ref({
  productId: null,
  productName: null,
  minDiscount: null,
  maxDiscount: null,
  minDiscountFactor: null,
  maxDiscountFactor: null,
  discountStarttime: null,
  discountEndtime: null,
  supplier: null,
  productStatus: null,
  productType: null,
  productSubtype: null,
  coverImage: null,
  spec: null,
  minPrice: null,
  maxPrice: null,
  start: null,
  rows: null,
  sort: null,
});

//載入頁面時向後端要所有商品資料
onMounted(async () => {
  await findAllProduct();
});

async function goWhichPage() {
  console.log();
  router.push({
    name: "productmanagement",
    params: {
      page: paginationParam.value.currentPage,
    },
  });
  scrollToElement();
  findAllProduct();
}

//用來更新狀態的方法
async function modifyProductStatus() {
  try {
    const response = await axios.get(
      `${path}/dashboard/product/update`,
      config
    );
    console.log(response);
  } catch (error) {
    console.log(error);
  }
}

//向後端請求所有商品的方法
const findAllProduct = async () => {
  let request = searchDatamethod();
  console.log(request);
  Swal.fire({
    title: "資料搜尋中",
    showConfirmButton: false,
    timer: 2000,
  });
  try {
    const response = await axios.post(
      `
      ${path}/dashboard/product/find`,
      request,
      config
    );
    forShortSweetalert("success", "搜尋完畢");
    console.log(response.data);
    if (response.data.data === "nodata") {
      isNoData.value = true;
    } else if (response.data.data.length != 0) {
      isNoData.value = false;
      totalNumberOfProduct.value = response.data.countOfData;
      product.value = response.data.data;
      editBotton.value = {};
      tryChanged.value = {};
      changeDetail.value = {};
      tempProductInfo.value = {};
      tempDiscountTime.value = {};
      let productIds = [];
      for (let id of product.value) {
        let showinit = {
          [id.productId]: false,
        };
        let infoinit = {
          [id.productId]: {},
        };
        let discountTimeInit = {
          [id.productId]: [id.discountStarttime, id.discountEndtime],
        };
        Object.assign(tryChanged.value, showinit);
        Object.assign(editBotton.value, showinit);
        Object.assign(changeDetail.value, showinit);
        Object.assign(tempProductInfo.value, infoinit);
        Object.assign(tempDiscountTime.value, discountTimeInit);
        productIds.push(Number(id.productId));
      }
      operationWidth.value = 120;
      const coverImageUrls = await findProductAllImage(productIds);
      for (let temp in product.value) {
        product.value[temp].coverImage =
          coverImageUrls[product.value[temp].productId];
      }
    }
    // for (let temp = 0; temp < product.value.length; temp++) {
    //   const coverImageUrls = await findProductImage(
    //     product.value[temp].productId
    //   );
    //   product.value[temp].coverImage = coverImageUrls.data;
    // }
  } catch (error) {
    console.log(error);
  }
};

function searchDatamethod() {
  let request = { ...searchData.value };
  request.start = paginationParam.value.currentPage;
  request.rows = paginationParam.value.pageSize;
  if (selectOfDatePicker.value == null || selectOfDatePicker.value[1] == null) {
    request.discountStarttime == null;
    request.discountEndtime == null;
  } else if (selectOfDatePicker.value.length != 0) {
    request.discountStarttime = selectOfDatePicker.value[0];
    request.discountEndtime = selectOfDatePicker.value[1];
  }
  if (request.productId) {
    request.productId = reformatProductId(request.productId);
  }
  return request;
}

const doedit = (row) => {
  console.log(row);
  tempProductInfo.value[row.productId] = Object.assign(
    tempProductInfo.value[row.productId],
    row
  );
  editBotton.value[row.productId] = !editBotton.value[row.productId];
  if (editBotton.value[row.productId]) {
    changeDetail.value[row.productId] = true;
  } else {
    changeDetail.value[row.productId] = false;
  }
};

//按詳細更改
function dodetailedit(productId) {
  router.push({
    name: "ChangeProduct",
    params: {
      id: productId,
    },
  });
}

//頁面按鈕

//改參數增加變換案紐
function changeData(id) {
  changeDetail.value[id] = true;
  tryChanged.value[id] = true;
  operationWidth.value = 200;
}

function changeInventoryQuantity(row) {
  if (row.productType == "digitalGames" || row.productType == "cash") {
    Swal.fire({
      title: "DigitalGames Error",
      text: "數位商品沒庫存",
      icon: "error",
      allowOutsideClick: false,
    }).then((result) => {
      //確認就去資料庫更新資料
      row.inventoryQuantity =
        tempProductInfo.value[row.productId].inventoryQuantity;
    });
  }
  changeData(row.productId);
}

//改參數不存變回原狀
function notChangeData(row) {
  Swal.fire({
    title: "放棄更改確認",
    text: "放棄您更變的項目嗎",
    icon: "question",
    allowOutsideClick: false,
    showCancelButton: true,
    confirmButtonColor: "#3085d6",
    confirmButtonText: "放棄更改",
    cancelButtonText: "繼續編輯",
  }).then((result) => {
    if (result.isConfirmed) {
      editBotton.value[row.productId] = false;
      tryChanged.value[row.productId] = false;
      changeDetail.value[row.productId] = true;
      console.log(editBotton.value[row.productId]);
      operationWidth.value = 120;
      row.productName = tempProductInfo.value[row.productId].productName;
      row.price = tempProductInfo.value[row.productId].price;
      row.productStatus = tempProductInfo.value[row.productId].productStatus;
      row.productType = tempProductInfo.value[row.productId].productType;
      row.productSubtype = tempProductInfo.value[row.productId].productSubtype;
      row.discount = tempProductInfo.value[row.productId].discount;
      row.discountFactor = tempProductInfo.value[row.productId].discountFactor;
      row.discountStarttime =
        tempProductInfo.value[row.productId].discountStarttime;
      row.discountEndtime =
        tempProductInfo.value[row.productId].discountEndtime;
      row.supplier = tempProductInfo.value[row.productId].supplier;
      row.inventoryQuantity =
        tempProductInfo.value[row.productId].inventoryQuantity;
      row.spec = tempProductInfo.value[row.productId].spec;
      tempDiscountTime.value[row.productId][0] =
        tempProductInfo.value[row.productId].discountStarttime;
      tempDiscountTime.value[row.productId][1] =
        tempProductInfo.value[row.productId].discountEndtime;
    }
  });
  console.log(row);
}

//按下Save後觸發事件 儲存至資料庫
function dosave(row) {
  console.log("before", tempDiscountTime.value);
  if (
    tempDiscountTime.value[row.productId] == null ||
    tempDiscountTime.value[row.productId][1] == null
  ) {
    row.discountStarttime = null;
    row.discountEndtime = null;
  } else {
    row.discountStarttime = tempDiscountTime.value[row.productId][0];
    row.discountEndtime = tempDiscountTime.value[row.productId][1];
  }
  if (row.price == 0 || row.price == null) {
    forShortSweetalert("error", "商品價格不可為0", 500, 3000);
    return;
  }
  Swal.fire({
    title: "更改商品資訊",
    text: "確定更改商品資訊嗎?",
    icon: "question",
    allowOutsideClick: false,
    showDenyButton: true,
    showCancelButton: true,
    confirmButtonColor: "#3085d6",
    denyButtonColor: "#d33",
    confirmButtonText: "確定更改",
    denyButtonText: `放棄更改`,
  }).then((result) => {
    //確認就去資料庫更新資料
    if (result.isConfirmed) {
      if (row.spec) {
        let arr = row.spec.split("/");
        row.size = arr[0];
        row.color = arr[1];
      }
      modifyProduct(row);
      //不存就恢復原狀
    } else if (result.isDenied) {
      notChangeData(row);
    } else {
    }
  });
}

//向後端請求更新前的驗證
function modifyProduct(row) {
  //如果是notsale的狀態直接請求更新
  if (row.productStatus == "notsale") {
    Swal.fire({
      title: "此商品未上架是否更改商品狀態?",
      text: "確定更改嗎?",
      icon: "question",
      allowOutsideClick: false,
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      confirmButtonText: "更改",
      cancelButtonText: "返回",
    }).then((result) => {
      //確認就去資料庫更新資料
      if (result.isConfirmed) {
        modifyMethod(row);
      }
    });
    //如果不是notsale的狀態每次請求都先確認庫存夠不夠
  } else {
    //如果庫存大於０往下if，沒有就去else
    if (row.inventoryQuantity > 0) {
      //如果商品狀態為onsale
      if (row.productStatus == "onsale") {
        if (row.discountEndtime != null && row.discountStarttime != null) {
          if (
            row.discount == null ||
            row.discountFactor == null ||
            row.discount == 0 ||
            row.discountFactor == 0
          ) {
            forShortSweetalert(
              "error",
              "如想預設折扣商品，商品折扣數或折扣價格為必填項目",
              850,
              3000
            );
            return;
          }
        }
        modifyMethod(row);
        //如果商品狀態為discount的情況下要進行驗證
      } else if (row.productStatus === "discount") {
        if (
          row.discount == null ||
          row.discountFactor == null ||
          row.discount == 0 ||
          row.discountFactor == 0
        ) {
          forShortSweetalert(
            "error",
            "折扣商品的折扣數或折扣價格為必填項目",
            580,
            3000
          );
          return;
        }
        //如果沒設定時間直接更新
        if (row.discountEndtime == null && row.discountStarttime == null) {
          forShortSweetalert(
            "error",
            "折扣商品的折扣時間為必填項目",
            550,
            3000
          );
          //如果偵測到商品折扣時間小於現在的時間跳錯誤
        } else if (new Date(row.discountEndtime) < new Date()) {
          forShortSweetalert("error", "折扣終止時間為不得小於今日", 550, 2000);
          //如果沒有起始時間或是偵測到設定的起始時間小於現在時間往下驗證
        } else if (row.discountStarttime != null || row.discountEndtime) {
          if (
            new Date(row.discountStarttime) < new Date() &&
            new Date(row.discountEndtime) > new Date()
          ) {
            //option[2].value為discount
            //起始時間沒問題的情況下，折扣時間要不等於null且大於現在時間
            row.productStatus = options[2].value;
            modifyMethod(row);
          } else if (
            new Date(row.discountStarttime) > new Date() &&
            new Date(row.discountEndtime) > new Date()
          ) {
            //option[0].value為onsale
            row.productStatus = options[0].value;

            modifyMethod(row);
          }
        }
      }
      //庫存小於等於跳警告已沒庫存還要更新嗎
    } else {
      if (tempProductInfo.value[row.productId].productStatus === "notsale") {
        forShortSweetalert("error", "庫存為零不可上架?", 400);
        return;
      }
      Swal.fire({
        title: "此商品庫存為零即將下架?",
        text: "確定更改商品資料嗎?",
        icon: "question",
        allowOutsideClick: false,
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        confirmButtonText: "更改",
      }).then((result) => {
        //確認就去資料庫更新資料
        if (result.isConfirmed) {
          //option[1].value為notsale
          row.productStatus = options[1].value;
          modifyMethod(row);
        }
      });
    }
  }
}

//向後端請求更新方法
function modifyMethod(row) {
  console.log("be", row);
  row.discountStarttime = formatFullAndFullDate(row.discountStarttime);
  row.discountEndtime = formatFullAndFullDate(row.discountEndtime);
  axios
    .put(path + "/dashboard/product", row)
    .then((response) => {
      console.log("savesuccess", response);
      //更新成功，並接收到後端回傳data
      row.price = response.data.price;
      row.productName = response.data.productName;
      row.spec = response.data.spec;
      row.inventoryQuantity = response.data.inventoryQuantity;
      row.productType = response.data.productType;
      row.productSubtype = response.data.productSubtype;
      row.discount = response.data.discount;
      row.discountFactor = response.data.discountFactor;
      row.discountStarttime = response.data.discountStarttime;
      row.discountEndtime = response.data.discountEndtime;
      row.supplier = response.data.supplier;
      row.productStatus = response.data.productStatus;
      if (response.data) {
        Swal.fire({
          html: `<h4 style="display: flex; justify-content: center; align-items: center; height: 100%; font-family= "Arial", sans-serif;">更新成功</h4>`,
          icon: `success`,
          showConfirmButton: false,
          width: 300,
          timer: 2000,
          toast: true,

          showClass: {
            popup: `
  animate__animated
  animate__fadeInUp
  animate__faster
  `,
          },
          hideClass: {
            popup: `
  animate__animated
  animate__fadeOutDown
  animate__faster
  `,
          },
        }).then(() => {
          editBotton.value[row.productId] = false;
          tryChanged.value[row.productId] = false;
          operationWidth.value = 120;
        });
      } else {
        forShortSweetalert("Error", "資料有誤更新錯誤", 300, 2000);

        notChangeData();
      }
    })
    .catch((error) => {
      console.log(error);
      Swal.fire({
        title: "Error",
        text: "System Failure",
        icon: "Error",
      });
      notChangeData();
    });
}
//批次儲存
async function modifyAllProduct() {
  let request = [];
  for (let temp of Object.keys(tryChanged.value)) {
    if (tryChanged.value[temp]) {
      for (let temp2 of product.value) {
        console.log(temp2);
        if (temp2.productId == temp) {
          let errormes = formatProductId(temp2.productId, temp2.productType);
          //困存大於０在這裡驗證 0直接下架
          if (temp2.inventoryQuantity > 0) {
            //不管怎樣價錢不可以為０
            if (temp2.price == 0 || temp2.price == null) {
              forShortSweetalert(
                "error",
                `${formatProductId(
                  temp2.productId,
                  temp2.productType
                )}商品價格不可為0`,
                500,
                3000
              );
              return;
            }
            if (
              tempDiscountTime.value[temp2.productId] == null ||
              tempDiscountTime.value[temp2.productId][1] == null
            ) {
              temp2.discountStarttime = null;
              temp2.discountEndtime = null;
            } else {
              temp2.discountStarttime =
                tempDiscountTime.value[temp2.productId][0];
              temp2.discountEndtime =
                tempDiscountTime.value[temp2.productId][1];
            }
            if (temp2.productStatus === "discount") {
              if (
                temp2.discount == null ||
                temp2.discountFactor == null ||
                temp2.discount == 0 ||
                temp2.discountFactor == 0
              ) {
                forShortSweetalert(
                  "error",
                  `${errormes}-折扣商品的折扣數或折扣價格為必填項目`,
                  580,
                  3000
                );
                return;
              }
              //如果沒設定時間直接更新
              if (
                temp2.discountEndtime == null &&
                temp2.discountStarttime == null
              ) {
                forShortSweetalert(
                  "error",
                  `${errormes}-折扣商品的折扣時間為必填項目`,
                  600,
                  3000
                );
                return;
              }
            }
            if (temp2.productStatus == "onsale") {
              if (
                temp2.discountEndtime != null &&
                temp2.discountStarttime != null
              ) {
                if (
                  temp2.discount == null ||
                  temp2.discountFactor == null ||
                  temp2.discount == 0 ||
                  temp2.discountFactor == 0
                ) {
                  forShortSweetalert(
                    "error",
                    `${errormes}-如想預設折扣商品，商品折扣數或折扣價格為必填項目`,
                    850,
                    3000
                  );
                  return;
                }
              }
            }
          } else {
            console.log(temp2);
            temp2.productStatus = "notsale";
          }
          request.push(temp2);
        }
      }
    }
  }
  console.log(request);
  if (request.length == 0) {
    Swal.fire({
      title: "Error",
      text: "沒有變更項目",
      icon: "error",
      allowOutsideClick: false,
    });
    return;
  }
  Swal.fire({
    title: "loading",
    showConfirmButton: false,
    timer: 1000,
  });
  try {
    const response = await axios.put(`${path}/dashboard/product/all`, request, {
      withCredentials: true,
    });
    console.log(response);
    if (response.data.length != 0) {
      Swal.fire({
        title: "Success",
        text: "Updated Successfully",
        icon: "success",
        allowOutsideClick: false,
      });
    }
    for (let temp = 0; temp < product.value.length; temp++) {
      for (let temp2 of response.data) {
        if (temp2.productId == product.value[temp].productId) {
          product.value[temp].price = temp2.price;
          product.value[temp].productName = temp2.productName;
          product.value[temp].spec = temp2.spec;
          product.value[temp].inventoryQuantity = temp2.inventoryQuantity;
          product.value[temp].productType = temp2.productType;
          product.value[temp].productSubtype = temp2.productSubtype;
          product.value[temp].discount = temp2.discount;
          product.value[temp].discountFactor = temp2.discountFactor;
          product.value[temp].discountStarttime = temp2.discountStarttime;
          product.value[temp].discountEndtime = temp2.discountEndtime;
          product.value[temp].supplier = temp2.supplier;
          product.value[temp].productStatus = temp2.productStatus;
          tryChanged.value[temp2.productId] = false;
          editBotton.value[temp2.productId] = false;
        }
      }
    }
  } catch (error) {
    console.log(error);
  }
}

//調整折扣趴數
function changeDiscountFactor(row) {
  changeData(row.productId);
  row.discount = Math.floor((row.price * (100 - row.discountFactor)) / 100);
}
//調整折扣價格
function changeDiscount(row) {
  changeData(row.productId);
  row.discountFactor = Math.floor(
    ((row.price - row.discount) / row.price) * 100
  );
}
//調整商品訂價
function changePrice(row) {
  changeData(row.productId);
  if ("notsale" !== row.productStatus) {
    if (row.price == 0 || row.price == null) {
      forShortSweetalert("error", `商品價格不可為0`, 500, 3000);
      row.price = tempProductInfo.value[row.productId].price;
      return;
    }
  }
  0;
  row.discount = Math.floor((row.price * (100 - row.discountFactor)) / 100);
}

//庫存顯示
function formatInventoryQuantity(row) {
  if ("digitalGames" == row.productType || "cash" == row.productType) {
    return "NaN";
  }
  return row.inventoryQuantity;
}

function formatFullDate(dateTime) {
  // 讓此格式的時間變成String"1970-01-01T00:00:00.000Z"
  if (dateTime == null) {
    // 如果 date 不是有效的 Date 對象
    return "yyyy-MM-dd";
  }
  return format(dateTime, "yyyy-MM-dd'T'HH:mm:ss.SSS");
}

async function findProductImage(productId) {
  try {
    const response = await axios.post(
      path + "/dashboard/productimage/" + productId
    );
    return response;
  } catch (error) {
    console.log(error);
  }
}

async function findProductAllImage(productIds) {
  try {
    const response = await axios.post(
      `${path}/dashboard/productimage/find`,
      productIds
    );
    return response.data;
  } catch (error) {
    console.log(error);
  }
}

function goToProductPage(productId) {
  router.push({
    name: "ProductDetailFromDashboard",
    params: {
      id: productId,
      from: "dashboard",
    },
  });
}

function showSearchBox() {
  isShowSearchBox.value = !isShowSearchBox.value;
  findproductType();
  findproductsubType();
}

function goSearch(howToSort) {
  paginationParam.value.currentPage = 1;
  findAllProduct();
}

function clearSearchRequest() {
  paginationParam.value.currentPage = 1;
  paginationParam.value.size = 10;
  searchData.value.productId = null;
  searchData.value.productName = null;
  searchData.value.minDiscount = null;
  searchData.value.maxDiscount = null;
  searchData.value.minDiscountFactor = null;
  searchData.value.maxDiscountFactor = null;
  searchData.value.discountStarttime = null;
  searchData.value.discountEndtime = null;
  searchData.value.supplier = null;
  searchData.value.productStatus = null;
  searchData.value.productType = null;
  searchData.value.productSubtype = null;
  searchData.value.coverImage = null;
  searchData.value.minPrice = null;
  searchData.value.maxPrice = null;
  selectOfDatePicker.value = [];

  goWhichPage();
  findproductsubType();
  findproductType();
}

async function findproductType() {
  try {
    const response = await axios.get(`${path}/dashboard/product/producttype`);
    optionOfProductType.value = [];
    for (let temp of response.data) {
      let tempobj = {
        value: temp,
        label: temp,
      };
      optionOfProductType.value.push(tempobj);
    }
  } catch (error) {
    console.log(error);
  }
}

async function findproductsubType(type) {
  let productType = {
    productType: null,
  };
  if (type == null) {
    productType.productType = "all";
  } else {
    productType.productType = type;
  }

  try {
    const response = await axios.post(
      `${path}/dashboard/product/subtype`,
      productType
    );
    optionOfProductSubType.value = [];
    for (let temp of response.data) {
      let tempobj = {
        value: temp,
        label: temp,
      };
      optionOfProductSubType.value.push(tempobj);
    }
    console.log(optionOfProductSubType.value);
  } catch (error) {
    console.log(error);
  }
}

function formatStatus(status) {
  if ("discount" === status) {
    return "特價商品";
  } else if ("onsale" === status) {
    return "一般商品";
  } else if ("notsale" === status) {
    return "下架商品";
  }
}
// async function countProduct() {
//   try {
//     const response = await axios.get(`${path}/dashboard/product/count`);
//     console.log(response.data);
//     return response.data;
//   } catch (error) {
//     console.log(error);
//   }
// }
</script>

<style scoped>
.layout {
  background: url("../../../public/images/loginImg.jpg") center/cover fixed;
  margin: 0;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.6);
  background-blend-mode: multiply;
}
.pagenation_prod {
  float: right;
}

.dash_image {
  width: 100%;
  display: block;
  border: none;
  object-fit: scale-down;
}
.order_searchbox {
  justify-content: flex-end;
  display: flex;
  align-items: center;
  margin-top: 20px;
  margin-bottom: 20px;
  margin-right: 20px;
  width: 98%;
}

.operation_icon_set {
  display: flex;
  justify-content: center;
  margin-right: 2px;
  margin-top: 5px;
  align-self: flex-start;
  padding: 7px;
  background-color: #354551;
  border-radius: 5px;
}
.searchbox {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  justify-content: flex-end;
}

.selectbox_label {
  width: 15%;
  margin-left: 10px;
  padding: 0;
}
.searchbox_out {
  background-color: #354551;
  padding: 0;
  padding: 10px;
  border: 0;
  color: aliceblue;
  cursor: pointer;
  border-radius: 10px;
  margin: 5px;
  width: 100%;
}

.ordermanag_btn {
  border: none;
  cursor: pointer;
  margin-left: 15px;
  text-align: center;
}
.selectbox_label {
  width: 15%;
  margin-left: 10px;
  padding: 0;
}
.selectbox_label_top {
  width: 5%;
  margin-left: 10px;
  padding: 0;
}
.product_input {
  width: 15%;
}
.price_input {
  width: 8%;
}
.discountFactor_input {
  width: 8%;
}
.sort_input {
  width: 10%;
}
.productType_input {
  width: 20%;
}
.productStatus_input {
  width: 20%;
}

.productName_input {
  width: 25%;
}

.dropdown_management {
  text-align: center;
  cursor: pointer;
  padding-top: 5px;
  padding-bottom: 5px;
}
.dropdown_management_menu {
  width: 100%;
}

.dropdown_management_btn {
  width: 100%;
}
.show_orderDetail {
  text-align: center;
}
.saveAll_btn {
  justify-content: flex-end;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.btn_bc {
  background-color: #354551;
  border: #354551;
}

.ondata_table_th {
  background-color: #272727;
  text-align: center;
}
.ondata_table_td {
  background-color: #7b7b7b;
  text-align: center;
  color: white;
}
/* 
.el-pagination__total {
  color: azure;
} */

/* .el-table__header-wrapper {
  z-index: 100;
  position: -webkit-sticky;
  top: 0;
  overflow: auto;
} */

/* // Neede to show the first row in the table
.el-table__body-wrapper {
  margin-top: 50px;
} */
</style>
