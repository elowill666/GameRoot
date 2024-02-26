<template>
         <Editor  :init="tinyMCEConfig" ></Editor>
</template>
    
<script setup>
import { ref, watch } from 'vue';
import axios from "axios";
import Editor from "@tinymce/tinymce-vue";
    //文本編譯器上傳圖片
const handleImageUpload = (blobInfo, progress) => new Promise((resolve, reject) => {
    const file = blobInfo.blob();
    const formData = new FormData();
    formData.append('file', file, file.name);
    axios.post('http://localhost:8080/upload', formData)
        .then(response => {
            console.log(response.data)
            if (response.length != 0) {
                const imageUrl = response.data;
                // 將圖片的 URL 插入到編輯器內容中
                // const imgElement = `<img src="${imageUrl}" alt="uploaded image">`;
                resolve(imageUrl);
                return;
            }
        })
        .catch(() => {
            reject("怪怪的")
        });
});
const tinyMCEConfig = {
    selector: '#tinydemo',
    skin: 'oxide-dark',
    language: 'zh_TW',
    language_url: 'https://cdn.tiny.cloud/1/no-api-key/tinymce/6.8.2/langs/zh_TW.js',
    plugins:
        'preview searchreplace autolink directionality visualblocks visualchars fullscreen image link media template code codesample help autosave autoresize',
    toolbar:
        'code undo redo restoredraft  | forecolor backcolor bold italic underline strikethrough link anchor | alignleft aligncenter alignright alignjustify outdent indent | \
        styleselect formatselect fontselect fontsizeselect | bullist numlist | \
        table image media charmap emoticons hr pagebreak insertdatetime| fullscreen',
    height: 650, //編輯器高度
    width: 650,
    min_height: 400,
    images_upload_handler: handleImageUpload,
};
</script>
    
<style>
    
</style>