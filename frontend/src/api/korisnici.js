import axios from 'axios';

export default {
  async registrujKorisnika(korisnik) {
    let response = await axios.post(
      `${process.env.VUE_APP_BACKEND_ROOT}/korisnici/registracija`,
      korisnik
    )
    return response.data;
  }
}