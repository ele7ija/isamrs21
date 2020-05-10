import tipoviPregleda from '@/api/tipoviPregleda';
//import klinike from '@/api/klinike';

const state = {
  klinika: null,
  tipoviPregleda: []
}
const getters = {
  getTipoviPregleda: (state) => {
    return state.tipoviPregleda;
  },
  getTipPregleda: (state) => (id) => {
    let filtriraniTipoviPrelgeda = state.tipoviPregleda.filter(x => x.id == id);
    if(filtriraniTipoviPrelgeda.length == 0){
      return null;
    }else{
      return filtriraniTipoviPrelgeda[0];
    }
  },
  getKlinika: (state) => {
    return state.klinika;
  }

}
const actions = {
  async loadTipoviPregleda({commit, rootGetters}){
    //let resp = await klinike.fetchKlinikaAdmina();
    let resp = rootGetters['klinike/getKlinikaAdmina'];
    commit('setCurrentKlinika', resp);

    let response = await tipoviPregleda.getAllTipoviPregleda(resp.id);
    commit('setTipoviPregleda', response);
  },

  async addTipPregleda({state, commit}, tipPregleda){
    let response = await tipoviPregleda.addTipPregleda(state.klinika.id, tipPregleda);
    commit('addNewTipPregleda', response);
  },

  async updateTipPregleda({state, commit}, tipPregleda){
    return new Promise((resolve, reject) => {
      tipoviPregleda.updateTipPregleda(state.klinika.id, tipPregleda)
      .then(({data: {result, success, message}}) => {
        if(success){
          commit('updateExistingTipPregleda', result);
          resolve(message);
        }else{
          reject(message);
        }
      });
    });    
  },

  async removeTipPregleda({state, commit, dispatch}, {idTipaPregleda, version}){
    return new Promise((resolve, reject) => {
      tipoviPregleda.removeTipPregleda(state.klinika.id, idTipaPregleda, version)
      .then(({data: {success, message}}) => {
        if(success){
          commit('deleteTipPregleda', idTipaPregleda);
          dispatch("cenovnici/loadCenovnici", {}, {root: true});
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
  setTipoviPregleda: (state, tipoviPregleda) => state.tipoviPregleda = tipoviPregleda,
  addNewTipPregleda: (state, tipPregleda) => state.tipoviPregleda.push(tipPregleda),
  updateExistingTipPregleda: (state, tipPregleda) => {
    let index = state.tipoviPregleda.findIndex(x => x.id == tipPregleda.id);
    state.tipoviPregleda.splice(index, 1);
    state.tipoviPregleda.splice(index, 0, tipPregleda);
  },
  deleteTipPregleda: (state, idTipaPregleda) => state.tipoviPregleda = state.tipoviPregleda.filter(x => x.id != idTipaPregleda),
  setCenovnikOfTipPregleda: (state, {tipPregleda, idCenovnika}) => {
    let index = state.tipoviPregleda.findIndex(x => x.id == tipPregleda.id);
    state.tipoviPregleda[index].cenovnik = {id: idCenovnika};
  },
  removeSpecijalista: (state, {idLekara, idTipaPregleda}) => {
    let tipPregleda = state.tipoviPregleda.filter( x=> x.id == idTipaPregleda)[0];
    tipPregleda.lekari = tipPregleda.lekari.filter(x => x.id != idLekara);
  },
  addSpecijalista: (state, {idLekara, idTipovaPregleda}) => {
    let temp = state.tipoviPregleda.filter(x => idTipovaPregleda.filter(y => y == x.id).length != 0);
    for(let elem of temp){
      elem.lekari.push({id: idLekara});
    }
  }

}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}