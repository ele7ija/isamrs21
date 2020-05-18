import axios from 'axios';
import util from './util';

export default {
  async fetchAllAdminiKlinike(){
    let options = util.prepareOptions();
    let response = await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/admin_klinike`,
      options
    )
    return response.data;
  },

  async addAdminKlinike(adminKlinike){
    let options = util.prepareOptions();
    let response = await axios.post(
      `${process.env.VUE_APP_BACKEND_ROOT}/admin_klinike`,
      adminKlinike,
      options
    );
    return response.data;
  },
  
}