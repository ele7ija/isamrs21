import korisnikAPI from '@/api/korisnici'


const state = {
  adminiKlinike: [],
}

const getters = {
  getAdminiKlinike: (state) => state.adminiKlinike,
}

const actions = {
  async fetchAdminiKlinike({commit}){
    let sviKorisnici = await korisnikAPI.fetchAllKorisnici();  
    var adminiKlinike = sviKorisnici.filter(
      function(korisnik){
        return korisnik.klinikaAdmina != undefined;
    });
    commit('setAdminiKlinika', adminiKlinike);
  },

  async addAdminKlinike({commit}, adminKlinike){
    let response = await korisnikAPI.addAdminKlinike(adminKlinike);
    commit('addAdminKlinike', response)
  }
}

const mutations = {
  setAdminiKlinika: (state, adminiKlinike) =>
    state.adminiKlinike = adminiKlinike,
  addAdminKlinike: (state, adminKlinike) =>
    state.adminiKlinike.push(adminKlinike),

}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}