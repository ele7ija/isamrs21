import upitiPreglediAdmin from '@/api/upitiPreglediAdmin';
import utility from '@/utility/utility';
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
    upit.pocetakPregleda = utility.addToDate(new Date(upit.pocetakPregleda));
    upit.krajPregleda = utility.addToDate(new Date(upit.krajPregleda));
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

  async obradiAdminCustom({state, commit, dispatch}, upit){
    return new Promise((resolve, reject) => {
      upitiPreglediAdmin.obradiAdminCustom(state.klinika.id, upit)
      .then(({data: {result, message, success}}) => {
        if(success && result != null){
          commit('updateExistingUpit', result);
          dispatch('preglediAdmin/fetchPreglediKlinike', {}, {root: true});
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
  setUpiti: (state, upiti_pregledi_admin) => {
    state.upiti_pregledi_admin = upiti_pregledi_admin
    for(let upit of state.upiti_pregledi_admin){
      upit.pocetakPregleda = utility.handleTimeZone(new Date(upit.pocetakPregleda));
      upit.krajPregleda = utility.handleTimeZone(new Date(upit.krajPregleda));
    }
  },
  addNewUpit: (state, upit) => {
    upit.pocetakPregleda = utility.handleTimeZone(new Date(upit.pocetakPregleda));
    upit.krajPregleda = utility.handleTimeZone(new Date(upit.krajPregleda));
    state.upiti_pregledi_admin.push(upit)
  },
  updateExistingUpit: (state, upit) => {
    let index = state.upiti_pregledi_admin.findIndex(x => x.id == upit.id);
    //nesvakidasnji nacin da se update-uje element u nizu, al za sad jedino resenje kod kojeg radi reactivity
    upit.pocetakPregleda = utility.handleTimeZone(new Date(upit.pocetakPregleda));
    upit.krajPregleda = utility.handleTimeZone(new Date(upit.krajPregleda));
    state.upiti_pregledi_admin.splice(index, 1);
    state.upiti_pregledi_admin.splice(index, 0, upit);
    if(upit.izmenjeniPregled){
      upit.izmenjeniPregled.pocetakPregleda = utility.handleTimeZone(new Date(upit.izmenjeniPregled.pocetakPregleda));
      upit.izmenjeniPregled.krajPregleda = utility.handleTimeZone(new Date(upit.izmenjeniPregled.krajPregleda));
      state.upiti_pregledi_admin.push(upit.izmenjeniPregled);
    }
  },
  deleteUpitZaPregled: (state, idUpita) => {
    let upit = state.upiti_pregledi_admin.filter(x => x.id == idUpita)[0];
    if(upit.originalniPregled){
      state.upiti_pregledi_admin = state.upiti_pregledi_admin.filter(x => x.id != idUpita && x.id != upit.originalniPregled.id);
    }else{
      state.upiti_pregledi_admin = state.upiti_pregledi_admin.filter(x => x.id != idUpita);
    }
  },
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