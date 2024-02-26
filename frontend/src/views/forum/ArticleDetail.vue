<!-- 單篇文章頁面 -->
<template>
  <!-- 文章 -->
  <div class="backg">
    <div class="container mt-5 mb-5">
      <div v-if="article" class="card">
        <div class="card-body">
          <div class="row">
            <div class="col">
              <div class="d-flex justify-content-between">
                <h2 style="font-weight: bold">{{ article.articleHead }}</h2>
                <div class="col-auto">
                  <div
                    class="dropdown"
                    v-if="showEditButton || showDeleteComment"
                  >
                    <button
                      class="btn btn-secondary dropdown-toggle"
                      type="button"
                      id="dropdownMenuButton"
                      data-bs-toggle="dropdown"
                      aria-expanded="false"
                    >
                      <i class="bi bi-gear"></i>
                    </button>
                    <ul
                      class="dropdown-menu"
                      aria-labelledby="dropdownMenuButton"
                    >
                      <li v-if="showEditButton">
                        <button
                          @click="editArticle"
                          class="dropdown-item w-100"
                          style="width: 100px"
                        >
                          <i class="bi bi-pencil-fill"></i> 編輯
                        </button>
                      </li>
                      <li v-if="showDeleteArticle">
                        <button
                          @click="hideArticle"
                          class="dropdown-item w-100"
                          style="width: 100px"
                        >
                          <i class="bi bi-x-lg"></i> 刪除
                        </button>
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
              <h5 style="font-weight: bold">
                【{{ article.articleGameType }}】
              </h5>
            </div>
            <hr />
            <!-- <div class="col-auto">
              <button v-if="showEditButton" @click="editArticle" class="btn btn-primary" style="width: 40px">
                <i class="bi bi-pencil-fill"></i>
              </button>
            </div>
            <div class="col-auto">
              <button v-if="showDeleteArticle" @click="hideArticle" class="btn btn-primary" style="width: 40px">
                <i class="bi bi-x-lg"></i>
              </button>
            </div> -->
            <!-- <div class="col-auto">
              <div class="dropdown" v-if="showEditButton || showDeleteComment">
                <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton"
                  data-bs-toggle="dropdown" aria-expanded="false"><i class="bi bi-gear"></i>
                </button>
                <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                  <li v-if="showEditButton">
                    <button @click="editArticle" class="dropdown-item w-100" style="width: 100px;">
                      <i class="bi bi-pencil-fill"></i> 編輯
                    </button>
                  </li>
                  <li v-if="showDeleteArticle">
                    <button @click="hideArticle" class="dropdown-item w-100" style="width: 100px;">
                      <i class="bi bi-x-lg"></i> 刪除
                    </button>
                  </li>
                </ul>
              </div>
            </div> -->
          </div>

          <div class="row">
            <div class="col">
              <el-tooltip
                effect="customized"
                popper-class="mytip"
                v-if="showChatRoom.isLogin && userId != articleuser.id"
              >
                <img
                  v-lazy="articleuser.userphoto"
                  class="image-container"
                  style="
                    object-fit: cover;
                    width: 60px;
                    height: 60px;
                    border-radius: 50%;
                  "
                />
                <template #content>
                  <di
                    @click="addChat(articleuser.id)"
                    style="color: #ffc928; cursor: pointer"
                  >
                    <el-icon size="28px">
                      <ChatDotRound />
                    </el-icon>
                  </di>
                </template>
              </el-tooltip>
              <img
                v-lazy="articleuser.userphoto"
                class="image-container"
                style="
                  object-fit: cover;
                  width: 60px;
                  height: 60px;
                  border-radius: 50%;
                "
                v-else
              />
              <span
                >&nbsp;{{ articleuser.username }}，發布時間：{{
                  formatDate(article.createdAt)
                }}</span
              >
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col">
              <p v-html="article.articleText"></p>
              <img
                v-for="img in images"
                :key="img.imagesId"
                :src="img.image"
                alt="無法顯示圖片"
                class="img-fluid"
              />
            </div>
          </div>
          <br />
          <br />
          <div class="icon">
            <AddLikes
              @addlike-event="handleevent"
              :articleId="article.articlesId"
            ></AddLikes
            >&nbsp;<span class="likes">{{ likesnumber }}</span
            >&nbsp;&nbsp;&nbsp;
            <i class="bi bi-chat-left-dots-fill commenticon"></i>&nbsp;<span
              class="comments"
              >{{ commentsnumber }}</span
            >&nbsp;&nbsp;
            <InsertCollecitons
              :articleId="article.articlesId"
            ></InsertCollecitons>
          </div>
        </div>
      </div>

      <!-- 新增留言 -->
      <div>
        <button
          type="button"
          class="btn btn-warning mt-3 mb-3"
          @click="showEditorHandler"
        >
          <i class="bi bi-chat-left-dots-fill"></i>&nbsp;我要留言
        </button>
        <editor
          v-if="showEditor"
          :init="tinyMCEConfig"
          v-model="content"
        ></editor>
      </div>

      <div v-if="showEditor">
        <!-- 取消按鈕 -->
        <button type="button" class="btn btn-warning me-2" @click="cancel">
          取消
        </button>
        <!-- 發布按鈕 -->
        <button type="button" class="btn btn-warning" @click="publish">
          發布
        </button>
      </div>

      <!-- 留言列表 -->
      <div v-if="commentsdetail.length > 0" class="mt-4">
        <h3>留言</h3>
        <ul class="list-group">
          <li
            v-for="(comment, index) in commentsdetail"
            :key="comment.commentId"
            class="list-group-item"
          >
            <div class="d-flex flex-column">
              <!-- <div>
                <img v-lazy="comment[1].userphoto" style="
                    object-fit: cover;
                    width: 40px;
                    height: 40px;
                    border-radius: 50%;
                  " />
                <span>&nbsp;{{ comment[1].username }}，發布時間：{{
                  formatDate(comment[0].createdAt)
                }}</span>
                <button v-if="showDeleteComment" @click="hideComment(comment[0].commentId)" class="btn btn-primary"
                  style="width: 40px"><i class="bi bi-x-lg"></i>
                </button>
              </div> -->
              <div style="display: flex; align-items: center">
                <img
                  v-lazy="comment[1].userphoto"
                  style="
                    object-fit: cover;
                    width: 40px;
                    height: 40px;
                    border-radius: 50%;
                  "
                />
                <span
                  >&nbsp;{{ comment[1].username }}，發布時間：{{
                    formatDate(comment[0].createdAt)
                  }}</span
                >
                <div style="margin-left: auto">
                  <div class="dropdown" v-if="showDeleteComment">
                    <button
                      class="btn btn-secondary dropdown-toggle"
                      type="button"
                      id="dropdownMenuButton"
                      data-bs-toggle="dropdown"
                      aria-expanded="false"
                    >
                      <i class="bi bi-gear"></i>
                    </button>
                    <ul
                      class="dropdown-menu"
                      aria-labelledby="dropdownMenuButton"
                    >
                      <li v-if="showDeleteComment">
                        <button
                          @click="hideComment(comment[0].commentId)"
                          class="dropdown-item w-100"
                          style="width: 100px"
                        >
                          <i class="bi bi-x-lg"></i> 刪除
                        </button>
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
              <br />
              <div style="display: flex; align-items: center">
                <div style="flex-grow: 1">
                  <!-- div靠左 -->
                  <div v-html="comment[0].commentText"></div>
                </div>
                <div class="position-absolute bottom-0 end-0 mb-2 me-2 icon">
                  <!-- 放置在右下角 -->
                  <AddLikes
                    @addlike-event="handleevent"
                    :commentId="comment[0].commentId"
                  ></AddLikes>
                  &nbsp;<span class="cmtlikes">{{
                    commentLikesMap[comment[0].commentId] !== undefined
                      ? commentLikesMap[comment[0].commentId]
                      : 0
                  }}</span>
                </div>
              </div>
              <div v-if="commentsimages.length > 0">
                <div v-for="img in commentsimages" :key="img.imageId">
                  <img
                    v-if="img.commentsId === comment[0].commentId"
                    :src="img.image"
                    alt="無法顯示圖片"
                    class="img-fluid"
                  />
                </div>
              </div>
              <!-- <button v-if="showDeleteComment" @click="hideComment(comment[0].commentId)" class="btn btn-primary"
                style="width: 40px"><i class="bi bi-x-lg"></i>
              </button> -->
            </div>
          </li>
        </ul>
      </div>

      <div v-else class="mt-4">
        <p>目前尚無留言</p>
      </div>
      <div style="position: fixed; bottom: 20px; left: 20px">
        <!-- <button class="btn btn-primary btn-lg fixed-button btn-warning" :style="buttonStyle"
                    @click="publishArticle">
                    <i class="bi bi-pencil-square"></i>
                </button> -->
        <write-article v-if="showAddArticle"></write-article>
      </div>
    </div>
  </div>
