<template>
  <div class="editInfoPage">
    <head-top ref="headTop"></head-top>
    <el-scrollbar
      style="height: 100%;"
      wrap-style="overflow-x: hidden; overflow-y: scroll;">
      <div style="padding: 10px; height: 100%">
        <div class="userInfoBox">
          <el-card class="userInfoCard">
            <!-- 卡片头部信息 -->
            <div slot="header" class="cardHeader">
              <span>个人信息</span>
            </div>
            <!-- 用户名 -->
            <div class="textItem">
              <span>用户名：{{ userInfo.userName }}</span>
            </div>
            <!-- 密码 -->
            <div class="textItem" v-loading="pwdChangeLoading">
              <el-collapse accordion>
                <el-collapse-item>
                  <template slot="title">
                    <i class="el-icon-lock" style="font-size: 16px;"></i>
                    <span style="font-size: 16px;">&nbsp;&nbsp;密码修改</span>
                  </template>
                  <el-form :model="pwdForm" status-icon :rules="pwdRules" ref="pwdForm"
                           label-width="100px" class="pwdForm">
                    <el-form-item label="原密码" prop="orgPwd" style="margin-bottom: 0; padding-bottom: 5px;">
                      <el-input type="password" v-model.trim="pwdForm.orgPwd" clearable
                                autocomplete="off" style="width: 70%;"
                                placeholder="请输入原密码" @blur="openPwdSubmitBtn()"></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" prop="newPwd" style="margin-bottom: 0; padding-bottom: 5px;">
                      <el-input type="password" v-model.trim="pwdForm.newPwd" clearable
                                autocomplete="off" style="width: 70%;"
                                placeholder="请输入新密码" @blur="openPwdSubmitBtn()"></el-input>
                    </el-form-item>
                    <el-form-item label="确认密码" prop="checkPwd" style="margin-bottom: 0; padding-bottom: 5px;">
                      <el-input type="password" v-model.trim="pwdForm.checkPwd" clearable
                                autocomplete="off" style="width: 70%;"
                                placeholder="请重新输入新密码" @blur="openPwdSubmitBtn()"></el-input>
                    </el-form-item>
                    <el-form-item style="margin-bottom: 0; padding-bottom: 5px;">
                      <el-button type="primary" @click="confirmModifyPwd('pwdForm')"
                                 :disabled="btnDisabled">提交
                      </el-button>
                      <el-button @click="resetPwdForm('pwdForm')">重置</el-button>
                    </el-form-item>
                  </el-form>
                </el-collapse-item>
              </el-collapse>
            </div>
            <!-- 个人信息 -->
            <el-form :model="userInfo" :rules="userInfoRules" ref="userInfoForm"
                     label-width="110px" class="userInfoForm" v-loading="userInfoFormLoading">
              <!-- 真实姓名 -->
              <el-form-item label="姓名：" prop="trueName">
                <el-input v-model="userInfo.trueName" class="inputBox"></el-input>
              </el-form-item>
              <!-- 性别 -->
              <el-form-item label="性别：" prop="gender">
                <el-radio v-model="userInfo.gender" label="男"><i class="el-icon-male"></i></el-radio>
                <el-radio v-model="userInfo.gender" label="女"><i class="el-icon-female"></i></el-radio>
              </el-form-item>
              <!-- 生日 -->
              <el-form-item label="出生日期：" prop="birthday">
                <el-date-picker
                  v-model="userInfo.birthday" type="date"
                  placeholder="" :editable="false" class="inputBox">
                </el-date-picker>
              </el-form-item>
              <!-- 身份证 -->
              <el-form-item label="身份证：" prop="identity">
                <el-input placeholder="" v-model="userInfo.identity"
                          clearable class="inputBox"></el-input>
              </el-form-item>
              <!-- 手机号 -->
              <el-form-item label="手机号：" prop="phone">
                <el-input placeholder="" v-model="userInfo.phone"
                          clearable class="inputBox"></el-input>
              </el-form-item>
              <!-- 电子邮件 -->
              <el-form-item label="邮箱：" prop="email">
                <el-input placeholder="" v-model="userInfo.email"
                          clearable class="inputBox"></el-input>
              </el-form-item>
            </el-form>
            <!-- 头像 -->
            <div class="textItem">
              <el-upload
                class="avatar-uploader"
                action=""
                :show-file-list="false"
                :before-upload="beforeAvatarUpload"
                :http-request="uploadPhoto">
                <!-- http-request 自定义上传行为 -->
                <img v-if="userInfo.photoURL" :src="userInfo.photoURL" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
              <span style="font-size: small">（点击更改头像，只能上传 JPG 格式且 < 2M）</span>
            </div>
          </el-card>
        </div>

        <!-- 确认提交 -->
        <div class="finalBtn">
          <el-button
            type="primary" @click="submitForm('userInfoForm')"
            round icon="el-icon-circle-check">
            确认修改
          </el-button>
          <br/>
          <span style="font-size: 10px">（对密码修改和图片修改不起作用）</span>
        </div>
      </div>
    </el-scrollbar>
  </div>

