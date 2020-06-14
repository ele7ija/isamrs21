import zahteviAPI from '@/api/zahteviZaRegistraciju'
const state = {
  zahtevi: [],
  podnetiZahtev: null,
}

const getters = {
  getAllZahtevi: (state) => state.zahtevi
}

const actions = {
  async fetchAllZahtevi({commit}){
    let data = await zahteviAPI.fetchAllZahtevi();
    commit('setZahtevi', data);
  },

  async prihvatiZahtev({commit}, zahtev){
    return new Promise((resolve, reject) => {
      zahteviAPI.prihvatiZahtev(zahtev)
      .then(({data:{result, success, message}}) =>{
        //ako je response uspesan
        if(success){
          commit('deleteZahtev', result);
          resolve(message);
        }
        //ako response nije bio uspesan
        else{
          reject(message);
        }
      });
    });
    
  },

  async odbijZahtev({commit}, zahtev){
    return new Promise((resolve, reject) => {
      zahteviAPI.odbijZahtev(zahtev)
      .then(({data:{result, message, success}}) => {
        if(success){
          commit('deleteZahtev', result);
          resolve(message);
        }else{
          reject(message);
        }
      });
    });
  }, 
  
  async podnesiZahtev({commit}, zahtev) {
    zahteviAPI.podnesiZahtev(zahtev);
    commit('setPodnetiZahtev', zahtev)
  },

  setMailSending({commit}, bool){
    commit('setMailSending', bool);
  },
  setMailSent({commit}, bool){
    commit('setMailSent', bool);
  },
  setMailNotSent ({commit}, bool){
    commit('setMailNotSent', bool);
  },

}

const mutations = {
  setZahtevi: (state, zahtevi) => {
    for (var item of zahtevi){
      var d= new Date(item.datumPodnosenja);
      item.datumString = d.toLocaleDateString("sr");
      item.vremeString =  d.toLocaleTimeString("sr");
    }
    state.zahtevi = zahtevi
  },
  //napraviti mutacije za prihvatiZahtev i odbijZahtev
  deleteZahtev: (state,zahtevToDelete) => {
    state.zahtevi = state.zahtevi.filter(
      function(zahtev){
        return zahtev.id != zahtevToDelete.id;
      }
    );
  },
  setPodnetiZahtev: (state, nesto) => state.podnetiZahtev = nesto

}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
