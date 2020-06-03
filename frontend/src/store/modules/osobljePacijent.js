import osobljeAPI from '@/api/osobljePacijent'
import oceneAPI from '@/api/ocene';


const state = {
  lekari: [],
  ocene: {}
}

const getters = {
  getOceneLekara: (state) => (lekarId) => {
    if (lekarId in state.ocene){
      return state.ocene[lekarId];  
    }
    return [];
  },
  getProsecnaOcenaLekara: (state) => (lekarId) => {
    if (lekarId in state.ocene){
      let suma = 0;
      for (let ocena of state.ocene[lekarId]) {
        suma += ocena.vrednost;
      }
      if (state.ocene[lekarId].length != 0) {
        return suma / state.ocene[lekarId].length;
      }
      return 0;
    }
    
  },
}
const actions = {
  async pronadjiLekare({commit}, idKlinike) {
    return new Promise((resolve, reject) => {
      osobljeAPI.pronadjiLekare(idKlinike)
      .then(({data: {result, message, success}}) => {
        if (success) {
          commit('setLekari', result);
          resolve(message);
        }
        else {
          reject(message);
        }
      })
    })
  },
  async loadAllOcene({commit, state}) {
    for (let lekar of state.lekari) {
      oceneAPI.pronadjiOceneLekara(lekar.id).then(({data}) => {
        commit('setOceneLekara', {lekarId: lekar.id, ocene: data});
      })
    }
  },
}
const mutations = {
  setLekari: (state, lekari) =>
    state.lekari = lekari,
  setOceneLekara: (state, obj) =>
    state.ocene[obj.lekarId] = obj.ocene
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}