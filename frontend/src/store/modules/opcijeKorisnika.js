const state = {
  optionDict: {
    'admin-klinike': [
      {
        title: 'Dashboard',
        action: 'mdi-view-dashboard',
        componentName: "home",
        items: []
      },
      { 
        title: 'Moj profil',
        action: 'mdi-account-circle-outline',
        componentName: 'tipovi_pregleda',
        items: []
      },
      {
        title: 'Klinika',
        action: 'mdi-hospital-building',
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
          },
          {
            title: 'Osoblje',
            action: 'mdi-alpha-o',
            componentName: 'osoblje'
          },
        ]
      }
      
    ],
    'pacijent': [
      {
        title: 'Dashboard',
        action: 'mdi-view-dashboard',
        componentName: "home",
        items: []
      },
      {
        title: 'Pacijent',
        action: 'mdi-account',
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