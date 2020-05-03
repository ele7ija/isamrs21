import axios from 'axios';
import util from './util';

export default{
  async getAllSale(idKlinike) {
    let options = util.prepareOptions();
    let response = await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/sala/${idKlinike}`,
      options
    );
    return response.data;
  },
  
  async addSala(idKlinike, sala){
    let options = util.prepareOptions();
    let response = await axios.post(
      `${process.env.VUE_APP_BACKEND_ROOT}/sala/${idKlinike}`,
      sala,
      options
    );
    return response.data;
  },

  async updateSala(idKlinike, sala){
    let options = util.prepareOptions();
    let response = await axios.put(
      `${process.env.VUE_APP_BACKEND_ROOT}/sala/${idKlinike}/${sala.id}`,
      sala,
      options
    );
    return response.data;
  },

  removeSala(idKlinike, idSale){
    let options = util.prepareOptions();
    return axios.delete(
      `${process.env.VUE_APP_BACKEND_ROOT}/sala/${idKlinike}/${idSale}`,
      options
    );
  },
}