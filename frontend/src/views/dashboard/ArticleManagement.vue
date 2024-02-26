<template>
  <div style="display: flex" class="layout">
    <DashboardRouterlink></DashboardRouterlink>
    <main
      style="
        width: 100%;
        margin-left: 30px;
        margin-bottom: 30px;
        margin-top: 30px;
      "
    >
      <div ref="scrollTarget"></div>

      <!-- 文章 -->
      <div
        class="order_searchbox"
        style="width: 98.5"
        v-show="forumManagementShow.isArticleShow"
      >
        <div class="operation_icon_set" @click="showArticleSearchBox">
          <el-icon size="24"><Operation /></el-icon>
        </div>
        <div class="searchbox_out_btn" v-show="!isShowArticleSearchBox">
          <button class="btn btn-secondary btn_bc" @click="changeAllStatus">
            saveAll
          </button>
        </div>
        <div
          class="dropdown searchbox_out_btn"
          style="margin-right: 25px"
          v-show="!isShowArticleSearchBox"
        >
          <button
            class="btn btn-secondary dropdown-toggle dropdown_management_btn btn_bc"
            type="button"
            data-bs-toggle="dropdown"
            aria-expanded="false"
          >
            {{ fomateBtn(forumManagement.one) }}
          </button>
          <ul class="dropdown-menu dropdown_management_menu">
            <li
              class="dropdown_management"
              @click="changeManagement(forumManagement.two)"
            >
              {{ fomateBtn(forumManagement.two) }}
            </li>
            <!-- <li
              class="dropdown_management"
              @click="changeManagement(forumManagement.three)"
            >
              {{ fomateBtn(forumManagement.three) }}
            </li> -->
          </ul>
        </div>

        <div v-show="isShowArticleSearchBox" class="searchbox_out">
          <div class="searchbox">
            <label class="selectbox_label" for="articleid_search"
              >文章編號:</label
            ><input
              type="text"
              class="form-control form-control-sm product_input"
              id="articleid_search"
              v-model="articleSearchBox.articlesId"
            />
            <label class="selectbox_label" for="userId_search">會員編號:</label
            ><input
              type="text"
              class="form-control form-control-sm productName_input"
              id="userId_search"
              v-model="articleSearchBox.userId"
            /><label class="selectbox_label" for="createdAt_search"
              >發布日期:</label
            >
            <el-date-picker
              id="createdAt_search"
              v-model="selectOfDateArtiCreatedAt"
              type="daterange"
              range-separator="To"
              start-placeholder="Start date"
              end-placeholder="End date"
              class="discountDate_select"
              style="width: 45%"
            />

            <label class="selectbox_label" for="articleGameType"
              >文章類別:</label
            >
            <select
              class="form-select form-select-sm productStatus_input"
              v-model="articleSearchBox.articleType"
              id="articleGameType"
              @change="findproduct(articleSearchBox.articleType)"
            >
              <option
                v-for="opt of searchSubtype"
                :key="opt.value"
                :value="opt.value"
              >
                {{ opt.label }}
              </option>
            </select>
            <label class="selectbox_label" style="width: 14%" for="articleType"
              >文章子類別:</label
            >
            <select
              class="form-select form-select-sm productType_input"
              id="articleType"
              v-model="articleSearchBox.articleGameType"
            >
              <option v-for="opt of searchProductName" :key="opt.value">
                {{ opt.label }}
              </option>
            </select>
            <label class="selectbox_label" for="status">文章狀態:</label>
            <select
              class="form-select form-select-sm productType_input"
              id="status"
              v-model="articleSearchBox.status"
            >
              <option v-for="opt of optionOfArtiStatus" :key="opt.value">
                {{ opt.label }}
              </option>
            </select>
          </div>
          <!-- 搜尋列第二行 -->
          <div class="searchbox">
            <!-- <div style="width: 30%"></div> -->
            <label
              class="selectbox_label_top"
              style="width: 6%"
              for="commentsNumber"
              >留言數大於:</label
            ><el-input-number
              class="price_input"
              v-model="articleSearchBox.commentsNumber"
              :min="Number(0)"
              controls-position="right"
              id="commentsNumber"
            ></el-input-number>
            <label
              class="selectbox_label_top"
              style="width: 9%"
              for="clicktimes"
              >文章瀏覽次數大於:</label
            ><el-input-number
              class="price_input"
              v-model="articleSearchBox.clicktimes"
              :min="Number(0)"
              controls-position="right"
              id="clicktimes"
            ></el-input-number>

            <label class="selectbox_label_top" for="articleHead"
              >文章標題:</label
            >
            <input
              type="text"
              class="form-control form-control-sm product_input"
              id="articleHead"
              v-model="articleSearchBox.articleHead"
            />
            <label class="selectbox_label_top" for="articleText"
              >文章內容:</label
            >
            <input
              type="text"
              class="form-control form-control-sm product_input"
              id="articleText"
              v-model="articleSearchBox.articleText"
            />
            <label class="selectbox_label_top" for="sortOption"
              >排序方式:</label
            >
            <select
              class="form-select form-select-sm sort_input"
              v-model="articleSearchBox.sort"
              id="sortOption"
            >
              <option
                v-for="opt of optionOfSort"
                :selected="opt.value === articleSearchBox.sort"
                :key="opt.value"
                :value="opt.value"
              >
                {{ opt.label }}
              </option>
            </select>
            <button
              class="btn btn-secondary ordermanag_btn"
              @click="goSearchArticle"
            >
              搜尋
            </button>
            <button
              class="btn btn-secondary ordermanag_btn"
              @click="clearArticleSearchRequest"
            >
              清除
            </button>
          </div>
          <div class="saveAll_btn">
            <button
              class="btn btn-secondary searchbox_out_btn"
              @click="changeAllStatus"
            >
              saveAll
            </button>
            <div class="dropdown searchbox_out_btn" style="margin-left: 10px">
              <button
                class="btn btn-secondary dropdown-toggle dropdown_management_btn"
                type="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                {{ fomateBtn(forumManagement.one) }}
              </button>
              <ul class="dropdown-menu dropdown_management_menu">
                <li
                  class="dropdown_management"
                  @click="changeManagement(forumManagement.two)"
                >
                  {{ fomateBtn(forumManagement.two) }}
                </li>
                <!-- <li
                  class="dropdown_management"
                  @click="changeManagement(forumManagement.three)"
                >
                  {{ fomateBtn(forumManagement.three) }}
                </li> -->
              </ul>
            </div>
          </div>
        </div>
      </div>
      <!-- -------------------留言用的搜索------------------ -->
      <div
        class="order_searchbox"
        style="width: 98.5"
        v-show="forumManagementShow.isCommentShow"
      >
        <div class="operation_icon_set" @click="showCommentSearchBox">
          <el-icon size="24"><Operation /></el-icon>
        </div>
        <div class="searchbox_out_btn" v-show="!isShowCommentSearchBox">
          <button
            class="btn btn-secondary btn_bc"
            @click="changeAllCommentStatus"
          >
            saveAll
          </button>
        </div>
        <div
          class="dropdown searchbox_out_btn"
          style="margin-right: 25px"
          v-show="!isShowCommentSearchBox"
        >
          <button
            class="btn btn-secondary dropdown-toggle dropdown_management_btn btn_bc"
            type="button"
            data-bs-toggle="dropdown"
            aria-expanded="false"
          >
            {{ fomateBtn(forumManagement.one) }}
          </button>
          <ul class="dropdown-menu dropdown_management_menu">
            <li
              class="dropdown_management"
              @click="changeManagement(forumManagement.two)"
            >
              {{ fomateBtn(forumManagement.two) }}
            </li>
            <!-- <li
              class="dropdown_management"
              @click="changeManagement(forumManagement.three)"
            >
              {{ fomateBtn(forumManagement.three) }}
            </li> -->
          </ul>
        </div>

        <div v-show="isShowCommentSearchBox" class="searchbox_out">
          <div class="searchbox">
            <label class="comment_selectbox_label" for="articleid_search"
              >留言編號:</label
            ><input
              type="text"
              class="form-control form-control-sm comment_articleid_input"
              id="articleid_search"
              v-model="commentSearchBox.commentId"
            />
            <label
              class="comment_selectbox_label"
              style="width: 9%"
              for="userId_search"
              >留言者編號:</label
            ><input
              type="text"
              class="form-control form-control-sm comment_userid_input"
              id="userId_search"
              v-model="commentSearchBox.userId"
            />
            <label class="comment_selectbox_label" for="comment_search"
              >文章編號:</label
            ><input
              type="text"
              class="form-control form-control-sm comment_articleid_input"
              id="comment_search"
              v-model="commentSearchBox.articlesId"
            />
            <label class="comment_selectbox_label" for="commentText"
              >留言內容:</label
            >
            <input
              type="text"
              class="form-control form-control-sm commentText_input"
              id="commentText"
              v-model="commentSearchBox.commentText"
            />

            <label class="comment_selectbox_label" for="commentstatus"
              >留言狀態:</label
            >
            <select
              class="form-select form-select-sm commentstatus_input"
              id="commentstatus"
              v-model="commentSearchBox.status"
            >
              <option
                v-for="opt of optionOfCommentStatus"
                :selected="opt.value === commentSearchBox.status"
                :key="opt.value"
              >
                {{ opt.label }}
              </option>
            </select>
          </div>
          <!-- 搜尋列第二行 -->
          <div class="searchbox">
            <!-- <div style="width: 30%"></div> -->
            <div style="width: 50%"></div>
            <label
              class="comment_selectbox_label"
              style="width: 5%"
              for="createdAt_search"
              >留言時間:</label
            >
            <el-date-picker
              id="createdAt_search"
              v-model="selectOfDateCommentCreatedAt"
              type="daterange"
              range-separator="To"
              start-placeholder="Start date"
              end-placeholder="End date"
              class="comment_createdAt_input"
              style="width: 5%"
            />
            <label
              class="comment_selectbox_label"
              style="width: 5%"
              for="sortOption"
              >排序方式:</label
            >
            <select
              class="form-select form-select-sm sort_input"
              v-model="commentSearchBox.sort"
              id="sortOption"
            >
              <option
                v-for="opt of optionOfCommentSort"
                :key="opt.value"
                :value="opt.value"
              >
                {{ opt.label }}
              </option>
            </select>
            <button
              class="btn btn-secondary ordermanag_btn"
              @click="goSearchComment"
            >
              搜尋
            </button>
            <button
              class="btn btn-secondary ordermanag_btn"
              @click="clearCommentSearchRequest"
            >
              清除
            </button>
          </div>
          <div class="saveAll_btn">
            <button
              class="btn btn-secondary searchbox_out_btn"
              @click="changeAllCommentStatus"
            >
              saveAll
            </button>
            <div class="dropdown searchbox_out_btn" style="margin-left: 10px">
              <button
                class="btn btn-secondary dropdown-toggle dropdown_management_btn"
                type="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                {{ fomateBtn(forumManagement.one) }}
              </button>
              <ul class="dropdown-menu dropdown_management_menu">
                <li
                  class="dropdown_management"
                  @click="changeManagement(forumManagement.two)"
                >
                  {{ fomateBtn(forumManagement.two) }}
                </li>
                <!-- <li
                  class="dropdown_management"
                  @click="changeManagement(forumManagement.three)"
                >
                  {{ fomateBtn(forumManagement.three) }}
                </li> -->
              </ul>
            </div>
          </div>
        </div>
      </div>
      <div class="allforum_table row">
        <div class="table-article">
          <table
            class="table main_table table-dark"
            v-show="forumManagementShow.isArticleShow"
          >
            <thead class="table-dark">
              <tr>
                <th colspan="11" class="headerOfTable">AritcleManagement</th>
              </tr>
              <tr style="text-align: center">
                <th scope="col" style="width: 100px">文章編號</th>
                <th scope="col" style="width: 100px">文章類別</th>
                <th scope="col" style="width: 250px">文章標題</th>
                <th scope="col" style="width: 80px">發文者</th>
                <th scope="col" style="width: 80px">留言數</th>
                <!-- <th scope="col" style="width: 80px">觀看數</th> -->
                <!-- <th scope="col" style="width: 90px">被檢舉數</th> -->
                <th scope="col" style="width: 90px">狀態</th>
                <th scope="col" style="width: 100px">最近更新時間</th>
                <th scope="col" style="width: 100px">建立時間</th>
                <th scope="col" style="width: 120px">查看內文</th>
              </tr>
            </thead>

            <tbody v-show="!isArticleSearchNoData">
              <template v-for="item of articlesData" :key="item.articleId">
                <tr style="text-align: center">
                  <td style="cursor: pointer" @click="goToArticle(item)">
                    {{ formatArticlesId(item.articlesId) }}
                  </td>
                  <td>{{ formateSubtype(item.articleType) }}</td>
                  <td>{{ item.articleHead }}</td>
                  <td>{{ formatUserId(item.userId) }}</td>
                  <td>{{ item.commentsNumber }}</td>
                  <!-- <td>{{ item.clicktimes }}</td> -->
                  <!-- <td>{{ item.reportsNumber }}</td> -->
                  <td>{{ formatForumStatus(item.status) }}</td>
                  <td>{{ formatFullDate(item.updateAt) }}</td>
                  <td>{{ formatFullDate(item.createdAt) }}</td>
                  <td @click="openArticle(item.articlesId)">
                    <el-icon v-if="!articles[item.articlesId].isReading">
                      <ArrowDownBold />
                    </el-icon>
                    <el-icon v-else>
                      <ArrowUpBold />
                    </el-icon>
                  </td>
                </tr>
                <tr v-show="articles[item.articlesId].isReading">
                  <th
                    :colspan="articles[item.articlesId].textColspan"
                    class="main_text"
                  >
                    文章內容
                  </th>
                  <th
                    class="main_text"
                    v-show="articles[item.articlesId].wantChangeStatus"
                  >
                    狀態
                  </th>
                  <th
                    class="main_text"
                    :colspan="articles[item.articlesId].operaColspan"
                  >
                    操作
                  </th>
                </tr>
                <tr v-show="articles[item.articlesId].isReading">
                  <td
                    :colspan="articles[item.articlesId].textColspan"
                    class="main_text"
                  >
                    {{ stripHtml(item.articleText) }}
                  </td>
                  <td
                    class="main_text"
                    v-show="articles[item.articlesId].wantChangeStatus"
                  >
                    <select
                      class="form-select form-select-sm mb-3"
                      aria-label=".form-select-lg example"
                      @change="changeArtiStatus(item.articlesId)"
                      v-model="articles[item.articlesId].statusBeforeSave"
                    >
                      <option
                        v-for="opt in optionOfStatus"
                        :selected="item.status === opt.value"
                        :value="opt.value"
                      >
                        {{ opt.label }}
                      </option>
                    </select>
                  </td>
                  <td
                    class="main_text"
                    v-show="articles[item.articlesId].isStatusChanged"
                  >
                    <button
                      class="btn btn-primary"
                      @click="
                        changeStatusById(
                          item,
                          articles[item.articlesId].statusBeforeSave
                        )
                      "
                    >
                      保存
                    </button>
                  </td>
                  <td class="main_text">
                    <button
                      class="btn btn-secondary"
                      @click="wantChangeStatus(item.articlesId)"
                      v-show="!articles[item.articlesId].wantChangeStatus"
                    >
                      變更狀態
                    </button>
                    <button
                      class="btn btn-secondary"
                      @click="clickBackBtn(item)"
                      v-show="articles[item.articlesId].wantChangeStatus"
                    >
                      返回
                    </button>
                  </td>
                </tr>
              </template>
            </tbody>
            <tbody v-show="isArticleSearchNoData">
              <td colspan="10" class="search_nodata">查無資料</td>
            </tbody>
          </table>
        </div>

        <!-- 留言 -->
        <div class="table-comment" v-show="forumManagementShow.isCommentShow">
          <table class="table main_table table-dark">
            <thead class="table-dark">
              <tr>
                <th colspan="11" class="headerOfTable">CommentManagement</th>
              </tr>
              <tr style="text-align: center">
                <th scope="col" style="width: 90px">留言編號</th>
                <th scope="col" style="width: 150px">留言簡述</th>
                <th scope="col" style="width: 90px">文章編號</th>
                <th scope="col" style="width: 90px">留言者</th>
                <th scope="col" style="width: 80px">狀態</th>
                <!-- <th scope="col" style="width: 80px">被檢舉數</th> -->
                <th scope="col" style="width: 80px">留言時間</th>
                <th scope="col" style="width: 130px">查看內文</th>
              </tr>
            </thead>
            <tbody v-show="!isCommentSearchNoData">
              <template v-for="item of commentsData" :key="item.commentId">
                <tr style="text-align: center">
                  <td>
                    {{ formatCommentId(item.commentId) }}
                  </td>
                  <td>{{ stripHtmlOnly15(item.commentText) }}</td>
                  <td @click="goToArticle(item)">
                    {{ formatArticlesId(item.articlesId) }}
                  </td>
                  <td>{{ formatUserId(item.userId) }}</td>
                  <td>{{ formatForumStatus(item.status) }}</td>
                  <!-- <td>{{ item.reportedNumber }}</td> -->
                  <td>{{ formatFullDate(item.createdAt) }}</td>
                  <td @click="openComment(item.commentId)">
                    <el-icon v-if="comments[item.commentId].isReading">
                      <ArrowUpBold />
                    </el-icon>
                    <el-icon v-else>
                      <ArrowDownBold />
                    </el-icon>
                  </td>
                </tr>
                <tr v-show="comments[item.commentId].isReading">
                  <th
                    class="main_text"
                    :colspan="comments[item.commentId].textColspan"
                  >
                    文章內容
                  </th>
                  <th
                    class="main_text"
                    v-show="comments[item.commentId].wantChangeStatus"
                  >
                    狀態
                  </th>
                  <th
                    :colspan="comments[item.commentId].operaColspan"
                    class="main_text"
                  >
                    操作
                  </th>
                </tr>
                <tr v-show="comments[item.commentId].isReading">
                  <td
                    :colspan="comments[item.commentId].textColspan"
                    class="main_text"
                  >
                    {{ stripHtml(item.commentText) }}
                  </td>
                  <td
                    class="main_text"
                    v-show="comments[item.commentId].wantChangeStatus"
                  >
                    <select
                      class="form-select form-select-sm mb-3"
                      aria-label=".form-select-lg example"
                      @change="changeCommentStatus(item.commentId)"
                      v-model="comments[item.commentId].statusBeforeSave"
                    >
                      <option
                        v-for="opt in optionOfStatusComment"
                        :selected="item.status === opt.value"
                        :value="opt.value"
                      >
                        {{ opt.label }}
                      </option>
                    </select>
                  </td>

                  <td
                    class="main_text"
                    v-show="comments[item.commentId].isStatusChanged"
                  >
                    <button
                      class="btn btn-primary"
                      @click="
                        changeStatusByCommentId(
                          item,
                          comments[item.commentId].statusBeforeSave
                        )
                      "
                    >
                      保存
                    </button>
                  </td>
                  <td class="main_text">
                    <button
                      class="btn btn-secondary"
                      @click="wantChangeCommentStatus(item.commentId)"
                      v-show="!comments[item.commentId].wantChangeStatus"
                    >
                      變更狀態
                    </button>
                    <button
                      class="btn btn-secondary"
                      @click="clickCommentBackBtn(item)"
                      v-show="comments[item.commentId].wantChangeStatus"
                    >
                      返回
                    </button>
                  </td>
                </tr>
              </template>
            </tbody>
            <tbody v-show="isCommentSearchNoData">
              <td colspan="10" class="search_nodata">查無資料</td>
            </tbody>
          </table>
        </div>
        <!-- 檢舉 -->
        <div>
          <button
            class="btn btn-primary"
            @click="changeAllReportStatus()"
            v-show="forumManagementShow.isReportShow"
          >
            Update All without Mercy
          </button>
        </div>
        <div class="table-comment" v-show="forumManagementShow.isReportShow">
          <table class="table main_table table-dark">
            <thead class="table-dark">
              <tr>
                <th colspan="11" class="headerOfTable">ReportManagement</th>
              </tr>
              <tr style="text-align: center">
                <th scope="col" style="width: 90px">檢舉編號</th>
                <th scope="col" style="width: 100px">原因</th>
                <th scope="col" style="width: 100px">處理狀態</th>
                <th scope="col" style="width: 80px">文章出處</th>
                <th scope="col" style="width: 110px">文章/留言編號</th>
                <th scope="col" style="width: 80px">被檢舉數</th>
                <th scope="col" style="width: 90px">被檢舉會員</th>
                <th scope="col" style="width: 100px">檢舉時間</th>
                <th scope="col" style="width: 100px">更新時間</th>
                <th scope="col" style="width: 100px">查看內文</th>
              </tr>
            </thead>

            <tbody>
              <template v-for="item of reportsData" :key="item.reportId">
                <tr style="text-align: center">
                  <td @click="goToArticle(item.reportId)">
                    {{ item.reportId }}
                  </td>
                  <td>{{ item.reason }}</td>
                  <td>{{ item.status }}</td>
                  <td>{{ item.reportedContentType }}</td>
                  <td>{{ item.reportedTextId }}</td>
                  <td>{{ item.numberOfReport }}</td>
                  <td>{{ item.reporterUserId }}</td>
                  <td>{{ formatFullDate(item.createdAt) }}</td>
                  <td>{{ formatFullDate(item.updatedAt) }}</td>
                  <td
                    @click="
                      openReport(
                        item.reportId,
                        item.reportedTextId,
                        item.reportedContentType
                      )
                    "
                  >
                    <el-icon v-if="!reports[item.reportId].isReading">
                      <ArrowDownBold />
                    </el-icon>
                    <el-icon v-else>
                      <ArrowUpBold />
                    </el-icon>
                  </td>
                </tr>
                <tr v-if="reports[item.reportId].isReading">
                  <th
                    :colspan="reports[item.reportId].textColspan"
                    class="main_text"
                  >
                    文章內容
                  </th>
                  <th
                    class="main_text"
                    v-show="reports[item.reportId].wantChangeStatus"
                  >
                    狀態
                  </th>
                  <th
                    :colspan="reports[item.reportId].operaColspan"
                    class="main_text"
                    style="width: 50px"
                  >
                    操作
                  </th>
                </tr>
                <tr v-if="reports[item.reportId].isReading">
                  <td
                    :colspan="reports[item.reportId].textColspan"
                    class="main_text"
                    v-html="reports[item.reportId].text"
                  ></td>
                  <td
                    class="main_text"
                    v-show="reports[item.reportId].wantChangeStatus"
                  >
                    <select
                      class="form-select form-select-sm mb-3"
                      aria-label=".form-select-lg example"
                      @change="changeRepStatus(item.reportId)"
                      v-model="reports[item.reportId].statusBeforeSave"
                    >
                      <option
                        v-for="opt in optionOfReportStatus"
                        :selected="item.status === opt"
                      >
                        {{ opt }}
                      </option>
                    </select>
                  </td>
                  <td
                    class="main_text"
                    v-show="reports[item.reportId].isStatusChanged"
                  >
                    <button
                      class="btn btn-primary"
                      @click="
                        gochangeReportStatusById(
                          item,
                          reports[item.reportId].statusBeforeSave
                        )
                      "
                    >
                      保存
                    </button>
                  </td>
                  <td class="main_text">
                    <button
                      class="btn btn-primary"
                      @click="wantReportChangeStatus(item.reportId)"
                      v-show="!reports[item.reportId].wantChangeStatus"
                    >
                      變更狀態
                    </button>
                    <button
                      class="btn btn-primary"
                      @click="clickReportBackBtn(item.reportId)"
                      v-show="reports[item.reportId].wantChangeStatus"
                    >
                      返回
                    </button>
                  </td>
                </tr>
              </template>
            </tbody>
          </table>
        </div>
        <div class="mt-4" style="display: flex; justify-content: flex-end">
          <el-pagination
            v-model:current-page="pageControl.currentPage"
            v-model:page-size="pageControl.size"
            :page-sizes="pageControl.pageSizes"
            background="true"
            layout="total,sizes, prev, pager, next"
            :total="pageControl.total"
            @size-change="findWhich()"
            @current-change="findWhich()"
            @prev-click="findWhich()"
            @next-click="findWhich()"
          />
        </div>
      </div>
    </main>
  </div>
