import klinikeAPI from '@/api/klinike';


const state = {
  klinike: [],
  klinikaAdmina: null,
  // Pretraga i sortiranje klinika
  pocetniDatum: null,
  krajnjiDatum: null,
  tipPregleda: null,
  dostupnaSortiranja: [
    {
      naziv: 'ID',
      atr: 'id',
      rastuce: true
    }
  ],
  sortiranja: [],
  // Pregled za rez
  odabranaKlinika: null,
  odabraniPregled: null,
  kreiranaPoseta: null,
  posete: []
}
const internalMethods = {
  sortByKey(array, key){
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
  sortByKeys(array, keys) {
    let new_arr = array;
    for (let key of keys) {
      new_arr = this.sortByKey(new_arr, key);
    }
    return new_arr;
  }
}
const getters = {
  getKlinikaAdmina: (state) => state.klinikaAdmina,
  /** Primenjuje pretrage i sortiranja nad klinikama */
  getPretrazeneKlinike: (state) => {
    return internalMethods.sortByKeys(
      state.klinike,
      state.sortiranja 
    )
  },

  getKlinike(state){
    return state.klinike;
  }
}
const actions = {
  async loadKlinike({commit}) {
    let data = await klinikeAPI.getAllKlinike();
    commit('setKlinike', data);
  },
  async fetchKlinikaUlogovanogKorisnika({commit}){
    let data = await klinikeAPI.fetchKlinikaAdmina();
    commit('setKlinikaAdmina', data);
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
  }
}
const mutations = {
  setKlinike: (state, klinike) =>
    state.klinike = klinike,
  setKlinikaAdmina: (state, klinika) => state.klinikaAdmina = klinika,
  reset: (state) => {
    state.klinike = [];
    state.klinikaAdmina = null
  },
  setPocetniDatum: (state, datum) => state.pocetniDatum = datum,
  setKrajnjiDatum: (state, datum) => state.krajnjiDatum = datum,
  setTipPregleda: (state, tip) => state.tipPregleda = tip,
  setSortiranja: (state, sortiranja) => 
    state.sortiranja = sortiranja,
  setOdabraniPregled: (state, pregled) => 
    state.odabraniPregled = pregled,
  setOdabranaKlinika: (state, klinika) => 
    state.odabranaKlinika = klinika,
  setKreiranaPoseta: (state, poseta) => 
    state.kreiranaPoseta = poseta,
  setPosete: (state, posete) => 
    state.posete = posete
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}