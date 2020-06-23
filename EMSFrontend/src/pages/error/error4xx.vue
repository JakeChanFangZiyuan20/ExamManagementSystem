<template>
  <div class="err4xxPage">
    <h1>资源无法定位</h1>
    <h2><a :href="returnPath">回到首页</a></h2>
  </div>
</template>

<script>
  export default {
    name: "error4xx",
    data() {
      return {
        returnPath: ''
      }
    },
    created() {
      // const role = sessionStorage.getItem('userInfo').role;
      this.$store.commit('loadUserInfo');
      const role = this.$store.state.userInfo.role;
      console.log('error page get role: ' + role);
      if(role === 1) {
        this.returnPath = '/emsAdmin';
      } else if (role === 2) {
        this.returnPath = '/emsMg';
      } else if (role === 3) {
        this.returnPath = '/emsExaminee';
      }
    },
  }
</script>

<style scoped>
  .err4xxPage {
    height: 100%;
    width: 100%;
  }
</style>
