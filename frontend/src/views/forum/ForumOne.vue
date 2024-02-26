<!-- 論壇主頁 -->
<template>
  <div class="backg">
    <div class="container-fluid mt-5 mb-5">
      <div class="row">
        <!-- 左側列表 -->
        <div class="col-md-3">
          <!-- 主分類下拉選單 -->
          <div>
            <div>
              <label for="mainCategory">遊戲類型：</label>
              <select v-model="selectedMainCategory" id="mainCategory" class="form-select"
                @change="findproduct(selectedMainCategory)">
                <option disabled value="">請選擇</option>
                <option v-for="category of searchSubtype" :key="category" :value="category.value">
                  {{ category.label }}
                </option>
              </select>
            </div>
            <!-- 次分類下拉選單 -->
            <div>
              <label for="subCategory">遊戲：</label>
              <select @change="filterArticles" v-model="selectedSubCategory" id="subCategory" class="form-select">
                <option disabled value="">請選擇</option>
                <option v-for="category of searchProductName" :key="category" :value="category.value">
                  {{ category.label }}
                </option>
              </select>
            </div>
          </div>
        </div>

        <!-- 主要內容區域 -->
        <div class="col-md-9">
          <!-- 搜尋框 -->
          <div class="container">
            <div class="input-group mb-3">
              <input v-model="keyword" @keyup.enter="clickSearch" type="text" class="form-control" placeholder="輸入關鍵字"
                aria-label="輸入關鍵字" aria-describedby="search-button" />
              <button @click="clickSearch" class="btn btn-outline-secondary" type="button" id="search-button">
                {{ searchButtonContent }}
              </button>
            </div>
            <!-- 文章列表排序選擇 -->
            <div class="row">
              <div class="col-md-6">
                <div class="input-group mb-3">
                  <button class="btn btn-outline-secondary" type="button" id="dropdownMenuButton"
                    data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    排序：{{ sortmethod }}
                  </button>
                  <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                    <li>
                      <a class="dropdown-item" href="#" @click="sortByDate">最新</a>
                    </li>
                    <li>
                      <a class="dropdown-item" href="#" @click="sortByCommentsNum">熱門</a>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <!-- 搜尋文章才出現 -->
            <div v-if="showSearchResult &&
              searchStatusCode === 200 &&
              searchResult !== null &&
              searchResult.length > 0
              " class="list-group">
              <h4>搜尋 "{{ keyword }}" 的相關結果：</h4>
              <router-link v-for="search in searchResult" :to="{
                name: 'articledetail',
                params: { id: search[0].articlesId },
              }" href="#" class="list-group-item list-group-item-action">
                <div class="d-flex w-100 justify-content-between">
                  <h5 class="mb-1" style="font-weight: bold">
                    {{ search[0].articleHead }}
                  </h5>
                  <small><img v-lazy="search[1].userphoto" style="
                        object-fit: cover;
                        width: 40px;
                        height: 40px;
                        border-radius: 50%;
                      " />&nbsp;{{ search[1].username }}</small>
                </div>
                <div class="d-flex w-100 justify-content-between">
                  <p class="mb-1">
                    {{
                      stripHtml(
                        search[0].articleText
                          ? search[0].articleText.slice(0, 50) + "..."
                          : ""
                      )
                    }}...
                  </p>
                  <small>發布時間：{{ formatDate(search[0].createdAt) }}</small>
                </div>
                <div>
                  <i class="bi bi-hand-thumbs-up-fill likeicon"></i>&nbsp;<span class="likesnum">{{
                    searchlikesnumberMap[search[0].articlesId] !== undefined
                    ? searchlikesnumberMap[search[0].articlesId]
                    : 0
                  }}</span>&nbsp;
                  <i class="bi bi-chat-left-dots-fill commenticon"></i>&nbsp;<span class="commentsnum">{{
                    searchcommentsnumberMap[search[0].articlesId] !== undefined
                    ? searchcommentsnumberMap[search[0].articlesId]
                    : 0
                  }}</span>
                </div>
              </router-link>
            </div>
            <div v-else-if="showSearchResult && searchStatusCode === 404">
              <h4>無符合搜尋結果</h4>
            </div>

            <!-- 文章列表 -->
            <div v-else class="list-group">
              <router-link v-for="article in articleList" :to="{
                name: 'articledetail',
                params: { id: article[0].articlesId },
              }" href="#" class="list-group-item list-group-item-action">
                <div class="d-flex w-100 justify-content-between">
                  <h5 class="mb-1" style="font-weight: bold">
                    {{ article[0].articleHead }}
                  </h5>
                  <small><img v-lazy="article[1].userphoto" style="
                        object-fit: cover;
                        width: 40px;
                        height: 40px;
                        border-radius: 50%;
                      " />&nbsp;{{ article[1].username }}</small>
                </div>
                <div class="d-flex w-100 justify-content-between">
                  <p class="mb-1">
                    {{
                      stripHtml(
                        article[0].articleText
                          ? article[0].articleText.slice(0, 50) + "..."
                          : ""
                      )
                    }}
                  </p>
                  <small>發布時間：{{ formatDate(article[0].createdAt) }}</small>
                </div>
                <div>
                  <i class="bi bi-hand-thumbs-up-fill likeicon"></i>&nbsp;<span class="likesnum">{{
                    likesnumberMap[article[0].articlesId] !== undefined
                    ? likesnumberMap[article[0].articlesId]
                    : 0
                  }}</span>&nbsp;
                  <i class="bi bi-chat-left-dots-fill commenticon"></i>&nbsp;<span class="commentsnum">{{
                    commentsnumberMap[article[0].articlesId] !== undefined
                    ? commentsnumberMap[article[0].articlesId]
                    : 0
                  }}</span>
                </div>
                <div></div>
              </router-link>
            </div>
            <!-- 撰寫文章按鈕 -->
            <!-- <button class="btn btn-primary btn-lg fixed-button btn-warning" :style="buttonStyle" @click="publishArticle">
              <i class="bi bi-pencil-square"></i>
            </button> -->
            <write-article v-if="showAddArticle"></write-article>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import axios from "axios";
