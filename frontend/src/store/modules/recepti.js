import receptiAPI from '@/api/recepti'

const state = {
  recepti: [],
}

const getters = {
  getAllRecepti: (state) => state.recepti,
  getOvereniRecepti: (state) => 
    state.recepti.filter( (recept) => recept.overen == true),
  getNeovereniRecepti: (state) => 
    state.recepti.filter( (recept) => recept.overen == false),
  
}

const actions = {
  async loadRecepti({commit}) {
    var response = await receptiAPI.fetchAllRecepti();
    console.log(response.data);
    commit('setRecepti', response.data);
  },

  async overiRecept({commit}, recept){
    var response = await receptiAPI.overiRecept(recept);
    console.log(response.data);
    commit('updateRecepti', response.data)
  }
}

const mutations = {
  setRecepti: (state, recepti) => state.recepti = recepti,
  updateRecepti: (state, recept) =>{
    //mora spilice metoda kako bi se pravilno izmenilo u vuex store-u
    var index = state.recepti.findIndex(x => x.poseta.id == recept.poseta.id)
    state.recepti.splice(index, 1, recept);
  },

}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
};