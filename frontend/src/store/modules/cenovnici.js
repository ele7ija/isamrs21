import cenovnici from '@/api/cenovnici';
//import klinike from '@/api/klinike';

const state = {
  klinika: null,
  cenovnici: []
}
const getters = {
  getCenovnici: (state) => {
    return state.cenovnici;
  },
  getCenovnik: (state) => (id) => {
    let filtriraniCenovnici = state.cenovnici.filter(x => x.id == id);
    if(filtriraniCenovnici.length == 0){
      return null;
    }else{
      return filtriraniCenovnici[0];
    }
  },
  getKlinika: (state) => {
    return state.klinika;
  }

}
const actions = {
  async loadCenovnici({commit, rootGetters}){
    //let resp = await klinike.fetchKlinikaAdmina();
    let resp = rootGetters['klinike/getKlinikaAdmina'];
    commit('setCurrentKlinika', resp);

    let response = await cenovnici.getAllCenovnici(resp.id);
    commit('setCenovnici', response);
  },

  async addCenovnik({state, commit}, cenovnik){
    let response = await cenovnici.addCenovnik(state.klinika.id, cenovnik);
    commit('addNewCenovnik', response);
  },

  async updateCenovnik({state, commit}, cenovnik){
    return new Promise((resolve, reject) => {
      cenovnici.updateCenovnik(state.klinika.id, cenovnik)
      .then(({data:{result, message,success}}) => {
        if(success){
          commit('updateExistingCenovnik', result);
          resolve(message);
        }else{
          reject(message);
        }
      })
    });
  },

  async removeCenovnik({state, commit}, {idCenovnika, version}){
    return new Promise((resolve, reject) => {
      cenovnici.removeCenovnik(state.klinika.id, idCenovnika, version)
      .then(({data: {success, message}}) => {
        if(success){
          commit('deleteCenovnik', idCenovnika);
          resolve(message);
        }else{
          reject(message);
        }
      });
    });
  },
  
}
const mutations = {
  setCurrentKlinika: (state, klinika) => state.klinika = klinika,
  setCenovnici: (state, cenovnici) => state.cenovnici = cenovnici,
  addNewCenovnik: (state, cenovnik) => state.cenovnici.push(cenovnik),
  updateExistingCenovnik: (state, cenovnik) => {
    let index = state.cenovnici.findIndex(x => x.id == cenovnik.id);
    state.cenovnici.splice(index, 1);
    state.cenovnici.splice(index, 0, cenovnik);
  },
  deleteCenovnik: (state, idCenovnika) => {
    state.cenovnici = state.cenovnici.filter(x => x.id != idCenovnika);
  }

}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}