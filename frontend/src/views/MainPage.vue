<template>
  <v-content class="ma-12 pa-2">
    <v-container>
      <h1>개인 보드</h1>
      <draggable
        v-model="rows"
        tag="v-layout"
        class="row wrap align-center sortable-list"
      >
        <v-flex
          v-for="row in rows"
          :key="row.index"
          class="sortable row-v"
          xs4
          pa-3
        >
          <v-card 
            style="height: 200px; border: 2px solid #eee;"
          >
            <v-card-text style="height: 120px; overflow: hidden;">
              <p class="display-1 text--primary">
                {{ row.title }}
              </p>
              <div class="text--primary">
                {{ row.description}}
              </div>
            </v-card-text>
            <v-card-actions>
              <v-btn
                text
                color="deep-purple accent-4"
                href="/detail"
              >
                More
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-flex>
        <v-flex
          class="sortable row-v"
          xs4
          pa-3
        >
          <v-card 
            style="height: 200px; border: 2px solid #eee;"
          >
            <v-card-actions style="height: 200px;" class="justify-center">
              <v-btn x-large @click="boardInputModalOn()">
                <v-icon>mdi-plus</v-icon>
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-flex>
      </draggable>
      <v-divider></v-divider>
      <h1>미니 프로젝트 보드</h1>
      <draggable
        v-model="rows"
        tag="v-layout"
        class="row wrap align-center sortable-list"
      >
        <v-flex
          v-for="row in rows"
          :key="row.index"
          class="sortable row-v"
          xs4
          pa-3
        >
          <v-card 
            style="height: 200px; border: 2px solid #eee;"
          >
            <v-card-text style="height: 120px; overflow: hidden;">
              <p class="display-1 text--primary">
                {{ row.title }}
              </p>
              <div class="text--primary">
                {{ row.description}}
              </div>
            </v-card-text>
            <v-card-actions>
              <v-btn
                text
                color="deep-purple accent-4"
              >
                More
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-flex>
        <v-flex
          class="sortable row-v"
          xs4
          pa-3
        >
          <v-card 
            style="height: 200px; border: 2px solid #eee;"
          >
            <v-card-actions style="height: 200px;" class="justify-center">
              <v-btn x-large @click="boardInputModalOn()">
                <v-icon>mdi-plus</v-icon>
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-flex>
      </draggable>
      <board-input-modal/>
    </v-container>
  </v-content>
</template>

<script>

  import { EventBus } from '../main.js'
  import BoardInputModal from "@/views/BoardInputModal.vue"
  import draggable from "vuedraggable";

  export default {
    props: {
      source: String,
    },
    components: {
      draggable,
      'board-input-modal': BoardInputModal
    },
    data: () => ({
      drawer: null,
      drawerRight: null,
      right: false,
      left: false,
      enabled: true,
      rows: [
        {
          index: 1,
          title: "Spring Project1",
          description: "Lorem ipsum, or lipsum as it is sometimes known, is dummy text used in laying out print, graphic or web designs. The passage is attributed to an unknown typesetter in the"
        },
        {
          index: 2,
          title: "2020/01/11 할일",
          description: "Lorem ipsum, or lipsum as it is sometimes known, is dummy text used in laying out print, graphic or web designs. The passage is attributed to an unknown typesetter in the unknown typesetter in the unknown typesetter in the unknown typesetter in the unknown typesetter in the unknown typesetter in the unknown typesetter in the "
        },
        {
          index: 3,
          title: "2020/02/01 할일",
          description: "Lorem ipsum, or lipsum as it is sometimes known, is dummy text used in laying out print, graphic or web designs. The passage is attributed to an unknown typesetter in the unknown typesetter in the unknown typesetter in the unknown typesetter in the unknown typesetter in the unknown typesetter in the unknown typesetter in the "
        },
        {
          index: 4,
          title: "2020/02/22 할일",
          description: "Lorem ipsum, or lipsum as it is sometimes known, is dummy text used in laying out print, graphic or web designs. The passage is attributed to an unknown typesetter in the unknown typesetter in the unknown typesetter in the unknown typesetter in the unknown typesetter in the unknown typesetter in the unknown typesetter in the "
        }
      ]
    }),
    methods : {
      boardInputModalOn: function () {
        EventBus.$emit('boardInputModal', true);
      }
    }
  }
</script>