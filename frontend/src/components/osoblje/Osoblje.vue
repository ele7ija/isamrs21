<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="transformedData"
      :search="search"
      :single-expand="singleExpand"
      :expanded.sync="expanded"
      item-key="email"
      show-expand
      class="elevation-1"
      >
      <template v-slot:top>
        <v-toolbar flat color="white">
          <v-toolbar-title>Medicinsko osoblje</v-toolbar-title>
          <v-divider
            class="mx-4"
            inset
            vertical
          ></v-divider>
          <v-spacer></v-spacer>
          
          <v-text-field
            v-model="search"
            append-icon="mdi-magnify"
            label="Search"
            single-line
            hide-details
          ></v-text-field>
          <v-divider
            class="mx-4"
            inset
            vertical
          ></v-divider>
          <v-spacer></v-spacer>
          
          <v-dialog v-model="dialog" max-width="500px">
            <template v-slot:activator="{ on }">
              <v-btn color="primary" dark class="mb-2" v-on="on">Dodaj</v-btn>
            </template>
            <v-card>
              <v-card-title>
                <span class="headline">{{formTitle}}</span>
              </v-card-title>
              <hr>
              <v-form v-model="isFormValid">
                <v-card-text>
                  <v-container>
                    <v-row>
                      <v-col cols="12" sm="6" md="4">
                        <v-text-field v-model="newItem.ime" label="Ime" :rules="notEmptyRule('Ime')"></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6" md="4">
                        <v-text-field v-model="newItem.prezime" label="Prezime" :rules="notEmptyRule('Prezime')"></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6" md="4">
                        <v-text-field v-model="newItem.email" label="Email" :rules="emailRules"></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6" md="4">
                        <v-text-field v-model="newItem.sifra" label="Sifra" :rules="notEmptyRule('Sifra')"></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6" md="4">
                        <v-select
                          v-model="newItem.radniKalendar.dnevnoRadnoVremeSati"
                          :items="[4,5,6,7,8]"
                          label="Radno vreme"
                          chips
                          hint="Koje je dnevno radno vreme u satima?"
                          persistent-hint
                        ></v-select>
                      </v-col>
                      <v-col cols="12" sm="6" md="4">
                        <v-select
                          v-model="newItem.pozicija"
                          :items="['lekar', 'sestra']"
                          label="Pozicija"
                          chips
                          hint="Koja je pozicija novog zaposlenog?"
                          persistent-hint
                        ></v-select>
                      </v-col>
                      <v-col cols="12" sm="12" md="12" v-if="newItem.pozicija == 'lekar' && tipoviPregleda.length != 0">
                        <v-select
                          v-model="newItem.tipovi_pregleda"
                          :items="_tipoviPregleda"
                          label="Specijalnosti"
                          multiple
                          chips
                          deletable-chips
                          hint="Koje su specijalnosti lekara?"
                          persistent-hint
                        ></v-select>
                      </v-col>
                    </v-row>
                  </v-container>
                </v-card-text>

                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="blue darken-1" text @click="close">Cancel</v-btn>
                  <v-btn color="blue darken-1" text @click="save" :disabled="!isFormValid">Save</v-btn>
                </v-card-actions>
              </v-form>
            </v-card>
          </v-dialog>
        </v-toolbar>
      </template>
      <template v-slot:expanded-item="{ headers, item }">
        <td :colspan="headers.length">
          <v-container fluid v-if="item.pozicija == 'lekar'">
            <v-row justify="space-between">
              <v-col cols="12" md="4">
                <v-card>
                  <v-card-title>Specijalnosti za: {{ item.ime }} {{ item.prezime }}</v-card-title>
                  <v-divider class="mt-n2 mb-2"></v-divider>
                  <v-card-text>
                    <span
                      v-for="tip in item.tipovi_pregleda"
                      :key="tip.id">
                      <v-chip
                        class="ma-2"
                        color="primary"
                        close
                        @click:close="removeFromSpecijalnost(item, tip)"
                      >
                        {{tip.naziv}}
                      </v-chip>
                    </span>
                    <v-container fluid>
                      <v-row>
                        <v-col cols="12" md="8">
                          <v-select
                            v-model="specijalnostiZaDodati"
                            :items="_slobodniTipoviPregleda({lekar: item, tipoviPregleda: tipoviPregleda})"
                            label="Specijalnosti"
                            multiple
                            chips
                            deletable-chips
                            hint="Koje su nove specijalnosti lekara?"
                            persistent-hint
                          ></v-select>
                        </v-col>
                        <v-col cols="12" md="2">
                          <v-btn 
                            color="blue darken-1"
                              class="mt-6"
                            :disabled="specijalnostiZaDodati.length == 0"
                            @click="addSpecijalnosti(item, specijalnostiZaDodati)">
                            Dodaj
                          </v-btn>
                        </v-col>
                      </v-row>
                    </v-container>
                  </v-card-text>
                </v-card>
              </v-col>
              <v-col cols="12" md="8">
                <v-card>
                  <v-card-title>Prelgedi za: {{ item.ime }} {{ item.prezime }}</v-card-title>
                  <v-divider class="mt-n2 mb-2"></v-divider>
                  <v-card-text>
                    <PreglediLekar :idLekara="item.id"></PreglediLekar>
                  </v-card-text>
                </v-card>
              </v-col>
            </v-row>
          </v-container>
        </td>
      </template>
      <template v-slot:item.actions="{ item }">
        <v-icon
          small
          @click="deleteItem(item)"
        >
          mdi-delete
        </v-icon>
      </template>
    </v-data-table>
    <v-snackbar
      v-model="snackbar"
      :timeout="snackbarTimeout"
      color="red darken-3"
    >
      {{ snackbarText }}
      <v-btn
        color="grey darken-3"
        text
        @click="snackbar = false"
      >
        Close
      </v-btn>
    </v-snackbar>
  </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';
