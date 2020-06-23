<template>
  <el-header>
    <el-row align="middle" style="position: relative; top: 10px;">
      <el-col :span="12">
        <div class="grid-content bg-purple" style="position: relative; top: 10px;">
          <el-breadcrumb separator-class="el-icon-arrow-right" style="font-size: 16px;">
            <el-breadcrumb-item>EMS</el-breadcrumb-item>
            <el-breadcrumb-item v-for="(item, index) in $route.meta" :key="index">{{item}}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
      </el-col>

      <el-col :span="12">
        <div class="grid-content bg-purple-light" style="text-align: right;">
          <el-dropdown>
            <span class="el-dropdown-link">
              <el-avatar shape="square" :src="photoURL" fit="fit" class="avatar">
              </el-avatar>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="toViewInfoPage()">首页</el-dropdown-item>
              <el-dropdown-item @click.native="logout()">退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-col>
    </el-row>
  </el-header>
</template>

<script>
  import { confirmLogout } from "../utils/logout";

  export default {
    name: "headTop",
    data() {
      return {
        toPath: '',
        viewInfoPath: '',
        photoURL: ''
      }
    },
    mounted() {
      this.$store.commit('loadUserInfo');
      const userPhotoURL = this.$store.state.userInfo.photoURL;
      this.photoURL = this.$requestURL + userPhotoURL;
      // console.log(this.photoURL);
    },
    methods: {
      toViewInfoPage (){
        this.$store.commit('loadUserInfo');
        const role = this.$store.state.userInfo.role;

        if(role === 1){
          this.viewInfoPath = '/emsAdmin';
        } else if(role === 2){
          this.viewInfoPath = '/emsMg';
        } else if(role === 3){
          this.viewInfoPath = '/emsExaminee';
        }

        this.$router.push({ path: this.viewInfoPath }).catch(data => { });
      },
      logout() {
        confirmLogout();
      },
      updateAvatar(avatarURL) {
        this.photoURL = avatarURL;
      }
    }
  }
</script>

<style scoped>
  .el-header {
    background-color: #EFF2F7;
  }

  .el-dropdown-link {
    cursor: pointer;
    color: #409EFF;
  }

</style>
