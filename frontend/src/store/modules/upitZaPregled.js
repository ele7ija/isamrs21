import upitAPI from '@/api/upitZaPregled'

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
  setUpit: (state, upit) =>
    state.kreiranUpit = upit,
  setNepotvrdjeniUpiti: (state, upiti) =>
    state.nepotvrdjeniUpiti = upiti,
  setNeodobreniOdradjeniUpiti: (state, upiti) =>
    state.neodobreniOdradjeniUpiti = upiti,
  setNeodobreniNeodradjeniUpiti: (state, upiti) =>
    state.neodobreniNeodradjeniUpiti = upiti,
  setObradjenNeodobren: (state, upiti) =>
    state.obradjenNeodobren = upiti,
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