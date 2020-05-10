import upitAPI from '@/api/upitZaPregled'

const state = {
  kreiranUpit: null,
  neodobreniUpiti: [],
  nepotvrdjeniUpiti: [],
  potvrdjenUpit: null
}

const getters = {
  
}
const actions = {
  async kreirajUpit({commit}, obj) {
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
  async dobaviNeodobreneUpite({commit}) {
    return new Promise((resolve, reject) => {
      upitAPI.pronadjiNeodobreneUpite()
      .then(({data: {result, message, success}}) => {
        if (success) {
          commit('setNeodobreniUpiti', result);
          resolve(message);
        }
        else {
          reject(message);
        }
      })
    })
  },
  async potvrdiUpit({commit}, upitId){
    return new Promise((resolve, reject) => {
      upitAPI.potvrdiUpit(upitId)
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
  async odustaniUpit({commit}, upitId){
    return new Promise((resolve, reject) => {
      upitAPI.odustaniUpit(upitId)
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
  setUpit: (state, upit) =>
    state.kreiranUpit = upit,
  setNepotvrdjeniUpiti: (state, upiti) =>
    state.nepotvrdjeniUpiti = upiti,
  setNeodobreniUpiti: (state, upiti) =>
    state.neodobreniUpiti = upiti,
  setPotvrdjenUpit: (state, upit) => 
    state.potvrdjenUpit = upit,
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