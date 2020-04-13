import axios from 'axios';

export default{
  async getAllKlinike(){
    let response = await axios.get(
      `http://localhost:8080/api/klinika`
    )
    return response.data;
  },

  async getKlinika(idKlinike){
    let response = await axios.get(
      `http://localhost:8080/api/klinika/${idKlinike}`
    );
    return response.data;
  },
  
  async addKlinika(klinika){
    let response = await axios.post(
      `http://localhost:8080/api/klinika`,
      klinika
    );
    return response.data;
  },
  
  async updateKlinika(klinika){
    let response = await axios.put(
      `http://localhost:8080/api/klinika/${klinika.id}`,
      klinika
    );
    return response.data;
  },
  
  async removeKlinika(idKlinike){
    let response = await axios.delete(
      `http://localhost:8080/api/klinika/${idKlinike}`
    );
    return response.data;
  },
}