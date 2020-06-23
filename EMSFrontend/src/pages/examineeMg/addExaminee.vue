<template>
  <div class="addExamineePage">
    <head-top></head-top>
    <el-scrollbar style="height: 100%;"
                  wrap-style="overflow-x: hidden; overflow-y: scroll;">
      <div style="padding: 10px; height: 100%;">
        <!-- 单独添加 -->
        <div class="addSingle">
          <el-card class="examineeInfoCard" v-loading="addSingleLoading">
            <div slot="header" class="clearfix">
              <span>单独添加</span>
            </div>
            <el-form :model="examineeInfo" :rules="examineeInfoRules" ref="examineeInfoForm"
                     label-width="120px" class="examineeInfoFormCard">
              <el-form-item label="考生账号" prop="userName">
                <el-input placeholder="请输入考生账号" v-model="examineeInfo.userName"
                          clearable prefix-icon="el-icon-user">
                </el-input>
              </el-form-item>
              <el-form-item label="考生密码" prop="password">
                <el-input placeholder="请输入考生初始密码" v-model="examineeInfo.password"
                          clearable show-password prefix-icon="el-icon-lock">
                </el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitForm('examineeInfoForm')">
                  <i class="el-icon-circle-plus-outline"/> 添加
                </el-button>
                <el-button @click="resetForm('examineeInfoForm')">
                  <i class="el-icon-remove-outline"/> 重置
                </el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </div>

        <!-- 列表添加 -->
        <div class="addList">
          <el-card class="examineeInfoCard" v-loading="addListLoading">
            <div slot="header" class="clearfix">
              <span>列表添加</span>
            </div>
            <el-form label-width="0px" class="examineeListUploadCard">
              <el-form-item>
                <el-upload class="uploadFile" action="" :limit="1" :show-file-list="true"
                           :before-upload="beforeUpload" :http-request="uploadFile"
                           :before-remove="beforeRemove" :on-remove="removeFile"
                           :disabled="uploadDisable">
                  <el-button size="small" type="primary">点击上传</el-button>
                  <span style="font-size: 13px; color: red;">（请按照格式上传，只接收 xls 文件）</span>
                  <span v-show="uploading"><i class="el-icon-loading"/>正在上传</span>
                </el-upload>
              </el-form-item>
              <el-form-item>
                <el-button size="small" type="primary" plain @click="examineeFormat">
                  下载格式文件<span style="color: red;">（推荐）</span>
                </el-button>
                <br/>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitList">
                  <i class="el-icon-circle-plus-outline"/> 添加列表
                </el-button>
                <span style="font-size: 12px;">（若数据库中已存在不会重复添加）</span>
              </el-form-item>
            </el-form>
          </el-card>
        </div>

      </div>
    </el-scrollbar>
  </div>

</template>

