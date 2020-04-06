<template>
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
</template>

<script>
export default {
  data () {
    return {
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
  },
  methods:{
    navigate(componentName){
      this.$router.push({name: componentName});
    }
  }
}
</script>

<style>
.indent-left{
  margin-left: 0.5rem;
}
.clickable:hover{
  cursor: pointer;
  background-color: #E3F2FD;
}
</style>