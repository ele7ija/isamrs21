<template>
  <div>
     <v-layout column fill-height>
      <v-list dense nav>
        <v-list-group
          v-for="item in options(userType)"
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
            @click="navigateUrl(item.dashboard_item.path)">
            <v-list-item-action>
              <v-icon>{{item.dashboard_item.action}}</v-icon>
              </v-list-item-action>             
              <v-list-item-content>
              <v-list-item-title v-text="item.dashboard_item.title"></v-list-item-title>
              </v-list-item-content>
          </v-list-item>

          <v-list-item
            v-for="subItem in item.items"
            :key="subItem.title"
            @click="navigate(subItem.componentName)"
          >
              <v-list-item-action>
              <v-icon >{{subItem.action}}</v-icon>
              </v-list-item-action>             
              <v-list-item-content>
              <v-list-item-title v-text="subItem.title"></v-list-item-title>
              </v-list-item-content>
          </v-list-item>
        </v-list-group>
      </v-list>
      <v-btn
        color="error"
        class="indent"
        @click="logout()">
        Logout
      </v-btn>
     </v-layout>
  </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';
export default {
  name: 'OpcijeKorisnika',
  props: [
    'userType'
  ],
  computed: mapGetters({
    'options':'opcijeKorisnika/getOptions'
  }),
  methods: {
    ...mapActions({
      'resetKorisnik': 'korisnici/reset',
      'resetAppLayout': 'layout/reset'
    }),
    navigate(componentName){
      this.$router.push({name: componentName}).catch(err => {err});
    },
    navigateUrl(componentPath){
      this.$router.push({path: componentPath}).catch(err => {err});
    },
    logout(){
      this.resetKorisnik();
      this.resetAppLayout();
      this.$router.push({name: 'login'});
    }
  }
}
</script>

<style>
.indent{
  margin-left: 1rem;
  margin-right: 1rem;
}
</style>