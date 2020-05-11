import zahteviAPI from '@/api/zahteviZaRegistraciju'
const state = {
  zahtevi: [],
}

const getters = {
  getAllZahtevi: (state) => state.zahtevi,
}

const actions = {
  async fetchAllZahtevi({commit}){
    let data = await zahteviAPI.fetchAllZahtevi();
    commit('setZahtevi', data);
  }
}

const mutations = {
  setZahtevi: (state, zahtevi) => {
    for (var item of zahtevi){
      var d= new Date(item.datumPodnosenja);
      item.datumString = d.toLocaleDateString("sr");
      item.vremeString =  d.toLocaleTimeString("sr");
    }
    state.zahtevi = zahtevi
  },
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
