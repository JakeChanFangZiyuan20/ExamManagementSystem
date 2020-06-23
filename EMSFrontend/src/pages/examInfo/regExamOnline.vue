<template>
  <div class="regExamOnlinePage">
    <head-top></head-top>
    <el-scrollbar
      style="height: 100%;"
      wrap-style="overflow-x: hidden; overflow-y: scroll;">
      <div style="padding: 10px; height: 100%;">
        <div class="regExamInfoBox">

          <!-- 考试查询，定位 -->
          <div class="examLocationBox">
            <el-card class="examLocationCard">
              <div slot="header" class="examLocationCardTitle">
                <span>考试定位</span>
              </div>

              <!-- 考试定位 Box -->
              <div class="searchBox">
                <el-form label-width="100px" class="examLocationForm">
                  <el-form-item label="考试编号">
                    <el-input v-model.trim="examNumber" placeholder="请输入考试编号"
                              clearable style="width: 250px;">
                    </el-input>
                    <el-button type="primary" style="width: 100px; margin-left: 10px;" @click="locateExam"
                               v-loading="locateExamBtnLoading">
                      <i class="el-icon-search"/> 查询
                    </el-button>
                  </el-form-item>
                </el-form>
              </div>

              <!-- 考试信息 -->
              <div class="examInfoBox">
                <el-table class="examInfoTable" border :data="examInfo"
                          v-loading="examInfoLoading">
                  <el-table-column label="考试编号" prop="examNumber">
                  </el-table-column>
                  <el-table-column label="考试名称" prop="examName">
                  </el-table-column>
                  <el-table-column label="日期" prop="examDate">
                  </el-table-column>
                  <el-table-column label="开始时间" prop="startTime">
                  </el-table-column>
                  <el-table-column label="结束时间" prop="endTime">
                  </el-table-column>
                  <el-table-column label="报名费" prop="regPrice">
                  </el-table-column>
                </el-table>
              </div>
            </el-card>
          </div>

          <!-- 教室容量 -->
          <div class="examClrSituationBox">
            <el-card class="examClrSituationCard">
              <div slot="header" class="examClrSituationCardTitle">
                <span>考试教室情况</span>
              </div>

              <div class="capacitySum">
                <el-row>
                  <el-col :span="12">
                    <el-row>
                      <el-col :span="4">
                        <div class="capacitySumLabel">总容量</div>
                      </el-col>
                      <el-col :span="8">
                        <div class="capacitySumContent">{{ totalCapacity }}</div>
                      </el-col>
                    </el-row>
                  </el-col>
                  <el-col :span="12">
                    <el-row>
                      <el-col :span="5">
                        <div class="capacitySumLabel">剩余容量</div>
                      </el-col>
                      <el-col :span="7">
                        <div class="capacitySumContent">{{ totalCanContain }}</div>
                      </el-col>
                    </el-row>
                  </el-col>
                </el-row>
              </div>


              <div class="clrListTableBox">
                <el-table max-height="300" stripe border :data="examClrList"
                          v-loading="examClrListLoading">
                  <el-table-column type="index" :index="clrListIndexMethod"></el-table-column>
                  <el-table-column label="教室地址" prop="address"></el-table-column>
                  <el-table-column label="教室编号" prop="clrNumber"></el-table-column>
                  <el-table-column label="教室容量" prop="capacity"></el-table-column>
                  <el-table-column label="剩余容量" prop="canContain"></el-table-column>
                </el-table>
              </div>

              <div class="clrListPagination" style="text-align: center;">
                <el-pagination layout="total, prev, pager, next" :total="examClrListTotal"
                               :current-page="examClrListPageNo"
                               :page-size="examClrListPageSize"
                               @current-change="handleExamClrCurrentChange">
                </el-pagination>
              </div>

            </el-card>
          </div>

          <!-- 准考证设置 -->
          <div class="examineeNumberSettingBox">
            <el-card class="examineeNumberSettingCard">
              <div slot="header" class="examineeNumberSettingCardTitle">
                <span>报名设置</span>
              </div>

              <div class="examineeNumberSettingInputBox">
                <el-form label-width="120px" class="examineeNumberSettingForm">
                  <el-form-item label="准考证">
                    <el-input v-model.trim="examineeNumber" placeholder="请输入准考证号"
                              clearable style="width: 250px;">
                    </el-input>
                    <el-button type="primary" style="width: 100px; margin-left: 10px;" @click="confirmAttendExam"
                               v-loading="confirmAttendExamBtnLoading" :disabled="confirmAttendExamBtnDisabled">
                      报名考试
                    </el-button>
                  </el-form-item>
                </el-form>
              </div>
            </el-card>

            <el-dialog title="缴费提醒" :visible.sync="paymentDialogVisible"
                       :before-close="handlePaymentDialogClose">
              <div class="payRegPriceExplain">
                <span>当前考试编号：{{ examNumber }}</span> <br/>
                <span>需缴费：{{ regPrice }}</span>
              </div>
              <span slot="footer" class="dialog-footer">
                <el-button @click="paymentDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveExaminee2Redis">确 定</el-button>
              </span>
            </el-dialog>
          </div>

        </div>
      </div>
    </el-scrollbar>
  </div>

