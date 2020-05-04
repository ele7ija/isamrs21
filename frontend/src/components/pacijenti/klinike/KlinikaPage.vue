<template>
  <v-container fluid>
    <v-row justify="center">
      <v-col lg='3' md='3' sm='6'>
        <PretragaKlinike>
        </PretragaKlinike>
      </v-col>
      <v-col cols=5>
        <v-card outlined="">
          <v-img
            gradient="to bottom, rgba(0,0,0,.0), rgba(0,0,0,.3)"
            class='align-end'
            height=250
            :src='odabranaKlinika.slika'>
          </v-img>
          <v-card-title>
            Klinika: "{{odabranaKlinika.naziv}}"
          </v-card-title>
          <v-card-subtitle>
            {{odabranaKlinika.adresa}}, 
            {{odabranaKlinika.grad}},
            {{odabranaKlinika.drzava}}
          </v-card-subtitle>
  
          <v-card-text>
            <v-container class='pa-0'>
              <v-row>
                <v-col>
                  <v-btn
                    color='primary'
                    outlined
                    @click.stop='dialog=true'
                    class='mr-4'>
                    <v-icon left>mdi-calendar</v-icon>
                    Pregledi
                  </v-btn>
                  <PreglediDialog
                    v-bind:dialog='dialog'
                    v-bind:klinika='odabranaKlinika'
                    @update-dialog='dialog=false'>
                  </PreglediDialog>
                  <v-btn
                    outlined
                    @click.stop='dialog=true'>
                    <v-icon left>mdi-account-group</v-icon>
                    Lekari
                  </v-btn>
                </v-col>
              </v-row>
              <v-row>
                <v-col cols=8 class='text-justify'>
                  {{odabranaKlinika.opis}}
                </v-col>
                <v-col cols=4>
                  Ocena: 5
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
import { mapState, mapActions, mapGetters } from 'vuex'
import PretragaKlinike from './PretragaKlinike'
import PreglediDialog from './PreglediDialog'
export default {
  name: 'KlinikaPage',
  props: ['klinikaId'],
  data: function() {
    return {
      dialog: false
    }
  },
  components: {
    PretragaKlinike,
    PreglediDialog
  },
  computed: {
    ...mapState('klinike', [
      'odabranaKlinika'
    ]),
    ...mapGetters('klinike',[
      ''
    ])
  },
  methods: {
    ...mapActions('klinike', [
      'dobaviPodatkeKlinikaPage'
    ]),
    dialogStartup() {
      this.dialog = true;
    }
  },
  created() {
    this.dobaviPodatkeKlinikaPage(this.klinikaId);
  }
}
</script>

<style>

</style>