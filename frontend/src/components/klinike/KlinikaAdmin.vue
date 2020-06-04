<template>
  <div>
    <v-container fluid>
      <v-row
        justify="space-between"
      >
        <v-col
          cols="12"
          md="4"
        >
          <v-card class="mx-auto" outlined>
            <v-card-title
              class="headline mb-1 mt-1 justify-center">
              Osnovne informacije
            </v-card-title>
            <v-card-text>
              <v-form v-model="isFormValid">
                <v-text-field
                  v-model="temp_klinika.naziv"
                  :rules="notEmptyRule"
                  label="Naziv klinike"
                ></v-text-field>
                <vuetify-google-autocomplete
                  id='map2'
                  ref='lokacija'
                  :key="locationKey"
                  append-icon='mdi-map-marker'
                  v-bind:disable='true'
                  placeholder="lokacija"
                  :value="_lokacija"
                  v-on:placechanged="getAddressData"
                  :options="{fields: ['geometry', 'address_components', 
                    'formatted_address']}"
                >
                </vuetify-google-autocomplete>
                <v-btn
                  class="mr-4"
                  @click="openLocation"

                >
                  <v-icon>mdi-map-marker</v-icon>
                </v-btn>
                <v-btn
                  :disabled="!isFormValid"
                  color="success"
                  class="mr-4"
                  @click="save"
                >
                  Save
                </v-btn>

                <v-btn
                  color="error"
                  class="mr-4"
                  @click="reset"
                >
                  Reset
                </v-btn>
              </v-form>
            </v-card-text>
          </v-card>
        </v-col>

        <v-col
          cols="12"
          md="8"
        >
          <v-card class="mx-auto" outlined>
            <v-card-title
              class="headline mb-2 mt-2 justify-center">
              Cenovnik klinike: {{klinikaAdmina.naziv}}
            </v-card-title>
            <v-card-text>
              <Cenovnici></Cenovnici>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
    <v-dialog v-model="dialogLocation">
      <v-card height=640>
        <Map :klinika="temp_klinika" :key="mapKey"/>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';
import Cenovnici from '@/components/cenovnici/Cenovnici';
import Map from '../maps/Map';
export default {
  name: 'KlinikaAdmina',
  components: {
    Cenovnici,
    Map
  },
  data: function(){
    return {
      dialogLocation: false,
      mapKey: 0,
      locationKey: 0,
      isFormValid: false,
      temp_klinika: null
    }
  },
  computed: {
    ...mapGetters({
      klinikaAdmina: 'klinike/getKlinikaAdmina',
      pozicija: 'osoblje/getPozicija'
    }),
    notEmptyRule: () => {
      const rules = [];
      const rule1 = v => !!v || `Ovo polje mora imati vrednost.`;
      rules.push(rule1);
      return rules;
    },
    _lokacija(){
      return `${this.klinikaAdmina.lokacija.adresa}, ${this.klinikaAdmina.lokacija.grad}, ${this.klinikaAdmina.lokacija.drzava}`;
    }
  },
  created(){
    this.temp_klinika = JSON.parse(JSON.stringify(this.klinikaAdmina));
  },
  methods: {
    ...mapActions({
      setKlinika: 'klinike/setKlinikaUlogovanogKorisnika',
    }),

    getAddressData(data){
      this.temp_klinika.lokacija.adresa = data.route;
      if(data.street_number)
        this.temp_klinika.lokacija.adresa += " " + data.street_number;
      this.temp_klinika.lokacija.grad = data.locality;
      this.temp_klinika.lokacija.drzava = data.country;
      this.temp_klinika.lokacija.geografskaDuzina = data.longitude;
      this.temp_klinika.lokacija.geografskaSirina = data.latitude;
    },

    reset(){
      this.temp_klinika = JSON.parse(JSON.stringify(this.klinikaAdmina));
      this.$refs.lokacija.clear();
      this.locationKey += 1;
    },
    save(){
      delete this.temp_klinika.medicinskoOsoblje;
      delete this.temp_klinika.cenovnici;
      delete this.temp_klinika.sale;
      delete this.temp_klinika.tipoviPregleda;
      delete this.temp_klinika.pregledi;
      delete this.temp_klinika.administratoriKlinike
      this.setKlinika(this.temp_klinika).then(
        () => {
          this.$refs.lokacija.clear();
          this.locationKey += 1;
        },
        null
      );
    },
    openLocation(){
      this.mapKey += 1;
      this.dialogLocation = true;
    }
  }
}
</script>

<style>

</style>