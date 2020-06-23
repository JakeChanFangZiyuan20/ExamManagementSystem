<template>
  <div class="addClrPage">
    <head-top></head-top>
    <el-scrollbar
      style="height: 100%;"
      wrap-style="overflow-x: hidden; overflow-y: scroll;">
      <div style="padding: 10px; height: 100%;">
        <div class="clrInfoBox">
          <el-form :model="clsInfo" :rules="rules" ref="clsInfoForm"
                   label-width="120px" class="addClsInfoForm">

            <!-- 教室编号 -->
            <el-form-item label="教室编号" prop="number">
              <el-input v-model="clsInfo.number" clearable
                        style="width: 220px;" placeholder="博学楼02-B202填B02B202">
              </el-input>
            </el-form-item>

            <!-- 详细地址 -->
            <el-form-item label="详细地址" prop="address">
              <el-input v-model="clsInfo.address" clearable
                        style="width: 300px" class="inputBox" placeholder="例如：中国矿业大学">
              </el-input>
            </el-form-item>

            <!-- 可容纳人数 -->
            <el-form-item label="容纳人数" prop="capacity">
              <el-input type="number" v-model.number="clsInfo.capacity"
                        autocomplete="off" style="width: 140px;"
                        prefix-icon="el-icon-user">
              </el-input>
              <span>人</span>
            </el-form-item>

            <!--负责人列表 导入 / 删除 -->
            <el-form-item label="负责人列表" required>
              <el-upload class="uploadFile" action="" :limit="1" :show-file-list="true"
                         :http-request="uploadFile" :before-remove="beforeRemove" :on-remove="removeFile"
                         :before-upload="beforeUpload" :disabled="uploadDisable">
                <el-button size="small" type="primary">点击上传</el-button>
                <span style="font-size: 13px; color: red;">（请按照格式上传，只接收 xls 文件）</span>
                <span v-show="uploading"><i class="el-icon-loading" />正在上传</span>
              </el-upload>
              <el-button size="small" type="primary" plain @click="mgFormat()">
                下载格式文件<span style="color: red;">（推荐）</span>
              </el-button>
            </el-form-item>

          </el-form>
        </div>

        <!-- 提交 / 重置 -->
        <div class="finalBtn">
          <span v-show="backEndProcess" style="font-size: 10px;">
            <i class="el-icon-loading" />
            （后台正在处理上传的负责人列表，请稍等，请勿刷新页面）
          </span> <br />
          <el-button
            type="primary" @click="submitForm('clsInfoForm')"
            round icon="el-icon-circle-plus-outline" :disabled="addClrBtnDisabled">
            添加教室
          </el-button>
          <el-button
            @click="resetForm('clsInfoForm')"
            round icon="el-icon-remove-outline">
            重置内容
          </el-button>
          <span style="font-size: 10px;">（重置对上传文件删除无效）</span>
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
    name: "addClr",
    data() {
      return {
        clsInfo: { // clsInfo alias clrInfo
          address: '',
          number: '',
          capacity: '',
          managers: []
        },
        rules: {
          number: [
            { required: true, message: '请输入教室编号', trigger: 'blur' },
            { min: 1, max: 10, message: '教室编号长度限制在 1 到 10 位', trigger: 'blur' }
          ],
          address: [
            { required: true, message: '请输入详细地址', trigger: 'blur' },
            { min: 3, max: 30, message: '详细地址长度限制在 3 到 30 位', trigger: 'blur' }
          ],
          capacity: [
            { required: true, message: '请输入可容纳人数', trigger: 'blur' }
          ]
        },
        addClrBtnDisabled: false,
        backEndProcess: false,
        haveUploadFile: false,
        uploadDisable: false,
        uploading: false
      }
    },
    components: {
      headTop
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if(this.haveUploadFile) {
              this.addClrBtnDisabled = true;
              this.uploadDisable = true;
              this.backEndProcess = true;

              const requestData = this.$store.state.userInfo;
              this.$axios.post('/insertClrMgList', {
                sessionID: requestData.sessionID,
                userName: requestData.userName,
                clrNumber: this.clsInfo.number,
                address: this.clsInfo.address,
                capacity: this.clsInfo.capacity
              }).then(response => {
                const responseData =response.data;
                if(responseData.status === 200){
                  this.$message({
                    type: 'success',
                    message: '添加教室成功'
                  })
                } else if(responseData.status === 400) {
                  this.$message({
                    type: 'warn',
                    message: '教室添加失败，已存在教室'
                  });
                  this.clsInfo.number = '';
                } else {
                  confirmLogout();
                }
                this.addClrBtnDisabled = false;
                this.uploadDisable = false;
                this.backEndProcess = false;
              }).catch(error => {
                console.log(error);
              });
            } else{
              this.$message({
                type: 'warning',
                message: '请上传负责人列表文件'
              });
              return false;
            }
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.clsInfo.address = '';
        this.clsInfo.number = '';
        this.clsInfo.capacity = '';
      },
      /* 上传 */
      beforeUpload(file) { // 文件类型检查
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
        this.$axios.post('/uploadXLSFile', xlsFileData).then(response => {
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
      /* 移除上传 */
      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${ file.name }？`);
      },
      removeFile() {
        this.haveUploadFile = false;
        this.$axios.post('/removeUploadFile', {
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
      /* 下载格式文件 */
      mgFormat() {
        download('/downloadClrMgList', '格式文件-教室负责人列表.xls');
      }
    },
    mounted() {
      this.$store.commit('loadUserInfo');
    }
  }
</script>

<style scoped>
  .addClrPage {
    height: 100%;
    overflow: hidden;
  }

  .clrInfoBox {
    height: 100%;
    width: 80%;
    padding-top: 2%;
    padding-left: 10%;
  }

  .finalBtn {
    padding-left: 20%;
    margin-bottom: 6.5%;
  }

  .el-col {
    width: 12%;
  }

  .el-col .el-form-item {
    padding-left: 0;
    padding-right: 0;
  }

  .line {
    width: 2%;
    text-align: center;
  }

</style>

<style>
  .el-upload-list__item {
    width: 280px;
  }

  .uploadFile {
    width: 400px;
  }

</style>
