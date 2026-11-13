<script setup>
import { onMounted, ref } from "vue";
import { storeToRefs } from "pinia";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import GroupRanking from "@/components/group/GroupRanking.vue";
import MemberCalendarView from "@/components/group/MemberCalendarView.vue";
import MemberRecordWithFeedback from "@/components/group/MemberRecordWithFeedback.vue";
import { Bell, Calendar as CalendarIcon, Users, LogOut } from "lucide-vue-next";
import { useGroupStore } from "@/stores/group/GroupStore";

const props = defineProps({
  group: { type: Object, required: true },
});

const emit = defineEmits(["leave-group"]);

const groupStore = useGroupStore();
const { groupRankings, memberDailyLog } = storeToRefs(groupStore);

const view = ref("calendar");
const selectedRecord = ref(null);
const unreadFeedbackCount = ref(0);

onMounted(() => {
  groupStore.initializeMemberView(props.group.id);
});

const handleRecordClick = (record) => {
  selectedRecord.value = record;
  view.value = "record-detail";
};

const handleBackToCalendar = () => {
  view.value = "calendar";
  selectedRecord.value = null;
};

const handleDateChange = async (date) => {
  await groupStore.fetchMyDailyLog(props.group.id, date);
};

const handleWithdrawGroup = async () => {
  if (!confirm("정말 그룹을 탈퇴하시겠습니까?")) return;
  try {
    await groupStore.withdrawGroup(props.group.id);
    emit("leave-group");
  } catch (error) {
    console.error("그룹 탈퇴 실패:", error);
    alert(error?.response?.data?.message || "그룹 탈퇴에 실패했습니다.");
  }
};
</script>

<template>
  <div class="bg-gray-50 min-h-screen">
    <div class="max-w-7xl mx-auto px-4 py-6 space-y-6">
      <div class="bg-white rounded-3xl border border-gray-200 shadow-sm p-6 flex items-center justify-between">
        <div class="flex items-center gap-3">
          <div class="w-12 h-12 bg-emerald-50 text-emerald-600 rounded-full flex items-center justify-center">
            <Users class="w-6 h-6" />
          </div>
          <div>
            <h1 class="text-xl font-bold text-gray-900">그룹 활동</h1>
            <p class="text-sm text-gray-500">그룹원들과 함께 목표를 달성하세요</p>
          </div>
        </div>

        <div class="flex items-center gap-2">
          <Button
            variant="ghost"
            class="text-gray-400 hover:text-red-500 hover:bg-red-50 rounded-xl"
            @click="handleWithdrawGroup"
          >
            <LogOut class="w-4 h-4 mr-2" />
            그룹 탈퇴
          </Button>
          <Button
            v-if="unreadFeedbackCount > 0"
            variant="outline"
            class="relative border-emerald-200 text-emerald-600 hover:bg-emerald-50 rounded-xl"
          >
            <Bell class="w-4 h-4 mr-2" />
            새 피드백
            <Badge class="ml-2 bg-destructive">{{ unreadFeedbackCount }}</Badge>
            <span class="absolute -top-1 -right-1 w-3 h-3 bg-destructive rounded-full animate-pulse" />
          </Button>
        </div>
      </div>

      <div class="mb-6">
        <GroupRanking :members="groupRankings" />
      </div>

      <div v-if="view === 'calendar' && !selectedRecord" class="bg-white rounded-3xl p-6 border border-gray-200 shadow-sm">
        <div class="flex items-center gap-2 mb-6">
          <CalendarIcon class="w-5 h-5 text-emerald-500" />
          <h3 class="text-lg font-semibold text-gray-900">나의 활동 기록</h3>
        </div>
        <MemberCalendarView
          :daily-log="memberDailyLog"
          @record-click="handleRecordClick"
          @date-change="handleDateChange"
        />
      </div>

      <div
        v-if="view === 'record-detail' && selectedRecord"
        class="bg-white rounded-3xl p-6 border border-gray-200 shadow-sm"
      >
        <MemberRecordWithFeedback
          :record="selectedRecord"
          :group-id="group.id"
          :daily-log="memberDailyLog"
          @back="handleBackToCalendar"
        />
      </div>
    </div>
  </div>
</template>
