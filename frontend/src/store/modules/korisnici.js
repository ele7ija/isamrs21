/* Deo store-a vezan za operacije sa neautentifikovanim
  korisnikom - login i registracija. 
  Ali i za dobavljanje svih korisnika cele aplikacije*/
import korisnikAPI from '@/api/korisnici'
import router from '@/router';

const state = {
  korisnici: [],
  korisnik: null, // {user_token, email, role}
  _korisnik: {}, // {email, password} - jer ne radi refresh za sada pa radimo ponovan login nakon isteka tokena
  registrovanKorisnik: null, // Ako bude trebao neki ispis
}

const getters = {
  _getKorsinik: (state) => { return state._korisnik; },
  getKorisnik: (state) => {return state.korisnik},
  getKorisnici: (state) => {return state.korisnici},
  getAdminiKlinika: (state) => { 
    var adminiKlinika = state.korisnici.filter(
      function(korisnik){
        return korisnik.klinikaAdmina != undefined;
    });
    return adminiKlinika;
  },
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
    commit('_setKorisnik', korisnik);
    commit('setKorisnik', data);
    dispatch('layout/setLayout', `${data.role}-layout`, {root:true});
    router.push(`home`);
  },

  refresh_token(){
    return korisnikAPI.refresh_token();
  }, 

  reset({commit}){
    commit('resetKorisnik');
  },

  removeKorisnik({commit}, korisnikId){
    commit('_removeKorisnik', korisnikId)
  },

  addKorisnik({commit}, korisnik){
    commit('_addKorisnik', korisnik)
  },
}

const mutations = {
  setRegistrovanKorisnik: (state, korisnik) => 
    state.registrovanKorisnik = korisnik,
  setKorisnik: (state, korisnik) =>
    state.korisnik = korisnik,
  _setKorisnik: (state, korisnik) => {
    state._korisnik = korisnik;
  },
  resetKorisnik: (state) => {
    state._korisnik = {};
    state.korisnik = null; //celokupan logout
  },
  setKorisnici: (state, korisnici) => state.korisnici = korisnici,
  _removeKorisnik: (state, korisnikId) => state.korisnici = state.korisnici.filter(x => x.id != korisnikId),
  _addKorisnik: (state, korisnik) => state.korisnici.push(korisnik)
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}