<template>
  <div class="viewRegPricePage">
    <head-top></head-top>
    <el-scrollbar
      style="height: 100%;"
      wrap-style="overflow-x: hidden; overflow-y: scroll;">
      <div class="contentBox">
        <div class="checkPaymentStatusBox">
          <el-card class="checkPaymentStatusCard">
            <div slot="header" class="checkPaymentStatusCardTitle">
              <span>未缴费信息</span>
            </div>

            <div class="examListTable">
              <el-table stripe border :data="examList" v-loading="examListTableLoading">
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
                <el-table-column label="操作">
                  <template slot-scope="scope">
                    <el-button size="mini" type="primary" @click="handlePayRegPrice(scope.$index, scope.row)">
                      缴费
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>

            <div class="examPagination" style="text-align: center;">
              <el-pagination layout="total, prev, pager, next" :total="examTotal"
                             :current-page="examListTablePageNo"
                             :page-size="examListTablePageSize"
                             @current-change="handleExamCurrentChange">
              </el-pagination>
            </div>

          </el-card>
        </div>

        <el-dialog title="缴费提醒" :visible.sync="paymentDialogVisible">
          <div class="payRegPriceExplain">
            <span>当前考试编号：{{ curExamNumber }}</span> <br/>
            <span>需缴费：{{ curRegPrice }}</span>
          </div>
          <span slot="footer" class="dialog-footer">
                <el-button @click="paymentDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="regPay">确 定</el-button>
              </span>
        </el-dialog>
      </div>
    </el-scrollbar>
  </div>
</template>

<script>
  import headTop from "../../components/headTop";
  import {confirmLogout} from "../../utils/logout";
  import regExam from "../examMg/regExam";

  export default {
    name: "viewRegPrice",
    data() {
      return {
        examList: [],
        examListTableLoading: false,
        examTotal: 0,
        examListTablePageNo: 1,
        examListTablePageSize: 6,
        curExamNumber: '',
        curRegPrice: 0,
        paymentDialogVisible: false,
      }
    },
    methods: {
      searchExam(userName, curPage) {
        this.examListTableLoading = true;
        this.$axios.post('/searchExamFromViewRegPrice', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: userName,
          curPage: curPage
        }).then(response => {
          const responseData = response.data;
          if (responseData.status === 200) {
            this.examList = responseData.examList;
            this.examTotal = responseData.examTotal;
          } else if (responseData.status === 400) {
            this.$message({
              type: 'warning',
              message: '暂无考试'
            })
          } else {
            confirmLogout();
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
        this.searchExam(this.$store.state.userInfo.userName, val);
      },
      handlePayRegPrice(index, row) {
        this.$confirm('确认缴费？', '确认提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.curExamNumber = row.examNumber;
          this.curRegPrice = row.regPrice;
          this.paymentDialogVisible = true;
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消缴费'
          });
        });
      },
      regPay() {
        const payRequestInfo = {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName,
          role: 31, // 3 -- Role 3，1 -- 事件：对未缴费项目进行缴费
          out_trade_no: this.$store.state.userInfo.userName + ':' + this.curExamNumber + '-' + (new Date()).valueOf(),
          total_amount: this.curRegPrice,
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
  .viewRegPricePage {
    height: 100%;
    overflow: hidden;
  }

  .contentBox {
    margin-bottom: 10%;
  }

  .checkPaymentStatusBox {
    width: 90%;
    padding-left: 5%;
    padding-top: 2%;
  }
</style>