</template>
<script setup>
import axios from "axios";
import Swal from "sweetalert2";
import { ref, onMounted, watch } from "vue";
import { format } from "../../../node_modules/date-fns";
import { useRouter, useRoute } from "vue-router";
import DashboardRouterlink from "./DashboardRouterlink.vue";
import { stripHtml, stripHtmlOnly15 } from "../../utils/forstripHtml.js";
import { formatFullDate } from "../../utils/someDateFormat.js";
import {
  formatUserId,
  reformatUserId,
  formatArticlesId,
  reformatArticlesId,
  formatCommentId,
  reformatCommentId,
} from "../../utils/someIdFormat.js";
import { forShortSweetalert } from "../../utils/someSweetalert.js";
import {
  formatForumStatus,
  reformatForumStatus,
} from "../../utils/someFormat.js";

const router = useRouter();
const route = useRoute();
const path = import.meta.env.VITE_JAVA_URL;
const config = {
  withCredentials: true,
};
//文章排序方式
const optionOfSort = ref([
  {
    value: "idacs",
    label: "文章編號小>大",
  },
  {
    value: "iddesc",
    label: "文章編號大>小",
  },
  {
    value: "userIdasc",
    label: "會員編號小>大",
  },
  {
    value: "userIddesc",
    label: "會員編號大>小",
  },
  {
    value: "createdatdesc",
    label: "發布日期近>遠",
  },
  {
    value: "createdatasc",
    label: "發布日期遠>近",
  },
  {
    value: "updatedatdesc",
    label: "更新日期近>遠",
  },
  {
    value: "updatedatasc",
    label: "更新日期遠>近",
  },
]);

