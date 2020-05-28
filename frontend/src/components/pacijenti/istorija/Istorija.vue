<template>
  <v-container fluid>
    <v-row justify='center'>
      <v-col 
        :cols=4
        v-if='nepotvrdjeniUpitiFlag'>
        <v-card outlined>
          <v-app-bar 
            color='primary' dark>
            <v-toolbar-title>
              Nepotvrđeni upiti
            </v-toolbar-title>
            <v-spacer></v-spacer>
            <v-toolbar-title class='caption'>
              Potvrdite da li želite ove preglede
            </v-toolbar-title>
          </v-app-bar>
          <v-list v-if='nepotvrdjeniUpiti.length != 0'>
            <template
              v-for='(upit, index) in nepotvrdjeniUpiti'>
              <v-list-item
                v-if='upit.izmenjeniPregled == null && upit.originalniPregled == null'
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
                <v-list-item-subtitle>
                  {{upit.klinika.adresa}}, {{upit.klinika.grad}},
                  {{upit.klinika.drzava}}
                </v-list-item-subtitle>
                </v-list-item-content>
                <v-list-item-action>
                  <v-list-item-action-text>
                    <v-btn
                      @click='potvrdi(upit.id, upit.version)'
                      color='success'
                      width='150'
                      class='mb-2'>
                      <v-icon>mdi-check</v-icon>
                      Potvrdi
                    </v-btn>
                  </v-list-item-action-text>
                  <v-list-item-action-text>
                    <v-btn
                      @click='odustani(upit.id, upit.version)'
                      color='red'
                      width='150'
                      >
                      <v-icon>mdi-cancel</v-icon>
                      Odustani
                    </v-btn>
                  </v-list-item-action-text>
                </v-list-item-action>
              </v-list-item>
              <v-list-item
                v-if='upit.izmenjeniPregled == null && upit.originalniPregled != null'
                class='izmenjeni'
                :key='upit.id'
                two-line>
               
                <v-list-item-content class='pt-0'>
                  <v-row justify="center">
                  <div class='justify-center overline font-weight-bold pb-1'>
                    IZMENJEN PREGLED
                  </div>
                  </v-row>
                <v-list-item-title>
                  {{upit.tipPregleda.naziv}}
                </v-list-item-title>
                <v-list-item-subtitle>
                  {{formatDate(upit.pocetakPregleda)}} - 
                  {{formatDateTime(upit.krajPregleda)}}
                </v-list-item-subtitle>
                <v-list-item-subtitle>
                  {{upit.klinika.adresa}}, {{upit.klinika.grad}},
                  {{upit.klinika.drzava}}
                </v-list-item-subtitle>
                </v-list-item-content>
                <v-list-item-action>
                  <v-list-item-action-text>
                    <v-btn
                      @click='potvrdi(upit.id, upit.version)'
                      color='success'
                      width='150'
                      class='mb-2'>
                      <v-icon>mdi-check</v-icon>
                      Potvrdi
                    </v-btn>
                  </v-list-item-action-text>
                  <v-list-item-action-text>
                    <v-btn
                      @click='odustani(upit.id, upit.version)'
                      color='red'
                      width='150'
                      >
                      <v-icon>mdi-cancel</v-icon>
                      Odustani
                    </v-btn>
                  </v-list-item-action-text>
                </v-list-item-action>
              </v-list-item>
              <v-divider
                :key='upit.id + "d"'
                v-if='index!=nepotvrdjeniUpiti.length-1'>
              </v-divider>
            </template>
          </v-list>
          <v-list 
            v-if='!nepotvrdjeniUpitiFlag'>
            <v-list-item>
              <v-list-item-content class='pt-0'>
              <v-list-item-title>
                Nema nepotvrdjenih upita
              </v-list-item-title>
              </v-list-item-content>
            </v-list-item>
          </v-list>
        </v-card>
      </v-col>
      <v-col 
        :cols=4
        v-if='neodobreniOdradjeniUpiti.length!==0'>
        <v-card outlined>
          <v-app-bar 
            color='primary' dark>
            <v-toolbar-title>
              Neodobreni upiti
            </v-toolbar-title>
            <v-spacer></v-spacer>
            <v-toolbar-title class='caption'>
              Potvrdite da ste obavešteni o ovome
            </v-toolbar-title>
          </v-app-bar>
          <v-list v-if='neodobreniOdradjeniUpiti.length != 0'>
            <template
              v-for='(upit, index) in neodobreniOdradjeniUpiti'>
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
                <v-list-item-subtitle>
                  {{upit.klinika.adresa}}, {{upit.klinika.grad}},
                  {{upit.klinika.drzava}}
                </v-list-item-subtitle>
                </v-list-item-content>
                <v-list-item-action>
                  <v-list-item-action-text>
                    <v-btn
                      @click='uredu(upit.id)'
                      color='info'
                      width='150'
                      class='mb-2'>
                      U redu
                    </v-btn>
                  </v-list-item-action-text>
                </v-list-item-action>
              </v-list-item>
              <v-divider
                :key='upit.id + "d"'
                v-if='index!=neodobreniOdradjeniUpiti.length-1'>
              </v-divider>
            </template>
          </v-list>
          <v-list 
            v-if='neodobreniOdradjeniUpiti.length==0'>
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
    </v-row>
    <v-row> 
      <v-col :cols=1></v-col>
      <!-- Upiti -->
      <v-col :cols=4>
        <v-row>
          <v-col>
            <v-card outlined>
              <v-app-bar 
                color='primary' dark>
                <v-toolbar-title>
                  Upiti koji čekaju odobrenje
                </v-toolbar-title>
                <v-spacer></v-spacer>
                <v-toolbar-title class='caption'>
                  Admin treba da ih odobri
                </v-toolbar-title>
              </v-app-bar>
              <v-list v-if='neodobreniNeodradjeniUpiti.length != 0'>
                <template
                  v-for='(upit, index) in neodobreniNeodradjeniUpiti'>
                  <v-list-item
                    class='pb-2 pt-3'
                    :key='upit.id' 
                    two-line="">
                    <v-list-item-content class='pt-0'>
                    <v-list-item-title>
                      {{upit.tipPregleda.naziv}}
                    </v-list-item-title>
                    <v-list-item-subtitle>
                      {{formatDate(upit.pocetakPregleda)}} - 
                      {{formatDateTime(upit.krajPregleda)}}
                    </v-list-item-subtitle>
                    <v-list-item-subtitle>
                      {{upit.klinika.adresa}}, {{upit.klinika.grad}},
                      {{upit.klinika.drzava}}
                    </v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                  <v-divider
                    :key='upit.id + "d"'
                    v-if='index!=neodobreniNeodradjeniUpiti.length-1'>
                  </v-divider>
                </template>
              </v-list>
              <v-list 
                v-if='neodobreniNeodradjeniUpiti.length==0'>
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
        </v-row>
        
      </v-col>
      <!-- Posete -->
      <v-col :cols=6>
        <v-row>
          <v-col>
            <v-card outlined>
              <v-app-bar 
                color='green darken-2' dark>
                <v-toolbar-title>
                  Posete klinikama
                </v-toolbar-title>
                <v-spacer></v-spacer>
              </v-app-bar>
              <v-list 
                v-if='posete.length != 0'
                subheader>
                <v-subheader>Buduće</v-subheader>
                <template
                  v-for='poseta in posete'>
                  <v-list-item
                    class='pb-2 pt-2'
                    :key='poseta.id' 
                    two-line=""
                    v-if='!posetaProsla(poseta)'>
                    <v-list-item-content class='pt-0'>
                    <v-list-item-title>
                      {{poseta.pregled.tipPregleda.naziv}}
                    </v-list-item-title>
                    <v-list-item-subtitle>
                      {{formatDate(poseta.pregled.pocetakPregleda)}} - 
                      {{formatDateTime(poseta.pregled.krajPregleda)}}
                    </v-list-item-subtitle>
                    <v-list-item-subtitle>
                      {{poseta.pregled.klinika.adresa}}, 
                      {{poseta.pregled.klinika.grad}},
                      {{poseta.pregled.klinika.drzava}}
                    </v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                </template>
                <v-subheader>Prošle</v-subheader>
                <template
                  v-for='poseta in posete'>
                  <v-list-item
                    class='pb-2 pt-1'
                    :key='poseta.id' 
                    two-line=""
                    v-if='posetaProsla(poseta)'>
                    <v-list-item-content class='pt-0'>
                    <v-list-item-title>
                      {{poseta.pregled.tipPregleda.naziv}}
                    </v-list-item-title>
                    <v-list-item-subtitle>
                      {{formatDate(poseta.pregled.pocetakPregleda)}} - 
                      {{formatDateTime(poseta.pregled.krajPregleda)}}
                    </v-list-item-subtitle>
                    <v-list-item-subtitle>
                      {{poseta.pregled.klinika.adresa}}, 
                      {{poseta.pregled.klinika.grad}},
                      {{poseta.pregled.klinika.drzava}}
                    </v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                </template>
              </v-list>
              <v-list 
                v-if='posete.length==0'>
                <v-list-item>
                  <v-list-item-content class='pt-0'>
                  <v-list-item-title>
                    Nema poseta
                  </v-list-item-title>
                  </v-list-item-content>
                </v-list-item>
              </v-list>
            </v-card>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
    <v-snackbar
      v-model="snackbarErr"
      :timeout="snackbarTimeout"
      color="red darken-3"
    >
      {{ snackbarText }}
      <v-btn
        color="grey darken-3"
        text
        @click="snackbarErr = false"
      >
        Close
      </v-btn>
    </v-snackbar>
    <v-snackbar
      v-model="snackbarSucc"
      :timeout="snackbarTimeout"
      color="green darken-3"
    >
      {{ snackbarText }}
      <v-btn
        color="grey darken-3"
        text
        @click="snackbarSucc = false"
      >
        Close
      </v-btn>
    </v-snackbar>
  </v-container>
