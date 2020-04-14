import axios from 'axios';

export default {
  async getAllKorisnici() {
    let response = await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/pacijenti/all`)
    return response.data;
  },
}