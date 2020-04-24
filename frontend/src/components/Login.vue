<!-- Login page -->

<template>
  <div>
    <v-container>
      <v-row align="center" justify="center">
        <v-col
          lg='4'
          md='6'
          sm='6'>
        <v-card>
          <v-card-title>
            Login
          </v-card-title>
          <v-card-text>
          <v-form 
            ref="loginForm">
            <v-text-field 
              type="text" 
              label="E-mail" 
              v-model="email" 
              prepend-icon="person" 
              :rules="inputRules"
              @keydown.enter="loginuj({
                username: email, 
                password: password})"
              outlined>
            </v-text-field>
            <v-text-field 
              type="password" 
              label="Password" 
              v-model="password" 
              prepend-icon="lock" 
              :rules="inputRules"
              @keydown.enter="loginuj({
                username: email, 
                password: password})"
              outlined>
            </v-text-field>
            <v-btn @click="loginuj({
              username: email, 
              password: password})"
              color='success'>
                Login
            </v-btn>
          </v-form>
          </v-card-text>
        </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import {mapActions} from 'vuex'

export default {
  name: "Login",
  data() {
    return {
      inputRules: [
        v => (v && v.length > 0) || 'Please fill out this field'
      ],
      email: null,
      password: null
    }
  },
  methods: {
    ...mapActions({
      loginuj: 'korisnici/loginKorisnik'
    })
  }
}
</script>

<style>

</style>