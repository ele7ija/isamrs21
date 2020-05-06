import pregledi from '@/api/pregledi';

const state = {
  klinika: null,
  preglediKlinike: [],
  noviPregled: null //PREGLED KOJI PRAVI ADMIN KLINIKE PRI OBRADI UPITA ZA CUSTOM PREGLEDOM
};
const getters = {
  getPreglediKlinike: (state) => state.preglediKlinike,
  getNoviPregled: (state) => state.noviPregled
};
const actions = {
  async fetchPreglediKlinike({commit, rootGetters}){
    let resp = rootGetters['klinike/getKlinikaAdmina'];
    commit('setCurrentKlinika', resp);

    let response = await pregledi.getAllPregledi(resp.id);
    commit('setPreglediKlinike', response);
  },

  async addPregled({state, commit, dispatch}, pregled){
    let {result, success, message} = await pregledi.addPregled(state.klinika.id, pregled);
    if(success){
      commit('addNewPregled', result);
      //azuriraj podatek za lekara, sale i tipovePregleda
      dispatch('osoblje/loadMedicinskoOsoblje', {}, {root: true});
      dispatch('sale/loadSale', {}, {root: true});
      dispatch('tipoviPregleda/loadTipoviPregleda', {}, {root: true});
    }else{
      return Promise.reject(message);
    }
  },

  async updatePregled({state, commit}, pregled){
    let {result, success, message} = await pregledi.updatePregled(state.klinika.id, pregled);
    if(success){
      commit('updateExistingPregled', result);
    }else{
      return Promise.reject(message);
    }
  },

  async removePregled({state, commit}, pregled){
    return new Promise((resolve, reject) => {
      pregledi.deletePregled(state.klinika.id, pregled.id)
      .then(({data:{success, message}}) => {
        if(success){
          commit('removePregled', pregled);
          resolve(message);
        }else{
          reject(message);
        }
      })
    });
  }
};
const mutations = {
  setCurrentKlinika: (state, klinika) => state.klinika = klinika,
  setPreglediKlinike: (state, pregledi) => state.preglediKlinike = pregledi,
  addNewPregled: (state, pregled) => state.preglediKlinike.push(pregled),
  updateExistingPregled: (state, pregled) => {
    let index = state.preglediKlinike.findIndex(x => x.id == pregled.id);
    state.preglediKlinike[index] = pregled;
  },
  removePregled: (state, pregled) => state.preglediKlinike = state.preglediKlinike.filter(x => x.id != pregled.id)
};

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}