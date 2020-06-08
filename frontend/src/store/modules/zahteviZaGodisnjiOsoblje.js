import zahteviZaGodisnji from '@/api/zahteviZaGodisnji';

const state = {
  poslatiZahteviZaGodisnji: [], //zahteve koje je clan osoblja poslao adminu, a admin ih jos nije obradio
  neobradjeniZahteviZaGodisnji: [], //zahteve koje je admin obradio, ali clan osoblja jos nije video odgovor
  idOsoblja: null,
  mojiOdobreniZahtevi: [],
}

const getters = {
  getPoslatiZahteviZaGodisnji: (state) => state.poslatiZahteviZaGodisnji,
  getNeobradjeniZahteviZaGodisnji: (state) => state.neobradjeniZahteviZaGodisnji,
  getMojiOdobreniZahtevi: (state) => state.mojiOdobreniZahtevi,
}

const actions = {
  async fetchMojiZahtevi({commit, rootGetters}){
    let resp = rootGetters['profil/getProfil'].id;
    commit('setIdOsoblja', resp);

    let data = await zahteviZaGodisnji.fetchPoslatiZahteviZaGodisnji(resp);
    commit('setPoslatiZahteviZaGodisnji', data);

    let data2 = await zahteviZaGodisnji.fetchNeobradjeniZahteviZaGodisnji(resp);
    commit('setNeobradjeniZahteviZaGodisnji', data2);
  },

  async addZahtev({commit}, zahtev){
    let data = await zahteviZaGodisnji.addZahtev(zahtev);
    commit('addNewZahtev', data);
  },

  async obradiOsoblje({commit}, idZahteva){
    await zahteviZaGodisnji.obradiOsoblje(idZahteva);
    commit('obradi', idZahteva);
  },

  async fetchAllZahtevi({commit, rootGetters}){
    //setuj id lekara ili sestre kako bi se pravilno filtrirali zahtevi
    let resp = rootGetters['profil/getProfil'].id;
    commit('setIdOsoblja', resp);
    //ucitaj sve zahteve i sortiraj ih u mutacijama
    let klinikaId = rootGetters['klinike/getKlinikaOsoblja'].id;
    var data = await zahteviZaGodisnji.fetchAllZahtevi(klinikaId);  
    commit('setMojiOdobreniZahtevi', data);
  }
}

const mutations = {
  setIdOsoblja: (state, idOsoblja) => state.idOsoblja = idOsoblja,
  setPoslatiZahteviZaGodisnji: (state, poslatiZahteviZaGodisnji) => state.poslatiZahteviZaGodisnji = poslatiZahteviZaGodisnji,
  setNeobradjeniZahteviZaGodisnji: (state, neobradjeniZahteviZaGodisnji) => state.neobradjeniZahteviZaGodisnji = neobradjeniZahteviZaGodisnji,
  obradi: (state, idZahteva) => {
    let index = state.neobradjeniZahteviZaGodisnji.findIndex(x => x.id == idZahteva);
    if(index != -1)
      state.neobradjeniZahteviZaGodisnji.splice(index, 1);
  },
  addNewZahtev: (state, zahtev) => state.poslatiZahteviZaGodisnji.push(zahtev),
  setMojiOdobreniZahtevi: (state, zahtevi) => 
    state.mojiOdobreniZahtevi = zahtevi.filter( (zahtev) => 
      zahtev.odobreno == true && zahtev.radniKalendar.id == state.idOsoblja )
  ,  
}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
