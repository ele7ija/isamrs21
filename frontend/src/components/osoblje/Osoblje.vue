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
                          :items="['lekar', 'medicinska sestra']"
                          label="Pozicija"
                          chips
                          hint="Koja je pozicija novog zaposlenog?"
                          persistent-hint
                        ></v-select>
                      </v-col>
                      <v-col cols="12" sm="6" md="4" v-if="newItem.pozicija == 'lekar' && tipoviPregleda.length != 0">
                        <v-select
                          v-model="newItem.tipovi_pregleda"
                          :items="_tipoviPregleda"
                          label="Specijalnosti"
                          multiple
                          chips
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
        <td :colspan="headers.length">Detaljnije informacije o: {{ item.ime }} {{ item.prezime }}</td>
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
  </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';
export default {
  name: "Osoblje",
  data: function(){
    return {
      dialog: false,
      search: '',
      isFormValid: false,
      expanded: [],
      singleExpand: false,
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
      }
    };
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
    }
  },

  created(){
    this.fetchData();
    this.fetchAllKorisnici();
  },
  methods: {
    ...mapActions(
      {
        fetchData: 'osoblje/loadMedicinskoOsoblje',
        fetchAllKorisnici: 'korisnici/fetchAllKorisnici',
        addMedicinskaOsoba: 'osoblje/addMedicinskaOsoba',
        removeMedicinskaOsoba: 'osoblje/removeMedicinskaOsoba'
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
      this.newItem.klinika = {id: 1};
      this.addMedicinskaOsoba(this.newItem);
      this.close();
    },

    deleteItem(item){
      this.removeMedicinskaOsoba(item.id);
    },

    validateRules(){
      for(let rule of this.emailRules){
        if(rule != true){ //mora ovako
          return true;
        }
      }
      for(let rule of this.notEmptyRule){
        if(rule != true){ //mora ovako
          return true;
        }
      }
      return false;
    }
  }
}
</script>

<style>

</style>