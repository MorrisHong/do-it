<template>
  <v-row justify="center">
    <v-dialog v-model="dialog" persistent max-width="600px">
      <template v-slot:activator="{ on }">

      </template>
      <v-card>
        <v-card-title>
          <span class="headline">새로운 카드 추가</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field label="Card Name*" required></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-textarea
                  name="Description"
                  label="Description"
                  outlined
                ></v-textarea>
              </v-col>
            </v-row>
          </v-container>
          <small>*indicates required field</small>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="dialog = false">Close</v-btn>
          <v-btn color="blue darken-1" text @click="dialog = false">Save</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
  import { EventBus } from '../main.js'

  export default {
    data: () => ({
      dialog: false,
    }),
    created () {
      var vm = this
      EventBus.$on('cardInputModal', function (value) {
        vm.dialog = value
      })
    }
  }
</script>