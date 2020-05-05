import upitiPreglediAdmin from '@/api/upitiPreglediAdmin';

const state = {
  klinika: null,
  upiti_pregledi_admin: []
}
const getters = {
  getUpiti: (state) => {
    return state.upiti_pregledi_admin;
  },
  getKlinika: (state) => {
    return state.klinika;
  }

}
const actions = {
  async loadUpiti({commit, rootGetters}){
    let resp = rootGetters['klinike/getKlinikaAdmina'];
    commit('setCurrentKlinika', resp);

    let response = await upitiPreglediAdmin.getAllUpiti(resp.id);
    commit('setUpiti', response);
  },

  async addUpit({state, commit}, upit){
    let response = await upitiPreglediAdmin.addUpit(state.klinika.id, upit);
    commit('addNewUpit', response);
  },

  async obradiAdmin({state, commit}, upit){
    let response = await upitiPreglediAdmin.obradiAdmin(state.klinika.id, upit);
    commit('updateExistingUpit', response);
  }
}
const mutations = {
  setCurrentKlinika: (state, klinika) => state.klinika = klinika,
  setUpiti: (state, upiti_pregledi_admin) => state.upiti_pregledi_admin = upiti_pregledi_admin,
  addNewUpit: (state, upit) => state.upiti_pregledi_admin.push(upit),
  updateExistingUpit: (state, upit) => {
    let index = state.upiti_pregledi_admin.findIndex(x => x.id == upit.id);
    state.upiti_pregledi_admin[index] = upit;
  }
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}