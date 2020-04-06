import axios from 'axios';

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
  setKlinika({commit}){
    //dobavi kliniku preko api call-a ili iz drugog modula
    commit('setKlinika', {'id': 1}); //za sada dummy klinika
  },

  async loadTipoviPregleda({state, commit}){
    let response = await axios.get(
      `http://localhost:8080/api/${state.klinika.id}`
    );
    commit('setTipoviPregleda', response.data);
  },

  async addTipPregleda({state, commit}, tipPregleda){
    let response = await axios.post(
      `http://localhost:8080/api/${state.klinika.id}`,
      tipPregleda
    );
    commit('addTipPregleda', response.data);
  },

  async updateTipPregleda({state, commit}, tipPregleda){
    let response = await axios.put(
      `http://localhost:8080/api/${state.klinika.id}/${tipPregleda.id}`,
      tipPregleda
    );
    commit('updateTipPregleda', response.data);
  },

  async removeTipPregleda({state, commit}, idTipaPregleda){
    let response = await axios.delete(
      `http://localhost:8080/api/${state.klinika.id}/${idTipaPregleda}`,
    );
    commit('deleteTipPregleda', response.data);
  },
  
}
const mutations = {
  setKlinika: (state, klinika) => state.klinika = klinika,
  setTipoviPregleda: (state, tipoviPregleda) => state.tipoviPregleda = tipoviPregleda,
  addTipPregleda: (state, tipPregleda) => state.tipoviPregleda.push(tipPregleda),
  updateTipPregleda: (state, tipPregleda) => {
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