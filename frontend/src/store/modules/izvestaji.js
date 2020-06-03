import ocene from '@/api/ocene';
import pregledi from '@/api/pregledi';

const state = {
  klinika: null,
  prosecnaOcenaKlinike: null,
  prosecneOceneLekara: [],
  preglediKlinike: []
}

const getters = {
  getNazivKlinike: (state) => state.klinika.naziv,
  getProsecnaOcenaKlinike: (state) => state.prosecnaOcenaKlinike,
  getProsecneOceneLekara: (state) => state.prosecneOceneLekara,
  getPreglediKlinike: (state) => state.preglediKlinike
}

const actions = {
  fetchKlinikaUlogovanogKorisnika({commit, rootGetters}){
    let resp = rootGetters['klinike/getKlinikaAdmina'];
    commit('setCurrentKlinika', resp);
  },
  async fetchOceneKlinike({state, commit}){
    return new Promise(() => {
      ocene.pronadjiOceneKlinike(state.klinika.id)
      .then(({data}) => {
        let sum = 0;
        for(let ocena of data)
          sum += ocena.vrednost;
        
        let value = data.length == 0 ? "Klinika nema nijednu ocenu": sum / data.length;
        commit("setProsecnaOcenaKlinike", value);
      });
    });
  },
  async fetchOceneLekaraUnutarKlinike({state, commit}){
    return new Promise(() => {
      ocene.pronadjiLekareKlinike(state.klinika.id)
      .then(({data: {result}}) => {
        let values = [];
        for(let lekar of result){
          let sum = 0;
          for(let ocena of lekar.ocene)
            sum += ocena.vrednost;
          let value = lekar.ocene.length == 0 ? "Lekar nema nijednu ocenu": sum / lekar.ocene.length;
          values.push({lekar: lekar, prosecnaOcena: value});
        }
        commit("setProsecneOceneLekara", values);
      });
    });
  },
  async fetchRezervisaniPregledi({state, commit}){
    let data = await pregledi.getOdrzaniPregledi(state.klinika.id);
    commit("setPreglediKlinike", data);
  }
}

const mutations = {
  setCurrentKlinika: (state, klinika) => state.klinika = klinika,
  setProsecnaOcenaKlinike: (state, prosecnaOcenaKlinike) => state.prosecnaOcenaKlinike = prosecnaOcenaKlinike,
  setProsecneOceneLekara: (state, prosecneOceneLekara) => state.prosecneOceneLekara = prosecneOceneLekara,
  setPreglediKlinike: (state, preglediKlinike) => state.preglediKlinike = preglediKlinike
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}