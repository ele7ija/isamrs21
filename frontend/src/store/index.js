import Vuex from 'vuex';
import Vue from 'vue';
import tipoviPregleda from './modules/tipoviPregleda';
import sale from './modules/sale';
import pacijenti from './modules/pacijenti';
import klinike from './modules/klinike';
import pregledi from './modules/pregledi';
import opcijeKorisnika from './modules/opcijeKorisnika';
import korisnici from './modules/korisnici';
import osoblje from './modules/osoblje';
import cenovnici from './modules/cenovnici';
import preglediAdmin from './modules/preglediAdmin';
import layout from './modules/layout';
import pregledDialog from './modules/pregledDialog';
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
    pregledi,
    korisnici,
    osoblje,
    cenovnici,
    preglediAdmin,
    layout,
    pregledDialog
  },

  plugins: [
    createPersistedState({
      paths: ['korisnici', 'layout', 'klinike']
    }),
  ]

})