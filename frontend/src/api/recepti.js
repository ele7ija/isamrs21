import axios from 'axios';
import util from './util';

export default{
  async fetchAllRecepti() {
    let options = util.prepareOptions();
    return await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/recept/`,
      options,
    );
  },
}