<!-- Login page -->

<template>
  <div>
    <v-container>
      <v-row align="center" justify="center">
        <v-col
          lg='4'
          md='6'
          sm='6'
          class='pt-6'>
        <v-card>
          <v-card-text class='pt-8'>
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
              @keydown.enter="myLogin({
                username: email, 
                password: password})"
              outlined>
            </v-text-field>
            <v-btn @click="myLogin({
              username: email, 
              password: password})"
              color='primary'>
                Login
            </v-btn>
          </v-form>
          </v-card-text>
        </v-card>
        </v-col>
      </v-row>
    </v-container>
    <v-snackbar
      v-model="snackbarErr"
      :timeout="snackbarTimeout"
      color="red darken-3"
    >
      {{ snackbarText }}
      <v-btn
        color="grey darken-3"
        text
        @click="snackbarErr = false"
      >
        Close
      </v-btn>
    </v-snackbar>
    <v-snackbar
      v-model="snackbarSucc"
      :timeout="snackbarTimeout"
      color="green darken-3"
    >
      {{ snackbarText }}
      <v-btn
        color="grey darken-3"
        text
        @click="snackbarSucc = false"
      >
        Close
      </v-btn>
    </v-snackbar>
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
      password: null,
      snackbarErr: false,
      snackbarSucc: false,
      snackbarTimeout: 2000,
      snackbarText: null,
    }
  },
  methods: {
    ...mapActions({
      loginuj: 'korisnici/loginKorisnik'
    }),
    myLogin: function(userObj) {
      this.loginuj(userObj)
      .then((message) => {
        this.snackbarText = message;
        this.snackbarSucc = true;
      }, (message) => {
        this.snackbarText = message;
        this.snackbarErr = true;
      })
    }
  }
}
</script>

<style>

</style>