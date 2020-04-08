<template>
  <div>
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
  </div>
</template>

<script>
import {mapGetters} from 'vuex';
export default {
  name: 'OpcijeKorisnika',
  props: [
    'userType'
  ],
  computed: mapGetters({
    'options':'opcijeKorisnika/getOptions'
  }),
  methods: {
    navigate(componentName){
      this.$router.push({name: componentName});
    }
  }
}
</script>

<style>

</style>