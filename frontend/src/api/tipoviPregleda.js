import axios from 'axios';
import util from './util';

export default{
  async getAllTipoviPregleda(idKlinike) {
    let options = util.prepareOptions();
    let response = await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/tipPregleda/${idKlinike}`,
      options
    );
    return response.data;
  },
  async addTipPregleda(idKlinike, tipPregleda){
    let options = util.prepareOptions();
    let response = await axios.post(
      `${process.env.VUE_APP_BACKEND_ROOT}/tipPregleda/${idKlinike}`,
      tipPregleda,
      options
    );
    return response.data;
  },
  async updateTipPregleda(idKlinike, tipPregleda){
    let options = util.prepareOptions();
    let response = await axios.put(
      `${process.env.VUE_APP_BACKEND_ROOT}/tipPregleda/${idKlinike}/${tipPregleda.id}`,
      tipPregleda,
      options
    );
    return response.data;
  },
  async removeTipPregleda(idKlinike, idTipaPregleda){
    let options = util.prepareOptions();
    let response = await axios.delete(
      `${process.env.VUE_APP_BACKEND_ROOT}/tipPregleda/${idKlinike}/${idTipaPregleda}`,
      options
    );
    return response.data;
  },
}