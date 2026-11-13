<script setup>
import { onMounted, ref, toRefs, watch } from "vue";
import { storeToRefs } from "pinia";
import { Button } from "@/components/ui/button";
import GroupDashboard from "@/components/group/GroupDashboard.vue";
import GroupRanking from "@/components/group/GroupRanking.vue";
import MemberDetailView from "@/components/group/MemberDetailView.vue";
import InviteMemberModal from "@/components/group/InviteMemberModal.vue";
import MemberSelectionModal from "@/components/group/MemberSelectionModal.vue";
import MealRegistrationModal from "@/components/meal/MealRegistrationModal.vue";
import { ArrowLeft, UserPlus, Settings, Trash2 } from "lucide-vue-next";
import { useGroupStore } from "@/stores/group/GroupStore";
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";

const props = defineProps({
  group: { type: Object, required: true },
});
const { group } = toRefs(props);

const groupStore = useGroupStore();
const { currentGroupMembers, groupRankings } = storeToRefs(groupStore);
const emit = defineEmits(["update:isDeepView", "deleteGroup"]);
const selectedMember = ref(null);
const isMemberDetailDeep = ref(false);
const isInviteModalOpen = ref(false);
const isMemberSelectionOpen = ref(false);
const isMealInputOpen = ref(false);
const selectedMemberForMeal = ref(null);
const isSettingsOpen = ref(false);
const groupNameDraft = ref("");

onMounted(() => {
  groupStore.initializeLeaderView(group.value?.id);
});

watch(
  () => group.value?.id,
  (newId, oldId) => {
    if (newId && newId !== oldId) {
      groupStore.initializeLeaderView(newId);
      selectedMember.value = null;
    }
  },
);

watch(
  () => selectedMember.value,
  (member) => {
    emit("update:isDeepView", !!member);
  },
  { immediate: true },
);

const handleMemberSelection = (member) => {
  selectedMemberForMeal.value = member;
  isMealInputOpen.value = true;
};

const handleMealSave = (mealData) => {
  if (selectedMemberForMeal.value) {
    isMealInputOpen.value = false;
    selectedMemberForMeal.value = null;
  }
};

const handleOpenSettings = () => {
  groupNameDraft.value = group.value?.name || "";
  isSettingsOpen.value = true;
};

const handleUpdateGroupName = async () => {
  if (!groupNameDraft.value.trim()) return;
  try {
    await groupStore.updateGroupName(group.value.id, groupNameDraft.value.trim());
    isSettingsOpen.value = false;
  } catch (error) {
    console.error("그룹 이름 수정 실패:", error);
    alert(error?.response?.data?.message || "그룹 이름 수정에 실패했습니다.");
  }
};

const handleDeleteGroup = async () => {
  if (!confirm("그룹을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.")) return;
  try {
    await groupStore.deleteGroup(group.value.id);
    isSettingsOpen.value = false;
    emit("deleteGroup");
  } catch (error) {
    console.error("그룹 삭제 실패:", error);
    alert(error?.response?.data?.message || "그룹 삭제에 실패했습니다.");
  }
};

const handleExpelMember = async (member) => {
  if (!member) return;
  if (!confirm(`${member.name}님을 그룹에서 강퇴하시겠습니까?`)) return;
  try {
    await groupStore.expelMember(group.value.id, member.id || member.userId);
    selectedMember.value = null;
  } catch (error) {
    console.error("그룹원 강퇴 실패:", error);
    alert(error?.response?.data?.message || "그룹원 강퇴에 실패했습니다.");
  }
};
</script>

