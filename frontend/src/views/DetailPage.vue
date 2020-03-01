<template>
  <v-content class="ma-12 pa-2">
    <v-container>
      <v-flex class="d-inline-flex">
        <h2 >Spring Project</h2>
        <v-divider class="mx-4" vertical></v-divider>
        <v-btn color="primary" class="mt-1">초대하기</v-btn>
        <v-divider class="mx-4" vertical></v-divider>
        <v-btn color="primary" class="mt-1">설정</v-btn>
      </v-flex>
      <v-divider class="ma-4"></v-divider>
      <div class="row">
        <div class="col-3">
          <h3>Todo List</h3>
          <draggable
            class="dragArea list-group"
            :list="list1"
            :group="{ name: 'people', pull: 'clone', put: false }"
            @change="log"
          >
            <div
              class="list-group-item"
              v-for="element in list1"
              :key="element.name"
            >
              {{ element.name }}
            </div>
          </draggable>
        </div>

        <div class="col-3">
          <h3>In Progress</h3>
          <draggable
            class="dragArea list-group"
            :list="list2"
            group="people"
            @change="log"
          >
            <div
              class="list-group-item"
              v-for="element in list2"
              :key="element.name"
            >
              {{ element.name }}
            </div>
          </draggable>
        </div>

        <rawDisplayer class="col-3" :value="list1" title="List 1" />

        <rawDisplayer class="col-3" :value="list2" title="List 2" />
      </div>
    </v-container>
  </v-content>
</template>

<script>

import draggable from "vuedraggable";


export default {
  name: "clone",
  display: "Clone",
  order: 2,
  components: {
    draggable
  },
  data() {
    return {
      list1: [
        { name: "John", id: 1 },
        { name: "Joao", id: 2 },
        { name: "Jean", id: 3 },
        { name: "Gerard", id: 4 }
      ],
      list2: [
        { name: "Juan", id: 5 },
        { name: "Edgard", id: 6 },
        { name: "Johnson", id: 7 }
      ]
    };
  },
  methods: {
    log: function(evt) {
      window.console.log(evt);
    }
  }
};
</script>
<style scoped></style>