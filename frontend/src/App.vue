<template>
  <div id="app">
  <v-app id="inspire">
    <v-navigation-drawer
      v-model="drawerRight"
      app
      clipped
      right
    >
      <v-list dense>
        <v-list-item @click.stop="right = !right">
          <v-list-item-action>
            <v-icon>mdi-exit-to-app</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Open Temporary Drawer</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar
      app
      clipped-right
      color="blue-grey"
      dark
    >
      <v-app-bar-nav-icon @click.stop="drawer = !drawer" />
      <v-toolbar-title>Do-it!</v-toolbar-title>
      <v-spacer />
      <v-app-bar-nav-icon @click.stop="drawerRight = !drawerRight" />
    </v-app-bar>

    <v-navigation-drawer
      v-model="drawer"
      app
    >
      <v-list dense>
        <v-list-item @click.stop="left = !left">
          <v-list-item-action>
            <v-icon>mdi-exit-to-app</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Open Temporary Drawer</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-navigation-drawer
      v-model="right"
      fixed
      right
      temporary
    />

    <router-view/>

    <v-footer
      app
      color="blue-grey"
      class="white--text"
    >
      <span>Vuetify</span>
      <v-spacer />
      <span>&copy; 2019</span>
    </v-footer>
	</v-app>
  </div>
</template>

<style>
</style>
<script>
  export default {
    props: {
      source: String,
    },
    data: () => ({
      drawer: false,
      drawerRight: false,
      right: false,
      left: false,
    }),
  }
</script>