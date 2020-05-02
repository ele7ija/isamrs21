import axios from 'axios';
import util from './util';

export default{
  async getAllOsoblje(idKlinike) {
    let options = util.prepareOptions();
    let response = await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/medicinskaOsoba/${idKlinike}`,
      options
    );
    return response.data;
  },
  async addMedicinskaOsoba(idKlinike, osoba){
    let options = util.prepareOptions();
    let response = await axios.post(
      `${process.env.VUE_APP_BACKEND_ROOT}/medicinskaOsoba/${idKlinike}`,
      osoba,
      options
    );
    return response.data;
  },
  async updateExistingMedicinskaOsoba(idKlinike, osoba){
    let options = util.prepareOptions();
    let response = await axios.put(
      `${process.env.VUE_APP_BACKEND_ROOT}/medicinskaOsoba/${idKlinike}/${osoba.id}`,
      osoba,
      options
    );
    return response.data;
  },
  async addSpecijalnostiMedicinskaOsoba(idKlinike, {idLekara, idTipovaPregleda}){
    let options = util.prepareOptions();
    let response = await axios.put(
      `${process.env.VUE_APP_BACKEND_ROOT}/medicinskaOsoba/${idKlinike}/specijalnosti/${idLekara}`,
      idTipovaPregleda,
      options
    );
    return response.data;
  },
  removeSpecijalnostiMedicinskaOsoba(idKlinike, {idLekara, idTipaPregleda}){
    let options = util.prepareOptions();
    return axios.delete(
      `${process.env.VUE_APP_BACKEND_ROOT}/medicinskaOsoba/${idKlinike}/specijalnosti/${idLekara}/${idTipaPregleda}`,
      options
    );
  },
  removeMedicinskaOsoba(idKlinike, idOsobe){
    let options = util.prepareOptions();
    return axios.delete(
      `${process.env.VUE_APP_BACKEND_ROOT}/medicinskaOsoba/${idKlinike}/${idOsobe}`,
      options
    );
  },
}