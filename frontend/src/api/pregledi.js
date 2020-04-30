import axios from 'axios';
import util from './util';

export default{
  async getAllPregledi(idKlinike){
    let options = util.prepareOptions();
    let response = await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/pregled/${idKlinike}`,
      options
    )
    return response.data;
  },
}