import korisnikAPI from '@/api/korisnici';

const state = {
  profil: null, //celokupni podaci ulogovanog korisnika, dobavljaju se samo pri izmeni profila korisnika
  copy: null
};

const getters = {
  getProfil: (state) => state.profil,
  getCopy: (state) => state.copy
};

const actions = {
  async updateProfil({state, rootGetters, commit}){
    let role = rootGetters["korisnici/getKorisnik"].role;
    let profil = state.copy;
    let poslednjaSifra = state.profil.sifra;
    return new Promise((resolve, reject) => {
      korisnikAPI.updateProfil({profil, poslednjaSifra, role})
      .then(({data:{result, success, message}}) => {
        if(success){
          commit('setProfil', result);
          commit("_setCopy", JSON.parse(JSON.stringify(result)));
          commit("korisnici/mutateKorisniciArray", result, {root: true});
          resolve(message);
        }else{
          reject(message);
        }
      });
    });
  }
};

const mutations = {
  setProfil: (state, profil) => {
    state.profil = profil;
  },
  _setCopy: (state, data) => {
    state.copy = data;
    state.copy.sifra = '';
  },
  setCopy: (state, copy) => state.copy = copy //poziva se iz .vue fajla
};

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
};