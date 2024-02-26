import UserManagement from "./UserManagement.vue";
import ProductManagement from "./ProductManagement.vue";
import OrderManagement from "./OrderManagement.vue";
import ArticleManagement from "./ArticleManagement.vue";
import DataManagement from "./DataManagement.vue";
import LaunchProduct from "./LaunchProduct.vue";
import DashboardUserProfile from "./DashboardUserProfile.vue";
import SuggestionBox from "./SuggestionBox.vue";
import ChangeProduct from "./ChangeProduct.vue";
import CouponInsert from "./CouponInsert.vue";
import CouponManagement from "./CouponManagement.vue";

export default [
  {
    path: "/dashboard/productmanagement/:page",
    name: "productmanagement",
    component: ProductManagement,
  },
  {
    path: "/dashboard/ordermanagement/:page",
    name: "ordermanagement",
    component: OrderManagement,
  },
  {
    path: "/dashboard/usermanagement/order/:userId/:page",
    name: "ordermanagementforuser",
    component: OrderManagement,
  },

  {
    path: "/dashboard/forummanagement/article/:page",
    name: "articlemanagement",
    component: ArticleManagement,
  },
  {
    path: "/dashboard/forummanagement/comment/:page",
    name: "commentmanagement",
    component: ArticleManagement,
  },
  {
    path: "/dashboard/usermanagement/forum/comment/:userId/:page",
    name: "commentmanagementforuser",
    component: ArticleManagement,
  },
  {
    path: "/dashboard/forummanagement/report/:page",
    name: "reportmanagement",
    component: ArticleManagement,
  },
  {
    path: "/dashboard/usermanagement/forum/article/:userId/:page",
    name: "articlemanagementforuser",
    component: ArticleManagement,
  },
  {
    path: "/dashboard/dataManagement",
    name: "dataManagement",
    component: DataManagement,
  },
  {
    path: "/dashboard/usermanagement/:page",
    name: "usermanagement",
    component: UserManagement,
  },
  {
    path: "/dashboard/launchproduct",
    name: "LaunchProduct",
    component: LaunchProduct,
  },
  {
    path: "/dashboard/productmanagement/changeproduct/:id",
    name: "ChangeProduct",
    component: ChangeProduct,
  },

  {
    path: "/dashboard/usermanagement/userprofile/:id",
    name: "DashboardUserProfile",
    component: DashboardUserProfile,
  },

  {
    path: "/dashboard/suggestionbox",
    name: "SuggestionBox",
    component: SuggestionBox,
  },
  {
    path: "/dashboard/couponinsert",
    name: "CouponInsert",
    component: CouponInsert,
  },
  {
    path: "/dashboard/couponmanagement",
    name: "CouponManagement",
    component: CouponManagement,
  },
];
