import axios from 'axios';
import util from './util';

export default{
  async fetchAllRecepti() {
    let options = util.prepareOptions();
    let response = await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/recept`,
      options,
    );
    console.log(response.data);
    return response;
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