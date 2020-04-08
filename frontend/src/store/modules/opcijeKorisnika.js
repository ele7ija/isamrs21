const state = {
  optionDict: {
    'adminKlinike': [
      {
        title: 'Klinika',
        action: 'mdi-hospital-building',
        items: [
          { 
            title: 'Tipovi pregleda',
            action: 'mdi-hospital',
            componentName: 'tipovi_pregleda'
          }
        ]
      }
    ]
  }
}
const getters = {
  getOptions: (state) => (userType) => {
    return state.optionDict[userType];
  }
}
const actions = {}
const mutations = {}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}