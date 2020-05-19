<template>
  <div>
    <v-container fluid class="pl-5 pr-5">
      <div
        :key="optionGroup.title" v-for="optionGroup in options(userType)">
        <div v-if="optionGroup.items.length!=0">
          <p class="text-center display-2">{{optionGroup.title}}</p>
          <template>
            <v-divider></v-divider>
          </template>
        </div>
        <div
          class="optionGroup">
          <div
            :key="option.title" v-for="option in optionGroup.items"
            @click="navigate(option.componentName)"
            class="option">
            <p class="headline">{{ option.title }}</p>
            <i class="mdi mdi-location-enter component-icon"></i>
          </div>
        </div>
      </div>
    </v-container>
    <v-dialog v-model="dialog">
      <v-form v-model="isFormValid">
        <v-card>
          <v-card-title class="justify-center headline" max-width="800px">
            Obavezna promena sifre
          </v-card-title>
          <v-card-subtitle>
            Pri prvom logovanju morate da promenite svoju sifru.
          </v-card-subtitle>
          <v-card-text>
            <v-text-field type="password" v-model="sifra" label="Nova sifra" :rules="notEmptyRule('Sifra')"></v-text-field>
            <v-text-field type="password" v-model="ponovljenaNovaSifra" label="Ponovljena nova sifra" :rules="notEmptyRule('Ponovljena Sifra')"></v-text-field>
          </v-card-text>
          <v-card-actions>
            <v-btn
              :disabled="!isFormValid"
              color="success"
              @click="promeni()"
            >
              Promeni
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-form>
    </v-dialog>
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
  </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';
export default {
  name: 'UserDashboard',
  props: [
    'userType'
  ],
  data: function(){
    return{
      isFormValid: false,
      ponovljenaNovaSifra: '',
      success: false,
      snackbar: false,
      snackbarText: null,
      snackbarTimeout: 3000,
    };
  },
  computed: {
    ...mapGetters({
      options:'opcijeKorisnika/getOptions',
      poslednjaPromenaSifre: 'profil/getPoslednjaPromenaSifre',
      copy: 'profil/getCopy'
    }),
    notEmptyRule: () => (property) => {
      const rules = [];
      const rule1 = v => !!v || `${property} mora imati vrednost.`;
      rules.push(rule1);
      return rules;
    },
    sifra: {
      get(){
        return this.copy.sifra;
      },
      set(newValue){
        this.$store.commit("profil/setCopySifra", newValue, {root: true});
      }
    },
    dialog: {
      get(){
        return this.poslednjaPromenaSifre == null
      }
    }
  },
  methods: {
    ...mapActions({
      updateProfil: "profil/updateProfil"
    }),
    navigate(componentName){
      this.$router.push({name: componentName});
    },
    promeni(){
      let staraSifra = this.$store.getters['korisnici/_getKorsinik'].password;
      let novaSifra = this.ponovljenaNovaSifra;
      console.log(staraSifra);
      console.log(novaSifra);
      this.updateProfil({staraSifra, novaSifra}).then(
        (message) => {
          this.snackbarText = message;
          this.snackbar = true;
          this.success = true;
          this.ponovljenaNovaSifra = '';
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
  .optionGroup{
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    grid-gap: 1rem;
    margin-top: 1.5rem;
  }
  .option{
    border: 1px solid #ccc;
    background: #89CFF0;
    padding: 0.3rem;
    border-radius: 5px;
    text-align: center;
    position: relative;
    cursor: pointer;
    display: table;
  }
  .option:hover{
    background-color: #80dfdf;
  }

  .component-icon{
    position: absolute;
    bottom: 5px;
    right: 10px;
  }
</style>