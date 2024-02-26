<template>
  <div class="article" v-for="(item, index) in productArt">
    <RouterLink
      :to="{ name: 'articledetail', params: { id: item[1]?.articlesId } }"
    >
      <span>{{ item[1]?.articleHead }}</span>
      <span style="float: right">{{
        item[1]?.createdAt.substring(0, 10)
      }}</span>
    </RouterLink>
    <hr />
    <div style="color: rgb(169, 169, 169)">文章概述</div>
    <span>{{ stripHtml(item[0]?.shortInfo).substring(0,50)+"..." }}</span>
  </div>
</template>

<script setup>
import { stripHtml } from "@/utils/forstripHtml";
const prop = defineProps(["productArt"]);


</script>

<style scoped>
a:link {
  color: gray;
}
a:visited {
  color: black;
}
.article {
  /* border-top: 1px grey solid; */
  border-bottom: 1px grey solid;
  background-color: white;
  color: black;
  padding: 30px;
}
</style>
