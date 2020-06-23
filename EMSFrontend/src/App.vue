<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
  export default {
    name: 'App',
  }
</script>

<style>
  html {
    height: 100%;
  }

  body {
    background: #f9f9f9;
    height: 100%;
    margin: 0;
  }

  #app {
    height: 100%;
    margin: 0;
    padding: 0;
  }

</style>
