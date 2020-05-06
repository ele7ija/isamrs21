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
  },

  async deleteUpit({state, commit, dispatch}, idUpita){
    return new Promise((resolve, reject) => {
      upitiPreglediAdmin.deleteUpit(state.klinika.id, idUpita)
      .then(({data: {success, message}}) => {
        if(success){
          commit('deleteUpitZaPregled', idUpita);
          dispatch("preglediAdmin/fetchPreglediKlinike", {}, {root: true});
          resolve(message);
        }else{
          reject(message);
        }
      }); 
    });
  }
}
const mutations = {
  setCurrentKlinika: (state, klinika) => state.klinika = klinika,
  setUpiti: (state, upiti_pregledi_admin) => state.upiti_pregledi_admin = upiti_pregledi_admin,
  addNewUpit: (state, upit) => state.upiti_pregledi_admin.push(upit),
  updateExistingUpit: (state, upit) => {
    let index = state.upiti_pregledi_admin.findIndex(x => x.id == upit.id);
    
    //nesvakidasnji nacin da se update-uje element u nizu, al za sad jedino resenje kod kojeg radi reactivity
    state.upiti_pregledi_admin.splice(index, 1);
    state.upiti_pregledi_admin.splice(index, 0, upit);
  },
  deleteUpitZaPregled: (state, idUpita) => state.upiti_pregledi_admin = state.upiti_pregledi_admin.filter(x => x.id != idUpita)
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}