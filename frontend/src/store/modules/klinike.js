import klinikeAPI from '@/api/klinike';


const state = {
  klinike: [],
  klinikaAdmina: null
}
const getters = {
  getKlinikaAdmina: (state) => state.klinikaAdmina
}
const actions = {
  async loadKlinike({commit}) {
    let data = await klinikeAPI.getAllKlinike();
    commit('setKlinike', data);
  },
  async fetchKlinikaUlogovanogKorisnika({commit}){
    let data = await klinikeAPI.fetchKlinikaAdmina();
    commit('setKlinikaAdmina', data);
  },
  resetKlinike({commit}){
    commit('reset');
  }
}
const mutations = {
  setKlinike: (state, klinike) =>
    state.klinike = klinike,
  setKlinikaAdmina: (state, klinika) => state.klinikaAdmina = klinika,
  reset: (state) => {
    state.klinike = [];
    state.klinikaAdmina = null
  }
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}