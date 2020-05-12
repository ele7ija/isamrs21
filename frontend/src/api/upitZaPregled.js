import axios from 'axios';
import util from './util';

export default{
  async kreirajUpit(obj) {
    let options = util.prepareOptions();
    return axios.post(
      `${process.env.VUE_APP_BACKEND_ROOT}/upit`,
      obj,
      options
    );
  },

  async pronadjiNeodobreneUpite() {
    let korisnik = util._getKorsinik();
    let options = util.prepareOptions();
    return axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/upit/neodobreni/${korisnik.username}`,
      options
    );
  },

  async pronadjiNepotvrdjeneUpite() {
    let korisnik = util._getKorsinik();
    let options = util.prepareOptions();
    return axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/upit/nepotvrdjeni/${korisnik.username}`,
      options
    );
  },

  async potvrdiUpit(id, verzija) {
    let options = util.prepareOptions();
    return axios.put(
      `${process.env.VUE_APP_BACKEND_ROOT}/upit/potvrdi/${id}/${verzija}`,
      {}, //obavezno prvo body da ne bi header bio protumacen kao body
      options
    );
  },

  async odustaniUpit(id, verzija) {
    let options = util.prepareOptions();
    return axios.put(
      `${process.env.VUE_APP_BACKEND_ROOT}/upit/odustani/${id}/${verzija}`,
      {}, //obavezno prvo body da ne bi header bio protumacen kao body
      options
    );
  }
}