<template>
    <index v-if="activeSign === null" v-on:loadSign="onLoadSign"></index>
    <simulator v-bind:sign="activeSign" v-else></simulator>
</template>

<script>
  import Index from './Index.vue';
  import Simulator from './Simulator.vue';

  export default {
    name: 'app',
    components: {
      Index,
      Simulator,
    },
    data() {
      return {
        activeSign: null
      }
    },
    methods: {
      onLoadSign(name) {
        console.log("loading sign: ", name);
        this.activeSign = name;
      }
    }
  }
</script>

<style>
</style>
