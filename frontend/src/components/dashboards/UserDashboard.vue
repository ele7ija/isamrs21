<template>
  <div>
    <v-container fluid  class="pa-3 ma-5">
      <div
        :key="optionGroup.title" v-for="optionGroup in options(userType)">
        <div>
          <p class="text-center display-2">{{optionGroup.title}}</p>
          <template>
            <v-divider></v-divider>
          </template>
        </div>
        <div
          class="optionGroup">
          <div
            :key="option.title" v-for="option in optionGroup.items"
            @click="navigate(option.componentName)"
            class="option">
            <p class="headline">{{ option.title }}</p>
            <i class="mdi mdi-location-enter component-icon"></i>
          </div>
        </div>
      </div>
    </v-container>
  </div>
</template>

<script>
import {mapGetters} from 'vuex';
export default {
  name: 'UserDashboard',
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
  .optionGroup{
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    grid-gap: 1rem;
    margin-top: 1.5rem;
  }
  .option{
    border: 1px solid #ccc;
    background: #89CFF0;
    padding: 0.3rem;
    border-radius: 5px;
    text-align: center;
    position: relative;
    cursor: pointer;
    display: table;
  }

  .component-icon{
    position: absolute;
    bottom: 5px;
    right: 10px;
  }
</style>