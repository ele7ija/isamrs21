<template>
  <v-card>
    <v-card-title>
        ID: {{klinika.id}}
    </v-card-title>
    <v-card-actions>
      <v-btn 
        :href='"/pacijent/klinike/"+klinika.id+"/pregledi"' 
        outlined>
      <v-icon left>mdi-calendar</v-icon>
      Pregledi
      </v-btn>
    </v-card-actions>
    </v-card>
</template>

<script>
export default {
  name: 'KlinikaCard',
  props: ['klinika']
}
</script>

<style>

</style>