</template>

<script setup>
import Swal from "sweetalert2";
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import router from "@/router";
import axios from "axios";
import Editor from "@tinymce/tinymce-vue";
import { useLoginStore } from "../../store/navbar";
import tinymce from "tinymce";
import WriteArticle from "./WriteArticle.vue";
import { useChatStore } from "@/store/chatroomOnetoOne.js";
import { useGetDerivedNamespace } from "element-plus";
import InsertCollecitons from "./InsertCollecitons.vue";
import AddLikes from "./AddLikes.vue";
const chatStore = useChatStore();
const { connectedUsersResponse } = chatStore;
const showChatRoom = useLoginStore();
const url = import.meta.env.VITE_JAVA_URL;

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

const article = ref(null);
// const comments = ref([]);
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
const userId = ref("");
const member = ref("user");

// 控制是否顯示編輯文章按鈕
const showEditButton = ref(false);
const showDeleteArticle = ref(false);
const showDeleteComment = ref(false);
const isAdmin = ref(false);
const showAddArticle = ref(true);

// 抓取該篇文章詳細內容及其所屬留言
const fetchData = async () => {
  try {
    // const articleId = useRoute().params.id;
    console.log("articleId=" + articleId);
    const response = await axios.get(`${url}/articles/detail/${articleId}`);
    // console.log("article = ",response.data);
    // 解構後端返回的數據
    const {
      article: fetchedArticle,
      commentsdetail: fetchedcommentsdetail,
      images: fetchedimages,
      commentsimages: fetchedcommentsimages,
      articleuser: fetchedarticleuser,
      commentsnumber: fetchedcommentsnumber,
      likesnumber: fetchedlikesnumber,
      commentdetail: fetchedcommentdetail,
      commentlikesdetail: fetchedcommentlikesdetail,
      commentLikesMap: fetchedcommentLikesMap,
    } = response.data;
    // 更新組件中的數據
    article.value = fetchedArticle;
    // comments.value = fetchedComments;
    commentsdetail.value = fetchedcommentsdetail;
    images.value = fetchedimages;
    commentsimages.value = fetchedcommentsimages;
    articleuser.value = fetchedarticleuser;
    commentsnumber.value = fetchedcommentsnumber;
    likesnumber.value = fetchedlikesnumber;
    commentdetail.value = fetchedcommentdetail;
    commentlikesdetail.value = fetchedcommentlikesdetail;
    commentLikesMap.value = fetchedcommentLikesMap;
    sortByDate();

    await axios
      .get(`${url}/user/profile`, { withCredentials: true })
      .then((response) => {
        userId.value = response.data.id;
        console.log("userId.value=" + userId.value);
        console.log("articleuser.value.id=" + articleuser.value.id);
      })
      .catch((error) => {});
    if (userId.value == articleuser.value.id) {
      showEditButton.value = true;
      showDeleteArticle.value = true;
      console.log(showEditButton.value);
      member.value = "user";
    }

    await axios
      .get(`${url}/user/adminAuthenticate`, { withCredentials: true })
      .then((response) => {
        isAdmin.value = response.data;
        console.log("isAdmin.value=" + isAdmin.value);
      })
      .catch((error) => {});
    if (isAdmin.value) {
      showDeleteArticle.value = true;
      showDeleteComment.value = true;
      member.value = "admin";
      showAddArticle.value = false;
    }
  } catch (error) {
    console.error("Error fetching data:", error);
    // 根據需要進行錯誤處理
  }
};