//文章狀態
const optionOfArtiStatus = ref([
  {
    value: "normal",
    label: "一般文章",
  },
  {
    value: "banned",
    label: "被禁用文章",
  },
]);

//留言排序方式
const optionOfCommentSort = ref([
  {
    value: "idacs",
    label: "留言編號小>大",
  },
  {
    value: "iddesc",
    label: "留言編號大>小",
  },
  {
    value: "articlesIdasc",
    label: "文章編號小>大",
  },
  {
    value: "articlesIddesc",
    label: "文章編號大>小",
  },
  {
    value: "userIdasc",
    label: "留言者編號小>大",
  },
  {
    value: "userIdesc",
    label: "留言者編號大>小",
  },
  {
    value: "createdatdesc",
    label: "留言日期近>遠",
  },
  {
    value: "createdatasc",
    label: "留言日期遠>近",
  },
]);
//留言狀態
const optionOfCommentStatus = ref([
  {
    value: "normal",
    label: "一般留言",
  },
  {
    value: "banned",
    label: "被禁用留言",
  },
]);

//文章的數據
const articlesData = ref([]);
//留言的數據
const commentsData = ref([]);
//檢舉的數據
const reportsData = ref([]);

//文章搜尋開關
const isShowArticleSearchBox = ref(false);
//留言搜尋開關
const isShowCommentSearchBox = ref(false);

