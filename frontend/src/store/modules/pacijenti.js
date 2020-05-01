import pacijenti from '@/api/pacijenti'

const state = {
  pacijenti: []
}
const getters = {
  getPacijenti: (state) => {
    return state.pacijenti;
  }
}
const actions = {
  async loadPacijenti({commit}){
    let data = await pacijenti.getAllKorisnici();
    commit('setPacijenti', data);
  },
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