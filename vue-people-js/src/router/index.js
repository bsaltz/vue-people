import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import Persons from '@/components/Persons'
import Person from '@/components/Person'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component: HelloWorld
    },
    {
      path: '/persons',
      name: 'Persons',
      component: Persons
    },
    {
      path: '/persons/:id',
      name: 'Person',
      component: Person
    }
  ]
})
