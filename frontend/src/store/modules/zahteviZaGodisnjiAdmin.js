import zahteviZaGodisnji from '@/api/zahteviZaGodisnji';
const state = {
  neobradjeniZahteviZaGodisnji: [], //zahtevi koje admin nije obradio
  allZahtevi: [], //svi zahtevi
  klinika: null,
}

const getters = {
  getNeobradjeniZahteviZaGodisnji: (state) => state.neobradjeniZahteviZaGodisnji,
  getAllZahtevi: (state) => state.allZahtevi
}

const actions = {
  async fetchAllZahtevi({commit, rootGetters}){
    let resp = rootGetters['klinike/getKlinikaAdmina'];
    commit('setCurrentKlinika', resp);

    let data = await zahteviZaGodisnji.fetchAllZahtevi(resp.id);
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
}

const mutations = {
  setCurrentKlinika: (state, klinika) => state.klinika = klinika,
  setZahteviZaGodisnji: (state, zahteviZaGodisnji) => {
    state.neobradjeniZahteviZaGodisnji = zahteviZaGodisnji.filter(x => !x.adminObradio);
    state.allZahtevi = zahteviZaGodisnji;
  },
  updateZahtevZaGodisnji: (state, zahtevZaGodisnji) => {
    let index = state.neobradjeniZahteviZaGodisnji.findIndex(x => x.id == zahtevZaGodisnji.id);
    state.neobradjeniZahteviZaGodisnji.splice(index, 1);
  }
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
