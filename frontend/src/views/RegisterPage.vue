<template>
  <v-container
    fluid
    fill-height
  >
    <v-layout
      align-center
      justify-center
    >
      <v-flex
        xs12
        sm8
      >
        <v-card class="elevation-12">
          <v-toolbar
            color="blue-grey"
            dark
            flat
          >
            <v-toolbar-title>Sign Up</v-toolbar-title>
            <v-spacer></v-spacer>
          </v-toolbar>
          <v-card-text>
            <v-form>
              <v-text-field
                label="ID"
                name="id"
                prepend-icon="mdi-account"
                type="text"
              ></v-text-field>

              <v-text-field
                id="password"
                label="Password"
                name="password"
                prepend-icon="mdi-lock"
                :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                :type="showPassword ? 'text' : 'password'"
                @click:append="showPassword = !showPassword"
              ></v-text-field>
              <v-text-field
                id="password-confirm"
                label="Password Confirm"
                name="password-confirm"
                prepend-icon="mdi-lock-outline"
                :append-icon="showPasswordConfirm ? 'mdi-eye' : 'mdi-eye-off'"
                :type="showPasswordConfirm ? 'text' : 'password'"
                @click:append="showPasswordConfirm = !showPasswordConfirm"
              ></v-text-field>
              <v-text-field
                id="email"
                label="Email"
                name="password"
                prepend-icon="mdi-email"
                type="email"
              ></v-text-field>
              <v-text-field
                id="username"
                label="Username"
                name="username"
                prepend-icon="mdi-account-box-outline"
                type="text"
              ></v-text-field>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue-grey" class="white--text">Submit</v-btn>
          </v-card-actions>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
  export default {
    props: {
      source: String,
    },
    data: () => ({
      showPassword: false,
      showPasswordConfirm: false
    }),
  }
</script>