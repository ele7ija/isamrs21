import pacijenti from '@/api/pacijenti'

const state = {
  pacijenti: []
}
const getters = {}
const actions = {
  async loadPacijenti({commit}){
    let response = await pacijenti.getAllKorisnici();
    commit('setPacijenti', response.data);
  }
}
const mutations = {
  setPacijenti: (state, _pacijenti) => 
    state.pacijenti = _pacijenti,
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}