import PreglediLekar from './PreglediLekar';
export default {
  name: "Osoblje",
  components: {
    PreglediLekar
  },
  data: function(){
    return {
      snackbar: false,
      snackbarTimeout: 3000,
      snackbarText: null,
      dialog: false,
      search: '',
      isFormValid: false,
      expanded: [],
      singleExpand: true,
      formTitle: "Dodavanje novog člana medicinskog osoblja",
      headers: [
        {
          text: 'Ime',
          value: 'ime',
          sortable: true,

        },
        {
          text: 'Prezime',
          value: 'prezime',
          sortable: false
        },
        {
          text: 'E-mail',
          value: 'email',
          sortable: true,
        },
        {
          text: 'Pozicija',
          value: 'pozicija',
          sortable: false
        },
        {
          text: 'Dnevno radno vreme u satima',
          value: 'radniKalendar.dnevnoRadnoVremeSati',
          sortable: true
        },
        {
          text: 'Aktiviran',
          value: 'aktiviran',
          sortable: true,
        },
        { 
          text: 'Akcije',
          value: 'actions',
          sortable: false,
        },
        {
          text: 'Detalji',
          value: 'data-table-expand'
        }
      ],
      newItem: {
        ime: '',
        prezime: '',
        email: '',
        sifra: '',
        pozicija: '',
        tipovi_pregleda: [],
        radniKalendar: {
          dnevnoRadnoVremeSati: 8
        }
      },
      specijalnostiZaDodati: []
    };
  },
  watch: {
    expanded: function () {
      this.specijalnostiZaDodati = [];
    },
  },
  computed: {
    ...mapGetters(
      {
        getAll: 'osoblje/getMedicinskoOsoblje',
        get: 'osoblje/getMedicinskaOsoba',
        tipoviPregleda: 'tipoviPregleda/getTipoviPregleda',
        korisnici: 'korisnici/getKorisnici'
      },
    ),
    _tipoviPregleda(){
      return this.tipoviPregleda.map(x => {
        return{
          text: x.naziv,
          value: x
        };
      });
    },
    transformedData: function(){
      let data = [];
      for(let osoblje of this.getAll){
        osoblje.aktiviran = osoblje.poslednjaPromenaSifre != null ? 'da': 'ne';
        data.push(osoblje);
      }
      return data;
    },

    notEmptyRule: () => (property) => {
      const rules = [];
      const rule1 = v => !!v || `${property} mora imati vrednost.`;
      rules.push(rule1);
      return rules;
    },

    emailRules: function(){
      const rules = [];
      const rule1 = v => !!v || 'Email ne sme ostati prazan';
      rules.push(rule1);

      const rule2 = v => this.korisnici.findIndex(x => x.email == v) == -1 || 'Email mora biti jedinstven';
      rules.push(rule2);
      return rules;
    },

    _slobodniTipoviPregleda: () => ({lekar, tipoviPregleda}) => {
      let temp = tipoviPregleda.filter(x => lekar.tipovi_pregleda.filter(y => y.id == x.id).length == 0);
      return temp.map(x => {
        return{
          text: x.naziv,
          value: x
        };
      });
    }
  },

  created(){
    this.fetchData();
    this.fetchTipoviTregleda();
    this.fetchAllKorisnici();
  },
  methods: {
    ...mapActions(
      {
        fetchData: 'osoblje/loadMedicinskoOsoblje',
        fetchTipoviTregleda: 'tipoviPregleda/loadTipoviPregleda',
        fetchAllKorisnici: 'korisnici/fetchAllKorisnici',
        addMedicinskaOsoba: 'osoblje/addMedicinskaOsoba',
        addSpecijalnostiMedicinskaOsoba: 'osoblje/addSpecijalnostiMedicinskaOsoba',
        removeSpecijalnostiMedicinskaOsoba: 'osoblje/removeSpecijalnostiMedicinskaOsoba',
        removeMedicinskaOsoba: 'osoblje/removeMedicinskaOsoba',
      }
    ),

    resetNewItem(){
      this.newItem = {
        ime: '',
        prezime: '',
        email: '',
        sifra: '',
        pozicija: '',
        tipoviPregleda: [],
        radniKalendar: {
          dnevnoRadnoVremeSati: 8
        }
      };
    },

    close(){
      this.resetNewItem();
      this.dialog = false;
    },

    save(){
      this.newItem.pozicija = this.newItem.pozicija == 'sestra' ? 'medicinska sestra' : 'lekar';
      this.newItem.tipovi_pregleda = this.newItem.tipovi_pregleda.map(x => {return {id: x.id}});
      this.addMedicinskaOsoba(this.newItem);
      this.close();
    },

    deleteItem(item){
      this.removeMedicinskaOsoba(item.id).then(null, (error) => {
        this.snackbarText = error;
        this.snackbar = true;
      });
    },

    addSpecijalnosti(lekar, tipoviPregleda){
      let idLekara = lekar.id;
      let idTipovaPregleda = tipoviPregleda.map(x => x.id);
      let version = lekar.version;
      this.addSpecijalnostiMedicinskaOsoba({idLekara, idTipovaPregleda, version}).then(null, (error) => {
        this.snackbarText = error;
        this.snackbar = true;
      });
      this.specijalnostiZaDodati = [];
    },

    removeFromSpecijalnost(lekar, tipPregleda){
      let idLekara = lekar.id;
      let idTipaPregleda = tipPregleda.id;
      let version = lekar.version;
      this.removeSpecijalnostiMedicinskaOsoba({idLekara, idTipaPregleda, version}).then(null, (error) => {
        this.snackbarText = error;
        this.snackbar = true;
      });
    }
    
  }
}
</script>

<style>
.container1{
  display: grid;
  grid-template-columns: repeat(3, 1fr);
}
.container1-element{
  display: table;
}
</style>