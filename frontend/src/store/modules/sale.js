import sale from '@/api/sale';
//import klinike from '@/api/klinike';

const state = {
  klinika: null,
  sale: []
}

const getters = {
  getSale: (state) => {
    return state.sale;
  },

  getSala: (state) => (id) => {
    let filtriraneSale = state.sale.filter(x => x.id == id);
    if(filtriraneSale.length == 0){
      return null;
    }else{
      return filtriraneSale[0];
    }
  },

  getKlinika: (state) => {
    return state.klinika;
  }

}

const actions = {
  async loadSale({commit}){
    //let resp = await klinike.getKlinika(1); //za sada dummy klinika
    //commit('setCurrentKlinika', resp);
    commit('setCurrentKlinika', {'id': 1});

    let response = await sale.getAllSale(1);
    commit('setSale', response);
  },

  async addSala({state, commit}, sala){
    let response = await sale.addSala(state.klinika.id, sala);
    commit('addNewSala', response);
  },

  async updateSala({state, commit}, sala){
    let response = await sale.updateSala(state.klinika.id, sala);
    commit('updateExistingSala', response);
  },

  async removeSala({state, commit}, idSale){
    await sale.removeSala(state.klinika.id, idSale);
    commit('deleteSala', idSale);
  },
  
}
const mutations = {
  setCurrentKlinika: (state, klinika) => state.klinika = klinika,
  setSale: (state, sale) => state.sale = sale,
  addNewSala: (state, sala) => state.sale.push(sala),
  updateExistingSala: (state, sala) => {
    let index = state.sale.findIndex(x => x.id == sala.id);
    state.sale[index].oznaka = sala.oznaka;
    //itd. za ostale atribute
  },
  deleteSala: (state, idSale) => state.sale = state.sale.filter(x => x.id != idSale)

}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}