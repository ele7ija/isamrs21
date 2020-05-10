import axios from 'axios';
import util from './util';

export default{
  async getAllCenovnici(idKlinike) {
    let options = util.prepareOptions();
    let {data} = await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/cenovnik/${idKlinike}`,
      options
    );
    return data;
  },
  async addCenovnik(idKlinike, cenovnik){
    let options = util.prepareOptions();
    let {data} = await axios.post(
      `${process.env.VUE_APP_BACKEND_ROOT}/cenovnik/${idKlinike}`,
      cenovnik,
      options
    );
    return data;
  },
  updateCenovnik(idKlinike, cenovnik){
    let options = util.prepareOptions();
    return axios.put(
      `${process.env.VUE_APP_BACKEND_ROOT}/cenovnik/${idKlinike}/${cenovnik.id}`,
      cenovnik,
      options
    );
  },
  removeCenovnik(idKlinike, idCenovnika, version){
    let options = util.prepareOptions();
    return axios.delete(
      `${process.env.VUE_APP_BACKEND_ROOT}/cenovnik/${idKlinike}/${idCenovnika}?version=${version}`,
      options
    );  
  }
}