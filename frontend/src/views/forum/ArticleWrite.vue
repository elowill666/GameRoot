<template>
  <div class="editor-container">
    <form @submit.prevent="publish">
      <div class="text-start">
        <!-- 主分類下拉選單 -->
        <div class="row">
          <div class="col">
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
          <div class="col">
            <label for="subCategory">遊戲：</label>
            <select v-model="selectedSubCategory" id="subCategory" class="form-select">
              <option disabled value="">請選擇</option>
              <option v-for="category of searchProductName" :key="category" :value="category.value">
                {{ category.label }}
              </option>
            </select>
          </div>
        </div>

        <!-- 文字標題框 -->
        <div class="row mt-3">
          <div class="col">
            <label for="title">標題：</label>
            <textarea v-model="title" id="title" class="form-control mb-3" style="
                width: 100%;
                height: 20px;
                overflow-y: auto;
                word-wrap: break-word;
              "></textarea>
          </div>
        </div>
      </div>

      <!-- 文章內容編輯器 -->
      <Editor :init="tinyMCEConfig" v-model="content"></Editor>

      <div class="button-container">
        <!-- 取消按鈕 -->
        <button type="button" class="btn btn-warning me-2" @click="cancel">
          取消
        </button>
        <!-- 發布按鈕 -->
        <button type="submit" class="btn btn-warning">發布</button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from "vue";
import axios from "axios";
import Editor from "@tinymce/tinymce-vue";
import Swal from "sweetalert2";
import router from "@/router";
import tinymce from "tinymce";

const mainCategories = ["冒險", "動作", "模擬", "競賽", "其他"]; // 主分類選項
const subCategories = ref([]); // 次分類選項
const selectedMainCategory = ref(""); // 選中的主分類
const selectedSubCategory = ref(""); // 選中的次分類
const url = import.meta.env.VITE_JAVA_URL;
const title = ref(""); // 標題
const content = ref("");
const userId = ref(null);
//向後端請求的數位遊戲子分類
const searchSubtype = ref([]);
//找到的商品名字
const searchProductName = ref([]);

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
  } else {
    subCategories.value = [];
  }
  selectedSubCategory.value = ""; // 清空選中的次分類
});

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
          return;
        }
      })
      .catch(() => {
        reject("怪怪的");
      });
  });
const tinyMCEConfig = {
  selector: "#tinydemo",
  menubar: false,
  statusbar: false,
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
  width: 1600,
  min_height: 600,
  images_upload_handler: handleImageUpload,
};
// 發布文章
const publish = () => {
  if (
    !selectedMainCategory.value ||
    !selectedSubCategory.value ||
    !title.value ||
    !content.value
  ) {
    // 如果有任何欄位為空，顯示錯誤消息並返回
    Swal.fire({
      title: "所有欄位皆為必填",
      icon: "warning",
      showConfirmButton: true,
      confirmButtonText: "確認",
    });
    return;
  }

  const formData = {
    userId: userId.value,
    mainCategory: selectedMainCategory.value,
    subCategory: selectedSubCategory.value,
    title: title.value,
    content: content.value,
    status: "normal",
  };

  axios
    .post(`${url}/articles/add`, formData)
    .then((response) => {
      // console.log(response.data);
      // console.log("click publish")
      // 在這裡可以處理後端返回的數據或執行相應的操作
      Swal.fire({
        title: response.data.text,
        icon: "success",
        showConfirmButton: false,
        timer: 1500,
        // confirmButtonText: "確認",
      });
      setTimeout(() => {
        Swal.close();
      }, 3000);
      // 在發布成功後，獲取返回的文章ID
      const newArticleId = response.data.articleId;
      // 導到該文章的詳細頁面
      router.push({ name: "articledetail", params: { id: newArticleId } });
    })
    .catch((error) => {
      console.error(error);
      Swal.fire({
        title: error.response.data.text,
        icon: "error",
        showConfirmButton: true,
        confirmButtonText: "確認",
      });
    });
};

onMounted(() => {
  // 找目前登入者是誰
  axios
    .get(`${url}/user/profile`, { withCredentials: true })
    .then((response) => {
      userId.value = response.data.id;
      console.log("userId=" + userId.value);
    })
    .catch((error) => { });
  findproductsubType();
});

const cancel = () => {
  tinymce.activeEditor.setContent("");
  router.back();
};

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
.editor-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;

  background: url("../../../public/images/Forumimg.jpg") center/cover fixed;
  margin: 0;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.2);
  background-blend-mode: multiply;
}

.button-container {
  display: flex;
  justify-content: center;
}
</style>