import { onMounted, ref, watch } from "vue";
import "bootstrap-icons/font/bootstrap-icons.css";
import router from "@/router";
import VueLazyload from "vue-lazyload";
import WriteArticle from "./WriteArticle.vue";

//向後端請求的數位遊戲子分類
const searchSubtype = ref([]);
//找到的商品名字
const searchProductName = ref([]);
const url = import.meta.env.VITE_JAVA_URL;
const mainCategories = ["全部", "冒險", "動作", "模擬", "競賽", "其他"]; // 主分類選項
const subCategories = ref([]); // 次分類選項
const selectedMainCategory = ref(""); // 選中的主分類
const selectedSubCategory = ref(""); // 選中的次分類
const showAddArticle = ref(true);

// 監聽選中的主分類變化，更新次分類選項
watch(selectedMainCategory, (newMainCategory) => {
  if (newMainCategory === "冒險") {
    subCategories.value = ["黑色沙漠"];
  } else if (newMainCategory === "動作") {
    subCategories.value = ["霍格華茲的傳承"];
  } else if (newMainCategory === "模擬") {
    subCategories.value = ["神奇寶貝系列"];
  } else if (newMainCategory === "競賽") {
    subCategories.value = ["League of Legends"];
  } else if (newMainCategory === "其他") {
    subCategories.value = ["神魔之塔"];
  } else if (newMainCategory === "全部") {
    subCategories.value = ["全部"];
  } else {
    subCategories.value = [];
  }
  selectedSubCategory.value = ""; // 清空選中的次分類
});

const filterArticles = async () => {
  console.log("call filterArticles");
  console.log("subCategories.value=" + selectedSubCategory.value);
  try {
    if (selectedSubCategory.value == "全部") {
      fetchData();
    } else {
      const response = await axios.get(
        `${url}/articles/findfiltered/${selectedSubCategory.value}`
      );
      // 解構後端返回的數據
      const {
        articleList: fetchedarticleList,
        commentsnumberMap: fetchedcommentsnumberMap,
        likesnumberMap: fetchedlikesnumberMap,
      } = response.data;
      // 更新組件中的數據
      articleList.value = fetchedarticleList;
      commentsnumberMap.value = fetchedcommentsnumberMap;
      likesnumberMap.value = fetchedlikesnumberMap;
      sortByCommentsNum();
    }
  } catch (error) {
    console.error("Error fetching data:", error);
    // 根據需要進行錯誤處理
  }
};

