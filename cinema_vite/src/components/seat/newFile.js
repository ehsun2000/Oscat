import { ref } from 'vue';
import SeatStatusDialog from './SeatStatusDialog.vue';

export default (await import('vue')).defineComponent({
  data() {
    return {
      findSeatApi: import.meta.env.VITE_API_FindSeat,
      seatIds: [],
      roomId: ref(''),
      seats: ref([]),
      errorMessage: '',
    };
  },
  methods: {
    async getSeatStatus() {
      try {
        const response = await fetch(`${this.findSeatApi}` + `${this.roomId}`);
        if (response.ok) {
          const data = await response.json();
          this.seats = data;
          this.errorMessage = '';
        } else {
          this.errorMessage = '請求失敗：' + response.statusText;
        }
      } catch (error) {
        this.errorMessage = '請求失敗：' + error.message;
      }
      openSeatStatusDialog(seat);
      {
        // 創建彈出式視窗容器
        const dialogContainer = document.createElement('div');

        // 使用 Portals 或您的彈出式視窗庫來顯示彈出式視窗，這取決於您的實際需求
        // 這裡使用 Vue 3 的 Portals 作為範例
        const portalTarget = document.querySelector('#dialog-container');
        if (portalTarget) {
          portalTarget.appendChild(dialogContainer);
        } else {
          // 如果找不到 portalTarget，您可以添加適當的錯誤處理邏輯
          console.error('找不到 portalTarget');
          return;
        }

        // 創建 Vue 應用
        const dialogApp = createApp(SeatStatusDialog);
        const dialogInstance = dialogApp.mount(dialogContainer);

        // 設置彈出式視窗的相關數據，以便根據所選座位進行修改
        dialogInstance.seat = seat;

        // 為彈出式視窗添加關閉方法
        dialogInstance.closeDialog = () => {
          // 從 Portals 中移除彈出式視窗
          if (portalTarget) {
            portalTarget.removeChild(dialogContainer);
          }

          // 可以在關閉時執行一些清理邏輯
          dialogInstance.unmount();
        };
      }
    },
  },
});
