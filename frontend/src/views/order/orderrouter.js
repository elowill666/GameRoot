import Shoppingcart from "./Shoppingcart.vue";
import Paydata from "./Paydata.vue";
import OrderDetails from "./OrderDetails.vue";
import OrderDetailsCard from "./OrderDetailsCard.vue";
import OrderRedfund from "./OrderRedfund.vue";
import RootNode from "./RootNode.vue";
import axios from "axios";
import Swal from "sweetalert2";
import UserOrderDetail from "./UserOrderDetail.vue";


export default [
  { path: "/order/rootnode", name: "rootnode", component: RootNode },
  {
    path: "/order/shoppingcart",
    name: "shoppingcart",
    component: Shoppingcart,
    meta: {
      requiresAuth: true, // 标记该路由需要认证
    },
    beforeEnter: (to, from, next) => {
      const API_URL = `${import.meta.env.VITE_JAVA_URL}`;
      axios.get(`${API_URL}/user/authenticate`, { withCredentials: true })
        .then((res) => {
          if (res.data == false) {
            Swal.fire({
              title: "Error!",
              text: "請先登入",
              icon: "error",
              confirmButtonText: "登入去",
            }).then((result) => {
              if (result.isConfirmed) {
                next("/user/userlogin");
                return;
              }
            });
          } else {
            axios.get(API_URL + '/shoppingcart/countshoppingcart', { withCredentials: true })
              .then((res) => {
                if (res.data == false) {
                  Swal.fire({
                    title: "購物車沒有商品",
                    text: "前往商店",
                    icon: "info",
                    confirmButtonText: "確定",
                  }).then((result) => {
                    if (result.isConfirmed) {
                      next("/product/products");
                      return;
                    }
                  });
                } else {
                  next();
                }
              })
          }
        })
        .catch((error) => {
          console.error(error);
        });
    },
  },
  { path: "/order/paydata", name: "paydata", component: Paydata },
  {
    path: "/order/orderDetails/:id",
    name: "orderDetails",
    component: OrderDetails,
  },
  {
    path: "/order/orderDetailsCard/:id",
    name: "orderDetailsCard",
    component: OrderDetailsCard,
  },
  {
    path: "/order/orderRedfund/:detailId",
    name: "orderRedfund",
    component: OrderRedfund,
  },
    {
    path: "/order/userorderdetail/:id",
    name: "UserOrderDetail",
    component: UserOrderDetail,
    props: true,
  },
];
