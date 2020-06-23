<template>
  <div class="viewInfoPage">
    <head-top></head-top>
    <el-scrollbar
      style="height: 100%;"
      wrap-style="overflow-x: hidden; overflow-y: scroll;">
      <div style="padding: 10px; height: 100%;">
        <h1 class="infoTitle">{{ role }}信息</h1>
        <div class="infoContent">
          <el-card class="box-card">
            <div class="text item">
              姓名：{{ trueName }}
            </div>
            <div class="text item">
              用户名：{{ userName }}
            </div>
            <div class="text item">
              性别：{{ gender }}
            </div>
            <div class="text item">
              手机：{{ phone }}
            </div>
            <div class="text item">
              <div class="block">
                <el-avatar shape="square" :size="60" :src="photoURL"></el-avatar>
              </div>
            </div>
          </el-card>
        </div>
      </div>
    </el-scrollbar>
  </div>
</template>

<script>
  import headTop from "../components/headTop";

  export default {
    name: "viewInfo",
    data() {
      return {
        role: '',
        userName: '',
        trueName: '',
        gender: '',
        phone: '',
        photoURL: ''
      }
    },
    components: {
      headTop
    },
    methods: {
      getUserRole(role) {
        if(role === 1) return '系统管理员';
        else if(role === 2) return '考试管理员';
        else if(role === 3) return '考生';
      }
    },
    mounted() {
      const userInfo = this.$store.state.userInfo;
      this.role = this.getUserRole(userInfo.role);
      this.userName = userInfo.userName;
      this.trueName = userInfo.trueName;
      this.gender = userInfo.gender;
      this.phone = userInfo.phone;
      this.photoURL = 'http://localhost:8082' + userInfo.photoURL;
    }
  }
</script>

<style scoped>
  .viewInfoPage{
    height: 100%;
    overflow: hidden;
  }

  .infoTitle {
    text-align: center;
    font-size: 25px;
    font-weight: normal;
    color: #666;
  }

  .box-card {
    background-color: #F9FAFC;
    margin: 0 auto;
  }

  .text {
    font-size: 16px;
  }

  .item {
    padding: 16px 0;
  }

  .box-card {
    width: 480px;
  }
</style>
