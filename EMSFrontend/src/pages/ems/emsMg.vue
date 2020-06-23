<template>
  <div class="managePage" style="height: 100%;">
    <el-container style="height: 100%; border: 0;">
      <!-- 左侧导航 -->
      <el-aside width="200px" style="background-color: rgb(50, 64, 87); height: 100%;">

        <el-scrollbar
          style="height: 100%;"
          wrap-style="overflow-x: hidden;">
          <el-menu
            default-active="2"
            background-color="rgb(50, 64, 87)"
            text-color="#fff"
            active-text-color="#20a0ff"
            style="width: 100%;"
            router>
            <el-menu-item index="emsMg"><i class="el-icon-menu" style="color: #f9f9f9;"></i>首页</el-menu-item>
            <!-- 考试管理 -->
            <el-submenu index="examMg">
              <template slot="title"><i class="el-icon-document" style="color: #f9f9f9;"></i>考试管理</template>
              <el-menu-item index="mgAddExam">添加考试</el-menu-item>
              <el-menu-item index="mgViewExam">查看考试</el-menu-item>
              <el-menu-item index="mgRegExam">考试报名</el-menu-item>
            </el-submenu>

            <!-- 教室管理 -->
            <el-submenu index="clrMg">
              <template slot="title"><i class="el-icon-house" style="color: #f9f9f9;"></i>教室管理</template>
              <el-menu-item index="mgAddClr">添加教室</el-menu-item>
              <el-menu-item index="mgViewClr">查看教室</el-menu-item>
            </el-submenu>

            <!-- 考生管理 -->
            <el-submenu index="examineeMg">
              <template slot="title"><i class="el-icon-user" style="color: #f9f9f9;"></i>考生管理</template>
              <el-menu-item index="mgAddExaminee">添加考生账号</el-menu-item>
              <el-menu-item index="mgViewExaminee">查看考生</el-menu-item>
            </el-submenu>

            <!-- 个人信息 -->
            <el-submenu index="personalInfo">
              <template slot="title"><i class="el-icon-s-custom" style="color: #f9f9f9;"></i>个人信息</template>
              <el-menu-item index="mgEditInfo">查看 / 编辑</el-menu-item>
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
    name: "emsMg",
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
