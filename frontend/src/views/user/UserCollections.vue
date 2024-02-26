<template>
        <div class="backg">
          <select class="form-select form-select-lg mb-3" aria-label=".form-select-lg example" style="width: 200px;margin-left: 230px;" v-model="pageData.sort" @change="changeSort">
  <!-- <option selected>排序方式</option> -->
  <option v-for="(sort,value) in sortOptions" :value="sort" :key="sort" >{{ value }}</option>
</select>
        <!-- 主要內容區域 -->
        <div class="col-md-9" >
        <!-- 文章列表 -->
        <div  class="list-group" >
              <div v-for="article in articles[0]"
                @click="pushtoforum(article.articlesId)"
                class="list-group-item list-group-item-action" >
                <div class="d-flex w-100 justify-content-between">
                  <h5 class="mb-1" style="font-weight: bold;">{{ article.articleHead }}</h5>
                  <span>
                    <InsertColletions :articleId="article.articlesId"></InsertColletions >
                  </span>
                </div>
                <div class="d-flex w-100 justify-content-between">
                  <p class="mb-1">{{ stripHtml(article.articleText ? article.articleText.slice(0, 50) + '...' : '')
                  }}</p>
                  <small>發布時間：{{ formatDate(article.createdAt) }}</small>
                </div>
                <div>
                  
                  <i class="bi bi-hand-thumbs-up-fill likeicon"></i>&nbsp;<span class="likesnum">{{
                    article.likes}}</span>&nbsp;
                  <i class="bi bi-chat-left-dots-fill commenticon"></i>&nbsp;<span class="commentsnum">{{
                    article.comments}}</span>
                </div>
              </div>
            </div>
            <el-pagination background="black" layout="prev, pager, next" :page-size="pageData.rows" :total="articles[1]"  @current-change="changePage(pageData.start)" v-model:current-page="pageData.start" style="padding: 20px;"/>
        </div>
        
</div>
</template>
    
<script setup >
import axios from 'axios';
import { onMounted, ref } from 'vue';
import { format } from '../../../node_modules/date-fns'
import { useRoute } from 'vue-router';
import InsertColletions from '../forum/InsertCollecitons.vue'
import router from '@/router';
const articles = ref([])
const pageData = ref({
  start:1,
  rows:5,
  sort:"createdDESC"
})

const route = useRoute();

onMounted(()=>{
  pageData.value.start = Number(route.params.page);
  getCollections();
})

//分頁式呼叫
async function getCollections(){
   const VITE_API_USERCOLLECTION = `${import.meta.env.VITE_JAVA_URL}/collections/findArticleById`
   let request = {
    page:{
      start:pageData.value.start,
      rows:pageData.value.rows,
      sort:pageData.value.sort
    }
  }
  console.log("request",request);
  const response = await axios.post(VITE_API_USERCOLLECTION, request.page, { withCredentials: true });
  articles.value = response.data;
  console.log("articles = ",articles.value);
}
async function changePage(value){
  console.log("page = ",value);
  router.push({
    name:"UserCollections",
    params:{
      page:value
    }
  })
  getCollections();
}
async function changeSort(){
  router.push({
    name:"UserCollections",
    params:{
      page:1
    }
  }).then(()=>{
    pageData.value.start = 1;
    getCollections();

  })


}
let sortOptions = {
  "文章新->舊":"createdDESC",
  "文章舊->新":"createdASC",
  "留言數多->少":"commentsDESC",
  "留言數少->多":"commentsASC",
  "讚數多->少":"likesDESC",
  "讚數少->多":"likesASC",
}


// 定義一個函數來去除 HTML 標籤
const stripHtml = (value) => {
  if (!value) return '';
  // 創建 DOMParser 實例
  const parser = new DOMParser();
  // 將 HTML 字串解析為 DOM 文檔
  const doc = parser.parseFromString(value, 'text/html');
  // 從解析後的 DOM 文檔中獲取純文本內容
  return doc.body.textContent || "";
};

//讓col內時間顯示為1970-01-01
function formatDate(dateTime) {
  let date = new Date(dateTime);
  // 讓此格式的時間變成String"1970-01-01T00:00:00.000Z"
  if (isNaN(date.getTime()) || dateTime == null) {
    // 如果 date 不是有效的 Date 對象
    return "yyyy-MM-dd";
  }
  return format(date, " yyyy-MM-dd");
}

function pushtoforum(aId){
  router.push({
    name:"articledetail",
    params:{
      id:aId
    }
  })
}
</script>
    
<style scoped>
    .backg {
  background: url('../../../public/images/Forumimg.jpg') center/cover fixed;
  margin: 0;
  padding: 50px;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.2);
  background-blend-mode: multiply;
}
.likeicon {
  color: #FFAC12;
}

.commenticon {
  color: #8C8CFF;
}
.col-md-9 {
  margin: auto;
}
</style>