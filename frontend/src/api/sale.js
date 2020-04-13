import axios from 'axios';

export default{
  async getAllSale(idKlinike) {
    let response = await axios.get(
      `http://localhost:8080/api/sala/${idKlinike}`
    );
    return response.data;
  },
  
  async addSala(idKlinike, sala){
    let response = await axios.post(
      `http://localhost:8080/api/sala/${idKlinike}`,
      sala
    );
    return response.data;
  },

  async updateSala(idKlinike, sala){
    let response = await axios.put(
      `http://localhost:8080/api/sala/${idKlinike}/${sala.id}`,
      sala
    );
    return response.data;
  },

  async removeSala(idKlinike, idSale){
    let response = await axios.delete(
      `http://localhost:8080/api/sala/${idKlinike}/${idSale}`,
    );
    return response.data;
  },
}