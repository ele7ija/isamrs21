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
            <v-tabs>
              <v-tab>
                Pretraženi pregledi
              </v-tab>
              <v-tab>
                Pretraženi tip pregleda
              </v-tab>
              <v-tab>
                Nepretražene klinike
              </v-tab>
              <v-tab-item>
                <v-container fluid>
                  <v-row
                    v-if='klinike.length!=0' >
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
                  <v-row
                    v-if='klinike.length==0'>
                    <v-col :cols=12>
                        <v-alert type="error">
                          Ne postoji klinika sa odgovarajućim pregledom.
                        </v-alert>
                    </v-col>
                  </v-row>
                </v-container>
              </v-tab-item>
              <v-tab-item>
                <v-container fluid>
                  <v-row>
                    <v-col 
                      v-for='klinika in moguceKlinike'
                      :key='klinika.id'
                      lg=4
                      md=6
                      sm=6>
                      <KlinikaCard v-bind:klinika='klinika'>
                      </KlinikaCard>
                    </v-col>
                  </v-row>
                </v-container>
              </v-tab-item>
              <v-tab-item>
                <v-container fluid>
                  <v-row>
                    <v-col 
                      v-for='klinika in nedostupneKlinike'
                      :key='klinika.id'
                      lg=4
                      md=6
                      sm=6>
                      <KlinikaCard class='disabled' v-bind:klinika='klinika'>
                      </KlinikaCard>
                    </v-col>
                  </v-row>
                </v-container>
              </v-tab-item>
            </v-tabs>

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
      'nedostupneKlinike',
      'moguceKlinike'
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

<style scoped>
  .disabled {
    opacity: 0.5;
  }
  .v-messages {
    display: none;
  }
  .v-text-field__details {
    display: none;
  }
  .v-list-item__content {
    padding-bottom: 0;
  }
</style>
