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
    return new Promise((resolve, reject) => {
      upitiPreglediAdmin.obradiAdmin(state.klinika.id, upit)
      .then(({data: {result, message, success}}) => {
        if(result != null)
          commit('updateExistingUpit', result);
        if(success){
          resolve(message);
        }else{
          reject(message);
        }
      })
    });
  },

  async deleteUpit({state, commit, dispatch}, {idUpita, version}){
    return new Promise((resolve, reject) => {
      upitiPreglediAdmin.deleteUpit(state.klinika.id, idUpita, version)
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
  deleteUpitZaPregled: (state, idUpita) => state.upiti_pregledi_admin = state.upiti_pregledi_admin.filter(x => x.id != idUpita),
  setPocetak: (state, object) => {
    state.upiti_pregledi_admin.filter(x => x.id == object.id)[0].pocetakPregleda = object.pocetak;
  },
  setKraj: (state, object) => {
    state.upiti_pregledi_admin.filter(x => x.id == object.id)[0].krajPregleda = object.kraj;
  }
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}