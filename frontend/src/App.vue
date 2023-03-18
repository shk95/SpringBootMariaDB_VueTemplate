<template>
  <div style="position:absolute;width:100%; ">
    <div style="position: relative; left:33%">
      <div class="divTable minimalistBlack" style="width:30%; text-align: center; ">
        <div class="divTableBody">
          <div class="divTableRow">
            <div class="divTableCell">
              <a href="/notice">공지사항</a>
            </div>
            <div class="divTableCell">
              None
            </div>
            <div class="divTableCell">
              None
            </div>
          </div>
        </div>
      </div>
    </div>
    <br />
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App',
}
</script>

<style lang="scss">
@import './assets/css/table.css';

input.form-control:focus,
textarea.form-control:focus {
  border: 1px solid #377EF6 !important;
}

.btn-cancel {
  color: #666 !important;
}

.public {
  .form {
    margin-top: 50px;
    width: 320px;

    .form-group {
      label {
        font-weight: bold;
        color: #555;
      }

      .error {
        line-height: 1;
        display: none;
        margin-top: 5px;
      }
    }
  }
}

.field-error {
  .error {
    display: block;
    color: #ff0000;
  }
}
</style>
