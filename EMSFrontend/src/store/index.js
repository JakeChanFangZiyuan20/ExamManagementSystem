import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    // userInfo { sessionID: '', userName: '', role: '', trueName: '', gender: '', phone: '', photoURL: ''}
    // otherInfo { identity: '', birthday: '', email: '' }
    userInfo: {},
  },
  mutations: {
    saveUserInfo (state, userInfo) {
      state.userInfo = userInfo;
      sessionStorage.setItem('userInfo', JSON.stringify(userInfo));
    },
    loadUserInfo(state) {
      state.userInfo = JSON.parse(sessionStorage.getItem('userInfo'));
    },
  }
})
