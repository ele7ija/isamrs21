<template>
  <v-container fluid>
    <v-row justify="center">
      <v-col lg=8 md=8 sm=12>
        <v-card outlined>
          <v-card-title>
            Klinike
          </v-card-title>
          <v-card-subtitle>
            Na ovom mestu možete videti sve klinike koje
            pripadaju našem kliničkom centru.
          </v-card-subtitle>
          <v-card-text>
            <v-container fluid>
              <v-row>
                <v-col 
                  v-for='klinika in sortedKlinike'
                  :key='klinika.id'
                  lg=4
                  md=6
                  sm=6>
                  <KlinikaCard v-bind:klinika='klinika'>
                  </KlinikaCard>
                </v-col>
              </v-row>
            </v-container>
            
          </v-card-text>
        </v-card>
      </v-col>
      <v-col lg='3' md='3' sm='6'>
        <v-card outlined>
          <v-card-text>
            <v-select
              v-model='chosenSort'
              :items='availableSorts'
              label='Sortiraj po'>
              <template v-slot:selection="{ item }">
                <v-chip color='orange'>
                  <span>{{ item }}</span>
                </v-chip>
              </template>
            </v-select>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import KlinikaCard from './KlinikaCard';
export default {
  name: 'Klinike',
  components: {
    KlinikaCard
  },
  data: function() {
    return {
      chosenSort: 'id',
      availableSorts: ['id'],
      headers: [
        {
          text: 'ID',
          value: 'id'
        }
      ]
    }
  },
  computed: {
    ...mapState('klinike', [
      'klinike'
    ]),
    sortedKlinike: function() {
      return this.sortByKey(this.klinike, this.chosenSort);
    },
  },
  methods: {
    ...mapActions('klinike', [
      'loadKlinike'
    ]),
    sortByKey(array, key){
      return array.sort(function(a, b)
      {
        var x = a[key]; var y = b[key];
        return ((x < y) ? -1 : ((x > y) ? 1 : 0));
      });
    }
  },
  created() {
    this.loadKlinike();
    console.log(this.klinike);
  }
}
</script>

<style>

</style>