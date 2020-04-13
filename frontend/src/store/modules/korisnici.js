/* Deo store-a vezan za operacije sa neautentifikovanim
  korisnikom - login i registracija. */
import korisnikAPI from '@/api/korisnici'

const state = {
  registrovanKorisnik: null, // Ako bude trebao neki ispis
}

const getters = {}

const actions = {
  // ne radi jos uvek nista
  async loadUlogovanKorisnik({commit}){
    let data = await korisnikAPI.getUlogovanKorisnik();
    commit('setKorisnik', data, { root: true })
  },

  async registrujKorisnika({commit}, korisnik) {
    let data = await korisnikAPI.registrujKorisnika(korisnik)
    commit('setRegistrovanKorisnik', data)
  }
}

const mutations = {
  setRegistrovanKorisnik: (state, korisnik) => 
    state.registrovanKorisnik = korisnik
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}