import store from '../store/index';

export default{
  prepareOptions(){
    let korisnik = store.getters['korisnici/getKorisnik'];
    return{
      headers: {
        "Authorization": `Bearer ${korisnik.accessToken}`
      }
    };
  }
}