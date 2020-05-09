import axios from 'axios';
import util from './util';

export default{
  async getAllTipoviPregleda(idKlinike) {
    let options = util.prepareOptions();
    let {data} = await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/tipPregleda/${idKlinike}`,
      options
    );
    return data;
  },
  async addTipPregleda(idKlinike, tipPregleda){
    let options = util.prepareOptions();
    let {data} = await axios.post(
      `${process.env.VUE_APP_BACKEND_ROOT}/tipPregleda/${idKlinike}`,
      tipPregleda,
      options
    );
    return data;
  },
  updateTipPregleda(idKlinike, tipPregleda){
    let options = util.prepareOptions();
    return axios.put(
      `${process.env.VUE_APP_BACKEND_ROOT}/tipPregleda/${idKlinike}/${tipPregleda.id}`,
      tipPregleda,
      options
    );
  },
  
  removeTipPregleda(idKlinike, idTipaPregleda){
    let options = util.prepareOptions();
    return axios.delete(
      `${process.env.VUE_APP_BACKEND_ROOT}/tipPregleda/${idKlinike}/${idTipaPregleda}`,
      options
    );
  }
}