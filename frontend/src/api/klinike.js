import axios from 'axios';
import util from './util';

export default{
  async getAllKlinike(){
    let options = util.prepareOptions();
    let response = await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/klinika`,
      options
    )
    return response.data;
  },

  async getKlinika(idKlinike){
    let options = util.prepareOptions();
    let response = await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/klinika/${idKlinike}`,
      options
    );
    return response.data;
  },
  
  async addKlinika(klinika){
    let options = util.prepareOptions();
    let response = await axios.post(
      `${process.env.VUE_APP_BACKEND_ROOT}/klinika`,
      klinika,
      options
    );
    return response.data;
  },
  
  async updateKlinika(klinika){
    let options = util.prepareOptions();
    let response = await axios.put(
      `${process.env.VUE_APP_BACKEND_ROOT}/klinika/${klinika.id}`,
      klinika,
      options
    );
    return response.data;
  },
  
  async removeKlinika(idKlinike){
    let options = util.prepareOptions();
    let response = await axios.delete(
      `${process.env.VUE_APP_BACKEND_ROOT}/klinika/${idKlinike}`,
      options
    );
    return response.data;
  },

  async fetchKlinikaAdmina(){
    let options = util.prepareOptions();
    let response = await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/klinika/klinikaUlogovanogKorisnika`,
      options
    );
    return response.data;
  }
}