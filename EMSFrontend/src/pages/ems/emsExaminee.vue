<template>
  <div class="managePage" style="height: 100%;">
    <el-container style="height: 100%; border: 0;">
      <!-- 左侧导航 -->
      <el-aside width="200px" style="background-color: rgb(50, 64, 87); height: 100%;">

        <el-scrollbar
          style="height: 100%;"
          wrap-style="overflow-x: hidden;">
          <el-menu default-active="2" background-color="rgb(50, 64, 87)" text-color="#fff"
            active-text-color="#20a0ff" style="width: 100%;" router>
            <el-menu-item index="emsExaminee"><i class="el-icon-menu" style="color: #f9f9f9;"></i>首页</el-menu-item>
            <!-- 考试信息 -->
            <el-submenu index="examInfo">
              <template slot="title"><i class="el-icon-tickets" style="color: #f9f9f9;"></i>考试信息</template>
              <el-menu-item index="examineeViewExamInfo">查看考试信息</el-menu-item>
              <el-menu-item index="examineeRegExamOnline">在线考试报名</el-menu-item>
              <el-menu-item index="examineeViewRegPrice">查看未缴费</el-menu-item>
            </el-submenu>

            <!-- 个人信息 -->
            <el-submenu index="personalInfo">
              <template slot="title"><i class="el-icon-s-custom" style="color: #f9f9f9;"></i>个人信息</template>
              <el-menu-item index="examineeEditInfo">查看 / 编辑</el-menu-item>
            </el-submenu>

          </el-menu>
        </el-scrollbar>
      </el-aside>

      <!-- 右侧内容 -->
      <el-container>
        <!-- 右侧主页 -->
        <el-main style="padding: 0;">
            <keep-alive>
              <router-view/>
            </keep-alive>
        </el-main>

      </el-container>

    </el-container>

  </div>
</template>

<script>
  export default {
    name: "emsExamminee",
    computed: {
      defaultActive: function () {
        return this.$route.path.replace('/', '');
      }
    },
    components: {}
  }
</script>

<style scoped>
  .el-submenu .el-menu-item {
    background-color: #202639 !important;
  }

  .el-submenu .el-menu-item:hover {
    background-color: #585e6e !important;
    overflow-x: hidden !important;
  }

</style>