</template>

<script>
  import headTop from "../../components/headTop";
  import {confirmLogout} from "../../utils/logout";

  export default {
    name: "editInfo",
    data() {
      return {
        userInfo: {
          userName: '', // 用户名
          trueName: '', // 真真实姓名
          gender: '',
          phone: '',
          photoURL: '',
          birthday: '',
          identity: '', // 身份证
          email: ''
        },
        userInfoRules: {
          trueName: [
            { required: true, message: '请输入姓名', trigger: 'blur' },
            { min: 2, max: 15, message: '长度在 1 到 15 个字符', trigger: 'blur' }
          ],
          gender: [
            { required: true, message: '请选择性别', trigger: 'blur' }
          ],
          birthday: [
            { required: true, message: '请选择出生日期', trigger: 'blur' }
          ],
          identity: [
            { required: true, message: '请输入身份证', trigger: 'blur' },
            { min: 18, max: 18, message: '身份证号长度为 18 位', trigger: 'blur' }
          ],
          phone: [
            { required: true, message: '请输入手机号', trigger: 'blur' },
            { min: 11, max: 11, message: '手机号长度为 11 位', trigger: 'blur' }
          ],
          email: [
            { required: true, message: '请输入手机号', trigger: 'blur' },
            { min: 5, max: 50, message: '邮箱长度长度限定为 5 到 50 位', trigger: 'blur' }
          ]
        },
        userInfoFormLoading: false,
        /* 密码修改 */
        pwdForm: {
          orgPwd: '',
          newPwd: '',
          checkPwd: ''
        },
        pwdRules: {
          orgPwd: [],
          newPwd: [],
          checkPwd: []
        },
        pwdChangeLoading: false,
        btnDisabled: true,
      }
    },
    components: {
      headTop
    },
    methods: {
      submitForm(formName) { // 确认修改
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.$confirm('确认修改？', '修改确认', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
            }).then(() => { // 确认修改
              this.userInfoFormLoading = true;
              const sessionUserInfo = sessionStorage.getItem('userInfo');
              if(sessionUserInfo.trueName === this.userInfo.trueName &&
                 sessionUserInfo.gender === this.userInfo.gender &&
                 sessionUserInfo.birthday === this.userInfo.birthday &&
                 sessionUserInfo.identity === this.userInfo.identity &&
                 sessionUserInfo.phone === this.userInfo.phone &&
                 sessionUserInfo.email === this.userInfo.email) {
                // 更改后于更改前相同，不需请求后端
              } else {
                // 更改后于更改前相同，请求后端更改数据，更改 sessionStorage 中数据
                const newUserInfo = {
                  sessionID: this.$store.state.userInfo.sessionID,
                  userName: this.$store.state.userInfo.userName,
                  trueName: this.$store.state.userInfo.trueName,
                  gender: this.$store.state.userInfo.gender,
                  birthday: this.$store.state.userInfo.birthday,
                  identity: this.$store.state.userInfo.identity,
                  phone: this.$store.state.userInfo.phone,
                  email: this.$store.state.userInfo.email
                };
                this.$axios.post('/updateUserInfo', newUserInfo).then(response => {
                  const updateUserInfo = response.data;
                  if(updateUserInfo){
                    this.$store.commit('saveUserInfo', updateUserInfo);
                    this.userInfo = this.$store.state.userInfo;
                  } else{
                    confirmLogout();
                  }
                }).catch(error => {

                });
              }
              this.$message('更改成功');
              this.userInfoFormLoading = false;
            }).catch(() => { // 取消修改
              this.$message('已取消删除');
            });
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      /* 头像修改 */
      beforeAvatarUpload(file) {
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
      uploadPhoto(param) {
        let formData = new FormData();
        formData.append("file", param.file);
        formData.append("sessionID", this.userInfo.sessionID);
        formData.append("userName", this.userInfo.userName);

        this.$axios.post('/editPhoto', formData).then(response => {
          const result = response.data; // result: { (bool)login: , (bool)upload: , (String)photoURL: }
          if (result.login) {
            this.userInfo.photoURL = this.$requestURL + result.photoURL + "?random=" + Math.random();
            this.$refs.headTop.updateAvatar(this.userInfo.photoURL);
          } else { // 服务器中已下线
            confirmLogout();
          }
        }).catch(error => {
          console.log(error);
        });
      },
      /* 密码修改 */
      openPwdSubmitBtn() {
        const orgPwd = this.pwdForm.orgPwd;
        const newPwd = this.pwdForm.newPwd;
        const checkPwd = this.pwdForm.checkPwd;

        this.btnDisabled = !(orgPwd.length && newPwd.length && checkPwd.length);
      },
      confirmModifyPwd(formName) {
        const orgPwd = this.pwdForm.orgPwd;
        const newPwd = this.pwdForm.newPwd;
        const checkPwd = this.pwdForm.checkPwd;

        // this.$store.commit('loadUserInfo');
        // console.log(this.$store.state.userInfo.sessionID);
        this.$axios.post('/verifyPwd', {
          sessionID: this.$store.state.userInfo.sessionID,
          userName: this.$store.state.userInfo.userName,
          pwd: orgPwd
        }).then(response => {
          const responseData = response.data;
          if (responseData) { // 服务器中在线
            if (responseData.status === 200) { // pwd 验证正确
              // 对输入密码进行验证
              let newPwdReg = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,20}$/;
              if (newPwdReg.test(newPwd)) { // 新密码格式输入正确
                if (newPwd === orgPwd) {
                  this.$message('新密码必须与旧密码不同');
                  this.pwdForm.newPwd = '';
                  this.pwdForm.checkPwd = '';
                  return;
                }
                // 新密码 和 重新输入密码 检验是否相等
                if (newPwd === checkPwd) { // 相等
                  this.$confirm('确认修改密码？', '密码修改确认提醒', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                  }).then(() => { // 确认修改
                    this.pwdChangeLoading = true;

                    this.$axios.post('/changePwd', {
                      sessionID: this.$store.state.userInfo.sessionID,
                      userName: this.$store.state.userInfo.userName,
                      pwd: newPwd
                    }).then(response => {
                      const responseData = response.data;
                      if (responseData) {
                        this.pwdChangeLoading = false;
                        this.pwdForm.orgPwd = '';
                        this.pwdForm.newPwd = '';
                        this.pwdForm.checkPwd = '';
                        if (responseData.status === 200) {
                          this.$message('密码修改成功');
                        } else {
                          this.$message('密码修改失败');
                        }
                      } else {
                        confirmLogout();
                      }
                    }).catch(error => {
                      console.log(error);
                    });
                  }).catch(() => { // 取消修改
                    this.pwdForm.orgPwd = '';
                    this.pwdForm.newPwd = '';
                    this.pwdForm.checkPwd = '';
                  })
                } else { // 不相等
                  this.pwdForm.checkPwd = '';
                  this.$message('重新输入密码与新密码不同');
                }
              } else {
                this.pwdForm.newPwd = '';
                this.pwdForm.checkPwd = '';
                this.$message('新密码由 6 到 20 位的数字、字母组成');
              }
            } else { // pwd 验证错误 responseData.status === 400
              this.pwdForm.orgPwd = '';
              this.$message('原密码输入错误');
            }
          } else { // 已在服务器下线
            confirmLogout();
          }
        }).catch(error => {
          console.log(error);
        });
      },
      resetPwdForm(formName) {
        this.pwdForm.orgPwd = '';
        this.pwdForm.newPwd = '';
        this.pwdForm.checkPwd = '';
      },
    },
    mounted() {
      this.$store.commit('loadUserInfo');
      this.userInfo = this.$store.state.userInfo;
      this.userInfo.photoURL = this.$requestURL + this.userInfo.photoURL;
    },
  }
</script>

<style scoped>
  .editInfoPage {
    height: 100%;
    overflow: hidden;
  }

  .userInfoBox {
    height: 100%;
    width: 650px;
    margin: 0 auto;
    padding-top: 2%;
  }

  .finalBtn {
    padding-top: 1%;
    text-align: center;
    margin-bottom: 6.5%;
  }

  .textItem {
    padding-top: 2%;
    padding-left: 4%;
  }

  .inputBox {
    width: 280px;
  }

  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }

  .avatar {
    border: 1px dashed #6f7fb4;
    border-radius: 2px;
    width: 178px;
    height: 178px;
    display: block;
  }

  .avatar:hover {
    opacity: 0.5;
    background: #272727;
    filter: alpha(Opacity=50);
    cursor: pointer;
  }

</style>

<style>
  .el-card {
    width: 620px;
  }

  .el-form {
    padding-top: 10px;
  }

  .el-form-item {
    margin: 0;
    padding-bottom: 20px;
  }

  .el-form-item .el-form-item__content {
    margin: 0;
  }

  .el-form-item__label {
    font-size: 16px;
    color: #000000;
    width: 150px;
  }
</style>
