<!-- Login page -->

<template>
  <div>
    Sadrzaj dala Login.vue komponenta. Ovo je homepage za neulog. korisnika <br>
    <v-btn 
      to="/registracija">
      Registracija
    </v-btn> <br> 
    <v-btn 
      @click.native='setGlobalLayout("pacijenti-layout")' 
      to="/pacijenti">
      Uloguj se kao pacijent
    </v-btn> <br> 
    <v-btn 
      @click.native='setGlobalLayout("admin-klinike-layout")' 
      to="/home/adminKlinike">Uloguj se kao admin klinike
    </v-btn> <br>

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
              placeholder="email" 
              v-model="email" 
              prepend-icon="person" 
              :rules="inputRules"
              @keydown.enter="loginuj({
                username: email, 
                password: password})">
            </v-text-field>
            <v-text-field 
              type="password" 
              placeholder="password" 
              v-model="password" 
              prepend-icon="lock" 
              :rules="inputRules"
              @keydown.enter="loginuj({
                username: email, 
                password: password})">
            </v-text-field>
            <v-btn @click="loginuj({
              username: email, 
              password: password})">
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