const articleList = ref([]);
const commentsnumberMap = ref(null);
const likesnumberMap = ref(null);
// 顯示文章列表
const fetchData = async () => {
  try {
    const response = await axios.get(`${url}/articles/find`);
    // 解構後端返回的數據
    const {
      articleList: fetchedarticleList,
      commentsnumberMap: fetchedcommentsnumberMap,
      likesnumberMap: fetchedlikesnumberMap,
    } = response.data;
    // 更新組件中的數據
    articleList.value = fetchedarticleList;
    commentsnumberMap.value = fetchedcommentsnumberMap;
    likesnumberMap.value = fetchedlikesnumberMap;
    sortByDate();
  } catch (error) {
    console.error("Error fetching data:", error);
    // 根據需要進行錯誤處理
  }
};

// const articlelist = ref([])
onMounted(() => {
  //顯示文章列表
  // axios.get(`${url}/articles/find`)
  //   .then(response => {
  //     console.log(response.data);
  //     articlelist.value = response.data
  //   })
  //   .catch(error => { console.log(error) })
  fetchData();
  findproductsubType();
  showAddArticleButton();
});

// 若登入者為管理員,不顯示發文按鈕
const showAddArticleButton = async () => {
  await axios
    .get(`${url}/user/authenticate`, { withCredentials: true })
    .then((res => {
      if (res.data == false) {
        showAddArticle.value = true;
      } else {
        axios
          .get(`${url}/user/adminAuthenticate`, { withCredentials: true })
          .then((response) => {
            if (response.data == true) {
              showAddArticle.value = false;
              console.log("showAddArticle.value=" + showAddArticle.value);
            }
          })
      }
    }))
}

const searchButtonContent = ref("搜尋");
const keyword = ref("");
// const searchResult = ref([]);
const showSearchResult = ref(false);
const searchStatusCode = ref(null);
// 切換搜尋狀態
const clickSearch = () => {
  if (showSearchResult.value) {
    // 取消搜尋
    closeSearch();
  } else {
    // 開始搜尋
    startSearch();
  }
};

const searchResult = ref([]);
const searchcommentsnumberMap = ref(null);
const searchlikesnumberMap = ref(null);
// 搜尋關鍵字
const fetchSearchData = async () => {
  try {
    const response = await axios.get(
      `${url}/articles/search?keyword=${keyword.value}`
    );
    // 解構後端返回的數據
    const {
      searchResult: fetchedsearchResult,
      searchcommentsnumberMap: fetchedsearchcommentsnumberMap,
      searchlikesnumberMap: fetchedsearchlikesnumberMap,
    } = response.data;
    // 更新組件中的數據
    searchResult.value = fetchedsearchResult;
    searchcommentsnumberMap.value = fetchedsearchcommentsnumberMap;
    searchlikesnumberMap.value = fetchedsearchlikesnumberMap;

    showSearchResult.value = true;
    searchButtonContent.value = "取消";
    searchStatusCode.value = response.status;
    sortByDate();
  } catch (error) {
    console.error("Error fetching data:", error);
    if (error.response) {
      // 如果有回應，但狀態碼不是200
      searchStatusCode.value = error.response.status;
      searchButtonContent.value = "取消";
      showSearchResult.value = true;
    } else {
      // 如果沒有回應，可能是網絡錯誤等
      console.error("Error:", error.message);
    }
  }
};

const startSearch = () => {
  if (!keyword.value.trim()) {
    return; // 未輸入關鍵字並點擊搜尋時,不做任何回應
  }
  fetchSearchData();
  //   axios.get(`${url}/articles/search?keyword=${keyword.value}`)
  //     .then(response => {
  //       searchResult.value = response.data;
  //       showSearchResult.value = true;
  //       searchButtonContent.value = '取消';
  //       searchStatusCode.value = response.status;
  //     })
  //     .catch(error => {
  //       if (error.response) {
  //         // 如果有回應，但狀態碼不是200
  //         searchStatusCode.value = error.response.status;
  //         searchButtonContent.value = '取消';
  //         showSearchResult.value = true;
  //       } else {
  //         // 如果沒有回應，可能是網絡錯誤等
  //         console.error('Error:', error.message);
  //       }
  //     });
};

