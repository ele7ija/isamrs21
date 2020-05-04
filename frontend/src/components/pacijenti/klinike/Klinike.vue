<template>
  <v-container fluid>
    <v-row justify="center">
      <v-col lg='3' md='3' sm='6'>
        <PretragaKlinika>
        </PretragaKlinika>
      </v-col>
      <v-col lg=8 md=8 sm=12>
        <v-card outlined>
          <v-card-title>
            Klinike
          </v-card-title>
          <v-card-subtitle>
            Na ovom mestu možete odabrati kliniku u kojoj ćete
            obaviti traženi pregled. 
          </v-card-subtitle>
          <v-card-text>
            <v-container fluid>
              <v-row>
                <v-col 
                  v-for='klinika in klinike'
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
    </v-row>
  </v-container>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'
import KlinikaCard from './KlinikaCard';
import PretragaKlinika from './PretragaKlinika';
export default {
  name: 'Klinike',
  components: {
    KlinikaCard,
    PretragaKlinika
  },
  data: function() {
    return {
    }
  },
  computed: {
    ...mapGetters('klinike', [
      'pretrazeneKlinike',
    ]),
    klinike: function(){ 
      return this.pretrazeneKlinike;
    },
  },
  methods: {
    ...mapActions('klinike', [
      'dobaviPodatkeKlinike'
    ]),
    
  },
  created() {
    this.dobaviPodatkeKlinike();
  }
}
</script>

<style>

</style>