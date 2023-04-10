<template>
  <Fragment>
    <div id="content">
        <canvas id="application" style="height: 100%; width: 100%;"></canvas>
    </div>
    <footer>
      <b-tabs content-class="mt-3">
        <b-tab title="Sequences" active>
          <Sequences v-bind:sign="sign"></Sequences>
        </b-tab>
        <b-tab title="Playlists">
          <Playlists v-bind:sign="sign"></Playlists>
        </b-tab>
        <b-tab title="Bulbs">
          <Bulbs v-bind:bulbs="bulbs"></Bulbs>
        </b-tab>
        <b-tab title="Development" lazy>
          <Development></Development>
        </b-tab>
        <b-tab title="Camera Controls">
          <b-button v-on:click="resetCam">Reset!</b-button>
        </b-tab>
      </b-tabs>
    </footer>
  </Fragment>
</template>

<script>
  import { init, loadSign, getBulbs, resetCameraPosition } from './simulator/sim.js';
  import { Fragment } from 'vue-fragment'
  import Sequences from './components/Sequences.vue'
  import Playlists from './components/Playlists.vue'
  import Bulbs from './components/Bulbs.vue'
  import Development from './components/Development.vue'

  export default {
    name: 'simulator',
    components: { 
      Fragment, 
      Sequences, 
      Playlists,
      Bulbs,
      Development,
    },
    props: {
      sign: String
    },
    mounted() {
      const api_base = "api/signs/" + this.sign;

      // create a PlayCanvas application
      const canvas = document.getElementById('application');
      init(canvas);

      loadSign(api_base).then(() => {
        this.bulbs = getBulbs();
      })

    },
    methods: {
      resetCam() {
        resetCameraPosition();
      }
    },
    data() {
      return {
        bulbs: []
      }
    },
  }
</script>

<style>
      html,
  body {
      height: 100%;
  }

  header {
      position: fixed;
      width: 100%;
      top: 0;
      padding: 1rem 0;
  }

  #content {
      padding-bottom: 16em;
      height: 100%;
      width: 100%;
  }

  footer {
      position: fixed;
      height: 16em;
      width: 100%;
      bottom: 0;
      line-height: 3rem;
      overflow-y: auto;
  }
</style>