import axios from 'axios';
import util from './util';

export default{
  async fetchAllRecepti() {
    let options = util.prepareOptions();
    return axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/recept`,
      options,
    );
  },
  async overiRecept(recept) {
    let options = util.prepareOptions();
    return axios.put(
      `${process.env.VUE_APP_BACKEND_ROOT}/recept/`,
      recept,
      options,
    ) 
  }
}