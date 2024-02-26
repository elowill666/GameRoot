//用來將使用者id轉換成某個形式
export function formatUserId(userId) {
  return `GRU${userId.toString().padStart(4, "0")}`;
}

//用來將會員id從GRU0001轉換回0001
export function reformatUserId(userId) {
  return Number(userId.replace(/[^\d]/g, ""));
}

//用來將產品id轉換成某個形式
export function formatProductId(productId, productType) {
  if (productType === "digitalGames") {
    return `PD${productId.toString().padStart(4, "0")}`;
  } else if (productType === "physical") {
    return `PM${productId.toString().padStart(4, "0")}`;
  } else if (productType === "cash") {
    return `PC${productId.toString().padStart(4, "0")}`;
  }
}
//用來將產品id從PX0001轉換回0001
export function reformatProductId(productId) {
  return Number(productId.replace(/[^\d]/g, ""));
}

//用來將訂單id轉換成某個形式
export function formatOrderId(orderId) {
  return `GRO${orderId.toString().padStart(8, "0")}`;
}

//用來將訂單id從GRO0000001轉換回00000001
export function reformatOrderId(orderId) {
  return Number(orderId.replace(/[^\d]/g, ""));
}

//用來將文章id轉換成某個形式
export function formatArticlesId(articlesId) {
  return `FA${articlesId.toString().padStart(8, "0")}`;
}

//用來將文章id從FA0000001轉換回00000001
export function reformatArticlesId(articlesId) {
  return Number(articlesId.replace(/[^\d]/g, ""));
}

//用來將留言id轉換成某個形式
export function formatCommentId(commentId) {
  return `FC${commentId.toString().padStart(8, "0")}`;
}

//用來將留言id從FC0000001轉換回00000001
export function reformatCommentId(commentId) {
  return Number(commentId.replace(/[^\d]/g, ""));
}
