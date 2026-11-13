<script setup>
import { onMounted, ref } from "vue";
import { storeToRefs } from "pinia";
import { Button } from "@/components/ui/button";
import { Users, Plus, ChevronRight } from "lucide-vue-next";
import CreateGroupModal from "@/components/group/CreateGroupModal.vue";
import { useGroupStore } from "@/stores/group/GroupStore";

const props = defineProps({
  userRole: { type: String, default: "member" },
});

const emit = defineEmits(["select-group"]);
const groupStore = useGroupStore();
const { groups, isLoading } = storeToRefs(groupStore);
const isCreateModalOpen = ref(false);

onMounted(() => {
  groupStore.fetchGroups();
});

const handleCreateGroup = async (groupName) => {
  try {
    await groupStore.createGroup(groupName);
    isCreateModalOpen.value = false;
  } catch (error) {
    console.error("그룹 생성 실패:", error);
    alert(error?.response?.data?.message || "그룹 생성에 실패했습니다.");
  }
};
</script>

<template>
  <div class="bg-gray-50 min-h-screen">
    <div class="max-w-7xl mx-auto px-4 py-8 space-y-6">
      <div class="bg-white rounded-3xl border border-gray-200 p-6 flex items-center justify-between">
        <div class="flex items-center gap-4">
          <div class="w-12 h-12 rounded-full bg-emerald-50 text-emerald-600 flex items-center justify-center">
            <Users class="w-6 h-6" />
          </div>
          <div>
            <h1 class="text-xl font-bold text-gray-900">그룹 선택</h1>
            <p class="text-sm text-gray-500">
              {{
                userRole === "leader"
                  ? "관리할 그룹을 선택하거나 새로운 그룹을 생성하세요"
                  : "참여 중인 그룹을 선택하세요"
              }}
            </p>
          </div>
        </div>

      </div>

      <div v-if="isLoading" class="bg-white rounded-3xl border border-gray-200 p-8 text-center text-sm text-gray-500">
        그룹을 불러오는 중입니다...
      </div>

      <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
        <div
          v-for="group in groups"
          :key="group.id"
          class="bg-white p-6 rounded-3xl border border-gray-200 hover:border-emerald-500 hover:shadow-md transition-all cursor-pointer flex flex-col justify-between h-[180px]"
          @click="emit('select-group', group)"
        >
          <div class="flex items-center gap-4">
            <div class="w-12 h-12 bg-emerald-100 text-emerald-600 rounded-2xl flex items-center justify-center">
              <Users class="w-6 h-6" />
            </div>
            <div class="flex-1">
              <div class="flex items-center gap-2">
                <h3 class="text-lg font-bold text-gray-900">{{ group.name }}</h3>
                <span v-if="group.isLeader" class="text-lg">👑</span>
              </div>
              <p class="text-sm text-gray-500">그룹원 {{ group.memberCount }}명</p>
            </div>
          </div>
          <div class="flex items-center justify-between mt-6">
            <span class="px-3 py-1 rounded-full bg-gray-100 text-gray-600 text-xs font-medium">
              멤버 {{ group.memberCount }}명
            </span>
            <ChevronRight class="w-5 h-5 text-emerald-500" />
          </div>
        </div>

        <div
          v-if="userRole === 'leader'"
          class="border-2 border-dashed border-gray-300 rounded-3xl flex flex-col items-center justify-center text-gray-400 hover:border-emerald-400 hover:text-emerald-600 hover:bg-emerald-50/50 h-[180px] transition-all cursor-pointer"
          @click="isCreateModalOpen = true"
        >
          <div class="w-12 h-12 rounded-2xl bg-white border border-gray-200 flex items-center justify-center mb-3 shadow-sm">
            <Plus class="w-6 h-6" />
          </div>
          <p class="font-semibold">새 그룹 만들기</p>
          <p class="text-sm text-gray-400">팀을 생성해 초대해보세요</p>
        </div>
      </div>

      <div
        v-if="!isLoading && groups.length === 0"
        class="bg-white rounded-3xl border border-gray-200 p-10 text-center shadow-sm"
      >
        <Users class="w-12 h-12 text-gray-300 mx-auto mb-3" />
        <h3 class="text-base font-semibold text-gray-800 mb-2">참여 중인 그룹이 없습니다</h3>
        <p class="text-sm text-gray-500 mb-4">
          {{
            userRole === "leader"
              ? "새로운 그룹을 생성하여 그룹원들을 관리해보세요"
              : "우측 상단 종 아이콘을 눌러 도착한 초대를 확인해보세요!"
          }}
        </p>
        <Button
          v-if="userRole === 'leader'"
          class="bg-emerald-500 hover:bg-emerald-600 text-white rounded-xl px-5"
          @click="isCreateModalOpen = true"
        >
          <Plus class="w-4 h-4 mr-2" />
          첫 그룹 만들기
        </Button>
      </div>

      <CreateGroupModal
        :open="isCreateModalOpen"
        @update:open="(val) => (isCreateModalOpen = val)"
        :on-create-group="handleCreateGroup"
      />
    </div>
  </div>
</template>
