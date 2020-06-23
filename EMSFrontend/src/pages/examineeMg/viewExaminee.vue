<template>
  <div class="vieExamineePage">
    <head-top></head-top>
    <el-scrollbar
      style="height: 100%;"
      wrap-style="overflow-x: hidden; overflow-y: scroll;">
      <div class="contentBox">

        <!-- 搜索考生 -->
        <div class="searchExamineeBox">
          <el-card class="searchExamineeCard">
            <div slot="header" class="searchExamineeCardTitle">
              <span>考生搜索</span>
            </div>

            <div class="searchBox">
              <el-form label-width="120px" class="examineeSearchForm">
                <el-form-item label="考生用户名">
                  <el-input v-model.trim="examineeUserName" placeholder="请输入考生用户名"
                            clearable style="width: 250px;">
                  </el-input>
                  <el-button  type="primary" style="width: 100px;" @click="searchExaminee" v-loading="searchBtnLoading">
                    <i class="el-icon-search" /> 查询
                  </el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 考生基本信息 -->
            <div class="examineeInfoBox">
              <el-card class="examineeInfo">
                <div slot="header" class="examineeInfoCardTitle">
                  <span>考生基本信息</span>
                </div>

                <el-form label-width="120" class="exmaineeInfoForm">
                  <el-form-item label="用户名">
                    <span class="examineeInfoDetail">{{ examineeInfo.userName }}</span>
                  </el-form-item>
                  <el-form-item label="姓名">
                    <span class="examineeInfoDetail">{{ examineeInfo.trueName }}</span>
                  </el-form-item>
                  <el-form-item label="性别">
                    <span class="examineeInfoDetail">{{ examineeInfo.gender }}</span>
                  </el-form-item>
                  <el-form-item label="出生日期">
                    <span class="examineeInfoDetail">{{ examineeInfo.birthday }}</span>
                  </el-form-item>
                  <el-form-item label="身份证号">
                    <span class="examineeInfoDetail">{{ examineeInfo.identity }}</span>
                  </el-form-item>
                  <el-form-item label="手机号">
                    <span class="examineeInfoDetail">{{ examineeInfo.phone }}</span>
                  </el-form-item>
                  <el-form-item label="邮箱">
                    <span class="examineeInfoDetail">{{ examineeInfo.email }}</span>
                  </el-form-item>
                  <el-form-item>
                    <el-image style="width: 150px; height: 150px"
                      :src="examineeInfo.photoURL" fit="fit">
                    </el-image>
                  </el-form-item>
                </el-form>
              </el-card>
            </div>

          </el-card>
        </div>

        <!-- 考试列表 -->
        <div class="examList">
          <el-card class="examListCard">
            <div slot="header" class="searchExamBoxCardTitle">
              <span>考试列表</span>
              <el-button style="float: right; padding: 8px" type="primary"
                         @click="exportExamList" v-loading="exportBtnLoading">
                导出列表
              </el-button>
            </div>

            <div class="examListTable">
              <template>
                <el-table stripe border :data="examList"
                          v-loading="examListTableLoading">
                  <el-table-column type="index" :index="examListTableIndexChange">
                  </el-table-column>
                  <el-table-column label="考试编号" prop="examNumber" width="110">
                  </el-table-column>
                  <el-table-column label="考试名称" prop="examName" width="150">
                  </el-table-column>
                  <el-table-column label="考试日期" prop="examDate" width="100">
                  </el-table-column>
                  <el-table-column label="考试开始时间" prop="startTime" width="110">
                  </el-table-column>
                  <el-table-column label="考试结束时间" prop="endTime" width="110">
                  </el-table-column>
                  <el-table-column label="考试报名费" prop="regPrice" width="110">
                  </el-table-column>
                  <el-table-column label="考生准考证号" prop="examineeNumber" width="140">
                  </el-table-column>
                  <el-table-column label="考生缴费状态" prop="paymentStatus" width="110">
                  </el-table-column>
                  <el-table-column label="考生考场" prop="clrNumber" width="100">
                  </el-table-column>
                  <el-table-column label="考生座位号" prop="seatNumber" width="100">
                  </el-table-column>
                </el-table>
              </template>
            </div>

            <!-- 页码 -->
            <div class="examPagination" style="text-align: center;">
              <el-pagination layout="total, prev, pager, next" :total="examTotal"
                             :current-page="examListTablePageNo"
                             :page-size="examListTablePageSize"
                             @current-change="handleExamineeCurrentChange">
              </el-pagination>
            </div>
          </el-card>
        </div>

      </div>
    </el-scrollbar>
  </div>

