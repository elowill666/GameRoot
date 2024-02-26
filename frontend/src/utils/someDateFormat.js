import { format } from "date-fns";

//用來將時間格式的顯示轉變為 "yyyy/MM/dd"
export function formatDate(date) {
  // 如果 date 不是有效的 Date 對象
  if (date == null) {
    return "";
  }
  // 讓此格式的時間變成String"1970/01/01"
  return format(date, "yyyy/MM/dd");
}

//用來將時間格式的顯示轉變為 "yyyy-MM-dd HH:mm:ss"
export function formatFullDate(dateTime) {
  // 如果 date 不是有效的 Date 對象
  if (dateTime == null) {
    return "-";
  }
  // 讓此格式的時間變成String"1970/01/01 00:00:00"
  return format(dateTime, "yyyy-MM-dd HH:mm:ss");
}

//用來將時間格式的顯示轉變為 yyyy-MM-dd'T'HH:mm:ss.SSS
export function formatFullAndFullDate(date) {
  if (date == null) {
    return null;
  }
  return format(date, "yyyy-MM-dd'T'HH:mm:ss.SSS");
}
