<template>
  <v-app>
    <v-navigation-drawer
      v-model="drawer"
      app>
      <v-list dense nav>
        <v-list-group
        v-for="item in items"
        :key="item.title"
        >
        <template v-slot:activator>
            <v-list-item-action>
            <v-icon>{{item.action}}</v-icon>
            </v-list-item-action>
            <v-list-item-content>
            <v-list-item-title v-text="item.title"></v-list-item-title>
            </v-list-item-content>
        </template>
          <v-list-item
            v-for="subItem in item.items"
            :key="subItem.title"
            class="clickable"
            @click="navigate(subItem.componentName)"
          >
              <v-list-item-action>
              <v-icon class="indent-left">{{subItem.action}}</v-icon>
              </v-list-item-action>             
              <v-list-item-content>
              <v-list-item-title v-text="subItem.title"></v-list-item-title>
              </v-list-item-content>
          </v-list-item>
        </v-list-group>
      </v-list>
      
      <template v-slot:append>
        <div class="pa-2">
          <v-btn block dark>Logout</v-btn>
        </div>
      </template>
    </v-navigation-drawer>
      <v-app-bar
      app
      color="indigo"
      dark
    >
        <v-app-bar-nav-icon @click.stop="drawer = !drawer" />
        <v-toolbar-title>Klinika menadzer</v-toolbar-title>
      </v-app-bar>
    <v-content>
      <router-view></router-view>
    </v-content>
  </v-app>
</template>

<script>
export default {
  name: "AdminKlinikeLayout",
  components: {
  },
  data: function(){
    return {
      drawer: false,
      items: [
        {
          title: 'Klinika',
          action: 'mdi-hospital-building',
          items: [
            { 
              title: 'Tipovi pregleda',
              action: 'mdi-hospital',
              componentName: 'tipovi_pregleda'
            }
          ]
        }
      ]
    }
  }
}
</script>

<style>

</style>