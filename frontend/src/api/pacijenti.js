import axios from 'axios';

export default {
    async getAllKorisnici() {
        axios.get('http://localhost:8080/api/pacijent')
        .then(function(response){
            return response;
        })
        .catch(function(error){
            console.log(error);
        })
    }
}