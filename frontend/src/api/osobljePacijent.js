import axios from 'axios';
import util from './util';

export default{
  async pronadjiLekare(idKlinike) {
    let options = util.prepareOptions();
    return axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/medicinskaOsoba/${idKlinike}/lekari`,
      options
    );
  }
}