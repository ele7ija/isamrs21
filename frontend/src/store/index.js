import Vuex from 'vuex';
import Vue from 'vue';
import tipoviPregleda from './modules/tipoviPregleda';
import pacijenti from './modules/pacijenti';
import opcijeKorisnika from './modules/opcijeKorisnika'
//Plugins
Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    opcijeKorisnika,
    tipoviPregleda,
    pacijenti
  },

  // root store
  state: {
    globalLayout: 'neulogovani-korisnik-layout'
  },
  mutations: {
    setGlobalLayout (state, payload){
      state.globalLayout = payload;
    }
  }
})