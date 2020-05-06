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
  obradiAdmin(idKlinike, upit){
    let options = util.prepareOptions();
    return axios.put(
      `${process.env.VUE_APP_BACKEND_ROOT}/upit/obradiAdmin/${idKlinike}/${upit.id}`,
      upit,
      options
    );
  },

  deleteUpit(idKlinike, idUpita){
    let options = util.prepareOptions();
    return axios.delete(
      `${process.env.VUE_APP_BACKEND_ROOT}/upit/${idKlinike}/${idUpita}`,
      options
    );
  }
}