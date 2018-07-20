import Vue from 'vue'
import Router from 'vue-router'
import Persons from '@/components/Persons'
import Person from '@/components/Person'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Persons',
      component: Persons
    },
    {
      path: '/:id',
      name: 'Person',
      component: Person
    }
  ]
})