//文章發布日期接收
const selectOfDateArtiCreatedAt = ref([]);
//留言發布日期接收
const selectOfDateCommentCreatedAt = ref([]);
//文章搜尋
const articleSearchBox = ref({
  articlesId: null,
  userId: null,
  articleGameType: null,
  articleHead: null,
  articleText: null,
  updateAt: null,
  minCreatedAt: null,
  maxCreatedAt: null,
  clicktimes: null,
  status: null,
  articleType: null,
  commentsNumber: null,
  reportsNumber: null,
  start: null,
  rows: null,
  sort: "createdatdesc",
});
//留言搜尋
const commentSearchBox = ref({
  commentId: null,
  commentText: null,
  minCreatedAt: null,
  maxCreatedAt: null,
  articlesId: null,
  userId: null,
  status: null,
  reportedNumber: null,
  start: null,
  rows: null,
  sort: "createdatdesc",
});
//檢舉搜尋
const reportSearchBox = ref({});

//如果文章搜尋沒有對應的數據
const isArticleSearchNoData = ref(false);
//如果留言搜尋沒有對應的數據
const isCommentSearchNoData = ref(false);

//將控制塞在同一個物件裡面
const articles = ref({});
const comments = ref({});
const reports = ref({});

//提供文章狀態改變的選單選項
const optionOfStatus = ref([
  {
    value: "normal",
    label: "一般文章",
  },
  {
    value: "banned",
    label: "禁用文章",
  },
]);

//提供文章狀態改變的選單選項
const optionOfStatusComment = ref([
  {
    value: "normal",
    label: "一般留言",
  },
  {
    value: "banned",
    label: "禁用留言",
  },
]);

//提供文章狀態改變的選單選項
const optionOfReportStatus = ref({
  pending: "pending",
  banned: "banned",
  confirmed: "confirmed",
  reject: "reject",
});

//用來控制顯示的表格
const forumManagement = ref({
  one: "ArticleManagement",
  two: "CommentManagement",
  // three: "ReportManagement",
});

//用來控制顯示表格的vshow
const forumManagementShow = ref({
  isArticleShow: true,
  isCommentShow: false,
  isReportShow: false,
});

//向後端請求的數位遊戲子分類
const searchSubtype = ref([]);
//找到的商品名字
const searchProductName = ref([]);

//頁數
const pageControl = ref({
  pageSizes: [5, 10, 20, 50],
  size: 10,
  currentPage: Number(route.params.page),
  sort: "idacs",
  total: 0,
});

watch(
  () => route.params.userId,
  (newParam, oldParam) => {
    findWhich();
  }
);