</template>

<script>
  import headTop from "../../components/headTop";
  import {confirmLogout} from "../../utils/logout";
  import {download} from "../../utils/DownloadFormatFile";

  export default {
    name: "viewExaminee",
    data() {
      return {
        /* 搜索考生 */
        examineeUserName: '',
        searchBtnLoading: false,
        examineeInfo: {
          userName: '',
          trueName: '',
          gender: '',
          birthday: '',
          identity: '',
          phone: '',
          email: '',
          photoURL: ''
        },
        /* 考试列表 */
        examList: [],
        exportBtnLoading: false,
        examListTableLoading: false,
        examTotal: 0,
        examListTablePageNo: 1,
        examListTablePageSize: 6,
      }
    },
    methods: {
      /* 搜索考生 */
      searchExaminee() {
        this.searchBtnLoading = true;
        if(this.examineeUserName === '') {
          this.$message({
            type: 'warning',
            message: '请先输入考生用户名'
          });
          this.searchBtnLoading = false;
        } else {
          this.$axios.post('/searchExamineeFromViewExaminee', {
            sessionID: this.$store.state.userInfo.sessionID,
            userName: this.examineeUserName,
          }).then(response => {
            const responseData = response.data;
            if(responseData.status === 200) {
              this.examineeInfo = responseData.userInfo;
              this.examineeInfo.photoURL = this.$requestURL + this.examineeInfo.photoURL;

              this.examListTableLoading = true;
              this.searchExam(this.examineeUserName, 1);
            } else if(responseData.status === 400) {
              this.$message({
                type: 'warning',
                message: '无查找的用户'
              })
            } else {
              confirmLogout();
            }
            this.searchBtnLoading = false;
          }).catch(error => {
            console.log(error);
            this.searchBtnLoading = false;
          });
        }
      },
      /* 考生列表 */
      searchExam(userName, curPage) {
        this.$axios.post('/searchExamListFromViewExaminee', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: userName,
          curPage: curPage
        }).then(response => {
          const responseData = response.data;
          if(responseData.status === 200) {
            this.examList = responseData.examList;
            this.examTotal = responseData.examListSize;
          } else if(responseData.status === 400) {
            this.$message({
              type: 'warning',
              message: '暂无考试'
            })
          }
          this.examListTableLoading = false;
        }).catch(error => {
          console.log(error);
          this.examListTableLoading = false;
        });
      },
      examListTableIndexChange(index) {
        return (this.examListTablePageNo - 1) * this.examListTablePageSize + index + 1;
      },
      handleExamineeCurrentChange(val) {
        this.examListTablePageNo = val;
        this.examListTableLoading = true;
        this.searchExam(this.examineeUserName, val);
      },
      exportExamList() {
        if(this.examineeUserName === '') {
          this.$message({
            type: 'warning',
            message: '请先输入考生用户名'
          });
          return false;
        }
        this.exportBtnLoading = true;
        this.$axios.post('/buildExamInfoList', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.examineeUserName,
          role: this.$store.state.userInfo.role
        }).then(response => {
          const responseData = response.data;
          if(responseData.status === 200) {
            let url = '/downloadExamInfoList' + '?userName=' + this.examineeUserName + '&role=' + this.$store.state.userInfo.role;
            download(url, this.examineeUserName + '考试信息列表' + '.xls');
          } else if(responseData.status === 400) {
            this.$message({
              type: 'warning',
              message: '暂无可导出内容'
            });
          } else {
            confirmLogout();
          }
          this.exportBtnLoading = false;
        }).catch(error => {
          console.log(error);
          this.exportBtnLoading = false;
        });
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
  .vieExamineePage {
    height: 100%;
    overflow: hidden;
  }

  .contentBox {
    margin-bottom: 10%;
  }

  .searchExamineeBox, .examList {
    width: 90%;
    padding-top: 5%;
    padding-left: 5%;
  }

  .examineeInfoBox {
    width: 80%;
    padding-left: 8%;
  }

  .examineeInfoDetail {
    font-size: 15px;
  }
</style>
