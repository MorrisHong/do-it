<template>
    <v-content>
      <v-container
        class="fill-height"
        fluid
      >
      
      </v-container>
    </v-content>
</template>

<script>
  export default {
    props: {
      source: String,
    },
    data: () => ({
      drawer: null,
      drawerRight: null,
      right: false,
      left: false,
    }),
  }
</script>