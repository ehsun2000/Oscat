<template>
  <div class="container w-75">
    <div class="card">
      <h5 class="card-header">平台服務條款</h5>
      <div class="card-body">
        <p class="card-text">
          Oscat公司（以下稱「甲方」），係依據下列服務條款（以下稱「本服務條款」）
          提供會員（以下稱「乙方」）於使用會員服務、參與甲方活動、於Oscat售票網
          (https://www.mai-mai.com.tw，(以下稱買買售票網) 及 Oscat影城官網
          (https://www.showtimes.com.tw，以下稱Oscat影城官網)
          購票等服務（以下稱「本服務」）。
          一、於乙方完成本服務之會員註冊手續、開始使用本服務前，應先行閱讀、瞭解本服務條款之全部內容，
          如乙方選擇同意並接受本服務條款或開始使用本服務時，將視為乙方已完全瞭解並同意全部接受本服務
          條款，於本服務現有或未來更新、新增服務內容或項目時，亦同。
          二、甲方有權因應法規要求、營業所需或其他理由，於任何時間修訂、變更或調整本服務條款之內容，
          該修訂、變更或調整後的服務條款內容，甲方將公告於買買售票網，惟不再個別另行通知乙方，乙方應
          隨時注意上開本服務條款之修訂、變更或調整。
          三、乙方如於前述本服務條款修訂、變更或調整後，表示同意或繼續使用本服務時，視為乙方已閱讀並
          完全瞭解、同意接受本服務條款暨其全部修訂、變更或調整。
          四、若乙方不同意本服務條款的任一約定，或不同意前述本服務條款之修訂、變更、調整之內容或方式
          ，乙方同意並知悉應立即停止使用本服務。
          五、如有任何事由致乙方不具備完全之行為能力（例如：未成年人、受輔助宣告之人），乙方於註冊、
          使用本服務前，應由其法定代理人、輔助人、監護人或類此身分之人閱讀、瞭解本服務條款，並得其法
          定代理人、輔助人、監護人或類此身分之人同意後，方得為之。
          六、本服務如有下述情事將暫時停止提供服務，乙方不得提任何主張或請求：
          1.網站進行例行性維護、搬遷、更換、升級、或維修。
          2.因第三人之行為或不可歸責於甲方之事由，經甲方判斷有暫停服務之需求。
          3.因天災、電信業者中斷其他不可抗力事由致暫停服務者。
          七、乙方同意使用本服務係基於個人意願，並自負任何風險，若因使用本服務及相關網站導致直接或間
          接之損失或損害(包括但不限於乙方資料流失等)，甲方均不負任何責任。
        </p>
      </div>
    </div>
    <div class="card">
      <h5 class="card-header">隱私政策及個人資料使用條款</h5>
      <div class="card-body">
        <p class="card-text">
          Oscat公司
          （以下稱「甲方」），依《個人資料保護法》第8條第1項之規定告知Oscat會員
          （以下稱「乙方」）下列事項。 （一）個人資料蒐集之目的：
          包含甲方行銷、營業管理與提供服務等目的所為之蒐集處理及利用。
          （二）蒐集個人資料之類別：
          依上開目的所需蒐集之乙方個人資料與甲、乙方往來資料，包括乙方真實姓名、出生年月日、聯絡電話
          （含手機）、E-Mail、性別、住址……等。
          （三）個人資料利用之期間、地區、對象及方式：
          1、期間：於會員與服務使用期間內。
          2、地區：甲方、與甲方因服務或業務需要訂有契約之第三人及乙方所在之地區。
          3、對象：甲方、與甲方因服務或業務需要訂有契約之第三人
          4、方式：以電子、自動化設備或其他非自動化之利用方式。
          （四）除法律另有規定外，依《個人資料保護法》第3條規定，乙方得就上開其所提供之個人資料行使下列
          權利：
          1、查詢或請求閱覽，惟甲方可依《個人資料保護法》第14條之規定酌收必要成本費用。
          2、請求製給複製本，惟甲方可依《個人資料保護法》第14條之規定酌收必要成本費用。
          3、請求補充或更正，惟乙方須依《個人資料保護法施行細則》第19條規定為適當之釋明。
          4、依據《個人資料保護法》第11條第2項至第4項規定，請求甲方停止蒐集、處理或利用。
          5、依據《個人資料保護法》第11條第3項、第4項規定請求刪除。
          （五）乙方得自由選擇是否提供個人資料，惟如乙方未提供前開各項個人資料時，將導致乙方無法使用完整
          之本服務或使用上之障礙。
          （六）若因電腦病毒入侵或發作、政府管制而造成暫時性關閉或其他不可抗力事由而造成乙方資料丟失、
          盜用、毀損或竄改等事由，乙方不得對甲方提出任何主張或請求。
        </p>
        <button type="button" class="btn btn-primary" @click="previousPage">
          返回
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
// 返回前一頁
const previousPage = () => {
  history.back();
};
</script>

<style scoped></style>
