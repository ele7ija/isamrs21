import axios from 'axios';

export default {
  async getAllKorisnici() {
    let response = await axios.get('http://localhost:8080/api/pacijenti')
    return response.data;
  }
}