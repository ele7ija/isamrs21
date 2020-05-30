import pacijenti from '@/api/pacijenti'
import utility from '@/utility/utility'
import posetaAPI from '@/api/posete'
const state = {
  pacijenti: [], //svi pacijenti
  odabraniPacijent: null, //pacijent kojem je trenutno pristupio lekar,
  lekarovPacijent: false, //da li lekar sme da pristupi zdravstvenom kartonu pacijenta?
  posetePacijenta: [],
  kartonPacijenta: [],
}
const getters = {
  getPacijenti: (state) => {
    return state.pacijenti;
  },
  getOdabraniPacijent: (state) => state.odabraniPacijent,
  isLekarovPacijent: (state) => state.lekarovPacijent,
  getPoseteOdabranogPacijenta: (state) => state.posetePacijenta,
  getSpecijalizacijeLekara: (state, getters, rootState, rootGetters) => {
    let profil = rootGetters['profil/getProfil'];
    return profil.tipovi_pregleda;
  },
  pregledMozeDaSeZapocne: (state) => (idPosete) => {
    var d = new Date()
    let poseta = state.odabraniPacijent.zdravstveniKarton.posete.filter(x => x.id == idPosete)[0];
    let vremePregleda = utility.handleTimeZone(new Date(poseta.pregled.pocetakPregleda));
    var diff = Math.abs(d.getTime() - vremePregleda.getTime());
    diff/60000;
    return (diff / 60000) <= 15; //pregled moze da se zapocne 15 minuta ranije
  },
  getZdravstveniKarton: (state) => state.kartonPacijenta,
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
  },


  loadPosete({getters, commit}){
    var pacijent = getters.getOdabraniPacijent;
    var posete = pacijent.zdravstveniKarton.posete;
    commit('setPosetePacijenta', posete);
  },

  async updatePoseta({commit}, poseta){
    let response = await posetaAPI.updatePoseta(poseta);
    // u response.data je updateovana poseta i updateovan karton;
    console.log(response.data);
    var retPoseta = response.data[0];
    var retKarton = response.data[1];
    commit("updatePosetePacijenta", retPoseta);
    commit("setZdravstveniKarton", retKarton);
  },

  async loadZdravstveniKarton({getters, commit}){
    var pacijent = getters.getOdabraniPacijent;
    var karton = pacijent.zdravstveniKarton;
    commit('setZdravstveniKarton', karton);
  },

}
const mutations = {
  setPacijenti: (state, _pacijenti) => 
    state.pacijenti = _pacijenti,
  setOdabraniPacijent: (state, pacijent) => state.odabraniPacijent = pacijent,
  setLekarovPacijent: (state, lekarovPacijent) => state.lekarovPacijent = lekarovPacijent,
  setPosetePacijenta: (state, posete) =>  state.posetePacijenta = posete,
  updatePosetePacijenta: (state, updateovanaPoseta) => {
    let staraPosetaIndex = state.posetePacijenta.findIndex( poseta=> poseta.id == updateovanaPoseta.id);
    state.posetePacijenta.splice(staraPosetaIndex, 1);
    state.posetePacijenta.splice(staraPosetaIndex, 0, updateovanaPoseta);
  },
  setZdravstveniKarton: (state, karton) => state.kartonPacijenta = karton,
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}