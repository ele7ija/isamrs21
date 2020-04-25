import Vuex from 'vuex';
import Vue from 'vue';
import tipoviPregleda from './modules/tipoviPregleda';
import sale from './modules/sale';
import pacijenti from './modules/pacijenti';
import klinike from './modules/klinike';
import opcijeKorisnika from './modules/opcijeKorisnika';
import korisnici from './modules/korisnici';
import osoblje from './modules/osoblje';
import layout from './modules/layout';
import createPersistedState from 'vuex-persistedstate'

//Plugins
Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    opcijeKorisnika,
    tipoviPregleda,
    sale,
    pacijenti,
    klinike,
    korisnici,
    osoblje,
    layout
  },

  plugins: [
    createPersistedState({
      paths: ['korisnici', 'layout']
    }),
  ]

})