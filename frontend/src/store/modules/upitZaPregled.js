import upitAPI from '@/api/upitZaPregled'
import utility from '@/utility/utility';

const state = {
  kreiranUpit: null,
  neodobreniNeodradjeniUpiti: [],
  neodobreniOdradjeniUpiti: [],
  obradjenNeodobren: null,
  nepotvrdjeniUpiti: [],
  potvrdjenUpit: null
}

const getters = {
  
}
const actions = {
  async kreirajUpit({commit}, obj) {
    //sredjivanje vremenskih zona -> kada se salje na server dodaju se dva sata
    obj.pocetakPregleda = utility.addToDate(new Date(obj.pocetakPregleda));
    obj.krajPregleda = utility.addToDate(new Date(obj.krajPregleda));

    return new Promise((resolve, reject) => {
      upitAPI.kreirajUpit(obj)
      .then(({data: {result, message, success}}) => {
        if (success) {
          commit('setUpit', result);
          resolve(message);
        }
        else {
          reject(message);
        }
      })
    })
  },
  async dobaviNepotvrdjeneUpite({commit}) {
    return new Promise((resolve, reject) => {
      upitAPI.pronadjiNepotvrdjeneUpite()
      .then(({data: {result, message, success}}) => {
        if (success) {
          commit('setNepotvrdjeniUpiti', result);
          resolve(message);
        }
        else {
          reject(message);
        }
      })
    })
  },
  async dobaviNeodobreneNeodradjeneUpite({commit}) {
    return new Promise((resolve, reject) => {
      upitAPI.pronadjiNeodobreneNeodradjeneUpite()
      .then(({data: {result, message, success}}) => {
        if (success) {
          commit('setNeodobreniNeodradjeniUpiti', result);
          resolve(message);
        }
        else {
          reject(message);
        }
      })
    })
  },
  async dobaviNeodobreneOdradjeneUpite({commit}) {
    return new Promise((resolve, reject) => {
      upitAPI.pronadjiNeodobreneOdradjeneUpite()
      .then(({data: {result, message, success}}) => {
        if (success) {
          commit('setNeodobreniOdradjeniUpiti', result);
          resolve(message);
        }
        else {
          reject(message);
        }
      })
    })
  },
  async obradiNeodobreniUpit({commit}, idUpita) {
    return new Promise((resolve, reject) => {
      upitAPI.obradiNeodobreniUpit(idUpita)
      .then(({data: {result, message, success}}) => {
        if (success) {
          commit('setObradjenNeodobren', result);
          resolve(message);
        }
        else {
          reject(message);
        }
      })
    })
  },
  async potvrdiUpit({commit}, {upitId, verzija}){
    console.log(verzija)
    return new Promise((resolve, reject) => {
      upitAPI.potvrdiUpit(upitId, verzija)
      .then(({data: {result, message, success}}) => {
        if (success) {
          commit('setPotvrdjenUpit', result);
          resolve(message);
        }
        else {
          reject(message);
        }
      })
    })
  },
  async odustaniUpit({commit}, {upitId, verzija}){
    return new Promise((resolve, reject) => {
      upitAPI.odustaniUpit(upitId, verzija)
      .then(({data: {result, message, success}}) => {
        if (success) {
          commit('setPotvrdjenUpit', result);
          resolve(message);
        }
        else {
          reject(message);
        }
      })
    })
  }
}
const mutations = {
  setUpit: (state, upit) =>{
    state.kreiranUpit = upit;

    //sredjivanje vremenskih zona -? ono sto procitas sa servera umanji za dva sata
    state.kreiranUpit.pocetakPregleda = utility.handleTimeZone(new Date(state.kreiranUpit.pocetakPregleda));
    state.kreiranUpit.krajPregleda = utility.handleTimeZone(new Date(state.kreiranUpit.krajPregleda));
  },
  setNepotvrdjeniUpiti: (state, upiti) => {
    state.nepotvrdjeniUpiti = upiti;
    for(let upit of state.nepotvrdjeniUpiti){
      upit.pocetakPregleda = utility.handleTimeZone(new Date(upit.pocetakPregleda));
      upit.krajPregleda = utility.handleTimeZone(new Date(upit.krajPregleda));
    }
  },
  setNeodobreniOdradjeniUpiti: (state, upiti) => {
    state.neodobreniOdradjeniUpiti = upiti;
    for(let upit of state.neodobreniOdradjeniUpiti){
      upit.pocetakPregleda = utility.handleTimeZone(new Date(upit.pocetakPregleda));
      upit.krajPregleda = utility.handleTimeZone(new Date(upit.krajPregleda));
    }
  },
  setNeodobreniNeodradjeniUpiti: (state, upiti) => {
    state.neodobreniNeodradjeniUpiti = upiti;
    for(let upit of state.neodobreniNeodradjeniUpiti){
      upit.pocetakPregleda = utility.handleTimeZone(new Date(upit.pocetakPregleda));
      upit.krajPregleda = utility.handleTimeZone(new Date(upit.krajPregleda));
    }
  },
  setObradjenNeodobren: (state, upiti) => {
    state.obradjenNeodobren = upiti
    for(let upit of state.obradjenNeodobren){
      upit.pocetakPregleda = utility.handleTimeZone(new Date(upit.pocetakPregleda));
      upit.krajPregleda = utility.handleTimeZone(new Date(upit.krajPregleda));
    }
  },
  setPotvrdjenUpit: (state, upit) => {
    state.potvrdjenUpit = upit;
    state.potvrdjenUpit.pocetakPregleda = utility.handleTimeZone(new Date(state.potvrdjenUpit.pocetakPregleda));
    state.potvrdjenUpit.krajPregleda = utility.handleTimeZone(new Date(state.potvrdjenUpit.krajPregleda));
  },
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