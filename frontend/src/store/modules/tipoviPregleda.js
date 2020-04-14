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
  async loadTipoviPregleda({commit}){
    //let resp = await klinike.getKlinika(1); //za sada dummy klinika
    //commit('setCurrentKlinika', resp);
    commit('setCurrentKlinika', {'id': 1});

    let response = await tipoviPregleda.getAllTipoviPregleda(1);
    commit('setTipoviPregleda', response);
  },

  async addTipPregleda({state, commit}, tipPregleda){
    let response = await tipoviPregleda.addTipPregleda(state.klinika.id, tipPregleda);
    commit('addNewTipPregleda', response);
  },

  async updateTipPregleda({state, commit}, tipPregleda){
    let response = await tipoviPregleda.updateTipPregleda(state.klinika.id, tipPregleda);
    commit('updateExistingTipPregleda', response);
  },

  async removeTipPregleda({state, commit}, idTipaPregleda){
    await tipoviPregleda.removeTipPregleda(state.klinika.id, idTipaPregleda);
    commit('deleteTipPregleda', idTipaPregleda);
  },
  
}
const mutations = {
  setCurrentKlinika: (state, klinika) => state.klinika = klinika,
  setTipoviPregleda: (state, tipoviPregleda) => state.tipoviPregleda = tipoviPregleda,
  addNewTipPregleda: (state, tipPregleda) => state.tipoviPregleda.push(tipPregleda),
  updateExistingTipPregleda: (state, tipPregleda) => {
    let index = state.tipoviPregleda.findIndex(x => x.id == tipPregleda.id);
    state.tipoviPregleda[index].naziv = tipPregleda.naziv;
    state.tipoviPregleda[index].opis = tipPregleda.opis;
    //itd. za ostale atribute
  },
  deleteTipPregleda: (state, idTipaPregleda) => state.tipoviPregleda = state.tipoviPregleda.filter(x => x.id != idTipaPregleda)

}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}