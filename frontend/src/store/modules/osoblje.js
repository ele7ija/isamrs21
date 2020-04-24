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
  async loadMedicinskoOsoblje({commit}){
    //let resp = await klinike.getKlinika(1); //za sada dummy klinika
    //commit('setCurrentKlinika', resp);
    commit('setCurrentKlinika', {'id': 1});

    let response = await osoblje.getAllOsoblje(1);
    commit('setMedicinskoOsoblje', response);
  },

  async addMedicinskaOsoba({state, commit}, osoba){
    let response = await osoblje.addMedicinskaOsoba(state.klinika.id, osoba);
    commit('addNewMedicinskaOsoba', response);
  },

  async updateMedicinskaOsoba({state, commit}, osoba){
    let response = await osoblje.updateExistingMedicinskaOsoba(state.klinika.id, osoba);
    commit('updateExistingMedicinskaOsoba', response);
  },

  async removeMedicinskaOsoba({state, commit}, idOsobe){
    await osoblje.removeMedicinskaOsoba(state.klinika.id, idOsobe);
    commit('deleteMedicinskaOsoba', idOsobe);
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