<template>
  <div>
    <OptionToolbar :drawer="drawer"/>
    <v-app-bar
      app
      color="indigo"
      dark
    >
      <v-app-bar-nav-icon @click.stop="drawer = !drawer" />
      <v-toolbar-title>Klinika menadzer</v-toolbar-title>
    </v-app-bar>
    <v-content>
      Bla Bla Bla
    </v-content>
  </div>
</template>

<script>
import OptionToolbar from "./toolbars/OptionToolbar";
export default {
  name: "Home",
  components: {
    OptionToolbar
  },
  data: function(){
    return {
      drawer: false
    }
  }
}
</script>

<style>

</style>