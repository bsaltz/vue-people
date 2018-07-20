import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    pageNumber: 1,
    sortBy: 'name',
    sortDesc: false
  },
  mutations: {
    setPageNumber(state, pageNumber) {
      state.pageNumber = pageNumber
    },
    setSortBy(state, sortBy) {
      state.sortBy = sortBy
    },
    setSortDesc(state, sortDesc) {
      state.sortDesc = sortDesc
    }
  }
})
