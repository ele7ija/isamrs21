import tipoviPregleda from '@/api/tipoviPregleda';
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
  }
}
const actions = {
  async loadTipoviPregleda({commit}){
    //dobavi kliniku preko api call-a ili iz drugog modula
    commit('setCurrentKlinika', {'id': 1}); //za sada dummy klinika

    let response = await tipoviPregleda.getAllTipoviPregleda(1); //state.klinike.id umesto 1 kada napravis klinike
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
    let response = await tipoviPregleda.removeTipPregleda(state.klinika.id, idTipaPregleda);
    commit('deleteTipPregleda', response);
  },
  
}
const mutations = {
  setCurrentKlinika: (state, klinika) => state.klinika = klinika,
  setTipoviPregleda: (state, tipoviPregleda) => state.tipoviPregleda = tipoviPregleda,
  addNewTipPregleda: (state, tipPregleda) => state.tipoviPregleda.push(tipPregleda),
  updateExistingTipPregleda: (state, tipPregleda) => {
    let toUpdate = state.tipoviPregleda.filter(x => x.id == tipPregleda.id);
    toUpdate.name = tipPregleda.name;
    toUpdate.description = tipPregleda.description;
    //itd. za ostale atribute
  },
  deleteTipPregleda: (state, tipPregleda) => state.tipoviPregleda = state.tipoviPregleda.filter(x => x.id != tipPregleda.id)

}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}