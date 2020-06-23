<template>
  <div class="addExamPage">
    <head-top></head-top>
    <el-scrollbar
      style="height: 100%;"
      wrap-style="overflow-x: hidden; overflow-y: scroll;">
      <div style="padding: 10px; height: 100%;" class="addExamPageContent">
        <div class="examInfoBox">
          <el-form :model="examInfo" :rules="rules" ref="examInfoForm"
                   label-width="120px" class="addExamInfoForm">

            <!-- 考试编号 -->
            <el-form-item label="考试编号" prop="number" required>
              <el-input v-model="examInfo.number" clearable
                        style="width: 260px;">
              </el-input>
            </el-form-item>

            <!-- 考试名称 -->
            <el-form-item label="考试名称" prop="name" required>
              <el-input v-model="examInfo.name" clearable
                        style="width: 260px">
              </el-input>
            </el-form-item>

            <!-- 考试时间 -->
            <el-form-item label="考试时间" required>
              <!-- 日期 -->
              <el-col :span="11">
                <el-form-item prop="date">
                  <el-date-picker type="date" placeholder="选择日期"
                                  v-model="examInfo.date" style="width: 140px"
                                  :editable="false" value-format="yyyy-MM-dd">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col class="line" :span="2">-</el-col>
              <!-- 开始时间 -->
              <el-col :span="11">
                <el-form-item prop="startTime">
                  <el-time-picker placeholder="开始时间" v-model="examInfo.startTime"
                                  style="width: 140px" :editable="false"
                                  value-format="HH-mm" format="HH-mm">
                  </el-time-picker>
                </el-form-item>
              </el-col>
              <el-col class="line" :span="2">-</el-col>
              <!-- 结束时间 -->
              <el-col :span="11">
                <el-form-item prop="endTime">
                  <el-time-picker placeholder="结束时间" v-model="examInfo.endTime"
                                  style="width: 140px" :editable="false"
                                  value-format="HH-mm" format="HH-mm">
                  </el-time-picker>
                </el-form-item>
              </el-col>
            </el-form-item>

            <!-- 考试项目个人所需报名费 -->
            <el-form-item label="考试报名费" prop="regPrice">
              <el-input type="number" v-model.number="examInfo.regPrice"
                        autocomplete="off" prefix-icon="el-icon-money"
                        style="width: 130px;">
              </el-input>
              <span>RMB</span>
            </el-form-item>

            <!-- 考试负责人列表 上传 ；格式文件下载 -->
            <el-form-item label="负责人列表" required>
              <el-upload class="uploadFile" action="" :limit="1" :show-file-list="true"
                         :before-upload="beforeUploadFormatFile" :http-request="uploadMgFile"
                         :before-remove="beforeRemoveFormatFile" :on-remove="removeMgFile"
                         :disabled="uploadMgFileDisable">
                <el-button size="small" type="primary">点击上传</el-button>
                <span style="font-size: 13px; color: red;">（请按照格式上传，只接收 xls 文件）</span>
                <span v-show="uploadingMgFile"><i class="el-icon-loading"/>正在上传</span>
              </el-upload>
              <el-button size="small" type="primary" plain @click="downloadFormatFile('格式文件-考试负责人列表.xls')">
                下载格式文件<span style="color: red;">（推荐）</span>
              </el-button>
            </el-form-item>

            <!-- 教室列表 上传 ；格式文件下载 -->
            <el-form-item label="教室列表" required>
              <el-upload class="uploadFile" action="" :limit="1" :show-file-list="true"
                         :before-upload="beforeUploadFormatFile" :http-request="uploadClrFile"
                         :before-remove="beforeRemoveFormatFile" :on-remove="removeClrFile"
                         :disabled="uploadClrFileDisable">
                <el-button size="small" type="primary">点击上传</el-button>
                <span style="font-size: 13px; color: red;">（请按照格式上传，只接收 xls 文件）</span>
                <span v-show="uploadingClrFile"><i class="el-icon-loading"/>正在上传</span>
              </el-upload>
              <el-button size="small" type="primary" plain @click="downloadFormatFile('格式文件-考试教室列表.xls')">
                下载格式文件<span style="color: red;">（推荐）</span>
              </el-button>
            </el-form-item>

            <!-- 考生列表 上传 ；格式文件下载 -->
            <el-form-item label="考生导入" required>
              <el-upload class="uploadFile" action="" :limit="1" :show-file-list="true"
                         :before-upload="beforeUploadFormatFile" :http-request="uploadExamineeFile"
                         :before-remove="beforeRemoveFormatFile" :on-remove="removeExamineeFile"
                         :disabled="uploadExamineeFileDisable">
                <el-button size="small" type="primary">点击上传</el-button>
                <span style="font-size: 13px; color: red;">（请按照格式上传，只接收 xls 文件）</span>
                <span v-show="uploadingExamineeFile"><i class="el-icon-loading"/>正在上传</span>
              </el-upload>
              <el-button size="small" type="primary" plain @click="downloadFormatFile('格式文件-考生列表.xls')">
                下载格式文件<span style="color: red;">（推荐）</span>
              </el-button>
            </el-form-item>

            <!-- 随机分配教室 -->
            <el-form-item label="随机分配教室">
              <el-switch v-model="randomArrange" active-text="确认随机分配"
                         @change="randomArrangeStatus">
              </el-switch>
              <br/>
              <template>
                <div class="randomMethodRadio">
                  <el-radio :disabled="randomMethodDisabled" v-model="randomMethod" label="1">
                    随机方式 1
                  </el-radio>
                  <el-tooltip placement="right">
                    <div slot="content">
                      N 个考场（考场间容量不同）共 M 个座位，座位序号从 1 到 M。<br />
                      X 个考生，X  ≤ M。<br />
                      考生从 M 个座位号中随机抽取 1 个座位号，并找到该座位号对应的教室。<br />
                    </div>
                    <i class="el-icon-info"/>
                  </el-tooltip>
                </div>
                <div class="randomMethodRadio">
                  <el-radio :disabled="randomMethodDisabled" v-model="randomMethod" label="2">
                    随机方式 2
                  </el-radio>
                  <el-tooltip placement="right">
                    <div slot="content">
                      N 个考场（考场间容量不同）共 M 个座位。<br />
                      X 个考生，X  ≤ M。<br />
                      先将考场列表和考生列表打乱。<br />
                      每个教室分配 X / N （向下取正）个考生，剩余 X % N 个考生将随机分配到 N 个考场中。<br />
                      每个教室在容量中随机抽取同分配考生数相同个座位，获取到一个作为列表。<br />
                      将已乱序考生列表中考生顺序按照每个教室的座位列表分配到已乱序教室列表中每个教室。
                    </div>
                    <i class="el-icon-info"/>
                  </el-tooltip>
                </div>
              </template>
            </el-form-item>
          </el-form>
        </div>

        <!-- 提交 / 重置 -->
        <div class="finalBtn">
          <el-button type="primary" @click="submitForm('examInfoForm')"
                     round icon="el-icon-circle-plus-outline">
            添加考试
          </el-button>
          <el-button @click="resetForm('examInfoForm')"
                     round icon="el-icon-remove-outline">
            重置内容
          </el-button>
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
    name: "addExam",
    data() {
      return {
        examInfo: {
          number: '', // 考试项目编号
          name: '', //考试项目名称
          date: '', // 开始日期
          startTime: '', // 开始时间
          endTime: '', // 结束时间
          regPrice: '', // 考试项目个人所需报名费
        },
        rules: {
          number: [
            {required: true, message: '请输入考试编号', trigger: 'blur'},
            {min: 2, max: 30, message: '考试编号在 2 到 30 位之间', trigger: 'blur'}
          ],
          name: [
            {required: true, message: '请输入考试名称', trigger: 'blur'},
            {min: 2, max: 30, message: '考试名称在 2 到 30 位之间', trigger: 'blur'}
          ],
          date: [
            {required: true, message: '请输入考试日期', trigger: 'blur'},
          ],
          startTime: [
            {required: true, message: '请输入考试开始时间', trigger: 'blur'},
          ],
          endTime: [
            {required: true, message: '请输入考试结束时间', trigger: 'blur'},
          ],
          regPrice: [
            {required: true, message: '请输入考试个人所需报名费', trigger: 'blur'},
          ]
        },
        /* 考试负责人列表文件上传状态 */
        haveMgFile: false,
        uploadMgFileDisable: false,
        uploadingMgFile: false,
        /* 教室列表文件上传状态 */
        haveClrFile: false,
        uploadClrFileDisable: false,
        uploadingClrFile: false,
        /* 考生列表文件上传状态 */
        haveExamineeFile: false,
        uploadExamineeFileDisable: false,
        uploadingExamineeFile: false,
        /* 数量 */
        clrTotalCapacity: 0,
        examineeNumber: 0,
        /* 随机分配 */
        randomArrange: true,
        randomMethod: '1',
        randomMethodDisabled: false
      };
    },
    components: {
      headTop
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if (this.haveMgFile && this.haveClrFile && this.haveExamineeFile) {
              this.$confirm('确认添加考试?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                let loadingText = '';
                if(this.randomArrange) {
                  loadingText = '后台服务器正在添加数据以及分配座位，请稍等，请勿刷新页面.....';
                } else {
                  loadingText = '后台服务器正在添加数据，请稍等，请勿刷新页面.....';
                }
                let loading = this.$loading({
                  lock: true,
                  text: loadingText,
                  spinner: 'el-icon-loading',
                  background: 'rgba(0, 0, 0, 0.7)',
                  target: document.querySelector('.addExamPageContent')
                });

                const requestData = {
                  sessionID: this.$store.state.userInfo.sessionID,
                  userName: this.$store.state.userInfo.userName,
                  examNumber: this.examInfo.number,
                  examName: this.examInfo.name,
                  examDate: this.examInfo.date,
                  examStartTime: this.examInfo.startTime,
                  examEndTime: this.examInfo.endTime,
                  examRegPrice: this.examInfo.regPrice,
                  isRandomArrange: this.randomArrange,
                  randomArrangeMethod: this.randomMethod
                };
                // console.log(requestData);
                this.axios.post('/addExam', requestData)
                  .then(response => {
                    const status = response.data.uploadStatus;
                    if (status === 401) {
                      this.$notify({
                        title: '添加失败提示',
                        type: 'success',
                        message: '添加失败，教室容量小于考生人数。\n可能性：教室列表中存在教室未录入系统。\n可能解决：从新上传教室列表文件\n或左侧栏中添加教室。',
                        duration: 0
                      });
                    } else if (status === 402) {
                      this.$message({
                        type: 'warning',
                        message: '添加失败，考试编号已存在！！！'
                      });
                      this.examInfo.examNumber = '';
                    } else if (status === 200) {
                      this.$message({
                        type: 'success',
                        message: '添加成功!'
                      });
                    }
                  }).catch(error => {
                  console.log(error);
                });

                loading.close();
              }).catch(() => {
                this.$message({
                  type: 'info',
                  message: '已取消添加'
                });
              });

            } else {
              this.$message({
                type: 'warning',
                message: '添加考试失败！！！请完整上传：考试负责人列表、教室列表、考生列表'
              });
            }
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.examInfo = {
          number: '',
          name: '',
          date: '',
          startTime: '',
          endTime: '',
          regPrice: '',
          isManager: false
        };
      },
      /* 通用 */
      /* 下载格式文件 */
      downloadFormatFile(fileName) {
        download('/downloadFormatFileFromAddExam' + '?downloadFileName=' + fileName, fileName);
      },
      /* 上传格式文件 */
      beforeUploadFormatFile(file) {
        const isXls = file.type === 'application/vnd.ms-excel';
        if (!isXls) {
          this.$message.error('上传文件只能是 xls 文件！')
        }
        return isXls;
      },
      uploadFormatFile(param, uploadFileName) {
        let xlsFileData = new FormData();
        xlsFileData.append("file", param.file);
        xlsFileData.append("sessionID", this.$store.state.userInfo.sessionID);
        xlsFileData.append("userName", this.$store.state.userInfo.userName);
        xlsFileData.append("uploadFileName", uploadFileName);
        this.$axios.post('/uploadXLSFileFromAddExam', xlsFileData).then(response => {
          const result = response.data;
          if (result.isLogin) {
            if (uploadFileName === '格式文件-考试负责人列表.xls') {
              this.haveMgFile = true;
            } else if (uploadFileName === '格式文件-考试教室列表.xls') {
              this.haveClrFile = true;
            } else if (uploadFileName === '格式文件-考生列表.xls') {
              this.haveExamineeFile = true;
            }

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
      },
      /* 移除上传格式文件 */
      beforeRemoveFormatFile(file) {
        return this.$confirm(`确定移除 ${file.name}？`);
      },
      removeFormatFile(uploadFilePath) {
        this.$axios.post('/removeUploadFileFromAddExam', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName,
          uploadFilePath: uploadFilePath,
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
      /* 考试负责人列表处理 */
      uploadMgFile(param) { // 考试负责人列表上传
        this.uploadingMgFile = true;
        this.uploadMgFileDisable = true;

        this.uploadFormatFile(param, '格式文件-考试负责人列表.xls');

        this.uploadingMgFile = false;
        this.uploadMgFileDisable = false;
      },
      removeMgFile() { // 考试负责人列表上传取消
        this.haveMgFile = false;
        this.removeFormatFile('AddExamMg');
      },
      /* 教室列表处理 */
      uploadClrFile(param) {
        this.uploadingClrFile = true;
        this.uploadClrFileDisable = true;

        this.uploadFormatFile(param, '格式文件-考试教室列表.xls');

        this.uploadingClrFile = false;
        this.uploadClrFileDisable = false;
      },
      removeClrFile() {
        this.haveClrFile = false;
        this.removeFormatFile('AddExamClr');
      },
      /* 考生列表处理 */
      uploadExamineeFile(param) {
        this.uploadingExamineeFile = true;
        this.uploadExamineeFileDisable = true;

        this.uploadFormatFile(param, '格式文件-考生列表.xls');

        this.uploadingExamineeFile = false;
        this.uploadExamineeFileDisable = false;
      },
      removeExamineeFile() {
        this.haveExamineeFile = false;
        this.removeFormatFile('AddExaminee');
      },
      /* 随机分配状态 */
      randomArrangeStatus() {
        if (this.randomArrange === false) {
          this.$confirm('确定取消自动随机分配?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.$message({
              type: 'success',
              message: '取消自动随机分配成功！'
            });
            this.randomArrange = false;
            this.randomMethodDisabled = true;
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '无取消自动随机分配！'
            });
            this.randomArrange = true;
          });
        } else {
          this.randomMethodDisabled = false;
        }
      }
    },
    mounted() {
      this.$store.commit('loadUserInfo');
    }
  }
</script>

<style scoped>
  .addExamPage {
    height: 100%;
    overflow: hidden;
  }

  .examInfoBox {
    height: 100%;
  }

  .el-form-item {
    padding-top: 1%;
    padding-left: 12%;
    padding-right: 20%;
  }

  .el-col {
    width: 140px;
  }

  .el-col .el-form-item {
    padding-left: 0;
    padding-right: 0;
  }

  .line {
    width: 2%;
    text-align: center;
  }

  .mgTable {
    width: 80%;
    border: 1px solid #d8dee2;
    border-radius: 2px;
    max-height: 250px;
  }

  .clsTable {
    width: 80%;
    border: 1px solid #d8dee2;
    border-radius: 2px;
    max-height: 250px;
  }

  .finalBtn {
    padding-left: 20%;
    margin-bottom: 6.5%;
  }

  .randomMethodRadio {
    width: 150px;
  }
</style>

<style>
  .el-upload-list__item {
    width: 280px;
  }

  .uploadFile {
    width: 400px;
  }

  .el-radio {
    margin: 0;
  }
</style>
