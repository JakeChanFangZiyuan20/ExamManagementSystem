<template>
  <div class="loginPage">

    <div class="pageHeader">
      <h1>EMS</h1>
    </div>

    <div class="main">
      <div class="loginError" v-show="showLoginError">
        <p>INCORRECT Username or Password</p>
      </div>
      <div class="loginBox" v-loading="loginBoxLoading">
        <el-form class="loginForm" :model="loginForm" :rules="rules" ref="ruleForm">
          <!-- 账号 -->
          <label for="userName">Username</label>
          <el-form-item prop="userName">
            <el-input id="userName" type="text" v-model="loginForm.userName" prefix-icon="el-icon-user"
                      class="inputBlock"></el-input>
          </el-form-item>

          <!-- 密码 -->
          <label for="password">Password</label>
          <el-form-item prop="password">
            <el-input id="password" type="password" v-model="loginForm.password" prefix-icon="el-icon-lock"
                      class="inputBlock" @keyup.enter.native="submitForm('ruleForm')"></el-input>
          </el-form-item>

          <!-- 登录按钮 -->
          <el-form-item>
            <el-button type="primary" class="loginBtn" @click="submitForm('ruleForm')">Sign in</el-button>
          </el-form-item>

        </el-form>
      </div>
    </div>

    <div class="pageFooter">

    </div>
  </div>
</template>

<script>
  export default {
    name: "login",
    data() {
      return {
        showLoginError: false,
        loginBoxLoading: false,
        loginForm: {
          userName: '',
          password: ''
        },
        rules: {
          userName: [
            { required: true, message: 'Please Input Username', trigger: 'blur' }
          ],
          password: [
            { required: true, message: 'Please Input Password', trigger: 'blur' },
            {min: 6, max: 20, message: 'password length between 6 and 20', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      changeLoginStatus() {
        this.showLoginError = true;
      },
      submitForm(formName) { // process login
        this.$refs[formName].validate((valid) => {
          if (valid) { // 登录表单验证
            this.loginBoxLoading = true;
            // send data to back end for verification
            this.$axios.post('/login', {
              sessionID: window.sessionStorage.getItem('sessionID'),
              userName: this.loginForm.userName,
              password: this.loginForm.password
            }).then(successRequest => {
              let userInfo = successRequest.data;
              if(userInfo.length === 0){
                this.loginForm.password = '';
                this.loginBoxLoading = false;
                this.changeLoginStatus();
                return false;
              } else {
                const userInfo = successRequest.data;
                const role = userInfo.role;
                var nextPath = '';
                this.$store.commit('saveUserInfo', userInfo);
                if(role === 1) {
                  nextPath = '/emsAdmin';
                } else if (role === 2) {
                  nextPath = '/emsMg';
                } else if (role === 3) {
                  nextPath = '/emsExaminee';
                }
                this.$router.replace({ path: nextPath });
                this.loginBoxLoading = false;
                this.$message({
                  type: 'success',
                  message: '登录成功'
                });
              }
            }).catch(errorRequest => {
              console.log('LoginException');
            });
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      }
    }
  }
</script>

<style scoped>
  .pageHeader {
    padding-top: 3%;
    padding-bottom: 1%;
    text-align: center;
    font-size: 30px;
  }

  .loginBox {
    padding: 20px;
    background-color: #fff;
    border: 1px solid #d8dee2;
    border-radius: 5px;
    margin: 0 auto;
    width: 280px;
  }

  .inputBlock {
    font-size: 16px;
    width: 280px;
  }

  .pageFooter {
    padding-top: 1%;
    padding-bottom: 3%;
  }

  .loginBtn {
    width: 100%;
  }

  .loginError{
    color: red;
    text-align: center;
    font-size: 10px;
  }
</style>
