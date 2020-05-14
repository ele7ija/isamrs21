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

  async prihvatiZahtev({dispatch,commit}, zahtev){
    //mejl se salje
    dispatch('setMailSending', true);
    zahteviAPI.prihvatiZahtev(zahtev)
    .then((response) =>{
      //nakon sto je zavrseno na beku, mejl se vise ne salje
      dispatch('setMailSending', false);
      var httpStatus = response.status;
      //ako je response uspesan
      if(httpStatus === 200){
        console.log("response: ", response.data);
        commit('deleteZahtev', response.data);
        dispatch('setMailSent', true);
      }
      //ako response nije bio uspesan
      else{
        console.log("response: ", response.data);
        dispatch('setMailNotSent', true);
      }
    })
    .catch( () =>{
      console.log("inside catch") 
    })
  },

  async odbijZahtev({dispatch,commit}, zahtev){
    //mejl se salje
    dispatch('setMailSending', true);
    zahteviAPI.odbijZahtev(zahtev)
    .then((response) =>{
      //nakon sto je zavrseno na beku, mejl se vise ne salje
      dispatch('setMailSending', false);
      var httpStatus = response.status;
      //ako je response bio uspesan onda je mailSent = true
      if(httpStatus === 200){
        console.log("response:", response.data)
        commit('deleteZahtev', response.data);
        dispatch('setMailSent', true);
      }
      //ako response nije bio uspesan onda je mailNotSent = true
      else{
        console.log("response: ", response.data)
        dispatch('setMailNotSent', true);
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