const closeSearch = () => {
  showSearchResult.value = false;
  searchButtonContent.value = "搜尋"; // 將按鈕內容還原為 "搜尋"
  keyword.value = ""; // 清空關鍵字
  console.log("取消");
  console.log(showSearchResult.value);
  console.log(searchStatusCode);
};

// 定義一個函數來去除 HTML 標籤
const stripHtml = (value) => {
  if (!value) return "";
  // 創建 DOMParser 實例
  const parser = new DOMParser();
  // 將 HTML 字串解析為 DOM 文檔
  const doc = parser.parseFromString(value, "text/html");
  // 從解析後的 DOM 文檔中獲取純文本內容
  return doc.body.textContent || "";
};

const sortmethod = ref("最新");
// 文章列表排序(日期)
const sortByDate = () => {
  articleList.value.sort((a, b) => {
    return new Date(b[0].createdAt) - new Date(a[0].createdAt);
  });
  searchResult.value.sort((a, b) => {
    return new Date(b[0].createdAt) - new Date(a[0].createdAt);
  });
  sortmethod.value = "最新";
};
// 文章列表排序(留言數)
const sortByCommentsNum = () => {
  articleList.value.sort((a, b) => {
    // 從Map中獲取文章ID對應的評論數進行比較
    return (
      commentsnumberMap.value[b[0].articlesId] -
      commentsnumberMap.value[a[0].articlesId]
    );
  });
  searchResult.value.sort((a, b) => {
    return (
      searchcommentsnumberMap.value[b[0].articlesId] -
      searchcommentsnumberMap.value[a[0].articlesId]
    );
  });
  sortmethod.value = "熱門";
};

// 轉換時間格式為"yyyy-MM-dd hh:mm"
const formatDate = (value) => {
  const date = new Date(value);
  const formattedDate = `${date.getFullYear()}-${padZero(
    date.getMonth() + 1
  )}-${padZero(date.getDate())} ${padZero(date.getHours())}:${padZero(
    date.getMinutes()
  )}`;
  return formattedDate;
};
const padZero = (num) => {
  return num.toString().padStart(2, "0");
};

// const buttonStyle = {
//   position: 'fixed',
//   bottom: '20px',
//   left: '20px',
// };

// const publishArticle = () => {
//   router.push({ name: 'articlewrite' });
// }

async function findproductsubType() {
  let productType = {
    productType: "digitalGames",
  };
  try {
    const response = await axios.post(
      `${url}/dashboard/product/subtype`,
      productType
    );
    searchSubtype.value = [];
    searchProductName.value = [];
    for (let temp of response.data) {
      let tempobj = {
        value: temp,
        label: formateSubtype(temp),
      };
      searchSubtype.value.push(tempobj);
    }

    return response.data;
  } catch (error) {
    console.log(error);
  }
}

async function findproduct(type) {
  let productSubtype = {
    productSubtype: type,
  };
  try {
    const response = await axios.post(
      `${url}/dashboard/product/productname`,
      productSubtype
    );
    console.log(response.data);
    searchProductName.value = [];
    for (let temp of response.data) {
      let tempobj = {
        value: temp,
        label: temp,
      };
      searchProductName.value.push(tempobj);
    }

    return response.data;
  } catch (error) {
    console.log(error);
  }
}

function formateSubtype(subtype) {
  if ("adventure" === subtype) {
    return "冒險";
  } else if ("action" === subtype) {
    return "動作";
  } else if ("simulation" === subtype) {
    return "模擬";
  } else if ("racing" === subtype) {
    return "競賽";
  } else if ("others" === subtype) {
    return "其他";
  }
}
</script>

<style scoped>
.backg {
  background: url("../../../public/images/Forumimg.jpg") center/cover fixed;
  margin: 0;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.2);
  background-blend-mode: multiply;
}

.col-md-3 {
  /* 調整左側列表的寬度 */
  max-width: 250px;
}

.fixed-button {
  margin-left: 10px;
  width: 70px;
  height: 70px;
  font-size: 20px;
}

.likeicon {
  color: #ffac12;
}

.commenticon {
  color: #8c8cff;
}

/* write-article{
  margin-left: 10px;
    width: 70px;
    height: 70px;
    font-size: 20px;
} */
</style>
