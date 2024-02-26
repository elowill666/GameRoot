import Swal from "sweetalert2";

//輸入statusIcon(通知Icon)、width(通知長度)、text通知內容、顯示時間
//如果需要接then貌似要另外處理
export function forShortSweetalert(statusIcon, text, width, timer) {
  if (width == null) {
    width = 300;
  }
  if (timer == null) {
    timer = 1200;
  }
  Swal.fire({
    html: `<h4 style="display: flex; justify-content: center; align-items: center; height: 100%; font-family= "Arial", sans-serif;">${text}</h4>`,
    icon: `${statusIcon}`,
    showConfirmButton: false,
    width: width,
    timer: `${timer}`,
    toast: true,

    showClass: {
      popup: `
  animate__animated
  animate__fadeInUp
  animate__faster
  `,
    },
    hideClass: {
      popup: `
  animate__animated
  animate__fadeOutDown
  animate__faster
  `,
    },
  });
}
