import Products from "./Products.vue";
import ProductDetail from "./ProductDetail.vue";
import ProductInterview from "./ProductInterview.vue";
import ProductComment from "./ProductComment.vue";
import ProductArticle from "./ProductArticle.vue";
import GameDemands from "./GameDemands.vue";
import ProductImage from "./ProductImage.vue";
import ProductsFindType from "./ProductsFindType.vue";
import ProductsFindSubType from "./ProductsFindSubType.vue";
import ProductWishlist from "./ProductWishlist.vue";
import ProductAds from "./ProductAds.vue";
import ProductFourVue from "./ProductFour.vue";

export default [
  {
    path: "/product/products",
    name: "Products",
    component: Products,
    props: true,
  },
  {
    path: "/product/detail/:id",
    name: "ProductDetail",
    component: ProductDetail,
    props: true,
    children: [
      {
        path: "/product/detail/:id/interview",
        name: "ProductInterview",
        component: ProductInterview,
        props: true,
      },
      {
        path: "/product/detail/:id/comment",
        name: "ProductComment",
        component: ProductComment,
        props: true,
      },
      {
        path: "/product/detail/:id/article",
        name: "ProductArticle",
        component: ProductArticle,
        props: true,
      },
      {
        path: "/product/detail/:id/gamedemand",
        name: "GameDemands",
        component: GameDemands,
        props: true,
      },
      {
        path: "/product/detail/:id/image",
        name: "ProductImage",
        component: ProductImage,
        props: true,
      },
    ],
  },
  {
    path: "/product/main/:type",
    name: "ProductsFindType",
    component: ProductsFindType,
    props: true,
  },
  {
    path: "/product/sub/:subtype",
    name: "ProductsFindSubType",
    component: ProductsFindSubType,
    props: true,
  },
  {
    path: "/product/productWishlist",
    name: "ProductWishlist",
    component: ProductWishlist,
    props: true,
  },
  {
    path: "/product/productads",
    name: "ProductAds",
    component: ProductAds,
  },
  {
    path: "/dashboard/productmanagement/detail/:id/:from",
    name: "ProductDetailFromDashboard",
    component: ProductDetail,
  },
  {
    path: "/product4",
    name: "ProductFourVue",
    component: ProductFourVue,
  },
];
