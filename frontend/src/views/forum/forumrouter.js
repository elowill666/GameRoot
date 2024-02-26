import ForumOne from "./ForumOne.vue";
import ForumTwo from "./ForumTwo.vue";
import ForumThree from "./ForumThree.vue";
import ForumFour from "./ForumFour.vue";
import ArticleDetail from "./ArticleDetail.vue";
import ArticleWrite from "./ArticleWrite.vue";
import ArticleEdit from "./ArticleEdit.vue";

export default [
  { path: "/forum/forumone", name: "forumone", component: ForumOne },
  { path: "/forum/forumtwo", name: "forumtwo", component: ForumTwo },
  { path: "/forum/forumthree", name: "forumthree", component: ForumThree },
  { path: "/forum/forumfour", name: "forumfour", component: ForumFour },
  { path: "/forum/articledetail/:id", name: "articledetail", component: ArticleDetail },
  { path: "/forum/articlewrite", name: "articlewrite", component: ArticleWrite },
  { path: "/forum/articleedit/:id", name: "articleedit", component: ArticleEdit },
];
