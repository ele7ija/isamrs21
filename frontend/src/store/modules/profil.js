import korisnikAPI from '@/api/korisnici';

const state = {
  profil: null, //celokupni podaci ulogovanog korisnika, dobavljaju se samo pri izmeni profila korisnika
  copy: null
};

const getters = {
  getProfil: (state) => state.profil,
  getCopy: (state) => state.copy,
  getPoslednjaPromenaSifre: (state) => state.profil.poslednjaPromenaSifre
};

const actions = {
  async updateProfil({state, rootGetters, commit}, {staraSifra, novaSifra}){
    let role = rootGetters["korisnici/getKorisnik"].role;
    let profil = JSON.parse(JSON.stringify(state.copy));
    let poslednjaSifra = rootGetters["korisnici/_getKorsinik"].password; //uzimamo ovu jer je nehesirana
    return new Promise((resolve, reject) => {
      if(profil.sifra != '' || novaSifra != ''){
        //korisnik je menjao sifru, proveri da li je sve ok
        if(poslednjaSifra != staraSifra){
          reject("Stara sifra nije ispravna");
          return;
        }
        if(profil.sifra != novaSifra){
          reject("Nove sifre koje ste uneli se ne poklapaju");
          return;
        }
      }else{
        //korisnik nije menjao sifru, proveri da li je stara sifra ispravna i postavi sifru na staru vrednost
        if(poslednjaSifra != staraSifra){
          reject("Stara sifra nije ispravna");
          return;
        }
        profil.sifra = rootGetters["korisnici/_getKorsinik"].password; //uzimamo ovu jer je nehesirana
      }
      poslednjaSifra = state.profil.sifra; //stara, hesirana sifra, potrebna za bek
      korisnikAPI.updateProfil({profil, poslednjaSifra, role})
      .then(({data:{result, success, message}}) => {
        if(success){
          commit('setProfil', result);
          commit("_setCopy", JSON.parse(JSON.stringify(result)));
          commit("korisnici/mutateKorisniciArray", result, {root: true});
          if(novaSifra != '')
            commit("korisnici/setNewSifra", novaSifra, {root: true});
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
  setCopy: (state, copy) => state.copy = copy, //poziva se iz .vue fajla
  setCopySifra: (state, sifra) => state.copy.sifra = sifra //poziva se iz .vue fajla
};

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
};