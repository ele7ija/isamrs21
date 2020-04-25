import klinikeAPI from '@/api/klinike';


const state = {
  klinike: []
}
const getters = {
}
const actions = {
  async loadKlinike({commit}) {
    let data = await klinikeAPI.getAllKlinike();
    commit('setKlinike', data);
  }
}
const mutations = {
  setKlinike: (state, klinike) =>
    state.klinike = klinike,
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}