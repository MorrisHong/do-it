<template>
  <v-content class="ma-12 pa-2">
    <v-container>
      <v-flex class="d-inline-flex">
        <h2 >Spring Project</h2>
        <v-divider class="mx-4" vertical></v-divider>
        <v-btn color="primary" class="mt-1">초대하기</v-btn>
        <v-divider class="mx-4" vertical></v-divider>
        <v-btn color="primary" class="mt-1">설정</v-btn>
        <v-divider class="mx-4" vertical></v-divider>
        <v-btn color="primary" class="mt-1"><v-icon>mdi-plus</v-icon>새카드리스트 추가</v-btn>
      </v-flex>
      <v-divider class="ma-4"></v-divider>

      <draggable
        v-model="cards"
        tag="v-layout"
        class="row wrap align-center sortable-list"
      >

      <v-flex
        v-for="card in cards"
        :key="card.index"
        class="sortable row-v align-self-start"
        xs4
        pa-3
      >
        <v-card 
          class="pa-3"
        >
          <h3>{{ card.title }}</h3>
          <draggable
            class="dragArea list-group"
            :list="card.cardList"
            @change="log"
          >
            <div
              class="list-group-item"
              v-for="element in card.cardList"
              :key="element.name"
            >
              {{ element.name }}
            </div>
          </draggable>
          <v-card-actions>
            <v-btn
              text
              color="deep-purple accent-4"
              @click="cardInputModalOn()"
            >
              <v-icon>mdi-plus</v-icon>새 카드 추가하기
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-flex>
      </draggable>
      <card-input-modal/>
    </v-container>
  </v-content>
</template>

<script>

import { EventBus } from '../main.js'
import draggable from "vuedraggable";
import CardInputModal from "@/views/CardInputModal.vue"

export default {
  name: "clone",
  display: "Clone",
  order: 2,
  components: {
    draggable,
    'card-input-modal': CardInputModal
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
      ],
      list3: [
        { name: "Juan", id: 5 },
        { name: "Edgard", id: 6 },
        { name: "Johnson", id: 7 }
      ],
      cards: [
        {
          title: "TodoList",
          index: 1,
          cardList: [
            { name: "John234234", id: 1 },
            { name: "Joao234234", id: 2 },
            { name: "Jean22", id: 3 },
            { name: "Gerard", id: 4 },
            { name: "Gerard", id: 4 },
            { name: "Gerard", id: 4 },
            { name: "Gerard", id: 4 },
            { name: "Gerard", id: 4 },
            { name: "Gerard", id: 4 },
            { name: "Gerard", id: 4 },
            { name: "Gerard", id: 4 },
            { name: "Gerard", id: 4 },
            { name: "Gerard", id: 4 },
            { name: "Gerard", id: 4 },
          ]
        },
        {
          title: "InProgress2",
          index: 2,
          cardList: [
            { name: "John11", id: 1 },
            { name: "Joao##", id: 2 },
            { name: "Jean#", id: 3 },
            { name: "Gerard", id: 4 }
          ]
        },
        {
          title: "InProgress3",
          index: 3,
          cardList: [
            { name: "John11", id: 1 },
            { name: "Joao##", id: 2 },
            { name: "Jean#", id: 3 },
            { name: "Gerard", id: 4 }
          ]
        },
        {
          title: "InProgress4",
          index: 4,
          cardList: [
            { name: "John11", id: 1 },
            { name: "Joao##", id: 2 },
            { name: "Jean#", id: 3 },
            { name: "Gerard", id: 4 }
          ]
        },
        {
          title: "InProgress5",
          index: 5,
          cardList: [
            { name: "John11", id: 1 },
            { name: "Joao##", id: 2 },
            { name: "Jean#", id: 3 },
            { name: "Gerard", id: 4 }
          ]
        },
        {
          title: "TodoList",
          index: 6,
          cardList: [
            { name: "John234234", id: 1 },
            { name: "Joao234234", id: 2 },
            { name: "Jean22", id: 3 },
            { name: "Gerard", id: 4 },
            { name: "Gerard", id: 4 },
            { name: "Gerard", id: 4 },
            { name: "Gerard", id: 4 },
            { name: "Gerard", id: 4 },
          ]
        },
      ]
    };
  },
  methods: {
    log: function(evt) {
      window.console.log(evt);
    },
    cardInputModalOn: function () {
      EventBus.$emit('cardInputModal', true);
    }
  }
};
</script>
<style scoped></style>