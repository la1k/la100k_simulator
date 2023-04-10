<template>
  <div class="container-fluid">
    <login-bar v-on:authChange="updateSigns()"></login-bar>
    <div class="row">
      <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
        <h1>Available Signs:</h1>
        <b-list-group>
          <b-list-group-item href="#" v-if="signs.length == 0" disabled>
            Loading signs...Please wait</b-list-group-item>
          <template v-for="sign in signs">
            <b-list-group-item
              href="#"
              v-bind:key="sign"
              v-on:click="loadSign(sign)"
              >{{ sign.toUpperCase() }}
              </b-list-group-item>
          </template>
        </b-list-group>
      </main>
    </div>
  </div>
</template>

<script>
import LoginBar from "./components/LoginBar.vue";

export default {
  name: "index",
  components: {
    LoginBar,
  },
  created() {
    this.updateSigns();
  },
  data() {
    return {
      signs: [],
    };
  },
  methods: {
    updateSigns() {
      fetch("api/signs.json")
        .then((response) => response.json())
        .then((data) => (this.signs = data));
    },
    loadSign(sign) {
      this.$emit("loadSign", sign);
    },
  },
};
</script>

<style>
</style>
