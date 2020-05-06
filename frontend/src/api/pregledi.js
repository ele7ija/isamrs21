import axios from 'axios';
import util from './util';

export default{
  async getAllPregledi(idKlinike){
    let options = util.prepareOptions();
    let response = await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/pregled/${idKlinike}/slobodni`,
      options
    )
    return response.data;
  },

  addPregled(idKlinike, pregled){
    //vraca promise koji se awaituje, konacan rezultat je customResponse objekat(pogledati backend)
    let options = util.prepareOptions();
    return axios.post(
      `${process.env.VUE_APP_BACKEND_ROOT}/pregled/${idKlinike}`,
      pregled,
      options
    ).then(response => response.data).catch(error => error.response.data);
  },

  updatePregled(idKlinike, pregled){
    //vraca promise koji se awaituje, konacan rezultat je customResponse objekat(pogledati backend)
    let options = util.prepareOptions();
    return axios.put(
      `${process.env.VUE_APP_BACKEND_ROOT}/pregled/${idKlinike}/${pregled.id}`,
      pregled,
      options
    ).then(response => response.data).catch(error => error.response.data);
  },

  deletePregled(idKlinike, idPregleda){
    let options = util.prepareOptions();
    return axios.delete(
      `${process.env.VUE_APP_BACKEND_ROOT}/pregled/${idKlinike}/${idPregleda}`,
      options
    ).then(response => response.data).catch(error => error.response.data);
  },
}