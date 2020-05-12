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
  },

  async prihvatiZahtev({commit}, zahtev){
    let data = await zahteviAPI.prihvatiZahtev(zahtev);
    commit('prihvatiZahtev', data);
  },

  async odbijZahtev({commit}, zahtev){
    let data = await zahteviAPI.odbijZahtev(zahtev);
    commit('deleteZahtev', data);
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
  //napraviti mutacije za prihvatiZahtev i odbijZahtev
  deleteZahtev: (state,zahtevToDelete) => {
    state.zahtevi = state.zahtevi.filter(
      function(zahtev){
        return zahtev.id != zahtevToDelete.id;
      }
    );
  }
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
