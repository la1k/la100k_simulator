<template>
  <div>
    <template  v-for="pgm in pgms">
      <button type="button" class="btn btn-outline-secondary pgm-button" v-bind:key="pgm" v-on:click="play(pgm)">{{ pgm }}</button>
    </template>
  </div>
</template>

<script>

import { PGMPlayer } from '../simulator/pgmplayer.js'
import { pushFrame, startFrameSource } from '../simulator/sim.js'

export default {
  name: "Sequences",
  components: {  },
  props: {
    sign: String,
  },
  created() {
    this.update();
    this.update_timer = setInterval(this.update, 5000);
  },
  destroyed() {
    clearInterval(this.update_timer);
  },
  data() {
    return {
      pgms: []
    };
  },
  methods: {
    play(pgm_path) {
      const api_base = "api/signs/" + this.sign;
      startFrameSource(new PGMPlayer(api_base + "/pgms/" + pgm_path, pushFrame));
    },
    update() {
      const api_base = "api/signs/" + this.sign;
      fetch(api_base + "/pgms.json").then(response => response.json()).then(data => {
        this.pgms = data;
      });
    }
  }
}
</script>


<style>
  .pgm-button {
      margin: 0 0.1em;
  }
</style>