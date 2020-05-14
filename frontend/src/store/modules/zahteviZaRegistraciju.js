import zahteviAPI from '@/api/zahteviZaRegistraciju'
const state = {
  zahtevi: [],
  mailSending: false,
  mailSent: false,
  mailNotSent: false,
}

const getters = {
  getAllZahtevi: (state) => state.zahtevi,
  getMailSending: (state) =>state.mailSending,
  getMailSent: (state) =>state.mailSent,
  getMailNotSent: (state) => state.mailNotSent,
}

const actions = {
  async fetchAllZahtevi({commit}){
    let data = await zahteviAPI.fetchAllZahtevi();
    commit('setZahtevi', data);
  },

  async prihvatiZahtev({commit}, zahtev){
    let data = await zahteviAPI.prihvatiZahtev(zahtev);
    commit('prihvatiZahtev', data);
  },

  async odbijZahtev({commit}, zahtev){
    //mejl se salje
    commit('setMailSending', true);
    zahteviAPI.odbijZahtev(zahtev)
    .then((response) =>{
      console.log(response);
      //nakon sto je zavrseno na beku, mejl se vise ne salje
      commit('setMailSending', false);
      
      //ako je response bio uspesan onda je mailSent = true
      if(response != undefined){
        console.log("response is defined")
        commit('deleteZahtev', response.data);
        commit('setMailSent', true);
      }
      //ako response nije bio uspesan onda je mailNotSent = true
      else{
        console.log("response is undefined")
        commit('setMailNotSent', true);
      }
    })
    .catch( () =>{
      console.log("inside catch")
      
    })
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

  setMailSending: (state, bool) => state.mailSending = bool,
  setMailSent: (state, bool) => state.mailSent = bool,
  setMailNotSent: (state, bool) => state.mailNotSent = bool,

}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
