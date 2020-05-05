import axios from 'axios';
import util from './util';

export default{
  async getAllUpiti(idKlinike) {
    let options = util.prepareOptions();
    let {data} = await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/upit/${idKlinike}`,
      options
    );
    return data;
  },
  async addUpit(idKlinike, upit){
    let options = util.prepareOptions();
    let {data} = await axios.post(
      `${process.env.VUE_APP_BACKEND_ROOT}/upit/${idKlinike}`,
      upit,
      options
    );
    return data;
  },
  async obradiAdmin(idKlinike, upit){
    let options = util.prepareOptions();
    let {data} = await axios.put(
      `${process.env.VUE_APP_BACKEND_ROOT}/upit/obradiAdmin/${idKlinike}/${upit.id}`,
      upit,
      options
    );
    return data;
  }
}