// 留言從舊到新排序
const sortByDate = () => {
  commentsdetail.value.sort((a, b) => {
    return new Date(a[0].createdAt) - new Date(b[0].createdAt);
  });
};

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

onMounted(() => {
  fetchData();
});

const handleevent = () => {
  console.log("監聽");
  fetchData();
};

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
  height: 200, //編輯器高度
  width: 1294,
  min_height: 200,
  images_upload_handler: handleImageUpload,
};

// 添加一個 ref 來控制是否顯示編輯器
const showEditor = ref(false);

// 添加一個方法來顯示編輯器
const showEditorHandler = () => {
  axios
    .get(`${url}/user/authenticate`, { withCredentials: true })
    .then((res) => {
      if (res.data == false) {
        Swal.fire({
          title: "尚未登入",
          text: "是否要立即登入？",
          icon: "warning",
          confirmButtonText: "登入",
          showCancelButton: true,
          cancelButtonText: "暫時不要",
        }).then((result) => {
          if (result.isConfirmed) {
            router.push("/user/userlogin");
            return;
          }
        });
      } else {
        axios
          .get(`${url}/user/profile`, { withCredentials: true })
          .then((response) => {
            userId.value = response.data.id;
            console.log("userId=" + userId.value);
          })
          .catch((error) => {});
        showEditor.value = true;
        console.log(showEditor.value);
      }
    })
    .catch((error) => {
      console.error(error);
    });
};

