<template>
  <div class="block " style="margin:0;padding-bottom:30px ; width: 1160px;">
    <el-carousel height="300px">
      <el-carousel-item v-for="(item, index) in items" :key="item">
        <img style="height: 400px;width: 1160px;object-fit:cover;" :src="item" :alt="index">
        <!-- <img style="height: 200px; width: 1200px;" src="../../../public/images/ProductAds/shooting-games-ads-888225351.png"  alt="1"> -->
      </el-carousel-item>
    </el-carousel>

  </div>
</template>


<script setup>
import { ref } from 'vue';

const items = ref([
  'https://cdn.akamai.steamstatic.com/steam/apps/1020790/extras/NBUNSC_GIF2_NarutoSasuke_616x236.gif?t=1706137497',
  '../../../public/images/ProductAds/shooting-games-ads-888225351.png',
  'https://cdn.akamai.steamstatic.com/steam/apps/578080/ss_2da334ea597d9588aaa8c716d71b3c2e60a69853.1920x1080.jpg?t=1701911655',
  'https://cdn.akamai.steamstatic.com/steam/apps/1868140/extras/DAVE_THE_DIVER_GIF_1.gif?t=1707096700'
])

</script>


<style scoped></style>