</template>

<script>
  import headTop from "../../components/headTop";
  import {confirmLogout} from "../../utils/logout";

  export default {
    name: "regExamOnline",
    data() {
      return {
        /* 定位考试 */
        examNumber: '',
        regPrice: 0,
        examLocationFormRules: {},
        locateExamBtnLoading: false,
        examInfoLoading: false,
        examInfo: [],
        /* 教室情况 */
        totalCapacity: 0,
        totalCanContain: 0,
        examClrListTotal: 0,
        examClrListPageNo: 1,
        examClrListPageSize: 6,
        examClrList: [],
        examClrListLoading: false,
        /* 确定报名 */
        examineeNumber: '',
        paymentDialogVisible: false,
        confirmAttendExamBtnLoading: false,
        confirmAttendExamBtnDisabled: true,
        infoComplete: false,
        inExam: false,
      }
    },
    methods: {
      /* 定位考试 */
      locateExam() {
        let examNumber = this.examNumber;
        if (examNumber === '') {
          this.$message({
            type: 'warning',
            message: '请先输入考试编号'
          });
          return false;
        }

        this.locateExamBtnLoading = true;
        this.examInfoLoading = true;
        this.$axios.post('/locateExam', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName,
          examNumber: examNumber
        }).then(response => {
          const responseData = response.data;
          if (responseData.status === 200) {
            this.regPrice = responseData.examInfo.regPrice;
            this.examInfo.push(responseData.examInfo);
            this.searchExamClr(this.examNumber, 1);
          } else if (responseData.status === 400) {
            this.$message({
              type: 'warning',
              message: '无找到考试，请重新输入考试编号'
            });
          } else {
            confirmLogout();
          }

          this.locateExamBtnLoading = false;
          this.examInfoLoading = false;
        }).catch(error => {
          console.log(error);
          this.locateExamBtnLoading = false;
          this.examInfoLoading = false;
        })
      },
      /* 教室情况 */
      searchExamClr(examNumber, curPage) {
        this.examClrListLoading = true;
        this.confirmAttendExamBtnLoading = true;
        this.$axios.post('/getExamClrList', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName,
          examNumber: examNumber,
          curPage: curPage
        }).then(response => {
          const responseData = response.data;
          if (responseData.status === 200) {
            this.totalCapacity = responseData.totalCapacity;
            this.totalCanContain = responseData.totalCanContain;
            this.examClrListTotal = responseData.examClrListTotal;
            this.examClrList = responseData.examClrInfoList;
            this.confirmAttendExamBtnLoading = false;
            this.confirmAttendExamBtnDisabled = false;
          } else if (responseData.status === 400) {
          } else {
            confirmLogout();
          }
          this.examClrListLoading = false;
        }).catch(error => {
          console.log(error);
        });
      },
      clrListIndexMethod(index) {
        return (this.examClrListPageNo - 1) * this.examClrListPageSize + index + 1;
      },
      handleExamClrCurrentChange(val) {
        this.examClrListPageNo = val;
        this.examClrListLoading = true;
        this.searchExamClr(this.examNumber, val);
      },
      /* 确定报名 */
      async confirmAttendExam() {
        // 先确认个人信息是否填写完整，若不完整则先到个人信息编辑页将信息填写完整
        this.confirmAttendExamBtnLoading = true;
        this.confirmAttendExamBtnDisabled = true;

        let examineeNumberReg = /^[a-zA-Z][a-zA-Z0-9]{5,30}$/;
        if(!examineeNumberReg.test(this.examineeNumber)) {
          this.$message({
            type: 'warning',
            message: '准考证号不为空且由字母开头的 5 到 30 个数字或字母组成'
          });
          this.confirmAttendExamBtnLoading = false;
          this.confirmAttendExamBtnDisabled = false;
          return false;
        }

        await this.isInfoComplete();

        await this.isAttended();

        if (this.infoComplete && !this.inExam) {
          this.paymentDialogVisible = true;
        }

        this.confirmAttendExamBtnLoading = false;
        this.confirmAttendExamBtnDisabled = false;
      },
      async isInfoComplete() {
        await this.$axios.post('/checkInfoComplete', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName
        }).then(response => {
          const responseData = response.data;
          if (responseData.status === 200) {
            if (responseData.isComplete) { // 信息完整，可进行报名
              this.infoComplete = true;
            } else {
              this.infoComplete = false;
              this.$message({
                type: 'warning',
                message: '信息填写不完整，请到信息修改处将信息补充完整。'
              })
            }
          } else if (responseData.status === 400) {
          } else {
            confirmLogout();
          }
        }).catch(error => {
          console.log(error);
        });
      },
      async isAttended() {
        await this.$axios.post('/isAttended', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName,
          examNumber: this.examNumber
        }).then(response => {
          const responseData = response.data;
          if (responseData.status === 200) {
            if (responseData.isAttended) {
              this.inExam = true;
              this.$message({
                type: 'warning',
                message: '已报名，不许重复报名！！！'
              })
            } else {
              this.inExam = false;
            }
          } else if (responseData.status === 400) {
          } else {
            confirmLogout();
          }
        }).catch(error => {
          console.log(error);
        });
      },
      saveExaminee2Redis() {
        if(this.totalCanContain === 0) {
          this.$message({
            type: 'warning',
            message: 'y已无考位'
          });
          return false;
        }

        this.$confirm('确认缴费报名?', '报名提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$axios.post('/processAttend', {
            sessionID: this.$store.state.userInfo.sessionID,
            userName: this.$store.state.userInfo.userName,
            examNumber: this.examNumber,
            examineeNumber: this.examineeNumber
          }).then(response => {
            const responseData = response.data;
            if(responseData.status === 200) {
              this.payRegPrice(responseData.regExamineeOnlineSessionID);
            } else if(responseData.status === 402) {
              this.$message({
                type: 'warning',
                message: '已无考位'
              });
            }  else if(responseData.status === 400) {
            } else {
              confirmLogout();
            }
          }).catch(error => {
            console.log(error);
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消报名'
          });
        });
      },
      payRegPrice(regExamineeOnlineSessionID) {
        const payRequestInfo = {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName,
          role: 3,
          out_trade_no: regExamineeOnlineSessionID,
          total_amount: this.regPrice,
          subject: 'ExamProject-' + this.examNumber
        };
        this.$axios.post('/payRegPrice', payRequestInfo)
          .then(response => {
            const payDiv = document.getElementById('payDiv');
            if (payDiv) {
              document.body.removeChild(payDiv);
            }
            const div = document.createElement('div');
            div.id = 'payDiv';
            div.innerHTML = response.data;
            document.body.appendChild(div);
            document.getElementById('payDiv').getElementsByTagName('form')[0].setAttribute('target', "_blank");
            document.getElementById('payDiv').getElementsByTagName('form')[0].submit();
          }).catch(error => {
          console.log(error);
        });
      },
      handlePaymentDialogClose() {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
            this.$message({
              type: 'warning',
              message: '未缴费，考试报名失败'
            });
          })
          .catch(_ => {
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
  .regExamOnlinePage {
    height: 100%;
    overflow: hidden;
  }

  .regExamInfoBox {
    margin-bottom: 10%;
  }

  .examLocationBox, .examClrSituationBox, .examineeNumberSettingBox {
    width: 90%;
    margin: 0 auto;
    padding-bottom: 10px;
  }

</style>
