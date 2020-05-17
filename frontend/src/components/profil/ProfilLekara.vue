<template>
  <v-container>
    <v-card>
      <v-card-title class="justify-center headline">
        Profil lekara
      </v-card-title>
      <v-card-text>
        <v-form v-model="isFormValid">
          <v-row>
            <v-col cols="12" md="6">
              <v-text-field v-model="_copy.ime" label="Ime" :rules="notEmptyRule('Ime')"></v-text-field>
            </v-col>
            <v-col cols="12" md="6">
              <v-text-field v-model="_copy.prezime" label="Prezime" :rules="notEmptyRule('Prezime')"></v-text-field>
            </v-col>
            <v-col cols="12" md="12">
              <v-text-field v-model="_copy.sifra" label="Sifra" :rules="notEmptyRule('Sifra')"></v-text-field>
            </v-col>
            <v-card-actions>
              <v-btn
                :disabled="!isFormValid"
                color="success"
                @click="promeni"
              >
                Promeni
              </v-btn>
              <v-btn
                color="error"
                @click="odustani"
              >
                Odustani
              </v-btn>
            </v-card-actions>
          </v-row>
        </v-form>
      </v-card-text>
    </v-card>
    <v-snackbar
      v-model="snackbar"
      :timeout="snackbarTimeout"
      :color="success ? 'green darken-3' : 'red darken-3'"
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
  </v-container>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';
export default {
  name: "ProfilLekara",
  data: function(){
    return{
      success: false,
      snackbar: false,
      snackbarTimeout: 3000,
      snackbarText: null,
      isFormValid: true,
      sifra: ''
    };
  },
  computed: {
    ...mapGetters({
      profil: "profil/getProfil",
      copy: "profil/getCopy"
    }),
    _copy:{
      get(){
        return this.copy;
      },
      set(newValue){
        this.$store.commit("profil/setCopy", newValue, {root: true});
      }
    },
    notEmptyRule: () => (property) => {
      const rules = [];
      const rule1 = v => !!v || `${property} mora imati vrednost.`;
      rules.push(rule1);
      return rules;
    }
  },
  methods: {
    ...mapActions({
      updateProfil: "profil/updateProfil"
    }),

    odustani(){
      this.$store.commit("profil/_setCopy", JSON.parse(JSON.stringify(this.profil)), {root: true});
    },
    promeni(){
      this.updateProfil().then(
        (message) => {
          this.snackbarText = message;
          this.snackbar = true;
          this.success = true;
        },
        (error) => {
          this.snackbarText = error;
          this.snackbar = true;
          this.success = false;
        }
      );
    }
  }
}
</script>

<style>

</style>