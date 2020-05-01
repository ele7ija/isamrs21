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
                <v-text-field
                  v-model="temp_klinika.adresa"
                  :rules="notEmptyRule"
                  label="Adresa klinike"
                ></v-text-field>
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
  </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';
import Cenovnici from '@/components/cenovnici/Cenovnici';
export default {
  name: 'KlinikaAdmina',
  components: {
    Cenovnici
  },
  data: function(){
    return {
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
  },
  created(){
    this.temp_klinika = JSON.parse(JSON.stringify(this.klinikaAdmina));
  },
  methods: {
    ...mapActions({
      setKlinika: 'klinike/setKlinikaUlogovanogKorisnika',
    }),

    reset(){
      this.temp_klinika = JSON.parse(JSON.stringify(this.klinikaAdmina));
    },
    save(){
      delete this.temp_klinika.medicinskoOsoblje;
      delete this.temp_klinika.cenovnici;
      delete this.temp_klinika.sale;
      delete this.temp_klinika.tipoviPregleda;
      delete this.temp_klinika.pregledi;
      delete this.temp_klinika.administratoriKlinike
      this.setKlinika(this.temp_klinika);
      return;
    }
  }
}
</script>

<style>

</style>