onMounted(() => {
  findWhich();
  findproductsubType();
});
//下拉式按鈕的內容表格尋找什麼物件
function findWhich(routeName) {
  if (routeName == null || routeName.length == 0) {
    routeName = route.name;
  } else {
    routeName = routeName.toLowerCase();
  }
  if (route.params.userId) {
    router
      .push({
        name: `${routeName}`,
        params: {
          userId: route.params.userId,
          page: pageControl.value.currentPage,
        },
      })
      .then(() => {
        useWhichFindArticle();
      })
      .then(() => {
        forumControl(routeName);
        switchButton(routeName);
      });
  } else {
    router
      .push({
        name: `${routeName}`,
        params: {
          page: pageControl.value.currentPage,
        },
      })
      .then(() => {
        useWhichFindArticle();
      })
      .then(() => {
        forumControl(routeName);
        switchButton(routeName);
      });
  }
}

function forumControl(params) {
  for (let temp in forumManagementShow.value) {
    forumManagementShow.value[temp] = false;
  }
  if (params == "articlemanagement" || params == "articlemanagementforuser") {
    forumManagementShow.value.isArticleShow = true;
  } else if (
    params == "commentmanagement" ||
    params == "commentmanagementforuser"
  ) {
    forumManagementShow.value.isCommentShow = true;
  } else if (params == "reportmanagement") {
    forumManagementShow.value.isReportShow = true;
  }
}

//用來判斷用哪個方法找文章，如果是用router找使用者的會帶參數
function useWhichFindArticle() {
  if (
    route.name === "articlemanagement" ||
    route.name === "articlemanagementforuser"
  ) {
    scrollToElement();
    findAllArticle();
  } else if (
    route.name === "commentmanagement" ||
    route.name === "commentmanagementforuser"
  ) {
    scrollToElement();
    findAllComment();
  } else if (route.name === "reportmanagement") {
    scrollToElement();
    findAllReport();
  }
}

//向後端請求更新
async function findAllArticle() {
  let request = searchArticle();
  console.log("reqbe", request);
  Swal.fire({
    title: "資料搜尋中",
    showConfirmButton: false,
    timer: 2000,
  });
  try {
    const response = await axios.post(
      path + "/dashboard/forum/article/find",
      request,
      config
    );
    console.log("arti", response.data);
    if (response.data.length != 0) {
      forShortSweetalert("success", "搜尋完畢");
      if (response.data.data !== "nodata") {
        //用id建立對應的物件方便控制
        isArticleSearchNoData.value = false;
        articleInitObj(response.data.data);
        pageControl.value.total = response.data.totalcount;
      } else {
        isArticleSearchNoData.value = true;
        pageControl.value.total = response.data.totalcount;
      }
    }
  } catch (error) {
    console.log(error);
  }
}

function searchArticle() {
  let request = {
    start: 1,
    rows: 10,
    ...articleSearchBox.value,
  };
  request.start = pageControl.value.currentPage;
  request.rows = pageControl.value.size;
  if (route.params.userId) {
    request.userId = route.params.userId;
  }
  if (selectOfDateArtiCreatedAt != null) {
    request.minCreatedAt = selectOfDateArtiCreatedAt.value[0];
    request.maxCreatedAt = selectOfDateArtiCreatedAt.value[1];
  }
  if (request.articlesId) {
    request.articlesId = reformatArticlesId(request.articlesId);
  }
  if (request.userId) {
    request.userId = reformatUserId(request.userId);
  }
  return request;
}

function articleInitObj(data) {
  articlesData.value = [];
  articlesData.value = data;
  for (let temp of articlesData.value) {
    let initObj = {
      [temp.articlesId]: {
        text: "",
        //文章內容欄位的合併數
        textColspan: 8,
        //操作的欄位合併數
        operaColspan: 1,
        //按下查看內文
        isReading: false,
        //按下編輯出現可編輯項
        wantChangeStatus: false,
        ///狀態被改變
        isStatusChanged: false,
        //下拉選單狀態
        statusBeforeSave: temp.status,
      },
    };
    Object.assign(articles.value, initObj);
  }
}

//用來個別控制哪個文章下拉
async function openArticle(articleId) {
  //控制下拉開關 isReading=false為關
  if (!articles.value[articleId].isReading) {
    //如果已經拿過text就不拿了
    articles.value[articleId].isReading = true;
  } else if (articles.value[articleId].isStatusChanged) {
    //也會確認狀態編輯狀態
    Swal.fire({
      title: "您更變的資料尚未儲存",
      text: "受否放棄更改?",
      icon: "question",
      allowOutsideClick: false,
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      confirmButtonText: "放棄更改",
      cancelButtonText: "返回",
    }).then((result) => {
      if (result.isConfirmed) {
        closeChangebyID(articleId);
        articles.value[articleId].isReading = false;
      }
    });
  } else {
    articles.value[articleId].isReading = false;
  }
}

//按下各欄位的保存觸發
async function changeStatusById(item, status) {
  Swal.fire({
    title: "確認的資料是否要改變文章狀態？",
    text: "改變文章狀態可能對會員權益造成影響",
    icon: "question",
    allowOutsideClick: false,
    showCancelButton: true,
    confirmButtonColor: "#3085d6",
    confirmButtonText: "確定更改",
    cancelButtonText: "返回",
  }).then((result) => {
    if (result.isConfirmed) {
      modifyArticleById(item, status);
    }
  });
}

//更改byId
async function modifyArticleById(item, status) {
  let articleId = item.articlesId;
  let request = item;
  if (request.status !== status) {
    request.status = status;
  } else {
    Swal.fire({
      title: "未更改文章狀態",
      text: "文章狀態未更改",
      icon: "error",
      allowOutsideClick: false,
      confirmButtonText: "返回",
    });
    return;
  }
  try {
    const response = await axios.put(
      `${path}/dashboard/forum/article/${articleId}`,
      request,
      config
    );
    console.log("success", response.data);
    if (response.data) {
      Swal.fire({
        title: "更新成功",
        text: "已將文章狀態更新",
        icon: "success",
        allowOutsideClick: false,
      }).then(() => {
        item.status = response.data.status;
        item.updateAt = response.data.updateAt;
        closeChangebyID(articleId);
        articles.value[articleId].isReading = false;
      });
    }
  } catch (error) {
    console.log(error);
  }
}

//按下saveAll觸發
async function changeAllStatus() {
  const temprequest = collectAllArticleIsChanged();
  let request = [];
  for (let temp of articlesData.value) {
    for (let temp2 in temprequest) {
      if (temp2 == temp.articlesId) {
        temp.status = temprequest[temp2];
        request.push(temp);
      }
    }
  }
  console.log(request);
  // 如果request收集到的物件為0沒有任何動作
  //所以用Object.key轉成陣列測長度
  if (Object.keys(request).length) {
    Swal.fire({
      title: '確認的資料是否"批次"改變文章狀態？',
      text: '可能對"大量會員"權益造成影響',
      icon: "question",
      allowOutsideClick: false,
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      confirmButtonText: "確定更改",
      cancelButtonText: "返回",
    }).then((result) => {
      if (result.isConfirmed) {
        modifyAllArticle(request);
      }
    });
  }
}
//請求後端更新全部
async function modifyAllArticle(request) {
  try {
    const response = await axios.put(
      `${path}/dashboard/forum/article/saveall`,
      request
    );
    console.log(response);
    if (response.status == 200) {
      Swal.fire({
        title: "更新成功",
        text: "已將文章狀態更新",
        icon: "success",
        allowOutsideClick: false,
      }).then(() => {
        findAllArticle();
      });
    }
  } catch (error) {
    console.log(error);
  }
}

