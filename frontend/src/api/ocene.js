import axios from 'axios';
import util from './util';

export default{
  posaljiOcenu(ocena){
    let options = util.prepareOptions();
    return axios.post(
      `${process.env.VUE_APP_BACKEND_ROOT}/ocena`,
      ocena,
      options
    );
  },

  pronadjiOceneKlinike(idKlinike){
    let options = util.prepareOptions();
    return axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/ocena/klinika/${idKlinike}`,
      options
    );
  },

  pronadjiOceneLekara(idLekara){
    let options = util.prepareOptions();
    return axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/ocena/lekar/${idLekara}`,
      options
    );
  },

  pronadjiOcenePacijenta(emailPacijenta){
    let options = util.prepareOptions();
    return axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/ocena/pacijent/${emailPacijenta}`,
      options
    );
  },
}