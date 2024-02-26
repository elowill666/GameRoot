//從論壇偷來的忽落html標籤的<>/

//此方法會將文字全部顯示
export function stripHtml(value) {
  if (!value) return "";
  // 創建 DOMParser 實例
  const parser = new DOMParser();
  // 將 HTML 字串解析為 DOM 文檔
  const doc = parser.parseFromString(value, "text/html");
  // 從解析後的 DOM 文檔中獲取純文本內容
  return doc.body.textContent || "";
}

//此方法如果文字長度超過15後面文字會變成...
export function stripHtmlOnly15(value) {
  if (!value) return "";
  // 創建 DOMParser 實例
  const parser = new DOMParser();
  // 將 HTML 字串解析為 DOM 文檔
  const doc = parser.parseFromString(value, "text/html");
  // 從解析後的 DOM 文檔中獲取純文本內容
  if (doc.body.textContent.length > 15) {
    doc.body.textContent = `${doc.body.textContent.substring(0, 15)}......`;
  }
  return doc.body.textContent || "";
}