</template>

<script>
import { mapActions, mapState } from 'vuex'
export default {
  name: 'Istorija',
  data: function() {
    return {
      snackbarErr: false,
      snackbarSucc: false,
      snackbarTimeout: 2000,
      snackbarText: null,
    }
  },
  computed: {
    ...mapState('klinike', [
      'posete']),
    ...mapState('upitZaPregled', [
      'neodobreniNeodradjeniUpiti',
      'neodobreniOdradjeniUpiti',
      'nepotvrdjeniUpiti']),
      nepotvrdjeniUpitiFlag: function() {
        let flag = false;
        for (let upit of this.nepotvrdjeniUpiti) {
          if (upit.originalniPregled != null || upit.izmenjeniPregled == null) {
            flag = true;
          }
        }
        return flag;
      }
  },
  methods: {
    ...mapActions('klinike', [
      'dobaviSvePosete',
    ]),
    ...mapActions('upitZaPregled', [
      'dobaviNeodobreneOdradjeneUpite',
      'dobaviNeodobreneNeodradjeneUpite',
      'obradiNeodobreniUpit',
      'dobaviNepotvrdjeneUpite',
      'potvrdiUpit',
      'odustaniUpit'
    ]),
    potvrdi: function(upitId, verzija) {
      this.potvrdiUpit({upitId, verzija}).then((message) => {
        this.snackbarText = message;
        this.snackbarSucc = true;
        this.dobaviNepotvrdjeneUpite();
        this.dobaviSvePosete();
      }, (error) => {
        this.snackbarText = error;
        this.snackbarErr = true;
      });
    },
    odustani: function(upitId, verzija) {
      this.odustaniUpit({upitId, verzija}).then((message) => {
        this.snackbarText = message;
        this.snackbarSucc = true;
        this.dobaviNepotvrdjeneUpite();
      }, (error) => {
        this.snackbarText = error;
        this.snackbarErr = true;
      });
    },
    uredu: function(upitId) {
      this.obradiNeodobreniUpit(upitId).then((message) => {
        this.snackbarText = message;
        this.snackbarSucc = true;
        this.dobaviNeodobreneOdradjeneUpite();
      }, (error) => {
        this.snackbarText = error;
        this.snackbarErr = true;
      });
    },
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
    posetaProsla: function(poseta) {
      let date = new Date(poseta.pregled.pocetakPregleda);
      let danas = new Date();
      return date < danas;
    }
  },
  created() {
    this.dobaviSvePosete();
    this.dobaviNeodobreneNeodradjeneUpite();
    this.dobaviNeodobreneOdradjeneUpite();
    this.dobaviNepotvrdjeneUpite();
  }
}
</script>

<style>
  .izmenjeni {
    background-color: rgba(255,0,0,0.2)
  }
</style>