<template>
    <el-pagination background="black" layout="prev, pager, next" :page-size="rows" :total="totalItem"  @current-change="changeHandler(currentPage)" v-model:current-page="currentPage"/>
</template>
    
<script setup>
import {defineProps ,defineEmits, ref} from 'vue';

const prop  = defineProps(["totalItem","rows"]);
const emit = defineEmits(["changePage"])
const currentPage = ref(1);
function changeHandler(currentPage){
    // console.log("111111",currentPage)
    emit('changePage',currentPage)
}
    
</script>
    
<style  >

/* .el-pagination.is-background .el-pager li:not(.is-disabled).is-active {
    background-color: #354551 !important;
} */
.el-pagination.is-background .el-pager li:not(.disabled).is-active{
    background-color: #354551;
    color: #fff;
  }
  .el-pagination.is-background .el-pager li.is-active {
    color: #fff;
    cursor: default;
  }
  .el-pagination.is-background .el-pager li:hover {
    color: #354551;
  }
  .el-pagination.is-background .el-pager li:not(.disabled):hover {
    color: #354551;
  }
  .el-pagination.is-background
  .el-pager
  li:not(.disabled).active:hover {
    background-color: #354551;
    color: #fff;
  }
  .el-select-dropdown__item.selected {
    color: #354551 !important;
  }
  .el-select .el-input.is-focus .el-input__inner {
    border-color: #354551 !important;
  }
  .el-pagination .el-select .el-input .el-input__inner:hover {
    border-color: #354551 !important;
  }
  .el-input.is-active .el-input__inner,
  .el-input__inner:focus {
    border-color: #354551 !important;
  }


</style>