// const articleId = useRoute().params.id;
const content = ref("");
// 發布留言
const publish = () => {
  if (!content.value) {
    Swal.fire({
      title: "請輸入內容",
      icon: "warning",
      showConfirmButton: true,
      confirmButtonText: "確認",
    });
    return;
  }

  const formData = {
    userId: userId.value,
    content: content.value,
    articlesId: articleId,
    staus: "normal",
  };

  axios
    .post(`${url}/comments/add`, formData)
    .then((response) => {
      // console.log(response.data);
      // 在這裡可以處理後端返回的數據或執行相應的操作
      Swal.fire({
        title: response.data.text,
        icon: "success",
        showConfirmButton: false,
        timer: 1500,
        // confirmButtonText: "確認",
      });
      fetchData();
    })
    .catch((error) => {
      Swal.fire({
        title: "留言失敗",
        icon: "error",
        showConfirmButton: true,
        confirmButtonText: "確認",
      });
    });
  tinymce.activeEditor.setContent("");
  showEditor.value = false;
};

// 取消留言
const cancel = () => {
  showEditor.value = false;
  tinymce.activeEditor.setContent("");
  console.log("showEditor.value=" + showEditor.value);
};

// const publishArticle = () => {
//     router.push({ name: 'articlewrite' });
// }

async function addChat(id) {
  await axios.get(`${url}/user/addfirend/${id}`, { withCredentials: true });
  await connectedUsersResponse();
  showChatRoom.showOneToOne = true;
}

const editArticle = () => {
  router.push({ name: "articleedit" });
};

const hideArticle = () => {
  let request = {
    member: member.value,
  };
  let title = "確定刪除？";
  let text = null;
  let icon = "question";
  if ("admin" == member.value) {
    title = "確定禁用此文章？";
    text = "禁用文章會影響會員權益";
    icon = "warning";
  }

  Swal.fire({
    title: title,
    text: text,
    icon: icon,
    confirmButtonText: "確定",
    showCancelButton: true,
    cancelButtonText: "取消",
  }).then((result) => {
    if (result.isConfirmed) {
      axios
        .put(`${url}/articles/hide/${articleId}`, request)
        .then((res) => {})
        .catch((error) => {
          console.error(error);
        });
      router.push({ name: "forumone" })
      .then(()=>{
        location.reload();
      })
      return;
    }
  });
};

const hideComment = (commentId) => {
  Swal.fire({
    title: "確定刪除？",
    // text: "登入後即可發文",
    icon: "question",
    confirmButtonText: "確定",
    showCancelButton: true,
    cancelButtonText: "取消",
  }).then((result) => {
    if (result.isConfirmed) {
      axios
        .put(`${url}/comments/hide/${commentId}`)
        .then((res) => {})
        .catch((error) => {
          console.error(error);
        });
      fetchData();
      return;
    }
  });
};
</script>

<style scoped>
.backg {
  background: url("../../../public/images/Forumimg.jpg") center/cover fixed;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.2);
  background-blend-mode: multiply;
}

/* .fixed-button {
    margin-left: 10px;
    width: 70px;
    height: 70px;
    font-size: 20px;
} */

.commenticon {
  font-size: 24px;
  color: #8c8cff;
}

.likes,
.comments {
  font-size: 20px;
}

.cmtlikes {
  font-size: 18px;
}

.icon {
  display: flex;
  /* 使用 Flexbox 進行排列 */
  align-items: center;
  /* 對齊元素至中 */
}
</style>
<style lang="scss">
.el-popper.is-customized {
  background: linear-gradient(40deg, #1f1f1f, #7d7d7d);
  display: flex;
  justify-content: center;
  align-items: center;
  width: 120px;
  height: 60px;
}

.el-popper.is-customized .el-popper__arrow::before {
  background: linear-gradient(40deg, #565656, #6e6e6e);
  right: 0;
}
</style>
