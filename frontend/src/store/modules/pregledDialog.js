const state = {
  pocetak: null,
  kraj: null,
  tipPregleda: null,
  lekar: null,
  sala: null,
  cena: null,
  popust: null,
  konacnaCena: null
};

const getters = {
  getPocetak: (state) => state.pocetak,
  getKraj: (state) => state.kraj,
  getTipPregleda: (state) => state.tipPregleda
};

const actions = {};

const mutations = {
  setPocetak: (state, pocetak) => state.pocetak = pocetak,
  setKraj: (state, kraj) => state.kraj = kraj,
  setTipPregleda: (state, tipPregleda) => state.tipPregleda = tipPregleda,
  setLekar: (state, lekar) => state.lekar = lekar,
  setSala: (state, sala) => state.sala = sala,
  setCena: (state, cena) => state.cena = cena,
  setPopust: (state, popust) => state.popust = popust,
  setKonacnaCena: (state, konacnaCena) => state.konacnaCena = konacnaCena
};

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}