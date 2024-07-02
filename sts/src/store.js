// src/store.js
import { createStore } from 'vuex';
import createPersistedState from 'vuex-persistedstate';

function initializeStore() {
  const savedState = JSON.parse(window.localStorage.getItem('userAuthState'));
  if (savedState) {
    return Object.assign({}, savedState);
  }
  return {
    isLoggedIn: false,
    userName: ''
  };
}

const store = createStore({
  state: initializeStore(),
  getters: {
    isLoggedIn: state => state.isLoggedIn,
    userName: state => state.userName
  },
  mutations: {
    setIsLoggedIn(state, value) {
      state.isLoggedIn = value;
    },
    setUserName(state, username) {
      state.userName = username;
    },
    logout(state) {
      state.isLoggedIn = false;
      state.userName = '';
    }
  },
  actions: {
    login({ commit }, userCredentials) {
      return new Promise((resolve, reject) => {
        setTimeout(() => {
          const loginSuccess = true; // 这里应该是根据实际响应来判断
          if (loginSuccess) {
            commit('setIsLoggedIn', true);
            commit('setUserName', userCredentials.username);
            resolve();
          } else {
            commit('logout'); // 登录失败，重置状态
            reject(new Error('Login failed'));
          }
        }, 1000);
      });
    },
    logout({ commit }) {
      commit('logout');
      window.localStorage.removeItem('userAuthState');
    }
  },
  modules: {},
  plugins: [createPersistedState({
    key: 'userAuthState',
    paths: ['isLoggedIn', 'userName'],
  })]
});

export default store;