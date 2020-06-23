import router from '../router'

function confirmLogout() {
  sessionStorage.clear();
  router.replace({ path: '/login' })
}

export {
  confirmLogout
}
