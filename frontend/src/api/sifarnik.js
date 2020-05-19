import axios from 'axios'
import util from './util'

export default{
  async fetchAllDijagnozeLekovi(){
    let options = util.prepareOptions()
    let response = await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/sifarnik`,
      options
    );
    return response;
  },

  async addDijagnozaIliLek(dijagnozaIliLek){
    console.log("sifarnik.js api")
    let options = util.prepareOptions()
    let response = await axios.post(
      `${process.env.VUE_APP_BACKEND_ROOT}/sifarnik`,
      dijagnozaIliLek,
      options
    );
    return response;
  }
}