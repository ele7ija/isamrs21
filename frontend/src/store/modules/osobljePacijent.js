import osobljeAPI from '@/api/osobljePacijent'

const state = {
  lekari: []
}

const getters = {
  
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
}
const mutations = {
  setLekari: (state, lekari) =>
    state.lekari = lekari,
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}