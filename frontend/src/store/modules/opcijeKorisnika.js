const state = {
  optionDict: {
    'admin-klinike': [
      {
        title: 'Klinika',
        action: 'mdi-hospital-building',
        dashboard_item: {
          title: 'Dashboard',
          action: 'mdi-view-dashboard',
          path: "/home/adminKlinike"
        },
        items: [
          { 
            title: 'Tipovi pregleda',
            action: 'mdi-alpha-t',
            componentName: 'tipovi_pregleda'
          },
          {
            title: 'Sale',
            action: 'mdi-alpha-s',
            componentName: 'sala'
          }
        ]
      }
    ],
    'pacijent': [
      {
        title: 'Pacijent',
        action: 'mdi-hospital-building',
        dashboard_item: {
          title: 'Dashboard',
          action: 'mdi-view-dashboard',
          path: "/home/adminKlinike"
        },
        items: [
          { 
            title: 'Profil',
            action: 'mdi-alpha-t',
            componentName: 'profil'
          },
          {
            title: 'Pacijenti',
            action: 'mdi-alpha-s',
            componentName: 'pacijenti'
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