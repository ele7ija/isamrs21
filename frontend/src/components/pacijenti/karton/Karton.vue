<template>
  <v-container fluid>
    <v-row justify="center">
      <v-card v-if='pacijent==null'>
          <v-card-title>
            Pacijent se učitava
            <v-progress-circular
              indeterminate
              color="primary"
            ></v-progress-circular>
          </v-card-title>
        </v-card>
      <v-col :cols=4>
        <v-card v-if='pacijent!=null'>
          <v-card-title
            class='blue lighten-3'>
            Osnovne informacije o pacijentu
          </v-card-title>
          <v-card-text>
            <v-container fluid>
              <v-row>
                <v-col>
                  <v-text-field
                    v-model='pacijent.ime'
                    label='Ime'
                    readonly
                    outlined>
                  </v-text-field>
                </v-col>
                <v-col>
                  <v-text-field
                    v-model='pacijent.prezime'
                    label='Prezime'
                    readonly
                    outlined>
                  </v-text-field>
                </v-col>
              </v-row>
              <v-row>
                <v-col>
                  <v-text-field
                    v-model='pacijent.zdravstveniKarton.visina'
                    label='Visina'
                    suffix='cm'
                    readonly
                    outlined
                    hide-details="">
                  </v-text-field>
                </v-col>
                <v-col>
                  <v-text-field
                    v-model='pacijent.zdravstveniKarton.tezina'
                    label='Tezina'
                    suffix='kg'
                    readonly
                    outlined
                    hide-details="">
                  </v-text-field>
                </v-col>
              </v-row>
              <v-row>
                <v-col>
                  <v-text-field
                    v-model='pacijent.zdravstveniKarton.krvnaGrupa'
                    label='Krvna grupa'
                    readonly
                    outlined
                    hide-details="">
                  </v-text-field>
                </v-col>
              </v-row>
              <v-row>
                <v-col>
                  <v-text-field
                    v-model='pacijent.zdravstveniKarton.dioptrija'
                    label='Dioptrija'
                    readonly
                    outlined>
                  </v-text-field>
                </v-col>
              </v-row>
            </v-container>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col :cols=6>
        <v-card v-if='pacijent!=null'>
          <v-card-title class='green lighten-3'>
            Dijagnoze i lekovi
          </v-card-title>
          <v-card-text>
            <v-container fluid>
              <v-row>
                <v-col :cols=6
                  v-for='poseta in pacijent.zdravstveniKarton.posete'
                    :key='poseta.id'>
                  <v-card
                    outlined>
                    <v-card-title class='subtitle-2'>
                      {{poseta.pregled.tipPregleda.naziv}}
                    </v-card-title>
                    <v-card-subtitle class='caption'>
                      {{formatDate(poseta.pregled.pocetakPregleda)}} - 
                      {{formatTime(poseta.pregled.krajPregleda)}}
                    </v-card-subtitle>
                    <v-card-text>
                      <v-list v-if='poseta.bolest==null'>
                        <v-list-item two-line="">
                          <v-list-item-avatar>
                            <v-icon>
                              mdi-alert
                            </v-icon>
                          </v-list-item-avatar>
                          <v-list-item-content v-if='poseta.bolest==null'>
                            <v-list-item-title>
                              Lekar još nije uneo podatke.
                            </v-list-item-title>
                          </v-list-item-content>
                        </v-list-item>
                      </v-list>
                      <v-list v-if='poseta.bolest!=null'>
                        <v-list-item two-line="">
                          <v-list-item-avatar>
                            <v-icon>
                              mdi-timeline-help
                            </v-icon>
                          </v-list-item-avatar>
                          <v-list-item-content v-if='poseta.bolest==null'>
                            <v-list-item-avatar>
                              mdi-alert
                            </v-list-item-avatar>
                            <v-list-item-title>
                              Lekar još nije uneo podatke o pregledu.
                            </v-list-item-title>
                          </v-list-item-content>
                          <v-list-item-content >
                            <v-list-item-subtitle class='subtitle-2'>
                              Dijagnoza:
                            </v-list-item-subtitle>
                            <v-list-item-title class='subtitle-1 font-weight-medium'>
                              {{poseta.bolest.naziv.charAt(0).toUpperCase() + 
                              poseta.bolest.naziv.slice(1)}}
                            </v-list-item-title>
                          </v-list-item-content>
                        </v-list-item>
                        <v-list-item 
                          v-for='(lek, index) in poseta.lekovi'
                          :key='lek.id'
                          two-line="">
                          <v-list-item-avatar>
                            <v-icon>
                              mdi-bottle-tonic-plus
                            </v-icon>
                          </v-list-item-avatar>
                          <v-list-item-content>
                            <v-list-item-subtitle class='subtitle-2'>
                              Lek {{index+1}}:
                            </v-list-item-subtitle>
                            <v-list-item-title class='subtitle-1 font-weight-medium'>
                              {{lek.naziv.charAt(0).toUpperCase() +
                              lek.naziv.slice(1)}}
                            </v-list-item-title>
                          </v-list-item-content>
                        </v-list-item>
                      </v-list>
                    </v-card-text>
                  </v-card>
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
import pacijentAPI from '@/api/pacijenti';
import { mapState } from 'vuex';

export default {
  name: 'Karton',
  data: function() {
    return {
      pacijent: null,
    }
  },
  computed: {
    ...mapState('korisnici', [
      '_korisnik',
      'korisnik',
    ])
  },
  methods: {
    dobavi: function(email) {
      pacijentAPI.getPacijent(email)
      .then(({data}) => {
        this.pacijent = data;
      })
    },
    formatDate: function(date) {
      let d = new Date(date);
      return ("0" + d.getDate()).slice(-2) + '.' +
        ("0" + (d.getMonth()+1)).slice(-2) + '.' +
        d.getFullYear() + '. ' +
        ('0' + d.getHours()).slice(-2) + ':' + ('0' + d.getMinutes()).slice(-2);
    },
    formatTime: function(date) {
      let d = new Date(date);
      return ('0' + d.getHours()).slice(-2) + ':' + ('0' + d.getMinutes()).slice(-2);
    },
  },
  mounted() {
    this.dobavi(this._korisnik.username)
  }
}
</script>

<style>

</style>