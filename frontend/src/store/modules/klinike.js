import klinikeAPI from '@/api/klinike';
import preglediAPI from '@/api/pregledi'

const state = {
  // Main data
  klinike: [],
  pregledi: {},
  klinikaAdmina: null,
  // Pretraga i sortiranje klinika
  pocetniDatum: null,
  krajnjiDatum: null,
  tipPregleda: null,
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
  // Upiti
  kreiranUpit: null,
  neodobreniUpiti: [],
  nepotvrdjeniUpiti: []
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
  getPretrazeniPregledi: (state) => (klinikaId) => {
    let postojeci = state.pregledi[klinikaId];
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
      return true;
    })
    return novi;
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
  async dobaviPodatkeKlinike({dispatch}) {
    // dobavi klinike
    // dobavi preglede
    await dispatch('loadKlinike');
    await dispatch('loadAllPregledi');
  },
  async loadKlinika({commit}, klinikaId){
    let data = await klinikeAPI.getKlinika(klinikaId);
    commit('setKlinike', [data]);
  },
  async dobaviPodatkeKlinikaPage({commit, state, dispatch}, klinikaId) {
    await dispatch('loadKlinika', klinikaId);
    commit('setOdabranaKlinika', state.klinike[0]);
    await dispatch('loadPregledi', klinikaId);
  },
  async kreirajUpit({commit}, obj) {
    let data = await klinikeAPI.kreirajUpit(obj);
    commit('setUpit', data)
  },
  async dobaviNepotvrdjeneUpite({commit}) {
    let data = await klinikeAPI.dobaviNepotvrdjeneUpite();
    commit('setNepotvrdjeniUpiti', data);
  },
  async dobaviNeodobreneUpite({commit}) {
    let data = await klinikeAPI.dobaviNeodobreneUpite();
    commit('setNeodobreniUpiti', data);
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
  setSortiranjeKlinika: (state, sortiranje) => 
    state.sortiranjeKlinika = sortiranje,
  setOdabraniPregled: (state, pregled) => 
    state.odabraniPregled = pregled,
  setOdabranaKlinika: (state, klinika) => 
    state.odabranaKlinika = klinika,
  setKreiranaPoseta: (state, poseta) => 
    state.kreiranaPoseta = poseta,
  setPosete: (state, posete) => 
    state.posete = posete,
  setPregledi: (state, pregledi) => state.pregledi = pregledi,
  setPreglediKlinike: (state, obj) =>
    state.pregledi[obj.id] = obj.data,
  setNerealizovanePosete: (state, data) =>
    state.nerealizovanePosete = data,
  setUpit: (state, upit) =>
    state.kreiranUpit = upit,
  setNepotvrdjeniUpiti: (state, upiti) =>
    state.nepotvrdjeniUpiti = upiti,
  setNeodobreniUpiti: (state, upiti) =>
    state.neodobreniUpiti = upiti,
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}