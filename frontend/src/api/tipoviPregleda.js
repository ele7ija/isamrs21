import axios from 'axios';

export default{
  async getAllTipoviPregleda(idKlinike) {
    let response = await axios.get(
      `http://localhost:8080/api/tipPregleda/${idKlinike}`
    );
    return response.data;
  },
  async addTipPregleda(idKlinike, tipPregleda){
    let response = await axios.post(
      `http://localhost:8080/api/tipPregleda/${idKlinike}`,
      tipPregleda
    );
    return response.data;
  },
  async updateTipPregleda(idKlinike, tipPregleda){
    let response = await axios.put(
      `http://localhost:8080/api/tipPregleda/${idKlinike}/${tipPregleda.id}`,
      tipPregleda
    );
    return response.data;
  },
  async removeTipPregleda(idKlinike, idTipaPregleda){
    let response = await axios.delete(
      `http://localhost:8080/api/tipPregleda/${idKlinike}/${idTipaPregleda}`,
    );
    return response.data;
  },
}