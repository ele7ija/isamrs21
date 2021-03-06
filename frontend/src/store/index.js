import Vuex from 'vuex';
import Vue from 'vue';
import tipoviPregleda from './modules/tipoviPregleda';
import sale from './modules/sale';
import pacijenti from './modules/pacijenti';
import upitZaPregled from './modules/upitZaPregled';
import klinike from './modules/klinike';
import opcijeKorisnika from './modules/opcijeKorisnika';
import korisnici from './modules/korisnici';
import osoblje from './modules/osoblje';
import cenovnici from './modules/cenovnici';
import preglediAdmin from './modules/preglediAdmin';
import layout from './modules/layout';
import pregledDialog from './modules/pregledDialog';
import upitiPreglediAdmin from './modules/upitiPreglediAdmin';
import adminiKlinike from './modules/adminiKlinike'
import osobljePacijent from './modules/osobljePacijent';
import salaFilter from './modules/salaFilter';
import createPersistedState from 'vuex-persistedstate'
import zahteviZaRegistraciju from './modules/zahteviZaRegistraciju'
import sifarnik from './modules/sifarnik';
import profil from './modules/profil';
import izvestaji from './modules/izvestaji';
import adminiCentra from './modules/adminiCentra'
import zahteviZaGodisnjiOsoblje from './modules/zahteviZaGodisnjiOsoblje';
import zahteviZaGodisnjiAdmin from './modules/zahteviZaGodisnjiAdmin';
import recepti from './modules/recepti'
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
    cenovnici,
    preglediAdmin,
    layout,
    pregledDialog,
    upitiPreglediAdmin,
    adminiKlinike,
    upitZaPregled,
    osobljePacijent,
    salaFilter,
    zahteviZaRegistraciju,
    sifarnik,
    profil,
    adminiCentra,
    zahteviZaGodisnjiOsoblje,
    zahteviZaGodisnjiAdmin,
    izvestaji,
    recepti
  },

  plugins: [
    createPersistedState({
      paths: ['korisnici', 'layout', 'klinike', 'profil', 'pacijenti', 'tipoviPregleda']
    }),
  ]

})