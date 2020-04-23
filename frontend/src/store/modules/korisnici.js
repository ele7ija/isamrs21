/* Deo store-a vezan za operacije sa neautentifikovanim
  korisnikom - login i registracija. */
import korisnikAPI from '@/api/korisnici'
import router from '@/router';


const state = {
  korisnik: null, // {user_token, email, role}
  registrovanKorisnik: null, // Ako bude trebao neki ispis
}

const getters = {
  getKorisnik: (state) => {return state.korisnik}
}

const actions = {
  // ne radi jos uvek nista
  async loadUlogovanKorisnik({commit}){
    let data = await korisnikAPI.getUlogovanKorisnik();
    commit('setKorisnik', data)
  },
  async registrujKorisnika({commit}, korisnik) {
    let data = await korisnikAPI.registrujKorisnika(korisnik)
    commit('setRegistrovanKorisnik', data)
  },
  async loginKorisnik({commit}, korisnik) {
    let data = await korisnikAPI.loginujKorisnika(korisnik);
    commit('setKorisnik', data);
    commit('setGlobalLayout', `${data.role}-layout`, {root:true});
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
  resetKorisnik: (state) => state.korisnik = null //celokupan logout
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}