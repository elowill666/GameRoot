//用來將產品類別轉換成某個形式
export function formatProductType(productId, productType) {
  if (productType === "digitalGames") {
    return `數位商品`;
  } else if (productType === "physical") {
    return `實體商品`;
  } else if (productType === "cash") {
    return `儲值點數`;
  }
}
//用來將使用者 "權限" 轉換成某個形式
export function formatPermission(permissions) {
  if (1 === permissions) {
    return "管理員";
  } else if (0 === permissions) {
    return "一般會員";
  } else if (-1 === permissions) {
    return "停權會員";
  }
}

//用來將文章及留言狀態轉換成某個形式
export function formatForumStatus(status) {
  if ("normal" === status) {
    return "一般文章";
  } else if ("banned" === status) {
    return "禁用文章";
  }
}

//用來將文章及留言狀態轉換回資料庫儲存的文字
export function reformatForumStatus(status) {
  if ("一般文章" === status) {
    return "normal";
  } else if ("禁用文章" === status) {
    return "banned";
  }
}
