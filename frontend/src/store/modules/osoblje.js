import osoblje from '@/api/osoblje';
//import klinike from '@/api/klinike';

const state = {
  klinika: null,
  medicinskoOsoblje: []
}
const getters = {
  getMedicinskoOsoblje: (state) => {
    return state.medicinskoOsoblje;
  },
  getMedicinskaOsoba: (state) => (id) => {
    let filtriraneOsobe = state.medicinskoOsoblje.filter(x => x.id == id);
    if(filtriraneOsobe.length == 0){
      return null;
    }else{
      return filtriraneOsobe[0];
    }
  },
  getKlinika: (state) => {
    return state.klinika;
  }

}
const actions = {
  async loadMedicinskoOsoblje({commit, rootGetters}){
    //let resp = await klinike.fetchKlinikaAdmina();
    let resp = rootGetters['klinike/getKlinikaAdmina'];
    commit('setCurrentKlinika', resp);

    let response = await osoblje.getAllOsoblje(resp.id);
    commit('setMedicinskoOsoblje', response);
  },

  async addMedicinskaOsoba({state, commit, dispatch}, osoba){
    let response = await osoblje.addMedicinskaOsoba(state.klinika.id, osoba);
    commit('addNewMedicinskaOsoba', response);
    dispatch('korisnici/addKorisnik', response, {root: true});
    console.log(response.id);
  },

  async updateMedicinskaOsoba({state, commit}, osoba){
    let response = await osoblje.updateExistingMedicinskaOsoba(state.klinika.id, osoba);
    commit('updateExistingMedicinskaOsoba', response);
  },

  async removeMedicinskaOsoba({state, commit, dispatch}, idOsobe){
    await osoblje.removeMedicinskaOsoba(state.klinika.id, idOsobe);
    commit('deleteMedicinskaOsoba', idOsobe);
    console.log(idOsobe);
    dispatch('korisnici/removeKorisnik', idOsobe, {root: true});
  },
  
}
const mutations = {
  setCurrentKlinika: (state, klinika) => state.klinika = klinika,
  setMedicinskoOsoblje: (state, medicinskoOsoblje) => state.medicinskoOsoblje = medicinskoOsoblje,
  addNewMedicinskaOsoba: (state, osoba) => state.medicinskoOsoblje.push(osoba),
  updateExistingMedicinskaOsoba: (state, osoba) => {
    let index = state.medicinskoOsoblje.findIndex(x => x.id == osoba.id);
    state.medicinskoOsoblje[index] = osoba;
  },
  deleteMedicinskaOsoba: (state, idOsobe) => state.medicinskoOsoblje = state.medicinskoOsoblje.filter(x => x.id != idOsobe)

}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}