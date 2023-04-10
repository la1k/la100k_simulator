<template>
  <div>
    <template  v-for="playlist in playlists">
      <button type="button" class="btn btn-outline-secondary pgm-button" v-bind:key="playlist.name" v-on:click="play(playlist)">{{ playlist.name }}</button>
    </template>
  </div>
</template>

<script>

import { PGMPlayer } from '../simulator/pgmplayer.js'
import { pushFrame, startFrameSource } from '../simulator/sim.js'

export default {
  name: "Playlists",
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
      playlists: [],
      active_playlist: null,
      playlist_idx: 0,
    };
  },
  methods: {
    play(playlist) {
      this.active_playlist  = playlist;
      this.playlist_idx = 0;
      if (this.active_playlist.pgms.length == 0) {
        return;
      }
      this.play_next();
    },
    play_next() {
      // Select the next PGM
      let pgm_path = this.active_playlist.pgms[this.playlist_idx % this.active_playlist.pgms.length];

      const api_base = "api/signs/" + this.sign;
      let player = new PGMPlayer(api_base + "/pgms/" + pgm_path, pushFrame);

      player.setOnCompleted(() => {
          this.playlist_idx += 1;
          this.play_next();
      });
      startFrameSource(player);
    },
    update() {
      const api_base = "api/signs/" + this.sign;
      fetch(api_base + "/playlists.json").then(response => response.json()).then(data => {
        this.playlists = data;
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