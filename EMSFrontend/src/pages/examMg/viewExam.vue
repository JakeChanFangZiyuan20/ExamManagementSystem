<template>
  <div class="viewExamPage">
    <head-top></head-top>
    <el-scrollbar
      style="height: 100%;"
      wrap-style="overflow-x: hidden; overflow-y: scroll;">
      <div class="contentBox">

        <!-- 查询部分 -->
        <div class="searchExamBox">
          <el-card class="searchExamBoxCard">
            <div slot="header" class="searchExamBoxCardTitle">
              <span>搜索考试</span>
            </div>

            <div class="searchBox">
              <el-form :model="examSearchForm" :rules="examSearchFormRules" ref="examSearchForm"
                       label-width="100px" class="examSearchForm">
                <el-row style="width: 1000px;">
                  <el-col :span="8">
                    <el-form-item label="考试编号" prop="examNumber">
                      <el-input v-model.trim="examSearchForm.examNumber" placeholder="请输入考试编号"
                                clearable style="width: 250px;">
                      </el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="考试名称" prop="examName">
                      <el-input v-model.trim="examSearchForm.examName" placeholder="请输入考试名称"
                                clearable style="width: 250px;">
                      </el-input>
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-form-item>
                  <el-button type="primary" style="width: 100px;" @click="searchExam" v-loading="searchBtnLoading">
                    <i class="el-icon-search"/> 查询
                  </el-button>
                  <el-button style="width: 100px;" @click="resetSearchInput">
                    <i class="el-icon-remove-outline"/> 重置
                  </el-button>
                  <el-button type="primary" round style="width: 120px;" @click="searchAll"
                             v-loading="searchAllBtnLoading">
                    <i class="el-icon-search"/> 查询所有
                  </el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 考试列表 -->
            <div class="examListTable">
              <span style="font-size: 14px;">（选中某一列展示考试负责人列表，考试教室列表，考生列表）</span>
              <template>
                <el-table stripe border :data="examList" max-height="300"
                          highlight-current-row @current-change="selectOneExam"
                          v-loading="examListTableLoading">
                  <el-table-column type="index" :index="examListTableIndexChange" width="50"></el-table-column>
                  <el-table-column label="考试编号" prop="examNumber">
                  </el-table-column>
                  <el-table-column label="考试名称" prop="examName">
                  </el-table-column>
                  <el-table-column label="考试日期" prop="examDate">
                  </el-table-column>
                  <el-table-column label="考试开始时间" prop="startTime">
                  </el-table-column>
                  <el-table-column label="考试结束时间" prop="endTime">
                  </el-table-column>
                  <el-table-column label="考试报名费" prop="regPrice">
                  </el-table-column>
                </el-table>
              </template>
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

        <!-- 考试负责人部分 -->
        <div class="examMgBox">
          <el-card class="examMgListCard">
            <div slot="header" class="examMgListCardTitle">
              <span>考试负责人列表</span>
              <el-button style="float: right; padding: 8px" type="primary"
                         @click="exportExamMgList" v-loading="exportExamMgListBtnLoading">
                导出列表
              </el-button>
            </div>

            <!-- 考试负责人列表 -->
            <div class="examMgListTable">
              <template>
                <el-table stripe border :data="examMgList" max-height="300"
                          v-loading="examMgListTableLoading">
                  <el-table-column type="index" :index="examMgListTableIndexChange" width="50">
                  </el-table-column>
                  <el-table-column label="负责人姓名" prop="mgName">
                  </el-table-column>
                  <el-table-column label="负责人手机" prop="mgPhone">
                  </el-table-column>
                </el-table>
              </template>
            </div>

            <!-- 页码 -->
            <div class="examMgPagination" style="text-align: center;">
              <el-pagination layout="total, prev, pager, next" :total="examMgTotal"
                             :current-page="examMgListTablePageNo"
                             :page-size="examMgListTablePageSize"
                             @current-change="handleExamMgCurrentChange">
              </el-pagination>
            </div>
          </el-card>
        </div>

        <!-- 考试教室部分 -->
        <div class="examClrBox">
          <el-card class="examClrListCard">
            <div slot="header" class="examClrListCardTitle">
              <span>考试教室列表</span>
              <el-button style="float: right; padding: 8px" type="primary"
                         @click="exportExamClrList" v-loading="exportExamClrListBtnLoading">
                导出列表
              </el-button>
            </div>

            <!-- 考试教室列表 -->
            <div class="examClrListTable">
              <template>
                <el-table stripe border :data="examClrList" max-height="300"
                          v-loading="examClrListTableLoading">
                  <el-table-column type="index" :index="examClrListTableIndexChange" width="50">
                  </el-table-column>
                  <el-table-column label="教室地址" prop="address">
                  </el-table-column>
                  <el-table-column label="教室编号" prop="clrNumber">
                  </el-table-column>
                  <el-table-column label="教室容量" prop="capacity">
                  </el-table-column>
                </el-table>
              </template>
            </div>

            <!-- 页码 -->
            <div class="examClrPagination" style="text-align: center;">
              <el-pagination layout="prev, pager, next" :total="examClrTotal"
                             :current-page="examClrListTablePageNo"
                             :page-size="examClrListTablePageSize"
                             @current-change="handleExamClrCurrentChange">
              </el-pagination>
            </div>
          </el-card>
        </div>

        <!-- 考生部分 -->
        <div class="examineeBox">
          <el-card class="examineeListCard">
            <div slot="header" class="examineeListCardTitle">
              <span>考生列表</span>
              <el-button style="float: right; padding: 8px" type="primary"
                         @click="exportExamineeList" v-loading="exportExamineeListBtnLoading">
                导出列表
              </el-button>
            </div>

            <!-- 考试教室列表 -->
            <div class="examineeListTable">
              <template>
                <el-table stripe border :data="examineeList" max-height="300"
                          v-loading="examineeListTableLoading">
                  <el-table-column type="index" :index="examineeListTableIndexChange" width="50">
                  </el-table-column>
                  <el-table-column label="考生准考证号" prop="examineeNumber" width="140">
                  </el-table-column>
                  <el-table-column label="考生用户名" prop="userName" width="100">
                  </el-table-column>
                  <el-table-column label="考生姓名" prop="trueName" width="100">
                  </el-table-column>
                  <el-table-column label="考生身份证" prop="identity" width="200">
                  </el-table-column>
                  <el-table-column label="考生缴费状态" prop="paymentStatus" width="110">
                  </el-table-column>
                  <el-table-column label="考生考场" prop="clrNumber" width="100">
                  </el-table-column>
                  <el-table-column label="考生座位号" prop="seatNumber" width="100">
                  </el-table-column>
                  <el-table-column label="考生性别" prop="gender">
                  </el-table-column>
                  <el-table-column label="出生日期" prop="birthday" width="100">
                  </el-table-column>
                  <el-table-column label="考生手机号" prop="phone" width="120">
                  </el-table-column>
                </el-table>
              </template>
            </div>

            <!-- 页码 -->
            <div class="examineePagination" style="text-align: center;">
              <el-pagination layout="total, prev, pager, next" :total="examineeTotal"
                             :current-page="examineeListTablePageNo"
                             :page-size="examineeListTablePageSize"
                             @current-change="handleExamineeCurrentChange">
              </el-pagination>
            </div>
          </el-card>
        </div>

        <!-- 考场核验表 -->
        <div class="examClrCheckBox">
          <el-card class="examClrCheckCard">
            <div slot="header" class="examClrCheckCardTitle">
              <span>考场核验表打印</span>
              <el-button style="float: right; padding: 8px; margin-left: 10px;" type="primary"
                         @click="printExamClrCheck" v-loading="printExamClrBtnLoading"
                         :disabled="printExamClrBtnDisable" v-print="'#examClrCheckTable'">
                打印
              </el-button>
              <el-button style="float: right; padding: 8px;" type="primary"
                         @click="viewExamClrCheck" v-loading="viewExamClrBtnLoading">
                预览
              </el-button>
            </div>

            <div id="examClrCheckTable">
              <div style="font-size: 20px; font-weight: bold; text-align: center;"
                   class="examClrCheckTableTitle">
                考场核验表
              </div>

              <div class="examClrCheckTableBasicInfo">
                <el-row>
                  <el-col :span="12">
                    <el-row>
                      <el-col :span="5">
                        <div class="examClrCheckBasicInfoLabel">
                          考试编号
                        </div>
                      </el-col>
                      <el-col :span="7">
                        <div class="examClrCheckBasicInfo">
                          {{ examInfo.examNumber }}
                        </div>
                      </el-col>
                    </el-row>
                  </el-col>
                  <el-col :span="12">
                    <el-row>
                      <el-col :span="5">
                        <div class="examClrCheckBasicInfoLabel">
                          考试名称
                        </div>
                      </el-col>
                      <el-col :span="7">
                        <div class="examClrCheckBasicInfo">
                          {{ examInfo.examName }}
                        </div>
                      </el-col>
                    </el-row>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="8">
                    <el-row>
                      <el-col :span="8">
                        <div class="examClrCheckBasicInfoLabel">
                          考试日期
                        </div>
                      </el-col>
                      <el-col :span="10">
                        <div class="examClrCheckBasicInfo">
                          {{ examInfo.examDate }}
                        </div>
                      </el-col>
                    </el-row>
                  </el-col>
                  <el-col :span="8">
                    <el-row>
                      <el-col :span="8">
                        <div class="examClrCheckBasicInfoLabel">
                          开始时间
                        </div>
                      </el-col>
                      <el-col :span="10">
                        <div class="examClrCheckBasicInfo">
                          {{ examInfo.startTime }}
                        </div>
                      </el-col>
                    </el-row>
                  </el-col>
                  <el-col :span="8">
                    <el-row>
                      <el-col :span="8">
                        <div class="examClrCheckBasicInfoLabel">
                          结束时间
                        </div>
                      </el-col>
                      <el-col :span="10">
                        <div class="examClrCheckBasicInfo">
                          {{ examInfo.endTime }}
                        </div>
                      </el-col>
                    </el-row>
                  </el-col>
                </el-row>
              </div>

              <div class="examClrCheckList">
                <template>
                  <el-table border :data="examClrCheckList"
                            v-loading="examClrCheckListLoading">
                    <el-table-column label="地址" prop="address">
                    </el-table-column>
                    <el-table-column label="教室编号" prop="clrNumber">
                    </el-table-column>
                    <el-table-column label="教室容量" prop="capacity">
                    </el-table-column>
                    <el-table-column label="核验人姓名">
                    </el-table-column>
                  </el-table>
                </template>
              </div>

            </div>

          </el-card>
        </div>

        <!-- 考试签到表 -->
        <div class="examineeCheckBox">
          <el-card class="examineeCheckCard">
            <div slot="header" class="examineeCheckCardTitle">
              <span>考试签到表打印</span>
              <el-button style="float: right; padding: 8px; margin-left: 10px;" type="primary"
                         @click="printxamineeCheck" v-loading="printExamineeBtnLoading"
                         :disabled="printExamineeBtnDisable" v-print="'#examineeCheckTable'">
                打印
              </el-button>
              <el-button style="float: right; padding: 8px;" type="primary"
                         @click="viewExamineeCheck" v-loading="viewExamineeBtnLoading">
                预览
              </el-button>
            </div>

            <div id="examineeCheckTable">
              <div style="font-size: 20px; font-weight: bold; text-align: center;"
                   class="examClrCheckTableTitle">
                考试签到表
              </div>

              <div class="examClrCheckTableBasicInfo">
                <el-row>
                  <el-col :span="12">
                    <el-row>
                      <el-col :span="5">
                        <div class="examClrCheckBasicInfoLabel">
                          考试编号
                        </div>
                      </el-col>
                      <el-col :span="7">
                        <div class="examClrCheckBasicInfo">
                          {{ examInfo.examNumber }}
                        </div>
                      </el-col>
                    </el-row>
                  </el-col>
                  <el-col :span="12">
                    <el-row>
                      <el-col :span="5">
                        <div class="examClrCheckBasicInfoLabel">
                          考试名称
                        </div>
                      </el-col>
                      <el-col :span="7">
                        <div class="examClrCheckBasicInfo">
                          {{ examInfo.examName }}
                        </div>
                      </el-col>
                    </el-row>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="8">
                    <el-row>
                      <el-col :span="8">
                        <div class="examClrCheckBasicInfoLabel">
                          考试日期
                        </div>
                      </el-col>
                      <el-col :span="10">
                        <div class="examClrCheckBasicInfo">
                          {{ examInfo.examDate }}
                        </div>
                      </el-col>
                    </el-row>
                  </el-col>
                  <el-col :span="8">
                    <el-row>
                      <el-col :span="8">
                        <div class="examClrCheckBasicInfoLabel">
                          开始时间
                        </div>
                      </el-col>
                      <el-col :span="10">
                        <div class="examClrCheckBasicInfo">
                          {{ examInfo.startTime }}
                        </div>
                      </el-col>
                    </el-row>
                  </el-col>
                  <el-col :span="8">
                    <el-row>
                      <el-col :span="8">
                        <div class="examClrCheckBasicInfoLabel">
                          结束时间
                        </div>
                      </el-col>
                      <el-col :span="10">
                        <div class="examClrCheckBasicInfo">
                          {{ examInfo.endTime }}
                        </div>
                      </el-col>
                    </el-row>
                  </el-col>
                </el-row>
              </div>

              <div class="examineeCheckList">
                <template>
                  <el-table border :data="examineeCheckList"
                            v-loading="examineeCheckListLoading">
                    <el-table-column label="准考证号" prop="examineeNumber">
                    </el-table-column>
                    <el-table-column label="身份证" prop="identity" width="160">
                    </el-table-column>
                    <el-table-column label="用户名" prop="userName">
                    </el-table-column>
                    <el-table-column label="姓名" prop="trueName">
                    </el-table-column>
                    <el-table-column label="考场" prop="clrNumber">
                    </el-table-column>
                    <el-table-column label="座位号" prop="seatNumber">
                    </el-table-column>
                    <el-table-column label="签名">
                    </el-table-column>
                  </el-table>
                </template>
              </div>

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
    name: "viewExam",
    data() {
      return {
        /* 查询部分 */
        examSearchForm: {
          examNumber: '',
          examName: ''
        },
        examSearchFormRules: {},
        examList: [],
        examTotal: 0,
        examListTablePageNo: 1,
        examListTablePageSize: 6,
        searchBtnLoading: false,
        searchAllBtnLoading: false,
        examListTableLoading: false,
        selectedExamNumber: '',
        /* 考试负责人部分 */
        examMgList: [],
        exportExamMgListBtnLoading: false,
        examMgTotal: 0,
        examMgListTablePageNo: 1,
        examMgListTablePageSize: 6,
        examMgListTableLoading: false,
        /* 考试教室部分 */
        examClrList: [],
        exportExamClrListBtnLoading: false,
        examClrTotal: 0,
        examClrListTablePageNo: 1,
        examClrListTablePageSize: 6,
        examClrListTableLoading: false,
        /* 考生部分 */
        examineeList: [],
        exportExamineeListBtnLoading: false,
        examineeTotal: 0,
        examineeListTablePageNo: 1,
        examineeListTablePageSize: 6,
        examineeListTableLoading: false,
        /* 考场核验表 */
        examInfo: {
          examNumber: '',
          examName: '',
          examDate: '',
          startTime: '',
          endTime: '',
        },
        examClrCheckList: [],
        examClrCheckListLoading: false,
        printExamClrBtnDisable: true,
        viewExamClrBtnLoading: false,
        printExamClrBtnLoading: false,
        /* 考试签到表 */
        examineeCheckList: [],
        examineeCheckListLoading: false,
        viewExamineeBtnLoading: false,
        printExamineeBtnLoading: false,
        printExamineeBtnDisable: true,

      }
    },
    components: {
      headTop
    },
    methods: {
      /* 查询部分 */
      resetSearchInput() {
        this.examSearchForm.examNumber = '';
        this.examSearchForm.examName = '';
      },
      searchExamGetData(curPage) {
        this.$axios.post('/searchExam', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName,
          examNumber: this.examSearchForm.examNumber,
          examName: this.examSearchForm.examName,
          curPage: curPage
        }).then(response => {
          const responseData = response.data;
          if (responseData.searchStatus === 200) {
            this.examList = responseData.resultSet;
            this.examTotal = responseData.resultSetSize;
          } else if (responseData.searchStatus === 400) {
            this.$message({
              type: 'warning',
              message: '无查询结果'
            })
          } else {
            confirmLogout();
          }
          this.searchBtnLoading = false;
          this.searchAllBtnLoading = false;
          this.examListTableLoading = false;
        }).catch(error => {
          console.log(error);
        });
      },
      searchExam() {
        this.selectedExamNumber = this.examSearchForm.examNumber;
        this.searchBtnLoading = true;

        this.searchExamGetData(1);
        this.searchExamMg(this.examSearchForm.examNumber, 1);
        this.searchExamClr(this.examSearchForm.examNumber, 1);
        this.searchExaminee(this.examSearchForm.examNumber, 1);
      },
      searchAll() {
        this.examSearchForm.examNumber = '';
        this.examMgList = this.examClrList = this.examineeList = [];

        this.searchAllBtnLoading = true;

        this.searchExamGetData(1);
      },
      handleExamCurrentChange(val) {
        this.examListTablePageNo = val;
        this.examListTableLoading = true;
        this.searchExamGetData(val);
      },
      examListTableIndexChange(index) {
        return (this.examListTablePageNo - 1) * this.examListTablePageSize + index + 1;
      },
      selectOneExam(val) {
        if (val === null) return;
        this.examMgList = this.examClrList = this.examineeList = [];

        this.selectedExamNumber = val.examNumber;
        this.examMgListTableLoading = true;
        this.searchExamMg(val.examNumber, 1);
        this.examClrListTableLoading = true;
        this.searchExamClr(val.examNumber, 1);
        this.examineeListTableLoading = true;
        this.searchExaminee(val.examNumber, 1);
      },
      /* 考试负责人部分 */
      searchExamMg(examNumber, curPage) {
        this.$axios.post('/searchExamMg', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName,
          examNumber: examNumber,
          curPage: curPage
        }).then(response => {
          const responseData = response.data;
          if (responseData.searchExamMgStatus === 200) {
            this.examMgList = responseData.examMgList;
            this.examMgTotal = responseData.examMgListSize;
            /*
                        this.$message({
                          type: 'warning',
                          message: '考试负责人成功查询'
                        })
            */
          } else if (responseData.searchExamMgStatus === 400) {
            /*
                        this.$message({
                          type: 'warning',
                          message: '无考试负责人查询结果'
                        });
            */
          } else {
            confirmLogout();
          }
          this.examMgListTableLoading = false;
        }).catch(error => {
          console.log(error);
        });
      },
      handleExamMgCurrentChange(val) {
        this.examMgListTablePageNo = val;
        this.examMgListTableLoading = true;
        this.searchExamMg(this.selectedExamNumber, val);
      },
      examMgListTableIndexChange(index) {
        return (this.examMgListTablePageNo - 1) * this.examMgListTablePageSize + index + 1;
      },
      exportExamMgList() {
        let exportExamNumber = '';
        this.examSearchForm.examNumber !== '' ? exportExamNumber = this.examSearchForm.examNumber : exportExamNumber = this.selectedExamNumber;
        if (exportExamNumber === '') {
          this.$message({
            type: 'warning',
            message: '请先输入或选中考试编号'
          });
          return false;
        }

        this.exportExamMgListBtnLoading = true;
        this.$axios.post('exportExamMgList', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName,
          examNumber: exportExamNumber
        }).then(response => {
          const responseData = response.data;
          if (responseData.status === 200) {
            let url = '/downloadExamMgList' + "?examNumber=" + exportExamNumber;
            download(url, exportExamNumber + '考试负责人列表.xls');
          } else if (responseData.status === 400) {
          } else {
            confirmLogout();
          }

          this.exportExamMgListBtnLoading = false;
        }).catch(error => {
          console.log(error);
          this.exportExamMgListBtnLoading = false;
        });
      },
      /* 考试教室部分 */
      searchExamClr(examNumber, curPage) {
        this.$axios.post('/searchExamClr', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName,
          examNumber: examNumber,
          curPage: curPage
        }).then(response => {
          const responseData = response.data;
          if (responseData.searchExamClrStatus === 200) {
            this.examClrList = responseData.examClrList;
            this.examClrTotal = responseData.examClrListSize;
            /*
                        this.$message({
                          type: 'warning',
                          message: '考试教室成功查询'
                        })
            */
          } else if (responseData.searchExamClrStatus === 400) {
            /*
                        this.$message({
                          type: 'warning',
                          message: '无考试教室查询结果'
                        });
            */
          } else {
            confirmLogout();
          }
          this.examClrListTableLoading = false;
        }).catch(error => {
          console.log(error);
        });
      },
      handleExamClrCurrentChange(val) {
        this.examClrListTablePageNo = val;
        this.examClrListTableLoading = true;
        this.searchExamClr(this.selectedExamNumber, val);
      },
      examClrListTableIndexChange(index) {
        return (this.examClrListTablePageNo - 1) * this.examClrListTablePageSize + index + 1;
      },
      exportExamClrList() {
        let exportExamNumber = '';
        this.examSearchForm.examNumber !== '' ? exportExamNumber = this.examSearchForm.examNumber : exportExamNumber = this.selectedExamNumber;
        if (exportExamNumber === '') {
          this.$message({
            type: 'warning',
            message: '请先输入或选中考试编号'
          });
          return false;
        }

        this.exportExamClrListBtnLoading = true;
        this.$axios.post('/exportExamClrList', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName,
          examNumber: exportExamNumber
        }).then(response => {
          const responseData = response.data;
          if (responseData.status === 200) {
            let url = '/downloadExamClrList' + "?examNumber=" + exportExamNumber;
            download(url, exportExamNumber + '考试教室负责人列表.xls');
          } else if (responseData.status === 400) {
          } else {
            confirmLogout();
          }

          this.exportExamClrListBtnLoading = false;
        }).catch(error => {
          console.log(error);
          this.exportExamClrListBtnLoading = false;
        });
      },
      /* 考生部分 */
      searchExaminee(examNumber, curPage) {
        this.$axios.post('/searchExaminee', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName,
          examNumber: examNumber,
          curPage: curPage
        }).then(response => {
          const responseData = response.data;
          if (responseData.searchExamineeStatus === 200) {
            this.examineeList = responseData.examineeList;
            this.examineeTotal = responseData.examineeListSize;
            /*
                        this.$message({
                          type: 'warning',
                          message: '考试负责人成功查询'
                        })
            */
          } else if (responseData.searchExamineeStatus === 400) {
            /*
                        this.$message({
                          type: 'warning',
                          message: '无考试负责人查询结果'
                        });
            */
          } else {
            confirmLogout();
          }
          this.examineeListTableLoading = false;
        }).catch(error => {
          console.log(error);
        });
      },
      handleExamineeCurrentChange(val) {
        this.examineeListTablePageNo = val;
        this.examineeListTableLoading = true;
        this.searchExaminee(this.selectedExamNumber, val);
      },
      examineeListTableIndexChange(index) {
        return (this.examineeListTablePageNo - 1) * this.examineeListTablePageSize + index + 1;
      },
      exportExamineeList() {
        let exportExamNumber = '';
        this.examSearchForm.examNumber !== '' ? exportExamNumber = this.examSearchForm.examNumber : exportExamNumber = this.selectedExamNumber;
        if (exportExamNumber === '') {
          this.$message({
            type: 'warning',
            message: '请先输入或选中考试编号'
          });
          return false;
        }

        this.exportExamineeListBtnLoading = true;
        this.$axios.post('/exportExamineeList', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName,
          examNumber: exportExamNumber
        }).then(response => {
          const responseData = response.data;
          if (responseData.status === 200) {
            let url = '/downloadExamineeList' + "?examNumber=" + exportExamNumber;
            download(url, exportExamNumber + '考生列表.xls');
          } else if (responseData.status === 400) {
          } else {
            confirmLogout();
          }

          this.exportExamineeListBtnLoading = false;
        }).catch(error => {
          console.log(error);
          this.exportExamineeListBtnLoading = false;
        });
      },
      /* 考场核验打印 */
      viewExamClrCheck() {
        let examNumber = '';
        this.examSearchForm.examNumber !== '' ? examNumber = this.examSearchForm.examNumber : examNumber = this.selectedExamNumber;
        if (examNumber === '') {
          this.$message({
            type: 'warning',
            message: '请先输入或选中考试编号'
          });
          return false;
        }

        this.examClrCheckListLoading = true;
        this.$axios.post('/viewExamClrCheck', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName,
          examNumber: examNumber
        }).then(response => {
          const responseData = response.data;
          if(responseData.status === 200) {
            this.examInfo = responseData.examInfo;
            this.examClrCheckList = responseData.examClrList;
            this.printExamClrBtnDisable = false;
          } else if(responseData.status === 400) {
            this.$message({
              type: 'warning',
              message: '查找不到考试'
            })
          } else {
            confirmLogout();
          }
          this.examClrCheckListLoading = false;
        }).catch(error => {
          console.log(error);
          this.examClrCheckListLoading = false;
        });

      },
      printExamClrCheck() {

      },
      /* 考场签到表 */
      viewExamineeCheck() {
        let examNumber = '';
        this.examSearchForm.examNumber !== '' ? examNumber = this.examSearchForm.examNumber : examNumber = this.selectedExamNumber;
        if (examNumber === '') {
          this.$message({
            type: 'warning',
            message: '请先输入或选中考试编号'
          });
          return false;
        }

        this.examineeCheckListLoading = true;
        this.$axios.post('/viewExamineeCheck', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName,
          examNumber: examNumber
        }).then(response => {
          const responseData = response.data;
          if(responseData.status === 200) {
            this.examInfo = responseData.examInfo;
            this.examineeCheckList = responseData.examineeCheckList;
            this.printExamineeBtnDisable = false;
          } else if(responseData.status === 400) {
            this.$message({
              type: 'warning',
              message: '查找不到考试'
            })
          } else {
            confirmLogout();
          }
          this.examineeCheckListLoading = false;
        }).catch(error => {
          console.log(error);
          this.examineeCheckListLoading = false;
        });

      },
      printxamineeCheck() {

      }
    },
    mounted() {
      this.$store.commit('loadUserInfo');
    }
  }
</script>

<style scoped>
  .viewExamPage {
    height: 100%;
    overflow: hidden;
  }

  .contentBox {
    margin-bottom: 10%;
  }

  .searchExamBox, .examMgBox, .examClrBox, .examineeBox, .examClrCheckBox, .examineeCheckBox {
    width: 90%;
    padding-left: 5%;
    padding-top: 2%;
  }

  .examClrCheckTableBasicInfo {
    padding-top: 20px;
    width: 90%;
    margin: 0 auto;
  }

  .el-row {
    padding-bottom: 10px;
  }

  .examClrCheckList, .examineeCheckList {
    padding-top: 20px;
    width: 90%;
    margin: 0 auto;
  }
</style>
