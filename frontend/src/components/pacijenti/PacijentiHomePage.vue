<template>
  <v-container fluid>
        <v-row justify='center'>
          <v-col cols=7>
            <UserDashboard :userType='userType'>
            </UserDashboard>
          </v-col>
          <v-col cols=3>
            <v-row>
              <v-col>
                <v-card outlined>
                <v-app-bar 
                  color='primary' dark>
                  <v-toolbar-title>
                    Rezervisani pregledi
                  </v-toolbar-title>
                </v-app-bar>
                <v-list v-if='nerealizovanePosete.length != 0'>
                  <template
                    v-for='(poseta, index) in nerealizovanePosete'>
                    <v-list-item
                      :key='poseta.id' 
                      two-line>
                      <v-list-item-content class='pt-0'>
                      <v-list-item-title>
                        {{poseta.pregled.tipPregleda.naziv}}
                      </v-list-item-title>
                      <v-list-item-subtitle>
                        {{formatDate(poseta.pregled.pocetakPregleda)}} - 
                        {{formatDateTime(poseta.pregled.krajPregleda)}}
                      </v-list-item-subtitle>
                      </v-list-item-content>
                    </v-list-item>
                    <v-divider
                      :key='poseta.id + "d"'
                      v-if='index!=nerealizovanePosete.length-1'>
                    </v-divider>
                  </template>
                </v-list>
                <v-list 
                  v-if='nerealizovanePosete.length==0'>
                  <v-list-item>
                    <v-list-item-content class='pt-0'>
                    <v-list-item-title>
                      Nema rezervisanih pregleda
                    </v-list-item-title>
                    </v-list-item-content>
                  </v-list-item>
                </v-list>
                </v-card>
              </v-col>
            </v-row>
            <!-- <v-row>
              <v-col>
                <v-card outlined>
                  <v-app-bar 
                    color='primary' dark>
                    <v-toolbar-title>
                      Neodobreni upiti
                    </v-toolbar-title>
                  </v-app-bar>
                  <v-list v-if='neodobreniUpiti.length != 0'>
                    <template
                      v-for='(upit, index) in neodobreniUpiti'>
                      <v-list-item
                        :key='upit.id' 
                        two-line>
                        <v-list-item-content class='pt-0'>
                        <v-list-item-title>
                          {{upit.tipPregleda.naziv}}
                        </v-list-item-title>
                        <v-list-item-subtitle>
                          {{formatDate(upit.pocetakPregleda)}} - 
                          {{formatDateTime(upit.krajPregleda)}}
                        </v-list-item-subtitle>
                        </v-list-item-content>
                      </v-list-item>
                      <v-divider
                        :key='upit.id + "d"'
                        v-if='index!=neodobreniUpiti.length-1'>
                      </v-divider>
                    </template>
                  </v-list>
                  <v-list 
                    v-if='neodobreniUpiti.length==0'>
                    <v-list-item>
                      <v-list-item-content class='pt-0'>
                      <v-list-item-title>
                        Nema neodobrenih upita
                      </v-list-item-title>
                      </v-list-item-content>
                    </v-list-item>
                  </v-list>
                </v-card>
              </v-col>
            </v-row> -->
            <v-row>
              <v-col>
                <v-card outlined>
                <v-app-bar 
                  color='primary' dark>
                  <v-toolbar-title>
                    Nepotvrđeni upiti
                  </v-toolbar-title>
                </v-app-bar>
                <v-list v-if='nepotvrdjeniUpiti.length != 0'>
                  <template
                    v-for='(upit, index) in nepotvrdjeniUpiti'>
                    <v-list-item
                      :key='upit.id' 
                      two-line>
                      <v-list-item-content class='pt-0'>
                      <v-list-item-title>
                        {{upit.tipPregleda.naziv}}
                      </v-list-item-title>
                      <v-list-item-subtitle>
                        {{formatDate(upit.pocetakPregleda)}} - 
                        {{formatDateTime(upit.krajPregleda)}}
                      </v-list-item-subtitle>
                      </v-list-item-content>
                    </v-list-item>
                    <v-divider
                      :key='upit.id + "d"'
                      v-if='index!=nepotvrdjeniUpiti.length-1'>
                    </v-divider>
                  </template>
                </v-list>
                <v-list 
                  v-if='nepotvrdjeniUpiti.length==0'>
                  <v-list-item>
                    <v-list-item-content class='pt-0'>
                    <v-list-item-title>
                      Nema nepotvrđenih upita
                    </v-list-item-title>
                    </v-list-item-content>
                  </v-list-item>
                </v-list>
              </v-card>
              </v-col>
            </v-row>
          </v-col>
        </v-row>
      </v-container>
</template>

<script>
import UserDashboard from "@/components/dashboards/UserDashboard";
import {mapState, mapActions} from 'vuex';
export default {
  name: 'PacijentiHomePage',
  props: [
    'userType'
  ],
  components: {
    UserDashboard
  },
  computed: {
    ...mapState('klinike', [
      'nerealizovanePosete'
    ]),
    ...mapState('upitZaPregled', [
      'neodobreniUpiti',
      'nepotvrdjeniUpiti'
    ])
  },
  methods: {
    ...mapActions('klinike', [
      'dobaviNerealizovanePosete'
    ]),
    ...mapActions('upitZaPregled', [
      'dobaviNeodobreneUpite',
      'dobaviNepotvrdjeneUpite'
    ]),
    formatDate: function(date) {
      let d = new Date(date);
      return ("0" + d.getDate()).slice(-2) + '.' +
        ("0" + (d.getMonth()+1)).slice(-2) + '.' +
        d.getFullYear() + '. ' +
        d.getHours() + ':' + ('0' + d.getMinutes()).slice(-2);
    },
    formatDateTime: function(date) {
      let d = new Date(date);
      return d.getHours() + ':' + ('0' + d.getMinutes()).slice(-2);
    },
  },
  created() {
    this.dobaviNeodobreneUpite();
    this.dobaviNepotvrdjeneUpite();
    this.dobaviNerealizovanePosete();
  }
}
</script>

<style>

</style>