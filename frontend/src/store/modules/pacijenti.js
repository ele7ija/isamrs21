import pacijenti from '@/api/pacijenti'

const state = {
  pacijenti: [], //svi pacijenti
  odabraniPacijent: null, //pacijent kojem je trenutno pristupio lekar,
  lekarovPacijent: false //da li lekar sme da pristupi zdravstvenom kartonu pacijenta?
}
const getters = {
  getPacijenti: (state) => {
    return state.pacijenti;
  },
  getOdabraniPacijent: (state) => state.odabraniPacijent,
  isLekarovPacijent: (state) => state.lekarovPacijent,
  getPoseteOdabranogPacijenta: (state) => state.odabraniPacijent.zdravstveniKarton.posete
}
const actions = {
  async loadPacijenti({commit}){
    let data = await pacijenti.getAllKorisnici();
    commit('setPacijenti', data);
  },

  checkLekarovPacijent({state, commit, rootGetters}){
    //api poziv ka beku koji proverava da li ulogovani lekar moze da pristupi zdravstvenom kartonu pacijenta 'odabraniPacijent'
    return new Promise((resolve, reject) => {
      if(!state.odabraniPacijent){
        reject("Nije odabran pacijent za proveru prava pristupa zdravstvenom kartonu");
        return;
      }
      let idPacijenta = state.odabraniPacijent.id
      let idLekara = rootGetters['profil/getProfil'].id;
      pacijenti.checkLekarovPacijent({idPacijenta, idLekara})
      .then(({data}) => {
        commit('setLekarovPacijent', data);
        resolve("Uspesna provera prava pristupa zdravstvenom kartonu");
      });
    });
  }

}
const mutations = {
  setPacijenti: (state, _pacijenti) => 
    state.pacijenti = _pacijenti,
  setOdabraniPacijent: (state, pacijent) => state.odabraniPacijent = pacijent,
  setLekarovPacijent: (state, lekarovPacijent) => state.lekarovPacijent = lekarovPacijent
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}