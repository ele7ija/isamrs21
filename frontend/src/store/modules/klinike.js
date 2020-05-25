import klinikeAPI from '@/api/klinike';
import preglediAPI from '@/api/pregledi'
import utility from '@/utility/utility';

const state = {
  // Main data
  klinike: [],
  pregledi: {},
  klinikaAdmina: null,
  klinikaOsoblja: null,
  // Pretraga i sortiranje klinika
  pocetniDatum: null,
  krajnjiDatum: null,
  odabraniTipPregleda: null,
  dostupnaSortiranja: [
    {
      naziv: 'Naziv',
      atr: 'naziv',
      rastuce: true
    },
    {
      naziv: 'Adresa',
      atr: 'adresa',
      rastuce: true
    }
  ],
  sortiranjeKlinika: null,
  // Pregled za rez
  odabranaKlinika: null,
  odabraniPregled: null,
  kreiranaPoseta: null,
  posete: [],
  nerealizovanePosete: [],
  tipPregleda: null,
}
const internalMethods = {
  sortByKey(array, key){
    if (key == null) {
      return array;
    }

    return array.sort(function(a, b)
    {
      var x = a[key.atr]; var y = b[key.atr];
      if (key.rastuce) {
        return ((x < y) ? -1 : ((x > y) ? 1 : 0));
      }
      else {
        return ((x > y) ? -1 : ((x < y) ? 1 : 0));
      }
    });
  },
  // sortByKeys(array, keys) {
  //   let new_arr = array;
  //   for (let key of keys) {
  //     new_arr = this.sortByKey(new_arr, key);
  //   }
  //   return new_arr;
  // }
}
const getters = {
  getKlinikaAdmina: (state) => state.klinikaAdmina,
  getKlinikaOsoblja: (state) => state.klinikaOsoblja,
  /** Primenjuje pretrage i sortiranja nad klinikama */
  pretrazeneKlinike: (state, getters) => {
    let retval = [];
    for (let klinika of state.klinike) {
      let pretrazeniPregledi = getters.getPretrazeniPregledi(klinika.id);
      if (pretrazeniPregledi.length !== 0) {
        retval.push(klinika);
      }
    }
    return internalMethods.sortByKey(
      retval,
      state.sortiranjeKlinika
    )
  },
  nepretrazeneKlinike: (state, getters) => {
    let retval = [];
    for (let k of state.klinike) {
      let l = getters.pretrazeneKlinike.filter(x => x.id == k.id);
      if (l.length == 0) {
        retval.push(k);
      }
    }
    return retval;
  },
  getKlinike(state){
    return state.klinike;
  },
  getPretrazeniPregledi: (state) => (klinikaId) => {
    let postojeci = state.pregledi[klinikaId];
    if (postojeci == undefined) {
      return [];
    }
    let novi = postojeci.filter((x) => {
      let dp = new Date(x.pocetakPregleda);
      let pd = new Date(state.pocetniDatum);
      if (state.pocetniDatum != null && dp < pd){
        return false;
      }
      let dk = new Date(x.krajPregleda);
      let kd = new Date(state.krajnjiDatum);
      kd.setHours(23); kd.setMinutes(59)
      if (state.krajnjiDatum != null && dk > kd){
        return false;
      }
      if(state.odabraniTipPregleda != null && 
        x.tipPregleda.id !== state.odabraniTipPregleda.id){
        return false;
      }
      return true;
    })
    return novi;
  },
  getPretrazeniPreglediLekara: (state, getters) => ({klinikaId, lekarId}) => {
    return getters.getPretrazeniPregledi(klinikaId).filter((x) => x.lekar.id == lekarId);
  },
  getNepretrazeniPreglediLekara: (state, getters) => ({klinikaId, lekarId}) => {
    let postojeci = state.pregledi[klinikaId];
    if (postojeci == undefined) {
      return [];
    }
    return postojeci.filter(x => {
      // oduzmi one koji su pretrazeni
      for (let pregled of getters.getPretrazeniPreglediLekara({klinikaId: klinikaId, lekarId: lekarId})) {
        if (pregled.id === x.id) {
          return false;
        }
      }
      if (x.lekar.id != lekarId) {
        return false;
      }
      return true
    })
  },
  dostupniTipoviPregleda: (state) => {
    let myset = new Set();
    let retval = [];
    for (let klinika of state.klinike) {
      if (state.pregledi[klinika.id] == undefined) {
        continue;
      }
      for (let pregled of state.pregledi[klinika.id]) {
        if (!myset.has(pregled.tipPregleda.naziv)) {
          retval.push(pregled.tipPregleda);
          myset.add(pregled.tipPregleda.naziv)
        }
        
      }
    }
    return retval;
  }
}
const actions = {
  async loadKlinike({commit}) {
    let data = await klinikeAPI.getAllKlinike();
    commit('setKlinike', data);
  },
  
  async addKlinika({commit}, klinika){
    //response je objekat klinika
    let response = await klinikeAPI.addKlinika(klinika); 
    commit('addKlinika', response);
  },

  async updateKlinikaFromAdminCentra({commit}, klinika){
    let response = await klinikeAPI.updateKlinikaFromAdminCentra(klinika);
    commit('updateKlinikaFromAdminCentra', response);
  },

  //ostale operacije sa klinikama?
  async fetchKlinikaUlogovanogKorisnika({commit}){
    let data = await klinikeAPI.fetchKlinikaAdmina();
    commit('setKlinikaAdmina', data);
    commit('setKlinikaOsoblja', data);
  },
  async setKlinikaUlogovanogKorisnika({commit}, klinika){
    let data = await klinikeAPI.updateKlinika(klinika);
    commit('setKlinikaAdmina', data);
  },
  resetKlinike({commit}){
    commit('reset');
  },
  async kreirajPosetu({commit}, obj) {
    let data = await klinikeAPI.kreirajPosetu(obj);
    commit('setKreiranaPoseta', data);
  },
  async dobaviSvePosete({commit}) {
    let data = await klinikeAPI.dobaviSvePosete();
    commit('setPosete', data);
  },
  async dobaviNerealizovanePosete({commit}) {
    let data = await klinikeAPI.dobaviNerealizovanePosete();
    commit('setNerealizovanePosete', data);
  },
  async loadAllPregledi({commit, state}){
    for (let klinika of state.klinike){
      let data = await preglediAPI.getAllPregledi(klinika.id);
      commit('setPreglediKlinike', {id: klinika.id, data: data})
    }
  },
  async loadPregledi({commit}, klinikaId) {
    let data = await preglediAPI.getAllPregledi(klinikaId);
      commit('setPreglediKlinike', {id: klinikaId, data: data})
  },
  async loadAllSlobodniPregledi({commit, state}){
    for (let klinika of state.klinike){
      let data = await preglediAPI.getSlobodniPregledi(klinika.id);
      commit('setPreglediKlinike', {id: klinika.id, data: data})
    }
  },
  async loadSlobodniPregledi({commit}, klinikaId) {
    let data = await preglediAPI.getSlobodniPregledi(klinikaId);
      commit('setPreglediKlinike', {id: klinikaId, data: data})
  },
  async dobaviPodatkeKlinike({dispatch}) {
    // dobavi klinike
    // dobavi preglede
    await dispatch('loadKlinike');
    await dispatch('loadAllSlobodniPregledi');
  },
  async loadKlinika({commit}, klinikaId){
    let data = await klinikeAPI.getKlinika(klinikaId);
    commit('setKlinike', [data]);
  },
  async dobaviPodatkeKlinikaPage({commit, state, dispatch}, klinikaId) {
    await dispatch('loadKlinika', klinikaId);
    commit('setOdabranaKlinika', state.klinike[0]);
    await dispatch('loadSlobodniPregledi', klinikaId);
  }
}
const mutations = {
  setKlinike: (state, klinike) =>
    state.klinike = klinike,
  addKlinika: (state, klinika) =>
    state.klinike.push(klinika),
    updateKlinikaFromAdminCentra: (state, klinika) => {
    // splice metoda radi isto sto i ===> state.klinike[index] = klinika; 
    //mora ovako kako bi se pravilno izmenilo u vuex store-u
    let index = state.klinike.findIndex(x => x.id == klinika.id);
    state.klinike.splice(index, 1, klinika);
  },
  setKlinikaAdmina: (state, klinika) => state.klinikaAdmina = klinika,
  setKlinikaOsoblja: (state, klinika) => state.klinikaOsoblja = klinika,
  reset: (state) => {
    state.klinike = [];
    state.klinikaAdmina = null
  },
  setPocetniDatum: (state, datum) => state.pocetniDatum = datum,
  setKrajnjiDatum: (state, datum) => state.krajnjiDatum = datum,
  setTipPregleda: (state, tip) => state.tipPregleda = tip,
  setSortiranjeKlinika: (state, sortiranje) => 
    state.sortiranjeKlinika = sortiranje,
  setOdabraniPregled: (state, pregled) => 
    state.odabraniPregled = pregled,
  setOdabranaKlinika: (state, klinika) => 
    state.odabranaKlinika = klinika,
  setKreiranaPoseta: (state, poseta) => 
    state.kreiranaPoseta = poseta,
  setPosete: (state, posete) => {
    state.posete = posete;
    for(let poseta of state.posete){
      poseta.pregled.pocetakPregleda = utility.handleTimeZone(new Date(poseta.pregled.pocetakPregleda));
      poseta.pregled.krajPregleda = utility.handleTimeZone(new Date(poseta.pregled.krajPregleda));
    }
  },
  setPregledi: (state, pregledi) => {
    state.pregledi = pregledi
  },
  setPreglediKlinike: (state, obj) => {
    state.pregledi[obj.id] = obj.data;
    for(let pregled of state.pregledi[obj.id]){
      pregled.pocetakPregleda = utility.handleTimeZone(new Date(pregled.pocetakPregleda));
      pregled.krajPregleda = utility.handleTimeZone(new Date(pregled.krajPregleda));
    }
  },
  setNerealizovanePosete: (state, data) =>
    state.nerealizovanePosete = data,
  setOdabraniTipPregleda: (state, tip) =>
    state.odabraniTipPregleda = tip
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}