<template>
  <div class="viewClrPage">
    <head-top></head-top>
    <el-scrollbar
      style="height: 100%;"
      wrap-style="overflow-x: hidden; overflow-y: scroll;">
      <div class="contentBox">

        <!-- 教室搜索 -->
        <div class="clrInfoBox">
          <el-card class="clrInfoCard">
            <div slot="header" class="clrInfoCardTitle">
              <span>教室信息</span>
            </div>

            <!-- 教室搜索 -->
            <div class="searchClrBox">
              <el-form label-width="100px" class="searchClrForm">
                <el-form-item label="教室编号">
                  <el-input v-model.trim="clrNumber" placeholder="请输入教室编号"
                            clearable style="width: 250px;">
                  </el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="findClr"
                             v-loading="searchOneBtnLoading">
                    <i class="el-icon-search"/> 查询
                  </el-button>
                  <el-button plain round type="primary" @click="findAllClr"
                             v-loading="searchAllBtnLoading">
                    <i class="el-icon-search"/> 查询所有
                  </el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 教室列表 -->
            <div class="clrListTable">
              <template>
                <el-table stripe border :data="clrList" max-height="400px;"
                          highlight-current-row @current-change="selectOneClr"
                          v-loading="clrListTableLoading" ref="clrListTable">
                  <el-table-column type="index" :index="clrListIndexMethod" width="50">
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
            <div class="clrPagination" style="text-align: center;">
              <el-pagination layout="total, prev, pager, next" :total="clrTotal"
                             :current-page="clrListTablePageNo"
                             :page-size="clrListTablePageSize"
                             @current-change="handleClrCurrentChange">
              </el-pagination>
            </div>
          </el-card>
        </div>

        <!-- 教室负责人部分 -->
        <div class="clrMgBox">
          <el-card class="clrMgListCard">
            <div slot="header" class="clrMgListCardTitle">
              <span>教室负责人列表</span>
              <el-button style="float: right; padding: 8px" type="primary"
                         @click="exportClrMgList">
                导出列表
              </el-button>
            </div>

            <!-- 教室负责人列表 -->
            <div class="clrMgListTable">
              <template>
                <el-table stripe border :data="clrMgList" max-height="400px;"
                          v-loading="clrMgListTableLoading">
                  <el-table-column type="index" :index="clrMgListIndexMethod">
                  </el-table-column>
                  <el-table-column label="负责人姓名" prop="mgName">
                  </el-table-column>
                  <el-table-column label="负责人手机" prop="mgPhone">
                  </el-table-column>
                  <el-table-column label="操作">
                    <template slot-scope="scope">
                      <el-button size="mini" @click="handleClrMgEdit(scope.$index, scope.row)">
                        编辑
                      </el-button>
                      <el-button size="mini" type="danger"
                                 @click="handleClrMgDelete(scope.$index, scope.row)">
                        删除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </template>
            </div>

            <!-- 页码 -->
            <div class="clrMgPagination" style="text-align: center;">
              <el-pagination layout="total, prev, pager, next" :total="clrMgTotal"
                             :current-page="clrMgListTablePageNo"
                             :page-size="clrMgListTablePageSize"
                             @current-change="handleClrMgCurrentChange">
              </el-pagination>
            </div>
          </el-card>

          <!-- 添加教室负责人 -->
          <el-dialog title="教室负责人编辑" :visible.sync="editClrMgDialogFormVisible">
            <el-form :model="editClrMgForm" :rules="editClrMgRules" ref="editClrMgRuleForm">
              <el-form-item label="负责人姓名" label-width="120px" prop="mgName">
                <el-input v-model.trim="editClrMgForm.mgName" autocomplete="off"
                          clearable style="width: 280px;">
                </el-input>
              </el-form-item>
              <el-form-item label="负责人手机" label-width="120px" prop="mgPhone">
                <el-input v-model.trim="editClrMgForm.mgPhone" autocomplete="off"
                          clearable style="width: 280px;">
                </el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="editClrMgDialogFormVisibleFooter">
              <el-button @click="editClrMgDialogFormVisible = false">取 消</el-button>
              <el-button type="primary" @click="confirmEditClrMg('editClrMgRuleForm')">确 定</el-button>
            </div>
          </el-dialog>

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
    name: "viewClr",
    data() {
      return {
        /* 教室l列表 */
        clrNumber: '',
        clrList: [],
        clrTotal: 0,
        clrListTablePageNo: 1,
        clrListTablePageSize: 6,
        searchOneBtnLoading: false,
        searchAllBtnLoading: false,
        clrListTableLoading: false,
        selectedClrNumber: '',
        /* 负责人信息 */
        clrMgList: [],
        clrMgTotal: 0,
        clrMgListTablePageNo: 1,
        clrMgListTablePageSize: 6,
        clrMgListTableLoading: false,
        exportBtnLoading: false,
        /* 编辑负责人信息 */
        editClrMgDialogFormVisible: false,
        editClrMgForm: {
          mgName: '',
          mgPhone: ''
        },
        editClrMgRules: {
          mgName: [
            {required: true, message: '请输入负责人姓名', trigger: 'blur'},
            {min: 2, max: 20, message: '负责人姓名限制在 2 到 20 个字符', trigger: 'blur'}
          ],
          mgPhone: [
            {required: true, message: '请输入负责人手机', trigger: 'blur'},
            {min: 5, max: 16, message: '负责人手机限制在 5 到 16 个字符', trigger: 'blur'}
          ]
        },
        selectedMgName: '',
        selectedMgPhone: ''
      }
    },
    components: {
      headTop
    },
    methods: {
      /* 教室搜索 */
      searchClr(clrNumber, curPage) {
        this.$axios.post('/searchClr', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName,
          clrNumber: clrNumber,
          curPage: curPage
        }).then(response => {
          const responseData = response.data;
          if (responseData.searchClrStatus === 200) {
            this.clrList = responseData.clrList;
            this.clrTotal = responseData.clrListSize;
          } else if (responseData.searchClrStatus === 400) {
            this.$message({
              type: 'warning',
              message: '无查询的教室'
            })
          } else {
            confirmLogout();
          }
          this.searchOneBtnLoading = false;
          this.searchAllBtnLoading = false;
          this.clrListTableLoading = false;
        }).catch(error => {
          console.log(error);
        });
      },
      findClr() {
        if (this.clrNumber === '') {
          this.$message({
            type: 'warning',
            message: '请先输入教室编号'
          });
          return;
        }
        this.selectedClrNumber = this.clrNumber;
        this.searchOneBtnLoading = true;
        this.searchClr(this.clrNumber, 1);

        this.clrMgListTableLoading = true;
        this.searchClrMg(this.clrNumber, 1);
      },
      findAllClr() {
        this.clrNumber = '';
        this.searchAllBtnLoading = true;
        this.searchClr('', 1);
      },
      handleClrCurrentChange(val) {
        this.clrListTablePageNo = val;
        this.clrListTableLoading = true;
        this.searchClr(this.clrNumber, val);
      },
      clrListIndexMethod(index) {
        return (this.clrListTablePageNo - 1) * this.clrListTablePageSize + index + 1;
      },
      selectOneClr(val) {
        if (val === null) return;
        this.selectedClrNumber = val.clrNumber;
        this.clrMgListTableLoading = true;
        this.searchClrMg(this.selectedClrNumber, 1);
      },
      /* 负责人处理 */
      searchClrMg(clrNumber, curPage) {
        this.$axios.post('/searchClrMg', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName,
          clrNumber: clrNumber,
          curPage: curPage
        }).then(response => {
          const responseData = response.data;
          if (responseData.searchClrMgStatus === 200) {
            this.clrMgList = responseData.clrMgList;
            this.clrMgTotal = responseData.clrMgListSize;
          } else if (responseData.searchClrMgStatus === 400) {
            this.clrMgList = [];
            this.$message({
              type: 'warning',
              message: '无查询的教室负责人'
            })
          } else {
            confirmLogout();
          }
          this.clrMgListTableLoading = false;
        }).catch(error => {
          console.log(error);
        });
      },
      handleClrMgCurrentChange(val) {
        this.clrMgListTablePageNo = val;
        this.clrMgListTableLoading = true;
        this.searchClrMg(this.selectedClrNumber, val);
      },
      clrMgListIndexMethod(index) {
        return (this.clrMgListTablePageNo - 1) * this.clrMgListTablePageSize + index + 1;
      },
      handleClrMgEdit(index, row) {
        this.editClrMgDialogFormVisible = true;
        this.selectedMgName = row.mgName;
        this.editClrMgForm.mgName = row.mgName;
        this.selectedMgPhone = row.mgPhone;
        this.editClrMgForm.mgPhone = row.mgPhone;
      },
      confirmEditClrMg(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.$axios.post('/updateClrMg', {
              sessionID: this.$store.state.userInfo.sessionID,
              userName: this.$store.state.userInfo.userName,
              clrNumber: this.selectedClrNumber,
              orgMgName: this.selectedMgName,
              orgMgPhone: this.selectedMgPhone,
              newMgName: this.editClrMgForm.mgName,
              newMgPhone: this.editClrMgForm.mgPhone
            }).then(response => {
              const responseData = response.data;
              if (responseData.updateClrMgStatus === 200) {
                this.searchClrMg(this.selectedClrNumber, this.clrMgListTablePageNo);
                this.$message({
                  type: 'success',
                  message: '编辑教室负责人编辑成功'
                });
              } else if (responseData.updateClrMgStatus === 400) {
                this.$message({
                  type: 'warning',
                  message: '编辑教室负责人编辑失败'
                });
              } else {
                confirmLogout();
              }
              this.editClrMgDialogFormVisible = false;
            }).catch(error => {
              console.log(error);
            });
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      handleClrMgDelete(index, row) {
        this.$confirm('确定删除该教师负责人?', '删除确认提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$axios.post('/deleteClrMg', {
            sessionID: this.$store.state.userInfo.sessionID,
            userName: this.$store.state.userInfo.userName,
            clrNumber: this.selectedClrNumber,
            mgName: row.mgName,
            mgPhone: row.mgPhone
          }).then(response => {
            const responseData = response.data;
            if (responseData.deleteClrMgStatus === 200) {
              let requestPage = this.clrMgListTablePageNo;
              let curIndex = (this.clrMgListTablePageNo - 1) * this.clrMgListTablePageSize + index + 1;
              if (curIndex === this.clrMgTotal && curIndex % 6 === 1) {
                requestPage -= 1;
                this.clrMgListTablePageNo = requestPage;
              }
              this.searchClrMg(this.selectedClrNumber, requestPage);
              this.$message({
                type: 'success',
                message: '删除考试负责人成功！'
              })
            } else if (responseData.deleteClrMgStatus === 400) {
              this.$message({
                type: 'warning',
                message: '删除考试负责人失败！'
              })
            } else {
              confirmLogout();
            }
          }).catch(error => {
            console.log(error);
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      exportClrMgList() {
        let exportClrNumber = '';
        this.clrNumber !== '' ? exportClrNumber = this.clrNumber : exportClrNumber = this.selectedClrNumber;
        if (exportClrNumber === '') {
          this.$message({
            type: 'warning',
            message: '请先输入或选中教室'
          });
          return false;
        }

        this.exportBtnLoading = true;
        this.$axios.post('/exportClrMgList', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName,
          clrNumber: exportClrNumber
        }).then(response => {
          const responseData = response.data;
          if (responseData.status === 200) {
            let url = '/downloadClrMgInfoList' + '?clrNumber=' + exportClrNumber;
            download(url, exportClrNumber + '教室负责人列表.xls');
          } else if (responseData.status === 400) {
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
    mounted() {
      this.$store.commit('loadUserInfo');
    }
  }
</script>

<style scoped>
  .viewClrPage {
    height: 100%;
    overflow: hidden;
  }

  .contentBox {
    margin-bottom: 10%;
  }

  .clrInfoBox, .clrMgBox {
    width: 90%;
    padding-left: 5%;
    padding-top: 2%;
  }

</style>
