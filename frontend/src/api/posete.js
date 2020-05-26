import axios from 'axios';
import util from './util';

export default{
  async updatePoseta(poseta){
    console.log("poseta"), poseta;
    let options = util.prepareOptions();
    let response = await axios.put(
      `${process.env.VUE_APP_BACKEND_ROOT}/posete`,
      poseta,
      options
    );
    return response;
  },
}