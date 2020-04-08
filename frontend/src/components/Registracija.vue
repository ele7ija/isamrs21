<template>
  <v-container fluid><v-row>
  <v-col class='md-3'></v-col>
  <v-col class='md-4'>
    <v-card>
    <v-card-title>Registracija</v-card-title>
    <v-card-text>
    <v-form ref='loginForm' v-model='valid'>
      <v-text-field
        v-model='ime'
        :counter='20'
        :rules='imePravila'
        label='Ime'
        required>
      </v-text-field>
      <v-text-field
        v-model='prezime'
        :counter='30'
        :rules='prezimePravila'
        label='Prezime'
        required>
      </v-text-field>
    </v-form>
    </v-card-text>
    </v-card>
  </v-col>
  <v-col></v-col>
  </v-row></v-container>
</template>

<script>
export default {
  name: 'Registracija',
  data: function() {
    return {
      valid: true,
      ime: '',
      imePravila: [

      ],
      prezime: '',
      prezimePravila: [

      ],
      email: '',
      emailPravila: [

      ],
    }
  }
}
</script>

<style>

</style>