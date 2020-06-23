// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios'
import VueAxios from 'vue-axios'
import store from "./store";
import { verify } from "./utils/verifyPath";
import Print from 'vue-print-nb'

Vue.use(ElementUI);
Vue.use(VueAxios, axios);
Vue.use(Print)

Vue.config.productionTip = false;

axios.defaults.baseURL = 'http://localhost:8082/api';
Vue.prototype.$axios = axios;

Vue.prototype.$requestURL =  'http://localhost:8082';

router.beforeEach((to, from, next) =>{
  if(to.matched.some(record => record.meta.requireAuth)) {
    store.commit('loadUserInfo');
    const userInfo = store.state.userInfo;
    if(userInfo !== null && userInfo.role) {
      const role = userInfo.role;
      let toPath = to.fullPath;
      if(toPath.includes('mgPaySuccess')) {
        toPath = '/mgPaySuccess';
      } else if(toPath.includes('examineePaySuccess')) {
        toPath = '/examineePaySuccess';
      }
      if(verify(role, toPath)){
        next();
      } else{
        if(role === 1) next({ path: '/emsAdmin' });
        else if(role === 2) next({ path: '/emsMg' });
        else if(role === 3) next({ path: '/emsExaminee' })
      }
    } else {
      // console.log('userInfo null');
      next({
        path: '/login',
      });
    }
  } else {
    next();
  }
});

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>',
});
