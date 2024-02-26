<template>
    <div class="editor-container">
        <form @submit.prevent="publish">
            <div class="text-start">

                <div class="row">
                    <div class="col">
                        <label for="mainCategory">遊戲類型：</label>
                        <input type="text" id="mainCategory" class="form-control readonly-input" :value="articletype"
                            readonly>
                    </div>
                    <div class="col">
                        <label for="subCategory">遊戲：</label>
                        <input type="text" id="subCategory" class="form-control readonly-input" :value="articlesubtype"
                            readonly>
                    </div>
                </div>




                <!-- 文字標題框 -->
                <div class="row mt-3">
                    <div class="col">
                        <label for="title">標題：</label>
                        <textarea v-model="title" id="title" class="form-control mb-3"
                            style="width: 100%; height: 20px; overflow-y: auto; word-wrap: break-word;"></textarea>
                    </div>
                </div>
            </div>

            <!-- 文章內容編輯器 -->
            <Editor :init="tinyMCEConfig" v-model="content"></Editor>

            <div class="button-container">
                <!-- 取消按鈕 -->
                <button type="button" class="btn btn-warning me-2" @click="cancel">取消</button>
                <!-- 發布按鈕 -->
                <button type="submit" class="btn btn-warning">發布</button>
            </div>
        </form>
    </div>
</template>


<script setup>
import { ref, watch, onMounted } from 'vue';
import axios from "axios";
import Editor from "@tinymce/tinymce-vue";
import Swal from "sweetalert2";
import router from '@/router';
import tinymce from 'tinymce';
import { useRoute } from 'vue-router';

const mainCategories = ['主分類1', '主分類2', '主分類3']; // 主分類選項
const subCategories = ref([]); // 次分類選項
const selectedMainCategory = ref(''); // 選中的主分類
const selectedSubCategory = ref(''); // 選中的次分類
const url = import.meta.env.VITE_JAVA_URL;
const title = ref(''); // 標題
const content = ref('');

// 監聽選中的主分類變化，更新次分類選項
// watch(selectedMainCategory, (newMainCategory) => {
//     if (newMainCategory === '主分類1') {
//         subCategories.value = ['次分類1-1', '次分類1-2', '次分類1-3'];
//     } else if (newMainCategory === '主分類2') {
//         subCategories.value = ['次分類2-1', '次分類2-2', '次分類2-3'];
//     } else if (newMainCategory === '主分類3') {
//         subCategories.value = ['次分類3-1', '次分類3-2', '次分類3-3'];
//     } else {
//         subCategories.value = [];
//     }
// selectedSubCategory.value = ''; // 清空選中的次分類
// });

//文本編譯器上傳圖片
const handleImageUpload = (blobInfo, progress) => new Promise((resolve, reject) => {
    const file = blobInfo.blob();
    const formData = new FormData();
    formData.append('file', file, file.name);
    axios.post(`${url}/upload`, formData)
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
    menubar: false,
    statusbar: false,
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
    width: 1600,
    min_height: 600,
    images_upload_handler: handleImageUpload,
};

const article = ref(null);
const commentsdetail = ref([]);
const images = ref([]);
const commentsimages = ref([]);
const articleuser = ref(null);
const commentsnumber = ref(null);
const likesnumber = ref(null);
const commentdetail = ref([]);
const commentlikesdetail = ref([]);
const commentLikesMap = ref();
const articleId = useRoute().params.id;
const articletype = ref('');
const articlesubtype = ref('');
// 從後端獲取原有文章的標題和內容
const fetchData = async () => {
    try {
        console.log("articleId=" + articleId)
        const response = await axios.get(`${url}/articles/detail/${articleId}`);
        // 解構後端返回的數據
        const { article: fetchedArticle, commentsdetail: fetchedcommentsdetail, images: fetchedimages, commentsimages: fetchedcommentsimages, articleuser: fetchedarticleuser, commentsnumber: fetchedcommentsnumber, likesnumber: fetchedlikesnumber, commentdetail: fetchedcommentdetail, commentlikesdetail: fetchedcommentlikesdetail, commentLikesMap: fetchedcommentLikesMap } = response.data;
        // 更新組件中的數據
        // article.value = fetchedArticle;
        // comments.value = fetchedComments;
        // commentsdetail.value = fetchedcommentsdetail;
        // images.value = fetchedimages;
        // commentsimages.value = fetchedcommentsimages;
        // articleuser.value = fetchedarticleuser;
        // commentsnumber.value = fetchedcommentsnumber;
        // likesnumber.value = fetchedlikesnumber;
        // commentdetail.value = fetchedcommentdetail;
        // commentlikesdetail.value = fetchedcommentlikesdetail;
        // commentLikesMap.value = fetchedcommentLikesMap;
        title.value = fetchedArticle.articleHead;
        content.value = fetchedArticle.articleText;
        // articletype.value = fetchedArticle.articleType;
        if (fetchedArticle.articleType == 'action') {
            articletype.value = '動作';
        }else if(fetchedArticle.articleType == 'simulation'){
            articletype.value = '模擬';
        }else if(fetchedArticle.articleType == 'adventure'){
            articletype.value = '冒險';
        }else if(fetchedArticle.articleType == 'racing'){
            articletype.value = '競賽';
        }else if(fetchedArticle.articleType == 'others'){
            articletype.value = '其他';
        }
        articlesubtype.value = fetchedArticle.articleGameType;
        console.log(articletype.value);
    } catch (error) {
        // console.error('Error fetching data:', error);
        // 根據需要進行錯誤處理
    }
};

onMounted(() => {
    fetchData();
});

// 完成編輯文章,發布
const publish = () => {
    if (!title.value || !content.value) {
        // 如果有任何欄位為空，顯示錯誤消息並返回
        Swal.fire({
            title: '所有欄位皆為必填',
            icon: 'warning',
            showConfirmButton: true,
            confirmButtonText: '確認'
        });
        return;
    }

    const formData = {
        articlesId: articleId,
        articleHead: title.value,
        articleText: content.value
    };

    axios.put(`${url}/articles/edit`, formData)
        .then(response => {
            // console.log(response.data);
            // console.log("click publish")
            // 在這裡可以處理後端返回的數據或執行相應的操作
            Swal.fire({
                // title: response.data.text,
                title: '編輯成功',
                icon: "success",
                showConfirmButton: false,
                timer: 1500
                // confirmButtonText: "確認",
            });
            setTimeout(() => {
                Swal.close();
            }, 3000);
            // 在發布成功後，獲取返回的文章ID
            const newArticleId = response.data.articleId;
            // 導到該文章的詳細頁面
            router.push({ name: 'articledetail', params: { id: newArticleId } });
        })
        .catch(error => {
            console.error(error);
            Swal.fire({
                title: error.response.data.text,
                icon: "error",
                showConfirmButton: true,
                confirmButtonText: "確認",
            });
        });
};

const cancel = () => {
    tinymce.activeEditor.setContent('');
    router.back();
}

</script>

<style scoped>
.editor-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100vh;
}

.button-container {
    display: flex;
    justify-content: center;
}

.readonly-input {
    cursor: default;
    background-color: #878787;
    border-color: #878787;
}
</style>