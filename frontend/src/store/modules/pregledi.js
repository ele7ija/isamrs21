/* Deo store-a vezan za operacije sa neautentifikovanim
  korisnikom - login i registracija. 
  Ali i za dobavljanje svih korisnika cele aplikacije*/
  import preglediAPI from '@/api/pregledi'
  
  
  const state = {
    pregledi: []
  }
  
  const getters = {
  }
  
  const actions = {
    // ne radi jos uvek nista
    async loadAllPregledi({commit}, idKlinike){
      let data = await preglediAPI.getAllPregledi(idKlinike);
      commit('setPregledi', data)
    },
  }
  
  const mutations = {
    setPregledi: (state, pregledi) => state.pregledi = pregledi
  }
  
  export default{
    namespaced: true,
    state,
    getters,
    actions,
    mutations
  }