<template>
  <div class="bg-gray-50 min-h-screen">
    <div class="max-w-7xl mx-auto px-4 py-6 space-y-6">
      <template v-if="selectedMember">
        <div v-if="!isMemberDetailDeep" class="mb-4">
          <Button
            variant="ghost"
            class="text-gray-500 hover:text-emerald-600 hover:bg-emerald-50 rounded-xl px-3"
            @click="selectedMember = null"
          >
            <ArrowLeft class="w-4 h-4 mr-2" />
            그룹 대시보드로 돌아가기
          </Button>
        </div>
        <MemberDetailView
          :member="selectedMember"
          :group-id="group.id"
          @update:is-deep-view="(val) => (isMemberDetailDeep = val)"
        />
      </template>

      <template v-else>
        <div class="bg-white rounded-3xl border border-gray-200 shadow-sm p-6 flex items-center justify-between">
          <div>
            <h1 class="text-xl font-bold text-gray-900">그룹 관리</h1>
            <p class="text-sm text-gray-500">그룹원들의 활동을 관리하고 피드백을 제공하세요</p>
          </div>
          <div class="flex items-center gap-2">
            <Button
              variant="outline"
              class="h-10 w-10 rounded-full border-gray-200 text-gray-500 hover:bg-gray-50"
              @click="handleOpenSettings"
            >
              <Settings class="w-4 h-4" />
            </Button>
            <!-- <Button
              variant="outline"
              class="rounded-xl text-emerald-700 border-emerald-200 hover:bg-emerald-50"
              @click="isMemberSelectionOpen = true"
            >
              <UtensilsCrossed class="w-4 h-4 mr-2" />
              식단 입력
            </Button> -->
            <Button
              class="bg-emerald-500 hover:bg-emerald-600 text-white rounded-xl shadow-md shadow-emerald-200"
              @click="isInviteModalOpen = true"
            >
              <UserPlus class="w-4 h-4 mr-2" />
              멤버 초대
            </Button>
          </div>
        </div>

        <div class="mb-6">
          <GroupRanking :members="groupRankings" :show-my-rank="false" />
        </div>

        <GroupDashboard
          :members="currentGroupMembers"
          @select-member="(member) => (selectedMember = member)"
        />
      </template>

      <InviteMemberModal
        :open="isInviteModalOpen"
        :on-open-change="(val) => (isInviteModalOpen = val)"
        :group-id="group?.id"
        :group-name="group.name"
        @update:open="(val) => (isInviteModalOpen = val)"
      />

      <Dialog :open="isSettingsOpen" @update:open="(val) => (isSettingsOpen = val)">
        <DialogContent class="sm:max-w-md rounded-3xl">
          <DialogHeader>
            <DialogTitle>그룹 설정</DialogTitle>
          </DialogHeader>
          <div class="space-y-4 py-2">
            <div class="space-y-2">
              <label class="text-sm font-medium">그룹 이름 수정</label>
              <Input
                v-model="groupNameDraft"
                placeholder="새 그룹 이름을 입력하세요"
                class="h-12 bg-gray-50 border-gray-200 rounded-xl focus-visible:ring-emerald-500 focus-visible:border-emerald-400"
              />
              <div class="flex justify-end">
                <Button
                  class="bg-emerald-500 hover:bg-emerald-600 text-white rounded-xl"
                  :disabled="!groupNameDraft.trim()"
                  @click="handleUpdateGroupName"
                >
                  저장
                </Button>
              </div>
            </div>
            <div class="space-y-2 pt-4 border-t">
              <label class="text-sm font-medium">그룹원 관리</label>
              <div class="space-y-2 max-h-[200px] overflow-y-auto pr-2 bg-gray-50 rounded-2xl p-3">
                <div
                  v-for="member in currentGroupMembers"
                  :key="member.id"
                  class="flex justify-between items-center p-3 bg-white border border-gray-100 rounded-xl mb-2"
                >
                  <span class="text-sm font-medium text-gray-800">{{ member.name }}</span>
                  <Button
                    variant="ghost"
                    size="sm"
                    class="text-red-400 hover:text-red-600 bg-red-50 hover:bg-red-100 p-2 rounded-lg h-8"
                    @click="handleExpelMember(member)"
                  >
                    강퇴
                  </Button>
                </div>
              </div>
            </div>
            <div class="border-t pt-4">
              <div class="flex items-center justify-between">
                <div>
                  <p class="text-sm font-medium text-red-500">그룹 삭제</p>
                  <p class="text-xs text-gray-500">
                    삭제 시 복구할 수 없습니다.
                  </p>
                </div>
                <Button
                  variant="destructive"
                  class="bg-red-500 hover:bg-red-600 text-white rounded-xl"
                  @click="handleDeleteGroup"
                >
                  <Trash2 class="w-4 h-4 mr-2" />
                  삭제
                </Button>
              </div>
            </div>
          </div>
          <DialogFooter>
            <Button variant="outline" @click="isSettingsOpen = false">닫기</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      <MemberSelectionModal
        :open="isMemberSelectionOpen"
        :on-open-change="(val) => (isMemberSelectionOpen = val)"
        :members="currentGroupMembers"
        @select-member="handleMemberSelection"
        @update:open="(val) => (isMemberSelectionOpen = val)"
      />

      <MealRegistrationModal
        v-if="selectedMemberForMeal"
        :isRegistrationOpen="isMealInputOpen"
        :on-close="() => { isMealInputOpen = false; selectedMemberForMeal = null; }"
        selectedMealType="lunch"
        :date="new Date()"
        :on-save="handleMealSave"
        :on-delete="() => {}"
        :custom-title="'추천 식단 등록'"
        :custom-description="`${selectedMemberForMeal.name}님에게 추천할 식단을 등록하세요`"
      />
    </div>
  </div>
</template>
