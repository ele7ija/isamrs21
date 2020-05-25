import axios from 'axios';
import util from './util';

export default{
  async fetchAllZahtevi(idKlinike){
    let options = util.prepareOptions();
    let response = await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/zahtevZaGodisnji/${idKlinike}`,
      options
    );
    return response.data;
  },
  
  async fetchPoslatiZahteviZaGodisnji(idOsoblja){
    let options = util.prepareOptions();
    let response = await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/zahtevZaGodisnji/poslati/${idOsoblja}`,
      options
    );
    return response.data;
  },

  async fetchNeobradjeniZahteviZaGodisnji(idOsoblja){
    let options = util.prepareOptions();
    let response = await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/zahtevZaGodisnji/neobradjeni/${idOsoblja}`,
      options
    );
    return response.data;
  },

  async addZahtev(zahtev){
    let options = util.prepareOptions();  
    let response = await axios.post(
      `${process.env.VUE_APP_BACKEND_ROOT}/zahtevZaGodisnji`,
      zahtev,
      options
    );
    return response.data;
  },

  updateZahtev(zahtev){
    let options = util.prepareOptions();
    return axios.put(
      `${process.env.VUE_APP_BACKEND_ROOT}/zahtevZaGodisnji/${zahtev.id}`,
      zahtev,
      options
    );
  },

  obradiOsoblje(idZahteva){
    let options = util.prepareOptions();
    return axios.put(
      `${process.env.VUE_APP_BACKEND_ROOT}/zahtevZaGodisnji/obradiOsoblje/${idZahteva}`,
      {},
      options
    );
  }
}