<template>
  <table id="bulbs-table">
    <thead>
      <tr>
        <th></th>
        <th colspan="3">Channels</th>
      </tr>
      <tr>
        <th>Id</th>
        <th>R</th>
        <th>G</th>
        <th>B</th>
      </tr>
    </thead>
    <tbody id="bulbs">
      <template v-for="bulb in bulbs">
        <tr v-bind:key="bulb.id">
          <td v-on:click="onChannelsClick(bulb.channels)">{{ bulb.id }}</td>
          <td v-on:click="onChannelsClick([bulb.channels[0]])">{{ bulb.channels[0] }}</td>
          <td v-on:click="onChannelsClick([bulb.channels[1]])">{{ bulb.channels[1] }}</td>
          <td v-on:click="onChannelsClick([bulb.channels[2]])">{{ bulb.channels[2] }}</td>
        </tr>
      </template>
    </tbody>
  </table>
</template>

<script>
import { pushFrame, cancelActiveFrameSource } from "../simulator/sim.js";

export default {
  name: "Bulbs",
  components: {},
  props: {
    bulbs: Array,
  },
  data() {
    return {
    };
  },
  methods: {
    onChannelsClick(channels) {
      cancelActiveFrameSource();

      const frame = new Array(512).fill(0);
      for (const channel of channels) {
        // Channels are 1-indexed, but the frame is 0-indexed
        frame[channel - 1] = 1;
      }
      pushFrame(frame);
    },
  },
};
</script>


<style>
#bulbs-table {
  line-height: normal;
}

#bulbs-table th {
  width: 50px;
  text-align: right;
  cursor: pointer;
}

#bulbs-table td {
  width: 50px;
  text-align: right;
  cursor: pointer;
}

#bulbs-table td:hover {
  background-color: gray;
}
</style>