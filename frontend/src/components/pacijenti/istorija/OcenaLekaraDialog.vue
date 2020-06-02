<template>
  <v-dialog
    v-model='$_dialog'
    v-if='lekar!=null'
    width='unset'>
      <v-card outlined>
        <v-card-title>
            Ocenjivanje lekara: "{{lekar.ime + ' ' + lekar.prezime}}"
        </v-card-title>
        <v-card-text>
            <v-rating
              v-model="rating"
              color="yellow darken-3"
              background-color="grey darken-1"
              empty-icon="$ratingEmpty"
              length=10
              hover
            ></v-rating>
        </v-card-text>
        <v-card-actions>
          <v-btn
            :disabled="rating==null"
            color='yellow darken-3'
            @click='posaljiOcenu'>
            Pošalji ocenu: {{rating}}
          </v-btn>
        </v-card-actions>
      </v-card>
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
  </v-dialog>
</template>

<script>
// import {mapMutations, mapGetters} from 'vuex';
import oceneAPI from '@/api/ocene';
import pacijentAPI from '@/api/pacijenti';
import { mapState } from 'vuex';
export default {
  name: 'OcenaLekaraDialog',
  props: ['dialog', 'lekar'],
  data: function(){
    return {
      rating: null,

      snackbarErr: false,
      snackbarSucc: false,
      snackbarTimeout: 2000,
      snackbarText: null,
    }
  },
  computed: {
     ...mapState('korisnici', [
      '_korisnik'
    ]),
    $_dialog: {
      get: function() {
        return this.dialog;
      },
      set: function(val) {
        this.$emit('update-dialog', val)
      }
    },
  },
  methods: {
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
    posaljiOcenu: function() {
      this.lekar.pozicija = 'lekar';
      let ocena = {
        pacijent: {id: this.pacijent.id},
        lekar: {id: this.lekar.id, pozicija: 'lekar'},
        vrednost: this.rating,
        datum: new Date()
      }
      oceneAPI.posaljiOcenu(ocena)
      .then(({data}) => {
        this.snackbarText = "Ocenili ste lekara ocenom: " + data.vrednost;
        this.snackbarSucc = true;
        setTimeout( () => this.$_dialog = false, 1500);
      })
    },
    dobavi: function(email) {
      pacijentAPI.getPacijent(email)
      .then(({data}) => {
        this.pacijent = data;
      })
    },
  },
  mounted() {
    this.dobavi(this._korisnik.username)
  }
}
</script>

<style>

</style>