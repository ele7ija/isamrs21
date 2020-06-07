import receptiAPI from '@/api/recepti'

const state = {
  recepti: [],
}

const getters = {
  getRecepti: (state) => state.recepti,
}

const actions = {
  async loadRecepti() {
    var response = await receptiAPI.fetchAllRecepti();
    console.log(response);
  }
}

const mutations = {
  setRecepti: (state, recepti) => state.recepti = recepti,

}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
};