import Vuex from 'vuex';
import Vue from 'vue';
import tipoviPregleda from './modules/tipoviPregleda';
import sale from './modules/sale';
import pacijenti from './modules/pacijenti';
import opcijeKorisnika from './modules/opcijeKorisnika';
import korisnici from './modules/korisnici';

//Plugins
Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    opcijeKorisnika,
    tipoviPregleda,
    sale,
    pacijenti,
    korisnici
  },

  // root store
  state: {
    globalLayout: 'neulogovani-korisnik-layout',
  },
  mutations: {
    setGlobalLayout (state, payload){
      state.globalLayout = payload;
    }
  },
  actions: {
  }
})