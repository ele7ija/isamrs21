<template>
  <v-dialog
    v-model='$_dialog'
    width='unset'>
    <v-container fluid>
      <v-row>
        <v-col>
          <v-card outlined>
            <v-card-title>
              Pregledi klinike: "{{klinika.naziv}}"
            </v-card-title>
            <v-card-text>
              <v-data-table
                :headers='headers'
                :items='pregledi'>
                <template v-slot:item="row">
                  <tr>
                    <td>{{row.item.id}}</td>
                    <td>{{row.item.tipPregleda.naziv}}</td>
                    <td>{{row.item.lekar.ime}}&nbsp;{{row.item.lekar.prezime}}</td>
                    <td>{{row.item.sala.oznaka}}</td>
                    <td>{{new Date(row.item.pocetakPregleda).getTime() | utcToFormat}}</td>
                    <td>{{formatDate(row.item.krajPregleda)}}</td>
                    <td>
                      <v-btn 
                        @click="rezervisi(row.item)"
                        color='primary'
                        >
                        Rezerviši
                      </v-btn>
                    </td>
                  </tr>
                </template>
              </v-data-table>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </v-dialog>
</template>

<script>
import {mapMutations} from 'vuex';

export default {
  name: 'PreglediDialog',
  props: ['dialog', 'pregledi', 'klinika'],
  data: function(){
    return {
      headers: [
        {
          text: 'ID',
          value: 'id'
        },
        {
          text: 'Tip pregleda',
          value: 'tip pregleda'
        },
        {
          text: 'Lekar',
          value: 'lekar'
        },
        {
          text: 'Sala',
          value: 'sala'
        },
        {
          text: 'Početak',
          value: 'pocetak'
        },
        {
          text: 'Kraj',
          value: 'kraj'
        }
      ],
    }
  },
  computed: {
    $_dialog: {
      get: function() {
        return this.dialog;
      },
      set: function(val) {
        this.$emit('update-dialog', val)
      }
    }
  },
  methods: {
    ...mapMutations('klinike', 
      ['setOdabraniPregled']),
    rezervisi: function(rowitem) {
      this.setOdabraniPregled(rowitem);
      this.$router.push('/pacijent/rezervacija-pregleda')
    },
    formatDate: function(date) {
      let d = new Date(date);
      return ("0" + d.getDate()).slice(-2) + '.' +
        ("0" + d.getMonth()).slice(-2) + '.' +
        d.getFullYear() + '. ' +
        d.getHours() + ':' + ('0' + d.getMinutes()).slice(-2);
    }
  },
  filters: {
    utcToFormat: function(t) {
      var d = new Date(t);
      // var day = d.getDate(); // the day of the month, 19
      // var year = d.getFullYear(); // the year, 2018
      // var month = d.getMonth() + 1; // the month number, zero-indexed, 2 for March
      // var fullDate = month + " " + day + ", " + year;
      //let d = new Date(t);
      return ("0" + d.getDate()).slice(-2) + '.' +
        ("0" + d.getMonth()).slice(-2) + '.' +
        d.getFullYear() + '. ' +
        d.getHours() + ':' + ('0' + d.getMinutes()).slice(-2);
    }
  }
}
</script>

<style>

</style>