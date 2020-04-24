/* Deo store-a vezan za operacije sa neautentifikovanim
  korisnikom - login i registracija. 
  Ali i za dobavljanje svih korisnika cele aplikacije*/
import korisnikAPI from '@/api/korisnici'
import router from '@/router';


const state = {
  korisnici: [],
  korisnik: null, // {user_token, email, role}
  registrovanKorisnik: null, // Ako bude trebao neki ispis
}

const getters = {
  getKorisnik: (state) => {return state.korisnik},
  getKorisnici: (state) => {return state.korisnici}
}

const actions = {
  // ne radi jos uvek nista
  async loadUlogovanKorisnik({commit}){
    let data = await korisnikAPI.getUlogovanKorisnik();
    commit('setKorisnik', data)
  },
  async fetchAllKorisnici({commit}){
    let data = await korisnikAPI.fetchAllKorisnici();
    commit('setKorisnici', data)
  },

  async registrujKorisnika({commit}, korisnik) {
    let data = await korisnikAPI.registrujKorisnika(korisnik)
    commit('setRegistrovanKorisnik', data)
  },
  async loginKorisnik({commit, dispatch}, korisnik) {
    let data = await korisnikAPI.loginujKorisnika(korisnik);
    commit('setKorisnik', data);
    dispatch('layout/setLayout', `${data.role}-layout`, {root:true});
    router.push(`home`);
  },
  reset({commit}){
    commit('resetKorisnik');
  }
}

const mutations = {
  setRegistrovanKorisnik: (state, korisnik) => 
    state.registrovanKorisnik = korisnik,
  setKorisnik: (state, korisnik) =>
    state.korisnik = korisnik,
  resetKorisnik: (state) => state.korisnik = null, //celokupan logout
  setKorisnici: (state, korisnici) => state.korisnici = korisnici
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}