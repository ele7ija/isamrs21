import Vuex from 'vuex';
import Vue from 'vue';
import tipoviPregleda from './modules/tipoviPregleda';
import pacijenti from './modules/pacijenti';
import opcijeKorisnika from './modules/opcijeKorisnika';
import korisnici from './modules/korisnici';
//Plugins
Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    opcijeKorisnika,
    tipoviPregleda,
    pacijenti,
    korisnici
  },

  // root store
  state: {
    // globalni layout se bavi app-barom i navigation-drawerom
    globalLayout: 'neulogovani-korisnik-layout',
    // id ulogovanog korisnika
    korisnik: null
  },
  mutations: {
    setGlobalLayout (state, payload){
      state.globalLayout = payload;
    },
    setKorisnik (state, payload) {
      state.korisnik = payload;
    }
  }
})