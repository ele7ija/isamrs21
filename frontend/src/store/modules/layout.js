const state = {
  globalLayout: 'neulogovani-korisnik-layout',
};

const getters = {
  getGlobalLayout: (state) => state.globalLayout
}

const actions = {
  reset({commit}){
    commit('setGlobalLayout', 'neulogovani-korisnik-layout');
  },
  setLayout({commit}, layout){
    commit('setGlobalLayout', layout);
  }
}

const mutations = {
  setGlobalLayout (state, payload){
    state.globalLayout = payload;
  }
};

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}