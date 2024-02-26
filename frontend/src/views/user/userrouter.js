import UserLogin from "./UserLogin.vue";
import UserProfile from "./UserProfile.vue"
import UserSignUp from "./UserSignUp.vue"
import UserFour from "./UserFour.vue";
import UserEmailCheck from "./UserEmailCheck.vue";
import UserChangePassword from "./UserChangePassword.vue";
import ChatOneToOne from "./ChatOneToOne.vue";
import UserCollections from "./UserCollections.vue"
import FirstLoginAuthentication from "./FirstLoginAuthentication.vue"

export default [
  { path: "/user/userlogin", name: "userlogin", component: UserLogin },
  { path: "/user/usersignup", name: "usersignup", component: UserSignUp },
  { path: "/user/userfour", name: "userfour", component: UserFour },
  { path: "/user/userEmailCheck", name: "userEmailCheck", component: UserEmailCheck },
  { path: "/user/userChangePassword", name: "userChangePassword", component: UserChangePassword },
  { path: "/user/userprofile", name: "userprofile", component: UserProfile },
  { path: "/user/chatOneToOne", name: "chatOneToOne", component: ChatOneToOne },

  { path: "/user/usercollections/:page", name: "UserCollections", component: UserCollections },
  { path: "/user/firstLoginAuthentication", name: "firstLoginAuthentication", component: FirstLoginAuthentication },
  { path: "/user/userlogin/:id", name: "googleuserlogin", component: UserLogin },
];
