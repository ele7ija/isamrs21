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
        action: 'mdi-account',
        dashboard_item: {
          title: 'Dashboard',
          action: 'mdi-view-dashboard',
          path: "/home/adminKlinike"
        },
        items: [
          {
            title: 'Pacijenti',
            action: 'mdi-account-circle-outline',
            componentName: 'pacijenti'
          },
          { 
            title: 'Klinike',
            action: 'mdi-hospital-building',
            componentName: 'pacijent-klinike'
          },
          { 
            title: 'Istorija',
            action: 'mdi-history',
            componentName: 'pacijent-istorija'
          },
          { 
            title: 'Zdravstveni karton',
            action: 'mdi-bottle-tonic-plus',
            componentName: 'pacijent-zdravstveni-karton'
          },
          { 
            title: 'Profil',
            action: 'mdi-account-cog',
            componentName: 'pacijent-profil'
          },
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