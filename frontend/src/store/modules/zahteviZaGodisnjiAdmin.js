import zahteviZaGodisnji from '@/api/zahteviZaGodisnji';
const state = {
  zahteviZaGodisnji: [],
  klinika: null,
}

const getters = {
  getAllZahteviZaGodisnji: (state) => state.zahteviZaGodisnji
}

const actions = {
  async fetchAllZahtevi({commit, rootGetters}){
    let resp = rootGetters['klinike/getKlinikaAdmina'];
    commit('setCurrentKlinika', resp);

    let data = await zahteviZaGodisnji.fetchAllZahtevi();
    commit('setZahteviZaGodisnji', data);
  },

  async updateZahtev({commit}, zahtev){
    return new Promise((resolve, reject) => {
      zahteviZaGodisnji.updateZahtev(zahtev)
      .then(({data: {result, message, success}}) => {
        if(success){
          commit('updateZahtevZaGodisnji', result);
          resolve(message);
        }else{
          reject(message);
        }
      });
    });
  },

  async deleteZahtev({commit}, idZahteva){
    return new Promise((resolve, reject) => {
      zahteviZaGodisnji.deleteZahtev(idZahteva)
      .then(({data: {result, message, success}}) => {
        if(success){
          commit('deleteZahtevZaGodisnji', result);
          resolve(message);
        }else{
          reject(message);
        }
      });
    });
  }
}

const mutations = {
  setCurrentKlinika: (state, klinika) => state.klinika = klinika,
  setZahteviZaGodisnji: (state, zahteviZaGodisnji) => state.zahteviZaGodisnji = zahteviZaGodisnji,
  updateZahtevZaGodisnji: (state, zahtevZaGodisnji) => {
    let index = state.zahteviZaGodisnji.findIndex(x => x.id == zahtevZaGodisnji.id);
    state.zahteviZaGodisnji.splice(index, 1);
    state.zahteviZaGodisnji.splice(index, 0, zahtevZaGodisnji);
  },
  deleteZahtevZaGodisnji: (state, zahtevZaGodisnji) =>
    state.zahteviZaGodisnji = state.zahteviZaGodisnji.filter(x => x.id != zahtevZaGodisnji.id)
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
