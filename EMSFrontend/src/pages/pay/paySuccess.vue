<template>
  <div class="paySuccessPage">
    <head-top></head-top>
    <el-scrollbar
      style="height: 100%;"
      wrap-style="overflow-x: hidden; overflow-y: scroll;">
      <div style="padding: 10px; height: 100%;">
        <div class="contentBox">
          <h1>支付成功</h1>
        </div>
      </div>
    </el-scrollbar>
  </div>

</template>
<script>
  import headTop from "../../components/headTop";

  export default {
    name: "paySuccess",
    data() {
      return {

      }
    },
    components: {
      headTop
    },
    mounted() {
      this.$store.commit('loadUserInfo');
    }
  }
</script>


<style scoped>

</style>
