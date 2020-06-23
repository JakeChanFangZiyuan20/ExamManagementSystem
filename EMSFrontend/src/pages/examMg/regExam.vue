<template>
  <div class="regExamPage">
    <head-top></head-top>
    <el-scrollbar
      style="height: 100%;"
      wrap-style="overflow-x: hidden; overflow-y: scroll;">
      <div style="padding: 10px; height: 100%;">
        <div class="contentBox">

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

          <!-- 报名信息 -->
          <div class="regBox">
            <el-card class="regCard">
              <div slot="header" class="examLocationCardTitle">
                <span>报名信息</span>
              </div>
              <div class="regInfoInputBox">
                <el-form :model="regInfoForm" :rules="regInfoFormRules" label-width="120px" class="regInfoForm"
                         ref="regInfoForm">
                  <el-form-item label="账号" prop="account">
                    <el-input clearable v-model.trim="regInfoForm.account" class="regInfoElInput"></el-input>
                  </el-form-item>
                  <el-form-item label="密码" prop="password">
                    <el-input clearable v-model.trim="regInfoForm.password" class="regInfoElInput"
                              type="password"></el-input>
                  </el-form-item>
                  <el-form-item label="姓名" prop="trueName">
                    <el-input clearable v-model.trim="regInfoForm.trueName" class="regInfoElInput"></el-input>
                  </el-form-item>
                  <el-form-item label="性别" prop="gender">
                    <el-radio v-model="regInfoForm.gender" label="男"><i class="el-icon-male"/>男</el-radio>
                    <el-radio v-model="regInfoForm.gender" label="女"><i class="el-icon-female"/>女</el-radio>
                  </el-form-item>
                  <el-form-item label="出生日期" prop="trueName">
                    <el-date-picker v-model="regInfoForm.birthday" type="date" placeholder="选择日期"
                                    :editable="false" style="width: 150px;">
                    </el-date-picker>
                  </el-form-item>
                  <el-form-item label="身份证" prop="trueName">
                    <el-input clearable v-model.trim="regInfoForm.identity" class="regInfoElInput"></el-input>
                  </el-form-item>
                  <el-form-item label="手机" prop="trueName">
                    <el-input clearable v-model.trim="regInfoForm.phone" class="regInfoElInput"></el-input>
                  </el-form-item>
                  <el-form-item label="邮箱" prop="trueName">
                    <el-input clearable v-model.trim="regInfoForm.email" class="regInfoElInput"></el-input>
                  </el-form-item>
                  <el-form-item label="头像上传" prop="photoURL">
                    <el-upload class="avatarUpload" :show-file-list="false" action=""
                               :before-upload="beforeUploadAvatar" :http-request="uploadAvatar">
                      <div class="avatarBox">
                        <el-image v-if="regInfoForm.photoURL" :src="regInfoForm.photoURL" fit="fit"
                                  class="avatar"></el-image>
                        <i v-else class="el-icon-plus avatarUploadIcon"></i>
                      </div>
                    </el-upload>
                    <span>（点击上传头像）</span>
                  </el-form-item>
                  <el-form-item label="准考证" prop="examineeNumber">
                    <el-input clearable v-model.trim="regInfoForm.examineeNumber" class="regInfoElInput"></el-input>
                  </el-form-item>
                  <el-form-item label="座位分配">
                    <span>（自动将该考生随机分配到已有空位）</span>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="confirmReg('regInfoForm')">确认报名</el-button>
                    <el-button @click="resetRegInfo">立即重置</el-button>
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
                <el-button type="primary" @click="regPay">确 定</el-button>
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
    name: "regExam",
    data() {
      let validateAccount = (rule, value, callback) => {
        if (value === '') {
          return callback(new Error('请输入账号'));
        }
        let accountReg = /^(?=.*[a-zA-Z0-9])[A-Za-z0-9]{6,20}$/;
        if (!accountReg.test(value)) {
          callback(new Error('账号由 6 到 20 位字母数字组成'));
        } else {
          callback();
        }
      };
      let validatePwd = (rule, value, callback) => {
        if (value === '') {
          return callback(new Error('请输入密码'));
        }
        let pwdReg = /^(?=.*[a-zA-Z0-9])[A-Za-z0-9]{6,20}$/;
        if (!pwdReg.test(value)) {
          callback(new Error('密码由 6 到 20 位字母数字组成'));
        } else {
          callback();
        }
      };
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
        /* 报名信息填写 */
        regInfoForm: {
          account: '08160101',
          password: '123456',
          trueName: 'jake',
          gender: '男',
          birthday: '2019-01-01',
          identity: '440681AAAABBBBCCCC',
          phone: '13978560934',
          email: 'asdf@sld.com',
          photoURL: '',
          examineeNumber: 'CMDK389234',
          paymentStatus: false,
          clrNumber: '',
          seatNumber: '',
        },
        regInfoFormRules: {
          account: [
            {required: true, message: '请输入账号', trigger: 'blur'},
            {validator: validateAccount, trigger: 'blur'},
          ],
          password: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            {validator: validatePwd, trigger: 'blur'}
          ],
          trueName: [
            {required: true, message: '请输入姓名', trigger: 'blur'},
            {min: 2, max: 15, message: '长度在 1 到 15 个字符', trigger: 'blur'}
          ],
          gender: [
            {required: true, message: '请选择性别', trigger: 'blur'}
          ],
          birthday: [
            {required: true, message: '请选择出生日期', trigger: 'blur'}
          ],
          identity: [
            {required: true, message: '请输入身份证', trigger: 'blur'},
            {min: 18, max: 18, message: '身份证号长度为 18 位', trigger: 'blur'}
          ],
          phone: [
            {required: true, message: '请输入手机号', trigger: 'blur'},
            {min: 11, max: 11, message: '手机号长度为 11 位', trigger: 'blur'}
          ],
          email: [
            {required: true, message: '请输入手机号', trigger: 'blur'},
            {min: 5, max: 50, message: '邮箱长度长度限定为 5 到 50 位', trigger: 'blur'}
          ],
          examineeNumber: [
            {required: true, message: '请输入准考证号', trigger: 'blur'},
            {min: 5, max: 30, message: '准考证号长度限定为 5 到 30 位', trigger: 'blur'}
          ],
          photoURL: [
            {required: true, message: '请上传头像', trigger: 'blur'},
          ]
        },
        paymentDialogVisible: false,
      };
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
      /* 报名信息 */
      addInitAccount(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.addInitAccountBtnLoading = true;
            this.$axios.post('addInitAccount', {
              sessionID: this.$store.state.userInfo.sessionID,
              userName: this.$store.state.userInfo.userName,
              initAccount: this.initAccountForm.account,
              initPassword: this.initAccountForm.password
            }).then(response => {
              const responseData = response.data;
              if (responseData.status === 200) {
                this.$message({
                  type: 'success',
                  message: '账户添加成功'
                });
                this.regInfoFormDisabled = false;
                this.curAccount = this.initAccountForm.account;
              } else if (responseData.status === 400) {
                this.$message({
                  type: 'warning',
                  message: '已存在账户'
                });
              } else {
                confirmLogout();
              }
              this.addInitAccountBtnLoading = false;
            }).catch(error => {
              console.log(error);
              this.addInitAccountBtnLoading = false;
            });
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      /* 报名信息填写 */
      beforeUploadAvatar(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;

        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
        }

        return isJPG && isLt2M;
      },
      uploadAvatar(param) {
        if (this.regInfoForm.account === '') {
          this.$message({
            type: 'warning',
            message: '请先输入账号'
          });
          return false;
        }
        let formData = new FormData();
        formData.append("file", param.file);
        formData.append("sessionID", this.$store.state.userInfo.sessionID);
        formData.append("userName", this.$store.state.userInfo.userName);
        formData.append("newExamineeUserName", this.regInfoForm.account);

        this.$axios.post('/uploadAvatar', formData).then(response => {
          const responseData = response.data;
          if (responseData.status === 200) {
            this.$message({
              type: 'success',
              message: '头像上传成功'
            });
            let that = this;
            this.regInfoForm.photoURL = this.$requestURL + '/images/temp/' + this.regInfoForm.account + '.jpg';
          } else if (responseData.status === 400) {
            confirmLogout();
          }
        }).catch(error => {
          console.log(error);
        })
      },
      regPay() {
        if(this.examNumber === '') {
          this.$message({
            type: 'warning',
            message: '请先输入考试编号'
          });
          return false;
        }

        // 暂存 Redis，若暂存 redis 成功则开始进行支付
        this.save2Redis();
      },
      save2Redis() {
        // 暂存 redis
        const regInfoForm = this.regInfoForm;
        this.$axios.post('/redisSaveRegExaminee', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName,
          account: regInfoForm.account,
          password: regInfoForm.password,
          role: 3,
          trueName: regInfoForm.trueName,
          gender: regInfoForm.gender,
          birthday: regInfoForm.birthday,
          identity: regInfoForm.identity,
          phone: regInfoForm.phone,
          email: regInfoForm.email,
          photoURL: regInfoForm.photoURL,
          examNumber: this.examNumber,
          examineeNumber: regInfoForm.examineeNumber,
          paymentStatus: 0,
          clrNumber: '',
          seatNumber: '',
        }).then(response => {
          const responseData = response.data;
          if(responseData.status === 200) {
            this.payRegPrice(responseData.regExamineeSessionID);
          } else if(responseData.status === 401) {
            this.$message({
              type: 'warning',
              message: '已存在考生用户，请重新填写考生用户'
            });
          } else if(responseData.status === 402) {
            this.$message({
              type: 'warning',
              message: '已无空位'
            });
          } else if(responseData.status === 400) {
          } else {
            confirmLogout();
          }
        }).catch(error => {
          console.log(error);
        })
      },
      payRegPrice(regExamineeSessionID) {
        const payRequestInfo = {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName,
          role: 2,
          out_trade_no: regExamineeSessionID,
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
      handlePaymentDialogClose(done) {
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
      },
      confirmReg(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if(this.examNumber === '') {
              this.$message({
                type: 'warning',
                message: '请先输入考试编号'
              });
              return false;
            }

            if(this.totalCanContain === 0) {
              this.$message({
                type: 'warning',
                message: '考试座位已满'
              });
              return false;
            }

            this.$confirm('确定报名?', '报名确认提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.paymentDialogVisible = true;
            }).catch(() => {
              this.$message({
                type: 'info',
                message: '未报名'
              });
            });
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetRegInfo() {
        this.regInfoForm = {
          account: '',
          password: '',
          trueName: '',
          gender: '',
          birthday: '',
          identity: '',
          phone: '',
          email: '',
          photoURL: '',
          examineeNumber: '',
          paymentStatus: false,
          clrNumber: '',
          seatNumber: '',
        }
      },
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
  .regExamPage {
    height: 100%;
    overflow: hidden;
  }

  .contentBox {
    margin-bottom: 10%;
  }

  .examLocationBox, .examClrSituationBox, .regBox {
    width: 90%;
    margin: 0 auto;
    padding-bottom: 10px;
  }

  .regInfoInputBox {
    width: 80%;
    margin: 0 auto;
  }

  .regInfoElInput {
    width: 300px;
  }

  .avatarBox {
    margin: 0 auto;
  }

  .avatar {
    width: 100px;
    height: 100px;
    border: 1px #6f7fb4 dashed;
    border-radius: 6px;
  }

  .avatarUploadIcon {
    font-size: 28px;
    color: #8c939d;
    width: 100px;
    height: 100px;
    line-height: 100px;
    text-align: center;
    border: 1px #6f7fb4 dashed;
    border-radius: 6px;
  }
</style>
