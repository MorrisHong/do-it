<template>
  <div id="app">
  <v-app id="inspire">
    <v-navigation-drawer
      v-model="drawerRight"
      app
      disable-resize-watcher
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
      <v-toolbar-title>
        <router-link to="/" class="toolbar-title">Do it!</router-link>
      </v-toolbar-title>
      <v-spacer />
      <div>
        <v-menu offset-y>
          <template v-slot:activator="{ on }">
            <v-btn
              v-on="on"
              color="white"
              class="black--text text-lowercase"
            >
              dev.sup2is@gmail.com
              <v-icon>mdi-chevron-down</v-icon>
            </v-btn>
          </template>
          <v-list>
            <v-list-item
              v-for="(item, index) in items"
              :key="index"
              @click="target(item.value)"
            >
              <v-list-item-title>{{ item.title }}</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
      </div>

      <v-app-bar-nav-icon @click.stop="drawerRight = !drawerRight" />
    </v-app-bar>

    <v-navigation-drawer
      v-model="drawer"
      disable-resize-watcher
      app
    >
      <v-list dense>
        <v-list-item @click.stop="left = !left">
          <v-list-item-action>
            <v-icon>mdi-exit-to-app</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Open Temporary Drawer2</v-list-item-title>
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
    <v-content>
      <router-view/> 
    </v-content>

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
      items: [
        {title: '내 정보 보기', value: '/my'},
        {title: '로그아웃', value: '/logout'}
      ],
    }),
    methods: {
      target(value) {
        this.$router.push(value)
      }
    }
  }
</script>

<style scoped>
  .toolbar-title {
    color: white;
    text-decoration: none;
  }
</style>
