<template>
  <div id="dcontainer">
    <b-button :variant="connected ? 'success' : 'warning'" disabled>
      <b-spinner small type="grow" v-if="connected == false"></b-spinner>
      {{ connected ? "Connected" : "Connecting..." }}
    </b-button>
  </div>
</template>

<script>
import { pushFrame, cancelActiveFrameSource } from "../simulator/sim.js";
import ReconnectingWebSocket from 'reconnecting-websocket';

export default {
  name: "Development",
  components: {},
  props: {
    bulbs: Array,
  },
  data() {
    return {
      connected: false,
      connection: null,
    };
  },
  mounted() {
    console.log("Loaded development tab!");
    cancelActiveFrameSource();
    this.connect();
  },
  destroyed() {
    console.log("unmounted");
    if (this.connection) {
      this.connection.close();
    }
  },
  methods: {
    connect() {
      this.connection = new ReconnectingWebSocket("ws://localhost:5678/");
      this.connection.binaryType = "arraybuffer";
      this.connection.onopen = (event) => {
        console.log("opened");
        this.connected = true;
      };
      this.connection.onclose = (event) => {
        console.log("closed");
        this.connected = false;
        this.connection = null;
      };
      this.connection.onmessage = (event) => {
        var dv = new Uint8Array(event.data);
        const frame = new Array(512).fill(0.0);
        dv.forEach((v, i) => {
          frame[i] = v/255.0;
        })

        pushFrame(frame);
      };   
    },
  },
};
</script>


<style>
#dcontainer {
  padding-left: 4em;
}
</style>