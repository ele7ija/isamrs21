import axios from 'axios';
import util from './util';

export default{
  async fetchAllZahtevi(){
    let options = util.prepareOptions();
    let response = await axios.get(
      `${process.env.VUE_APP_BACKEND_ROOT}/zahtevi_za_registraciju`,
      options
    );
    return response.data;
  },

  async odbijZahtev(zahtev){
    let options = util.prepareOptions();  
    return  axios.post(
      `${process.env.VUE_APP_BACKEND_ROOT}/zahtevi_za_registraciju/odbij`,
      zahtev,
      options
    )
  }
}