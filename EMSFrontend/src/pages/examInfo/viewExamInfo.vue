<template>
  <div class="vieExamInfoPage">
    <head-top></head-top>
    <el-scrollbar
      style="height: 100%;"
      wrap-style="overflow-x: hidden; overflow-y: scroll;">
      <div class="contentBox">
        <div class="examInfoBox">
          <el-card class="examInfoCard">
            <div slot="header" class="examInfoCardTitle">
              <span>考试信息</span>
              <el-button style="float: right; padding: 8px" type="primary"
                         @click="exportExamList" v-loading="exportExamListBtnLoading">
                导出列表
              </el-button>
            </div>

            <div class="examListTable">
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
            </div>

            <!-- 页码 -->
            <div class="examPagination" style="text-align: center;">
              <el-pagination layout="total, prev, pager, next" :total="examTotal"
                             :current-page="examListTablePageNo"
                             :page-size="examListTablePageSize"
                             @current-change="handleExamCurrentChange">
              </el-pagination>
            </div>

          </el-card>
        </div>

        <div class="printInfoBox">
          <el-card class="printInfoCard">
            <div slot="header" class="printInfoCardTitle">
              <span>准考证打印</span>
            </div>

            <div class="inputInfoBox">
              <el-form label-width="120px" class="examineeNumberForm">
                <el-form-item label="准考证号">
                  <el-input v-model.trim="examineeNumber" placeholder="请输入准考证号"
                            clearable style="width: 250px">
                  </el-input>
                  <el-button plain type="primary" @click="viewExamineeCertification">预览</el-button>
                  <el-button type="primary" @click="printExamineeCertification"
                             :disabled="printBtnDisabled" v-print="'#certification'">
                    打印
                  </el-button>
                </el-form-item>
              </el-form>

              <el-card class="certificationCard" id="certification">
                <div style="font-size: 30px; font-weight: bold; text-align: center;"
                     class="certificationCardTitle">
                  准考证
                </div>
                <div class="certificationContent">
                  <div class="basicInfoTable">
                    <el-row>
                      <el-col :span="12" class="basicInfoCol">
                        <el-row>
                          <el-col :span="5">
                            <div class="basicInfoLabel">准考证号</div>
                          </el-col>
                          <el-col :span="13">
                            <div class="basicInfoContent">{{ printData.examineeNumber }}</div>
                          </el-col>
                        </el-row>
                        <el-row>
                          <el-col :span="5">
                            <div class="basicInfoLabel">身份证号</div>
                          </el-col>
                          <el-col :span="13">
                            <div class="basicInfoContent">{{ printData.identity }}</div>
                          </el-col>
                        </el-row>
                        <el-row>
                          <el-col :span="5">
                            <div class="basicInfoLabel">用户名</div>
                          </el-col>
                          <el-col :span="13">
                            <div class="basicInfoContent">{{ printData.userName }}</div>
                          </el-col>
                        </el-row>
                        <el-row>
                          <el-col :span="3">
                            <div class="basicInfoLabel">姓名</div>
                          </el-col>
                          <el-col :span="6">
                            <div class="basicInfoContent">{{ printData.trueName }}</div>
                          </el-col>
                        </el-row>
                        <el-row>
                          <el-col :span="3">
                            <div class="basicInfoLabel">性别</div>
                          </el-col>
                          <el-col :span="6">
                            <div class="basicInfoContent">{{ printData.gender }}</div>
                          </el-col>
                        </el-row>
                      </el-col>
                      <el-col :span="12" class="photoCol">
                        <el-image style="width: 150px; height: 200px; "
                                  :src="printData.photoURL" fit="fit">
                        </el-image>
                      </el-col>
                    </el-row>
                  </div>

                  <template>
                    <el-table stripe border :data="printExamList"
                              v-loading="printExamListLoading">
                      <el-table-column label="编号" prop="examNumber">
                      </el-table-column>
                      <el-table-column label="名称" prop="examName">
                      </el-table-column>
                      <el-table-column label="日期" prop="examDate">
                      </el-table-column>
                      <el-table-column label="开始时间" prop="startTime">
                      </el-table-column>
                      <el-table-column label="结束时间" prop="endTime">
                      </el-table-column>
                      <el-table-column label="考场" prop="clrNumber">
                      </el-table-column>
                      <el-table-column label="座位号" prop="seatNumber">
                      </el-table-column>
                    </el-table>
                  </template>
                </div>
              </el-card>
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
    name: "viewExamInfo",
    data() {
      return {
        examList: [],
        examListTableLoading: false,
        examTotal: 0,
        examListTablePageNo: 1,
        examListTablePageSize: 6,
        /* 导出 */
        exportExamListBtnLoading: false,
        /* 打印准考证 */
        examineeNumber: '',
        printData: {
          examineeNumber: '',
          identity: this.$store.state.userInfo.identity,
          userName: this.$store.state.userInfo.userName,
          trueName: this.$store.state.userInfo.trueName,
          gender: this.$store.state.userInfo.gender,
          photoURL: this.$requestURL + this.$store.state.userInfo.photoURL,
        },
        printBtnDisabled: true,
        printExamList: [],
        printExamListLoading: false,
      }
    },
    methods: {
      searchExam(userName, curPage) {
        this.$axios.post('/searchExamListFromViewExaminee', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: userName,
          curPage: curPage
        }).then(response => {
          const responseData = response.data;
          if (responseData.status === 200) {
            this.examList = responseData.examList;
            this.examTotal = responseData.examListSize;
          } else if (responseData.status === 400) {
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
      handleExamCurrentChange(val) {
        this.examListTablePageNo = val;
        this.examListTableLoading = true;
        this.searchExam(this.$store.state.userInfo.userName, val);
      },
      exportExamList() {
        this.exportExamListBtnLoading = true;
        this.$axios.post('/buildExamInfoList', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName,
          role: this.$store.state.userInfo.role
        }).then(response => {
          const responseData = response.data;
          if (responseData.status === 200) {
            let url = '/downloadExamInfoList' + '?userName=' + this.$store.state.userInfo.userName + '&role=' + this.$store.state.userInfo.role;
            download(url, this.$store.state.userInfo.userName + '考试信息列表' + '.xls');
          } else if (responseData.status === 400) {
            this.$message({
              type: 'warning',
              message: '暂无可导出内容'
            });
          } else {
            confirmLogout();
          }
          this.exportExamListBtnLoading = false;
        }).catch(error => {
          console.log(error);
          this.exportExamListBtnLoading = false;
        });
      },
      /* 打印准考证 */
      viewExamineeCertification() {
        if (this.examineeNumber === '') {
          this.$message({
            type: 'warning',
            message: '请先输入准考证号'
          });
          return false;
        }

        this.printExamListLoading = true;
        this.$axios.post('/viewCertification', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName,
          examineeNumber: this.examineeNumber
        }).then(response => {
          const responseData = response.data;
          if (responseData.status === 200) {
            this.printData.examineeNumber = this.examineeNumber;
            this.printBtnDisabled = false;
            this.printExamList = responseData.examInfoList;
          } else if (responseData.status === 400) {
            this.printBtnDisabled = true;
            this.$message({
              type: 'warning',
              message: '暂无考试或无此准考证'
            });
          } else {
            confirmLogout();
          }

          this.printExamListLoading = false;
        }).catch(error => {
          console.log(error);
          this.printExamListLoading = false;
        });
      },
      printExamineeCertification() {
        if (this.examineeNumber === '') {
          this.$message({
            type: 'warning',
            message: '请先输入准考证号'
          });
          return false;
        }

        this.viewExamineeCertification();


      }
    },
    components: {
      headTop
    },
    mounted() {
      this.$store.commit('loadUserInfo');
      this.searchExam(this.$store.state.userInfo.userName, 1);
    }
  }
</script>

<style scoped>
  .vieExamInfoPage {
    height: 100%;
    overflow: hidden;
  }

  .contentBox {
    margin-bottom: 10%;
  }

  .examInfoBox, .printInfoBox {
    width: 90%;
    padding-top: 5%;
    padding-left: 5%;
  }

  .certificationContent {
    padding-top: 30px;
  }

  .certificationCard {
    width: 85%;
    margin: 0 auto;
  }

  .el-row {
    padding-bottom: 15px;
  }

  .basicInfoLabel {
    width: 100px;
    font-weight: bold;
    font-size: 20px;
  }

  .basicInfoContent {
    font-size: 18px;
  }
</style>
