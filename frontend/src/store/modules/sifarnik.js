import sifarnikAPI from '@/api/sifarnik'

const state = {
  dijagnozeLekovi: [],
}

const getters = {
  getDijagnozeLekovi: (state) => state.dijagnozeLekovi,
}

const actions = {
  async fetchDijagnozeLekovi({commit}){
    let response = await sifarnikAPI.fetchAllDijagnozeLekovi()
    commit('setDijagnozeLekovi', response.data)
  },

  async addDijagnozaIliLek({commit}, dijagnozaIliLek){
    let response = await sifarnikAPI.addDijagnozaIliLek(dijagnozaIliLek);
    commit('addDijagnozaIliLek', response.data);
  }

}

const mutations = {
  setDijagnozeLekovi: (state, dijagnozeLekovi) => 
    state.dijagnozeLekovi = dijagnozeLekovi,
  addDijagnozaIliLek: (state, dijagnozaIliLek) =>
    state.dijagnozeLekovi.push(dijagnozaIliLek),
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}