<script>
  import headTop from "../../components/headTop";
  import {download} from "../../utils/DownloadFormatFile";
  import {confirmLogout} from "../../utils/logout";

  export default {
    name: "addExaminee",
    data() {
      var validateUserName = (rule, value, callback) => {
        let userNameReg = /^(?=.*[a-zA-Z\d])[a-zA-Z\d]{6,10}$/;
        if (userNameReg.test(value)) {
          callback();
        } else {
          callback(new Error('考生账号输入错误，考生账号由 6 到 20 位数字字母组成'));
        }
      };
      var validatePassword = (rule, value, callback) => {
        let pwdReg = /^(?=.*[a-zA-Z\d])[a-zA-Z\d]{6,10}$/;
        if (pwdReg.test(value)) {
          callback();
        } else {
          callback(new Error('考生密码输入错误，考生账号由 6 到 20 位数字字母组成（可全为数字）'));
        }
      };
      return {
        examineeInfo: {
          userName: '',
          password: ''
        },
        examineeInfoRules: {
          userName: [
            {validator: validateUserName, trigger: 'blur'}
          ],
          password: [
            {validator: validatePassword, trigger: 'blur'}
          ]
        },
        addSingleLoading: false,
        addListLoading: false,
        /* 列表处理 */
        haveUploadFile: false,
        uploading: false,
        uploadDisable: false,
      }
    },
    components: {
      headTop
    },
    methods: {
      /* 单独添加 */
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.$confirm('确认添加该考生账号？', '确认提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.addSingleLoading = true;

              this.$axios.post('/addSingleExamineeAccount', {
                sessionID: this.$store.state.userInfo.sessionID,
                userName: this.$store.state.userInfo.userName,
                newUserName: this.examineeInfo.userName,
                password: this.examineeInfo.password,
                role: 3
              }).then(response => {
                const status = response.data.status;
                if (status === 200) {
                  this.$message({
                    message: '单独添加学生账户成功',
                    type: 'success'
                  });
                } else if (status === 401) {
                  this.$message({
                    message: '考生账户已存在，单独添加考生账户失败',
                    type: 'warning'
                  });
                  this.examineeInfo.userName = '';
                } else {
                  this.$message.error('单独添加考生账户失败');
                }
                this.addSingleLoading = false;
              }).catch(error => {
                console.log(error);
                this.$message.error('单独添加考生账户失败');
                this.addSingleLoading = false;
              });
            }).catch(() => {
              this.$message({
                type: 'info',
                message: '已取消添加'
              });
            });
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.examineeInfo.userName = '';
        this.examineeInfo.password = '';
      },
      /* 列表处理 */
      /* 获取格式文件 */
      examineeFormat() {
        download('/downloadExamineeAccountFormatFile', '格式文件-考生账号列表.xls');
      },
      /* 上传 */
      beforeUpload(file) {
        const isXls = file.type === 'application/vnd.ms-excel';
        if (!isXls) {
          this.$message.error('上传文件只能是 xls 文件！')
        }
        return isXls;
      },
      uploadFile(param) {
        this.uploading = true;
        this.uploadDisable = true;

        let xlsFileData = new FormData();
        xlsFileData.append("file", param.file);
        xlsFileData.append("sessionID", this.$store.state.userInfo.sessionID);
        xlsFileData.append("userName", this.$store.state.userInfo.userName);
        this.$axios.post('/uploadXLSFileFromAddExaminee', xlsFileData).then(response => {
          const result = response.data;
          if (result.isLogin) {
            this.haveUploadFile = true;
            this.$message({
              message: '上传成功！',
              type: 'success'
            });
          } else {
            confirmLogout();
          }
        }).catch(error => {
          console.log(error);
        });

        this.uploadDisable = false;
        this.uploading = false;
      },
      /* 移除 */
      beforeRemove(file) {
        return this.$confirm(`确定移除 ${file.name}？`);
      },
      removeFile() {
        this.haveUploadFile = false;
        this.$axios.post('/removeUploadFileFromAddExaminee', {
          sessionId: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName
        }).then(response => {
          const responseData = response.data;
          if (responseData.status) {
            this.$message({
              type: 'success',
              message: '已成功删除'
            });
          } else {
            confirmLogout();
          }
        }).catch(error => {
        });
      },
      /* 确认添加列表 */
      submitList() {
        if (this.haveUploadFile) {
          this.$confirm('确认添加考生账号列表?', '添加提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.addListLoading = true;

            const requestData = this.$store.state.userInfo;
            this.$axios.post('/insertExamineeAccountList', {
              sessionID: requestData.sessionID,
              userName: requestData.userName,
            }).then(response => {
              const responseData = response.data;
              if (responseData.status === 200) {
                this.$message({
                  type: 'success',
                  message: '考生账号列表添加成功'
                })
              } else if (responseData.status === 400) {
                this.$message({
                  type: 'warn',
                  message: '考生账号列表添加添加失败'
                });
              } else {
                confirmLogout();
              }
              this.addListLoading = false;
            }).catch(error => {
              console.log(error);
              this.addListLoading = false;
            });
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消添加'
            });
          });

        } else {
          this.$message({
            type: 'warning',
            message: '请上传考生账号列表文件'
          });
          return false;
        }
      }
    },
    mounted() {
      this.$store.commit('loadUserInfo');
    }
  }
</script>

<style scoped>
  .addExamineePage {
    height: 100%;
    overflow: hidden;
  }

  .addSingle {
    width: 600px;
    padding-top: 20px;
    padding-left: 150px;
    padding-bottom: 20px;
  }

  .addList {
    width: 600px;
    padding-left: 150px;
    margin-bottom: 30px;
  }
</style>

<style>
  .el-input {
    width: 250px;
  }

  .examineeListUploadCard .el-form-item {
    margin-bottom: 8px;
  }

  .el-upload-list__item {
    width: 300px;
  }
</style>
