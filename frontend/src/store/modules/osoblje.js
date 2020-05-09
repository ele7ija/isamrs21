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
  getPozicija: (state) => (id) => {
    let filtriraneOsobe = state.medicinskoOsoblje.filter(x => x.id == id);
    if(filtriraneOsobe.length == 0){
      return null;
    }else{
      return filtriraneOsobe[0].pozicija;
    }
  },

  getKlinika: (state) => {
    return state.klinika;
  },

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
    console.log(response);
  },

  async updateMedicinskaOsoba({state, commit}, osoba){
    let response = await osoblje.updateExistingMedicinskaOsoba(state.klinika.id, osoba);
    commit('updateExistingMedicinskaOsoba', response);
  },

  async removeSpecijalnostiMedicinskaOsoba({state, commit}, {idLekara, idTipaPregleda, version}){
    return new Promise((resolve, reject) => {
      osoblje.removeSpecijalnostiMedicinskaOsoba(state.klinika.id, {idLekara, idTipaPregleda, version})
      .then(({data: {success, message}}) => {
        if(success){
          commit('removeSpecijalnost', {idLekara, idTipaPregleda});
          commit('tipoviPregleda/removeSpecijalista', {idLekara, idTipaPregleda}, {root: true})
          resolve(message);
        }else{
          reject(message);
        }
      });
    });    
  },

  async addSpecijalnostiMedicinskaOsoba({state, commit}, {idLekara, idTipovaPregleda, version}){
    return new Promise((resolve, reject) => {
      osoblje.addSpecijalnostiMedicinskaOsoba(state.klinika.id, {idLekara, idTipovaPregleda, version})
      .then(({data:{result, success, message}}) => {
        if(success){
          let tipoviPregleda = result.tipovi_pregleda.filter(x => idTipovaPregleda.filter(i => i == x.id).length != 0);
          commit('addSpecijalnosti', {idLekara, tipoviPregleda});
          commit('tipoviPregleda/addSpecijalista', {idLekara, idTipovaPregleda}, {root: true});
          resolve(message);
        }else{
          reject(message);
        }
      });
    })    
  },

  async removeMedicinskaOsoba({state, commit, dispatch}, idOsobe){
    return new Promise((resolve, reject) => {
      osoblje.removeMedicinskaOsoba(state.klinika.id, idOsobe)
      .then(({data:{success, message}}) => {
        if(success){
          commit('deleteMedicinskaOsoba', idOsobe);
          dispatch('korisnici/removeKorisnik', idOsobe, {root: true});
          resolve(message);
        }else{
          reject(message);
        }
      });
    });
    
  },
  
}
const mutations = {
  setCurrentKlinika: (state, klinika) => state.klinika = klinika,
  setMedicinskoOsoblje: (state, medicinskoOsoblje) => state.medicinskoOsoblje = medicinskoOsoblje,
  addNewMedicinskaOsoba: (state, osoba) => state.medicinskoOsoblje.push(osoba),
  updateExistingMedicinskaOsoba: (state, osoba) => {
    let index = state.medicinskoOsoblje.findIndex(x => x.id == osoba.id);
    state.medicinskoOsoblje.slice(index, 1);
    state.medicinskoOsoblje.slice(index, 0, osoba);
  },
  deleteMedicinskaOsoba: (state, idOsobe) => state.medicinskoOsoblje = state.medicinskoOsoblje.filter(x => x.id != idOsobe),
  removeSpecijalnost: (state, {idLekara, idTipaPregleda}) => {
    let index = state.medicinskoOsoblje.findIndex(x => x.id == idLekara);
    state.medicinskoOsoblje[index].tipovi_pregleda = state.medicinskoOsoblje[index].tipovi_pregleda.filter(x => x.id != idTipaPregleda);
    state.medicinskoOsoblje[index].version += 1;
  },
  addSpecijalnosti: (state, {idLekara, tipoviPregleda}) => {
    let index = state.medicinskoOsoblje.findIndex(x => x.id == idLekara);
    state.medicinskoOsoblje[index].tipovi_pregleda.push(...tipoviPregleda);
    state.medicinskoOsoblje[index].version += 1;
  }

}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}