//用來收集所有被更改狀態的文章
function collectAllArticleIsChanged() {
  let tempnotsave = {};
  for (let temp in articles.value) {
    if (articles.value[temp].isStatusChanged) {
      let tempopj = { [temp]: articles.value[temp].statusBeforeSave };
      Object.assign(tempnotsave, tempopj);
    }
  }
  return tempnotsave;
}

//如果更改狀態出現選項
function changeArtiStatus(articleId) {
  if (!articles.value[articleId].isStatusChanged) {
    articles.value[articleId].isStatusChanged = true;
    articles.value[articleId].textColspan = 6;
    articles.value[articleId].operaColspan = 2;
  }
}
//如果點擊更變狀態
function wantChangeStatus(articleId) {
  if (!articles.value[articleId].wantChangeStatus) {
    articles.value[articleId].wantChangeStatus = true;
    articles.value[articleId].textColspan = 7;
  }
}

//如果點擊返回
async function clickBackBtn(item) {
  //狀態更變狀態下返回
  if (articles.value[item.articlesId].isStatusChanged) {
    Swal.fire({
      title: "您更變的資料尚未儲存",
      text: "受否放棄更改?",
      icon: "question",
      allowOutsideClick: false,
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      confirmButtonText: "放棄更改",
      cancelButtonText: "返回",
    }).then((result) => {
      if (result.isConfirmed) {
        closeChangebyID(item.articlesId);
        articles.value[item.articlesId].statusBeforeSave = item.status;
      }
    });
    //變更狀態下返回
  } else if (articles.value[item.articlesId].wantChangeStatus) {
    closeChangebyID(item.articlesId);
  }
}
//用來關閉因保存改變的表個
function closeChangebyID(articleId) {
  articles.value[articleId].wantChangeStatus = false;
  articles.value[articleId].isStatusChanged = false;
  articles.value[articleId].textColspan = 8;
  articles.value[articleId].operaColspan = 1;
}

function clearArticleSearchRequest() {
  for (let temp in articleSearchBox.value) {
    articleSearchBox.value[temp] = null;
  }
  pageControl.value.currentPage = 1;
  pageControl.value.size = 10;
  selectOfDateArtiCreatedAt.value = [];
  findWhich();
}

function goSearchArticle() {
  pageControl.value.currentPage = 1;
  findWhich();
}

function showArticleSearchBox() {
  isShowArticleSearchBox.value = !isShowArticleSearchBox.value;
}

//文章id跳轉至該文章
function goToArticle(item) {
  if (item.status != null && "banned" === item.status) {
    Swal.fire({
      title: "文章已被禁用",
      icon: "warning",
      allowOutsideClick: false,
    });
    return;
  }
  router.push({
    name: "articledetail",
    params: {
      id: item.articlesId,
    },
  });
}

//----------------------------------comment---------------------------------------------

async function findAllComment() {
  let request = searchComment();
  console.log("com", request);
  try {
    const response = await axios.post(
      `${path}/dashboard/forum/comment/find`,
      request,
      config
    );
    console.log("comments", response);
    if (response.data.data.length != 0 && response.data.data != "nodata") {
      isCommentSearchNoData.value = false;
      commentsData.value = response.data.data;

      //用id建立對應的物件方便控制
      commentInitObj(commentsData.value);
      pageControl.value.total = response.data.totalcount;
    } else {
      isCommentSearchNoData.value = true;
      pageControl.value.total = response.data.totalcount;
    }
  } catch (error) {
    console.log(error);
  }
}
//留言初始化
function commentInitObj(data) {
  commentsData.value = [];
  commentsData.value = data;
  for (let temp of commentsData.value) {
    let initObj = {
      [temp.commentId]: {
        text: "",
        //文章內容欄位的合併數
        textColspan: 6,
        //操作的欄位合併數
        operaColspan: 1,
        //按下查看內文
        isReading: false,
        //按下編輯出現可編輯項
        wantChangeStatus: false,
        ///狀態被改變
        isStatusChanged: false,
        //下拉選單狀態
        statusBeforeSave: temp.status,
      },
    };
    Object.assign(comments.value, initObj);
  }
}
//用來收集參數
function searchComment() {
  let request = {
    start: 1,
    rows: 10,
    ...commentSearchBox.value,
  };
  request.start = pageControl.value.currentPage;
  request.rows = pageControl.value.size;
  if (route.params.userId) {
    request.userId = route.params.userId;
  }
  if (selectOfDateArtiCreatedAt != null) {
    request.minCreatedAt = selectOfDateCommentCreatedAt.value[0];
    request.maxCreatedAt = selectOfDateCommentCreatedAt.value[1];
  }
  if (request.commentId) {
    request.commentId = reformatCommentId(request.commentId);
  }
  if (request.userId) {
    request.userId = reformatCommentId(request.userId);
  }
  return request;
}
//向後端請求修改狀態
async function changeStatusByCommentId(item, status) {
  Swal.fire({
    title: "確認的資料是否要改變文章狀態？",
    text: "改變文章狀態可能對會員權益造成影響",
    icon: "question",
    allowOutsideClick: false,
    showCancelButton: true,
    confirmButtonColor: "#3085d6",
    confirmButtonText: "確定更改",
    cancelButtonText: "返回",
  }).then((result) => {
    if (result.isConfirmed) {
      modifyCommentById(item, status);
    }
  });
}

//更改byId
async function modifyCommentById(item, status) {
  let request = item;
  request.status = status;
  let commentId = item.commentId;
  console.log("befcommod", request);
  try {
    const response = await axios.put(
      `${path}/dashboard/forum/comment/${commentId}`,
      request,
      config
    );
    if (response.data) {
      Swal.fire({
        title: "更新成功",
        text: "已將文章狀態更新",
        icon: "success",
        allowOutsideClick: false,
      }).then(() => {
        item.updateAt = response.data.updated;
        closeCommentChangebyID(commentId);
        comments.value[commentId].isReading = false;
      });
    }
  } catch (error) {
    console.log(error);
  }
}
//按下saveAll觸發
async function changeAllCommentStatus() {
  const temprequest = collectAllCommentIsChanged();
  let request = [];
  for (let temp of commentsData.value) {
    for (let temp2 in temprequest) {
      if (temp2 == temp.commentId) {
        temp.status = temprequest[temp2];
        request.push(temp);
      }
    }
  }
  console.log(request);
  // 如果request收集到的物件為0沒有任何動作
  //所以用Object.key轉成陣列測長度
  if (Object.keys(request).length) {
    Swal.fire({
      title: '確認的資料是否"批次"改變留言狀態？',
      text: '可能對"大量會員"權益造成影響',
      icon: "question",
      allowOutsideClick: false,
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      confirmButtonText: "確定更改",
      cancelButtonText: "返回",
    }).then((result) => {
      if (result.isConfirmed) {
        modifyAllComment(request);
      }
    });
  }
}
//請求後端更新全部
async function modifyAllComment(request) {
  try {
    const response = await axios.put(
      `${path}/dashboard/forum/comment/saveall`,
      request
    );
    console.log(response);
    if (response.status == 200) {
      Swal.fire({
        title: "更新成功",
        text: "已將留言狀態更新",
        icon: "success",
        allowOutsideClick: false,
      }).then(() => {
        findAllComment();
      });
    }
  } catch (error) {
    console.log(error);
  }
}

//用來收集所有被更改狀態的文章
function collectAllCommentIsChanged() {
  let tempnotsave = {};
  for (let temp in comments.value) {
    if (comments.value[temp].isStatusChanged) {
      let tempopj = { [temp]: comments.value[temp].statusBeforeSave };
      Object.assign(tempnotsave, tempopj);
    }
  }
  return tempnotsave;
}
//用來個別控制哪個{留言}文章下拉
async function openComment(commentId) {
  comments.value[commentId].isReading = !comments.value[commentId].isReading;
  //如果已經拿過text就不拿了
}

