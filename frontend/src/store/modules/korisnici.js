/* Deo store-a vezan za operacije sa neautentifikovanim
  korisnikom - login i registracija. */
import korisnikAPI from '@/api/korisnici'
import router from '@/router';


const state = {
  korisnik: null, // {user_token, email, role}
  registrovanKorisnik: null, // Ako bude trebao neki ispis
}

const getters = {}

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
    // TODO
    //if (data.role === 'pacijent') {
      commit('setGlobalLayout', 'pacijenti-layout', {root:true});
      router.push('pacijenti')
    //}
  }
}

const mutations = {
  setRegistrovanKorisnik: (state, korisnik) => 
    state.registrovanKorisnik = korisnik,
  setKorisnik: (state, korisnik) =>
    state.korisnik = korisnik,
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}