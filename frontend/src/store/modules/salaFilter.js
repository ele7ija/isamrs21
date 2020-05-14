const state = {
  dateTimeStart: null,
  dateTimeEnd: null
};

const getters = {
  dateTimeStart: (state) => state.dateTimeStart,
  dateTimeEnd: (state) => state.dateTimeEnd,
};

const actions = {};

const mutations = {
  setDateTimeStart: (state, dateTimeStart) => state.dateTimeStart = dateTimeStart,
  setDateTimeEnd: (state, dateTimeEnd) => state.dateTimeEnd = dateTimeEnd,
  reset: (state) => {
    state.dateTimeStart = null;
    state.dateTimeEnd = null;
  }
};

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
};