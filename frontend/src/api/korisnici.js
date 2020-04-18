import axios from 'axios';

export default {
  async registrujKorisnika(korisnik) {
    let response = await axios.post(
      `${process.env.VUE_APP_BACKEND_ROOT}/auth/registracija`,
      korisnik
    )
    return response.data;
  }
}