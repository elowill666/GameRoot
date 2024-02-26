<template>
  <div>
    <h1>SuggestionBox</h1>
  </div>
  <main style="width: 100%; display: flex">
    <table>
      <tr>
        <td>主旨</td>
        <td><input type="text" /></td>
      </tr>
      <tr>
        <td>Email</td>
        <td><input type="text" /></td>
      </tr>
      <tr>
        <td>內容</td>
        <td>
          <textarea v-model="withtextarea" /><span v-html="withtextarea"></span>
        </td>
      </tr>
    </table>
    <!-- auto-upload一上傳就傳送 -->
    <el-upload
      v-model="fileList"
      :file-list="fileList"
      action="http://localhost:8080/dashboard/photo/download"
      list-type="picture-card"
      name="file"
      :on-success="goupload"
      :auto-upload="true"
      limit="5"
    >
      <el-icon><Plus /></el-icon>
    </el-upload>
    <el-dialog v-model="dialogVisible">
      <img w-full :src="dialogImageUrl" alt="Preview Image" />
    </el-dialog>

    <div>
      <h1>我是回傳的照片</h1>
      <img :src="photo" />
    </div>
  </main>
</template>
<script setup>
import { ref, onMounted } from "vue";
import { Plus } from "@element-plus/icons-vue";
import axios from "axios";

const path = import.meta.env.VITE_JAVA_URL;
const fileList = ref([]);
const photo = ref([]);
function goupload(response, file, fileList) {
  console.log(response);
  console.log(file);
  console.log(fileList);
  photo.value = response;
}
</script>
<style lang="" scoped></style>