//如果點擊返回
async function clickCommentBackBtn(item) {
  //狀態更變狀態下返回
  if (comments.value[item.commentId].isStatusChanged) {
    Swal.fire({
      title: "您更變的資料尚未儲存",
      text: "受否放棄更改?",
      icon: "question",
      allowOutsideClick: false,
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      confirmButtonText: "放棄更改",
      cancelButtonText: "返回",
    }).then((result) => {
      if (result.isConfirmed) {
        closeCommentChangebyID(item.commentId);
        comments.value[item.commentId].statusBeforeSave = item.status;
      }
    });
    //變更狀態下返回
  } else if (comments.value[item.commentId].wantChangeStatus) {
    closeCommentChangebyID(item.commentId);
  }
}

function closeCommentChangebyID(commentId) {
  comments.value[commentId].wantChangeStatus = false;
  comments.value[commentId].isStatusChanged = false;
  comments.value[commentId].textColspan = 6;
  comments.value[commentId].operaColspan = 1;
}

//如果更改狀態出現選項
function changeCommentStatus(commentId) {
  if (!comments.value[commentId].isStatusChanged) {
    comments.value[commentId].isStatusChanged = true;
    comments.value[commentId].textColspan = 4;
    comments.value[commentId].operaColspan = 2;
  }
}
//如果點擊更變狀態
function wantChangeCommentStatus(commentId) {
  if (!comments.value[commentId].wantChangeStatus) {
    comments.value[commentId].wantChangeStatus = true;
    comments.value[commentId].textColspan = 5;
  }
}
function goSearchComment() {
  pageControl.value.currentPage = 1;
  findWhich();
}

function showCommentSearchBox() {
  isShowCommentSearchBox.value = !isShowCommentSearchBox.value;
}
function clearCommentSearchRequest() {
  for (let temp in commentSearchBox.value) {
    commentSearchBox.value[temp] = null;
  }
  pageControl.value.currentPage = 1;
  pageControl.value.size = 10;
  selectOfDateCommentCreatedAt.value = [];
  findWhich();
}
//----------------------------------report---------------------------------------------
function useWhichFindReport() {
  if (route.params.userId) {
    findReportByUserId();
  } else {
    findAllReport();
  }
}

async function findAllReport() {
  try {
    const response = await axios.post(`${path}/dashboard/forum/report/find`);
    console.log("reports", response);
    if (response.data.length != 0) {
      reportInitObj(response.data);
    }
  } catch (error) {
    console.log(error);
  }
}

async function findReportByUserId() {
  try {
    const response = await axios.get(
      `${path}/dashboard/forum/report/${route.params.userId}`
    );
    console.log("reports", response);
    if (response.data.length != 0) {
      reportInitObj(response.data);
    }
  } catch (error) {
    console.log(error);
  }
}

function reportInitObj(data) {
  reportsData.value = [];
  reportsData.value = data;
  //用id建立對應的物件方便控制
  for (let temp of reportsData.value) {
    let initObj = {
      [temp.reportId]: {
        text: "",
        isReading: false,
        textColspan: 9,
        operaColspan: 1,
        isReading: false,
        wantChangeStatus: false,
        isStatusChanged: false,
        statusBeforeSave: temp.status,
      },
    };
    Object.assign(reports.value, initObj);
  }
}

//用來個別控制哪個{留言}文章下拉
async function openReport(reportId, reportTextId, reportedContentType) {
  //控制下拉開關 isReading=false為關
  if (!reports.value[reportId].isReading) {
    //如果已經拿過text就不拿了
    if (!reports.value[reportId].text) {
      if ("article" == reportedContentType) {
        reports.value[reportId].text = await findArticleByArticleId(
          reportTextId
        );
      } else if ("comment" == reportedContentType) {
        reports.value[reportId].text = await findCommentTextByCommentId(
          reportTextId
        );
      }
    }
    reports.value[reportId].isReading = true;
  } else if (reports.value[reportId].isStatusChanged) {
    //也會確認狀態編輯狀態
    Swal.fire({
      title: "您更變的資料尚未儲存",
      text: "受否放棄更改?",
      icon: "question",
      allowOutsideClick: false,
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      confirmButtonText: "放棄更改",
      cancelButtonText: "返回",
    }).then((result) => {
      if (result.isConfirmed) {
        closeReportChangebyID(reportId);
        reports.value[reportId].isReading = false;
      }
    });
  } else {
    reports.value[reportId].isReading = false;
  }
}
//按下想要更變狀態的選項
function wantReportChangeStatus(reportId) {
  if (!reports.value[reportId].wantChangeStatus) {
    reports.value[reportId].wantChangeStatus = true;
    reports.value[reportId].textColspan = 8;
  }
}

//偵測調整狀態
function changeRepStatus(reportId) {
  if (!reports.value[reportId].isStatusChanged) {
    reports.value[reportId].isStatusChanged = true;
    reports.value[reportId].textColspan = 7;
    reports.value[reportId].operaColspan = 2;
  }
}

//按下保存按鈕
function gochangeReportStatusById(item, status) {
  Swal.fire({
    title: "確認的資料是否要改變文章狀態？",
    text: "改變文章狀態可能對會員權益造成影響",
    icon: "question",
    allowOutsideClick: false,
    showCancelButton: true,
    confirmButtonColor: "#3085d6",
    confirmButtonText: "確定更改",
    cancelButtonText: "返回",
  }).then((result) => {
    if (result.isConfirmed) {
      modifyReportById(item, status);
    }
  });
}

async function modifyReportById(item, status) {
  let request = item;
  request.status = status;
  request.createdAt = Date.parse(item.createdAt);
  request.updatedAt = Date.parse(item.updatedAt);
  let reportId = item.reportId;
  try {
    const response = await axios.put(
      `${path}/dashboard/forum/report/${reportId}`,
      request
    );
    console.log(response);
    if (response.data.length != 0) {
      Swal.fire({
        title: "更新成功",
        text: "已將文章狀態更新",
        icon: "success",
        allowOutsideClick: false,
      }).then(() => {
        item.status = response.data.status;
        item.updatedAt = response.data.updated;
        closeReportChangebyID(reportId);
        reports.value[reportId].isReading = false;
      });
    }
  } catch (error) {
    console.log(error);
  }
}

//按下saveAll觸發
async function changeAllReportStatus() {
  const temprequest = collectAllReportsIsChanged();
  let request = [];
  for (let temp of reportsData.value) {
    for (let temp2 in temprequest) {
      if (temp2 == temp.reportId) {
        temp.status = temprequest[temp2];
        request.push(temp);
      }
    }
  }
  console.log(request);
  // 如果request收集到的物件為0沒有任何動作
  //所以用Object.key轉成陣列測長度
  if (Object.keys(request).length) {
    Swal.fire({
      title: '確認的資料是否"批次"改變文章狀態？',
      text: '可能對"大量會員"權益造成影響',
      icon: "question",
      allowOutsideClick: false,
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      confirmButtonText: "確定更改",
      cancelButtonText: "返回",
    }).then((result) => {
      if (result.isConfirmed) {
        modifyAllReport(request);
      }
    });
  }
}
//請求後端更新全部
async function modifyAllReport(request) {
  try {
    const response = await axios.put(
      `${path}/dashboard/forum/report/saveall`,
      request
    );
    console.log(response);
    if (response.status == 200) {
      Swal.fire({
        title: "更新成功",
        text: "已將文章狀態更新",
        icon: "success",
        allowOutsideClick: false,
      }).then(() => {
        findAllReport();
      });
    }
  } catch (error) {
    console.log(error);
  }
}

