<template>
  <div class="comment" v-for="(item, index) in productCom">
    <span style="padding-right: 10px;">
      <el-avatar :src="item[2]" />
    </span>
    <span style="padding-right: 10px">{{
      item[1].substring(0, 1) + "*****" + item[1].substring(item[1].length - 1)
    }}</span>
    <span
      ><el-rate
        v-model="item[0].score"
        disabled
        text-color="#ff9900"

    /></span>
    <span style="float: right;">
    {{ item[0].createAt.substring(0,10) }}
    </span>
    <hr />
    <h4>{{ item[0].text }}</h4>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";

const prop = defineProps(["productCom"]);

// onMounted(async () => {
//     const comment = ref({prop});
//     value.value = prop.productCom[0][0].score;

// })
</script>

<style scoped>
.comment {
  /* border-top: 1px grey solid; */
  border-bottom: 1px grey solid;
  background-color: white;
  color: gray;
  padding: 30px;
}
</style>
