import adminAPI from '@/api/admini'

const state = {
  adminiKlinike: [],
}

const getters = {
  getAdminiKlinike: (state) => state.adminiKlinike,
}

const actions = {
  async fetchAdminiKlinike({commit}){
    let admini = await adminAPI.fetchAllAdminiKlinike();
    commit('setAdminiKlinika', admini);
  },

  async addAdminKlinike({commit}, adminKlinike){
    let response = await adminAPI.addAdminKlinike(adminKlinike);
    commit('addAdminKlinike', response)
  }
}

const mutations = {
  setAdminiKlinika: (state, adminiKlinike) =>
    state.adminiKlinike = adminiKlinike,
  addAdminKlinike: (state, adminKlinike) =>
    state.adminiKlinike.push(adminKlinike),

}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}