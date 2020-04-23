import axios from 'axios';
import store from '../store/index';

export default {
  async getAllKorisnici() {
    // dobavi trenutno ulogovanog korisnika zbog accessTokena
    let korisnik = store.getters['korisnici/getKorisnik'];
    if (korisnik == null){
      alert('Poruka od: "api/pacijenti/getAllPacijenti". There is no access token (you are not logged in). This should have been stopped by the router.')
      return;
    }
    const options = {
      headers: {
        "Authorization": `Bearer ${korisnik.accessToken}`
      }};
    let response = await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/pacijenti/all`,
      options)
    return response.data;
  },
}