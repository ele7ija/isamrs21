<template>
  <v-container fluid>
    <v-card>
      <v-card-title class="justify-center headline">Izeštaji poslovanja unutar klinike</v-card-title>
      <v-card-text>
        <v-expansion-panels popout v-model="panelIndex">
          <v-expansion-panel
            v-for="(item, i) in items"
            :key="i"
          >
            <v-expansion-panel-header>{{item.title}}</v-expansion-panel-header>
            <v-expansion-panel-content>
              <component v-bind:is="item.componentName" :key="item.key"></component>
            </v-expansion-panel-content>
          </v-expansion-panel>
        </v-expansion-panels>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import ProsecnaOcenaKlinike from "./ProsecnaOcenaKlinike";
import ProsecnaOcenaLekara from "./ProsecnaOcenaLekara";
import StatistikaPregleda from "./StatistikaPregleda";
import StatistikaPrihoda from "./StatistikaPrihoda";
import {mapActions} from 'vuex';
export default{
  name: "Izvestaji",
  components: {
    ProsecnaOcenaKlinike,
    ProsecnaOcenaLekara,
    StatistikaPregleda,
    StatistikaPrihoda
  },
  data: function(){
    return{
      panelIndex: null,
      items: [
        {
          title: "Prosečna ocena klinike",
          componentName: "ProsecnaOcenaKlinike",
          key: 1000,
        },
        {
          title: "Prosečna ocena lekara",
          componentName: "ProsecnaOcenaLekara",
          key: 2000,
        },
        {
          title: "Statistika održanih pregleda",
          componentName: "StatistikaPregleda",
          key: 3000,
        },
        {
          title: "Statistika prihoda klinike",
          componentName: "StatistikaPrihoda",
          key: 4000,
        }
      ]
    };
  },
  watch:{
    panelIndex (newValue){
      if(newValue)
        this.items[newValue].key += 1;
    }
  },
  created(){
    this.fetchKlinikaUlogovanogKorisnika();
  },
  methods:{
    ...mapActions({
      fetchKlinikaUlogovanogKorisnika: "izvestaji/fetchKlinikaUlogovanogKorisnika"
    })
  }

}
</script>