//用來收集所有被更改狀態的文章
function collectAllReportsIsChanged() {
  let tempnotsave = {};
  for (let temp in reports.value) {
    if (reports.value[temp].isStatusChanged) {
      let tempopj = { [temp]: reports.value[temp].statusBeforeSave };
      Object.assign(tempnotsave, tempopj);
    }
  }
  return tempnotsave;
}

//按下返回按鈕
function clickReportBackBtn(reportId) {
  if (reports.value[reportId].isStatusChanged) {
    Swal.fire({
      title: "您更變的資料尚未儲存",
      text: "受否放棄更改?",
      icon: "question",
      allowOutsideClick: false,
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      confirmButtonText: "放棄更改",
      cancelButtonText: "返回",
    }).then((result) => {
      if (result.isConfirmed) {
        closeReportChangebyID(reportId);
      }
    });
    //變更狀態下返回
  } else if (reports.value[reportId].wantChangeStatus) {
    closeReportChangebyID(reportId);
  }
}

function closeReportChangebyID(reportId) {
  reports.value[reportId].wantChangeStatus = false;
  reports.value[reportId].isStatusChanged = false;
  reports.value[reportId].textColspan = 9;
  reports.value[reportId].operaColspan = 1;
}

//用來找展開的文章
async function findArticleByArticleId(articleId) {
  try {
    const response = await axios.get(
      `${path}/dashboard/forum/article/text/${articleId}`
    );
    //塞進對應的文章ID

    return response.data;
  } catch (error) {
    console.log(error);
  }
}
//用來找展開的{留言}
async function findCommentTextByCommentId(commentId) {
  try {
    const response = await axios.get(
      `${path}/dashboard/forum/comment/text/${commentId}`
    );
    //塞進對應的文章ID
    return response.data;
  } catch (error) {
    console.log(error);
  }
}
async function findArticleByUserId() {
  try {
    const response = await axios.get(
      `${path}/dashboard/forum/article/${route.params.userId}`,
      {
        withCredentials: true,
      }
    );
    console.log("userforum", response.data);

    articleInitObj(response.data);
  } catch (error) {
    console.log(error);
  }
}
// ---------------------------------下拉選單------------------------------------
//控制下拉按鈕
function changeManagement(routeName) {
  if (route.params.userId) {
    if (routeName.toLowerCase() === "articlemanagement") {
      routeName = "articlemanagementforuser";
    } else if (routeName.toLowerCase() === "commentmanagement") {
      routeName = "commentmanagementforuser";
    }
  }

  findWhich(routeName);
  switchButton(routeName);
  for (let temp in commentSearchBox.value) {
    commentSearchBox.value[temp] = null;
  }
  selectOfDateCommentCreatedAt.value = [];
  for (let temp in articleSearchBox.value) {
    articleSearchBox.value[temp] = null;
  }
  selectOfDateArtiCreatedAt.value = [];
  articleSearchBox.value.sort = "createdatdesc";
}

//一下拉式按鈕的內容表格更改內容文字
function switchButton(params) {
  if (params == "articlemanagement" || params == "articlemanagementforuser") {
    forumManagement.value.one = "articlemanagement";
    forumManagement.value.two = "commentmanagement";
    // forumManagement.value.three = "reportmanagement";
  } else if (
    params == "commentmanagement" ||
    params == "commentmanagementforuser"
  ) {
    forumManagement.value.one = "commentmanagement";
    forumManagement.value.two = "articlemanagement";
    // forumManagement.value.three = "reportmanagement";
  }
  // else if (params == "reportmanagement") {
  //   forumManagement.value.one = "reportmanagement";
  //   forumManagement.value.two = "articlemanagement";
  //   forumManagement.value.three = "commentmanagement";
  // }
}
function fomateBtn(params) {
  if (params.toLowerCase() == "articlemanagement") {
    return "文章管理";
  } else if (params.toLowerCase() == "commentmanagement") {
    return "留言管理";
  } else if (params.toLowerCase() == "reportmanagement") {
    return "檢舉系統";
  }
}

const scrollTarget = ref(null);

function scrollToElement() {
  // document.documentElement.scrollTop = 0;
  scrollTarget.value.scrollIntoView({
    behavior: "smooth",
    block: "start",
    inline: "nearest",
  });
}

async function findproductsubType() {
  let productType = {
    productType: "digitalGames",
  };
  try {
    const response = await axios.post(
      `${path}/dashboard/product/subtype`,
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
      `${path}/dashboard/product/productname`,
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
  } else if (subtype == null) {
    return "其他";
  } else {
    return subtype;
  }
}
</script>

<style scoped>
.layout {
  background: url("../../../public/images/loginImg.jpg") center/cover fixed;
  margin: 0;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.6);
  background-blend-mode: multiply;
}

.table_article {
  width: 100%;
  margin: auto;
}

.main_table {
  width: 100%;
}

.main_text {
  border-color: rgb(169, 182, 193);
  text-align: center;
  background-color: rgb(84, 84, 85);
}

.dropdown_management {
  text-align: center;
  cursor: pointer;
  padding-top: 5px;
  padding-bottom: 5px;
}

.dropdown_management_menu {
  width: 120%;
}

.dropdown_management_btn {
  width: 120%;
}

.headerOfTable {
  text-align: center;
  font-size: 1.2em;
}

.allforum_table {
  width: 100%;
}

.order_searchbox {
  justify-content: flex-end;
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  margin-right: 20px;
  width: 99%;
}

.operation_icon_set {
  display: flex;
  margin-top: 5px;
  align-self: flex-start;
  padding: 6px;
  background-color: #354551;
  border-radius: 5px;
  margin-right: 5px;
}
.searchbox {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  justify-content: flex-end;
}

.selectbox_label {
  width: 5%;
  margin-left: 10px;
  padding: 0;
}
.searchbox_out {
  background-color: #354551;
  padding: 0;
  padding: 10px;
  border: 0;
  color: aliceblue;
  cursor: pointer;
  border-radius: 10px;
  margin: 5px;
  width: 100%;
}

.ordermanag_btn {
  border: none;
  cursor: pointer;
  margin-left: 15px;
  text-align: center;
}
.selectbox_label {
  width: 11%;
  margin-left: 10px;
  padding: 0;
}
.selectbox_label_top {
  width: 5%;
  margin-left: 10px;
  padding: 0;
}

.comment_selectbox_label {
  width: 8%;
  margin-left: 10px;
  padding: 0;
}
.product_input {
  width: 15%;
}
.price_input {
  width: 8%;
}
.discountFactor_input {
  width: 8%;
}
.sort_input {
  width: 10%;
}
.productType_input {
  width: 20%;
}
.productStatus_input {
  width: 20%;
}

.productName_input {
  width: 25%;
}

.btn_bc {
  background-color: #354551;
  border: #354551;
}

.searchbox_out_btn {
  display: flex;
  width: 5%;
  margin-top: 5px;
  margin-left: 2px;
}

.saveAll_btn {
  width: 99%;
  display: flex;
  justify-content: flex-end;
  margin-bottom: 10px;
}

.comment_articleid_input {
  width: 20%;
}

.comment_userid_input {
  width: 20%;
}
.comment_createdAt_input {
  width: 10%;
}
.commentText_input {
  width: 25%;
}
.commentstatus_input {
  width: 20%;
}
.search_nodata {
  background-color: #222121;
  text-align: center;
  font-size: 1.5em;
  height: 5em;
  vertical-align: middle;
}
</style>
