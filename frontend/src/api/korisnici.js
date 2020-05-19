import axios from 'axios';
import util from './util';

export default {
  async registrujKorisnika(korisnik) {
    let response = await axios.post(
      `${process.env.VUE_APP_BACKEND_ROOT}/auth/registracija`,
      korisnik
    )
    return response.data;
  },

  async loginujKorisnika(loginData) {
    let response = await axios.post(
      `${process.env.VUE_APP_BACKEND_ROOT}/auth/login`,
      loginData
    )
    return response.data;
  },

  async fetchAllKorisnici(){
    let options = util.prepareOptions();
    let response = await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/korisnici`,
      options
    )
    return response.data;
  },

  async fetchKorisnik(email){
    let options = util.prepareOptions();
    let response = await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/korisnici/profil?email=${email}`,
      options
    )
    return response.data;
  },

  updateProfil({profil, poslednjaSifra, role}){
    let options = util.prepareOptions();
    let url = `${process.env.VUE_APP_BACKEND_ROOT}/`;
    let obj = {
      poslednjaSifra
    };
    if(role == 'lekar'){
      url += `medicinskaOsoba/${profil.klinika.id}/lekar/profil`;
      obj.lekar = profil
      obj.lekar.pozicija = 'lekar';
    }
    else if(role == 'medicinska-sestra'){
      url += `medicinskaOsoba/${profil.klinika.id}/sestra/profil`;
      obj.sestra = profil;
      obj.sestra.pozicija = 'medicinska sestra';
    }
    else if(role == 'admin-klinike'){
      url += `admin_klinike/profil`;
      obj.adminKlinike = profil
    }
    //itd. za ostale role
    else if(role == 'admin-klinickog-centra') {
      url += `admin_centra/profil`;
      obj.admin = profil;
      
    }

    return axios.put(
      url,
      obj,
      options
    );
  },

  refresh_token(){
    let korisnik = util._getKorsinik();
    return new Promise((resolve, reject) => {
      axios
        .post(
          `${process.env.VUE_APP_BACKEND_ROOT}/auth/login`,
          korisnik,
        )
        .then(response => {
          resolve(response);
        })
        .catch(error => {
          reject(error);
        })
    });
  },


}