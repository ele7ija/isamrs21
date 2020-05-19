import adminAPI from '@/api/admini'

const state = {
  adminiCentra: [],
}

const getters = {
  getAdminiCentra: (state) => state.adminiCentra,
}

const actions = {
  async fetchAdminiCentra({commit}){
    let admini = await adminAPI.fetchAllAdminiCentra();
    commit('setAdminiCentra', admini);
  },

  async addAdminCentra({commit}, adminCentra){
    let response = await adminAPI.addAdminCentra(adminCentra);
    commit('addAdminCentra', response)
  }
}

const mutations = {
  setAdminiCentra: (state, adminiCentra) =>
    state.adminiCentra = adminiCentra,
  addAdminCentra: (state, adminCentra) =>
    state.adminiCentra.push(adminCentra),

}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}