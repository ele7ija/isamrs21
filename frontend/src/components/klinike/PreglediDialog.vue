<template>
  <v-dialog
    v-model='$_dialog'
    width='unset'>
    <v-container fluid>
      <v-row>
        <v-col>
          <v-card outlined>
            <v-card-title>
              Pregledi klinike: {{klinika.naziv}}
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
                    <td>{{row.item.pocetakPregleda}}</td>
                    <td>{{row.item.krajPregleda}}</td>
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
    rezervisi: function(rowitem) {
      console.log(rowitem)
    }
  }
}
</script>

<style>

</style>