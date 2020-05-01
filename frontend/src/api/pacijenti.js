import axios from 'axios';
import util from './util'

export default {
  async getAllKorisnici() {
    // dobavi trenutno ulogovanog korisnika zbog accessTokena
    let options = util.prepareOptions();
    let response = await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/pacijenti/all`,
      options)
    return response.data;
  }
}