<script setup>
import { ref } from "vue";
import { storeToRefs } from "pinia";
import { Button } from "@/components/ui/button";
import GroupSelectionView from "@/views/group/GroupSelectionView.vue";
import GroupLeaderView from "@/views/group/GroupLeaderView.vue";
import GroupMemberView from "@/views/group/GroupMemberView.vue";
import { useGroupStore } from "@/stores/group/GroupStore";
import { ArrowLeft } from "lucide-vue-next";

const groupStore = useGroupStore();
const { currentUserRole } = storeToRefs(groupStore);
const selectedGroup = ref(null);
const isLeaderViewDeep = ref(false);

const handleSelectGroup = (payload) => {
  selectedGroup.value = payload;
};

const handleBack = () => {
  selectedGroup.value = null;
};
</script>

<template>
  <div>
    <template v-if="!selectedGroup">
      <GroupSelectionView :user-role="currentUserRole" @select-group="handleSelectGroup" />
    </template>
    <template v-else>
      <div class="max-w-7xl mx-auto px-4 pt-2">
        <Button
          v-if="!isLeaderViewDeep"
          variant="ghost"
          size="sm"
          class="text-gray-500 hover:text-emerald-600 hover:bg-emerald-50 rounded-xl px-3"
          @click="handleBack"
        >
          <ArrowLeft class="w-4 h-4 mr-2" />
          그룹 선택으로 돌아가기
        </Button>
      </div>
      <GroupLeaderView
        v-if="selectedGroup?.isLeader"
        :group="selectedGroup"
        @update:is-deep-view="(val) => (isLeaderViewDeep = val)"
        @delete-group="handleBack"
      />
      <GroupMemberView v-else :group="selectedGroup" @leave-group="handleBack" />
    </template>
  </div>
</template>
