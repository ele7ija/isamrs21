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

  async addAdminKlinike(adminKlinike){
    let options = util.prepareOptions();
    let response = await axios.post(
      `${process.env.VUE_APP_BACKEND_ROOT}/admin_klinike`,
      adminKlinike,
      options
    );
    return response.data;
  },
}