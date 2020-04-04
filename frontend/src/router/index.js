import Vue from 'vue';
import Router from 'vue-router';
import template_component from '@/components/TemplateComponent';

//Plugins
Vue.use(Router)

export default new Router({
  routes : [
    {
      path: '/template_component',
      name: 'template_component',
      component: template_component
    },
    {
      path: '',
      redirect: '/template_component'
    }
  ]
});