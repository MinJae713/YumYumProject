<script setup>
import { computed, onMounted, ref } from "vue";
import { storeToRefs } from "pinia";
import { Card } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { ArrowLeft, MessageSquare, Send, Flame, Utensils, Pencil, Trash } from "lucide-vue-next";
import { useGroupStore } from "@/stores/group/GroupStore";
import { useAuthStore } from "@/stores/auth";
import { formatNumber, calculateTotalNutrients } from "@/utils/groupUtils";

const props = defineProps({
  member: { type: Object, default: () => ({}) },
  record: { type: Object, default: null },
  groupId: { type: [Number, String], required: true },
});

const emit = defineEmits(["back"]);

const groupStore = useGroupStore();
const { recordDetail, recordFeedbacks } = storeToRefs(groupStore);
const authStore = useAuthStore();

const feedbackText = ref("");
const submitting = ref(false);
const editingFeedbackId = ref(null);
const editingText = ref("");

const totalNutrients = computed(() => calculateTotalNutrients(recordDetail.value?.foods));

// 컨텍스트에 따른 색상/테마 결정
const isMeal = computed(() => props.record?.type === "meal");
const contextTheme = computed(() => isMeal.value ? 
  { color: 'text-orange-600', bg: 'bg-orange-50', border: 'border-orange-100', badge: 'bg-orange-100 text-orange-700' } : 
  { color: 'text-blue-600', bg: 'bg-blue-50', border: 'border-blue-100', badge: 'bg-blue-100 text-blue-700' }
);

const handleSubmitFeedback = async () => {
  if (!feedbackText.value.trim() || !props.record?.type) return;
  submitting.value = true;
  try {
    const recordId =
      props.record.type === "meal"
        ? props.record.data?.mealId
        : props.record.data?.exerciseRecordId;
    await groupStore.registerFeedback(props.groupId, props.record.type, recordId, feedbackText.value.trim());
    feedbackText.value = "";
  } catch (error) {
    console.error("피드백 등록 실패:", error);
    alert(error?.response?.data?.message || "피드백 등록에 실패했습니다.");
  } finally {
    submitting.value = false;
  }
};

const startEdit = (feedback) => {
  editingFeedbackId.value = feedback.feedbackId || feedback.id;
  editingText.value = feedback.content;
};

const cancelEdit = () => {
  editingFeedbackId.value = null;
  editingText.value = "";
};

const handleSaveEdit = async (feedback) => {
  if (!editingText.value.trim()) return;
  const feedbackId = feedback.feedbackId || feedback.id;
  try {
    await groupStore.updateFeedback(props.groupId, props.record.type, feedbackId, editingText.value.trim());
    cancelEdit();
  } catch (error) {
    console.error("피드백 수정 실패:", error);
    alert(error?.response?.data?.message || "피드백 수정에 실패했습니다.");
  }
};

const handleDelete = async (feedback) => {
  const feedbackId = feedback.feedbackId || feedback.id;
  if (!confirm("이 피드백을 삭제하시겠습니까?")) return;
  try {
    await groupStore.deleteFeedback(props.groupId, props.record.type, feedbackId);
  } catch (error) {
    console.error("피드백 삭제 실패:", error);
    alert(error?.response?.data?.message || "피드백 삭제에 실패했습니다.");
  }
};

const isMyFeedback = (feedback) => {
  return String(feedback.writerUserId) === String(authStore.user?.id || authStore.user?.userId);
};

// 피드백 작성자 역할 확인 헬퍼
const isTrainer = (role) => {
  return role === 'LEADER' || role === 'TRAINER';
};

const displayMealTotalCalorie = computed(() => {
  const value = recordDetail.value?.totalCalorie ?? props.record?.data?.totalCalorie;
  if (typeof value !== "number") return 0;
  return Math.round(value);
});

const displayExerciseBurnedCalorie = computed(() => {
  const value = recordDetail.value?.burnedCalorie ?? props.record?.data?.burnedCalorie;
  if (typeof value !== "number") return 0;
  return Math.round(value);
});

const mealFoodsWithDisplayCalorie = computed(() =>
  (recordDetail.value?.foods || []).map((food) => ({
    ...food,
    displayCalorie: Math.round(typeof food.calorie === "number" ? food.calorie : 0),
  })),
);

onMounted(async () => {
  if (!props.record?.type) return;
  const recordId =
    props.record.type === "meal"
      ? props.record.data?.mealId
      : props.record.data?.exerciseRecordId;
  await groupStore.fetchRecordDetail(props.groupId, props.record.type, recordId);
});

const formatDateDisplay = (dateLike, options) => {
  if (!dateLike) return "";
  const d = new Date(dateLike);
  if (isNaN(d.getTime())) return dateLike; // 날짜 변환에 실패하면 원본 문자열을 그대로 반환합니다.
  return d.toLocaleDateString("ko-KR", options);
};
</script>

<template>
  <div class="space-y-6 w-full">
    
    <div class="flex flex-col gap-4">
      <Button 
        variant="ghost" 
        class="w-fit text-gray-600 hover:bg-gray-100 rounded-xl px-4" 
        @click="emit('back')"
      >
        <ArrowLeft class="w-4 h-4 mr-2" />
        목록으로 돌아가기
      </Button>

      <div class="flex items-center gap-5 px-2">
        <Avatar class="w-16 h-16">
          <AvatarFallback class="w-full h-full flex items-center justify-center bg-blue-200 text-gray-900 font-bold text-2xl">
            {{ member.name?.[0] }}
          </AvatarFallback>
        </Avatar>
        <div>
          <h2 class="text-xl font-semibold text-gray-900">{{ member.name }}님의 기록 상세</h2>
          <p class="text-sm text-gray-500 mt-1">
            {{ formatDateDisplay(record?.date, { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' }) }}
          </p>
        </div>
      </div>
    </div>

    <Card class="rounded-3xl border border-gray-200 shadow-sm bg-white overflow-hidden">
      <div class="p-6 pb-4 flex items-center justify-between border-b border-gray-100">
        <div class="flex items-center gap-2">
          <div :class="`p-2 rounded-lg ${contextTheme.bg}`">
            <Utensils v-if="isMeal" :class="`w-5 h-5 ${contextTheme.color}`" />
            <Flame v-else :class="`w-5 h-5 ${contextTheme.color}`" />
          </div>
          <h3 class="font-semibold text-gray-900 text-lg">
            {{ isMeal ? "식단 상세" : "운동 상세" }}
          </h3>
        </div>
        <Badge :class="`rounded-lg px-3 py-1 font-medium border-0 ${contextTheme.badge}`">
          {{ isMeal ? "식단" : "운동" }}
        </Badge>
      </div>

      <div class="p-6 pt-6">
        <template v-if="isMeal">
          <div class="space-y-6">
            <div class="grid grid-cols-2 gap-4">
              <div class="p-5 bg-gray-50 rounded-2xl">
                <div class="text-xs text-gray-500 mb-1 font-medium">식사 시간</div>
                <div class="text-lg font-semibold text-gray-900">{{ record.data.mealType }}</div>
              </div>
              <div class="p-5 bg-orange-50 rounded-2xl border border-orange-100">
                <div class="text-xs text-orange-600/80 mb-1 font-medium">총 칼로리</div>
                <div class="text-xl font-bold text-orange-600">
                  {{ displayMealTotalCalorie }} 
                  <span class="text-sm font-normal">kcal</span>
                </div>
              </div>
            </div>

            <div>
              <h4 class="text-sm font-semibold text-gray-900 mb-3">섭취한 음식</h4>
              <div class="space-y-2">
                <div
                  v-for="(food, idx) in mealFoodsWithDisplayCalorie || []"
                  :key="idx"
                  class="flex items-center gap-4 p-4 bg-white border border-gray-100 rounded-xl hover:bg-gray-50 transition-colors"
                >
                  <div class="w-2 h-2 rounded-full bg-orange-400 shrink-0" />
                  <div class="flex-1">
                    <div class="text-sm font-medium text-gray-900">{{ food.name }}</div>
                    <div class="text-xs text-gray-500 mt-0.5">
                      {{ formatNumber(food.amount) }}{{ food.unitName || "" }}
                    </div>
                  </div>
                  <div class="text-sm font-semibold text-gray-900">
                    {{ food.displayCalorie }} kcal
                  </div>
                </div>
              </div>
            </div>

            <div class="p-5 bg-gray-50 rounded-2xl">
              <h4 class="text-xs font-semibold text-gray-500 mb-4 uppercase tracking-wider">영양 분석</h4>
              <div class="grid grid-cols-3 gap-4 text-center divide-x divide-gray-200">
                <div>
                  <div class="text-xs text-gray-500 mb-1">단백질</div>
                  <div class="text-lg font-semibold text-gray-900">{{ formatNumber(totalNutrients.protein) }}g</div>
                </div>
                <div>
                  <div class="text-xs text-gray-500 mb-1">탄수화물</div>
                  <div class="text-lg font-semibold text-gray-900">{{ formatNumber(totalNutrients.carbohydrate) }}g</div>
                </div>
                <div>
                  <div class="text-xs text-gray-500 mb-1">지방</div>
                  <div class="text-lg font-semibold text-gray-900">{{ formatNumber(totalNutrients.fat) }}g</div>
                </div>
              </div>
            </div>
          </div>
        </template>

        <template v-else>
          <div class="space-y-6">
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div class="p-5 bg-gray-50 rounded-2xl col-span-2 md:col-span-1">
                <div class="text-xs text-gray-500 mb-1 font-medium">운동 종류</div>
                <div class="text-lg font-semibold text-gray-900">{{ recordDetail?.exerciseName || record?.data.exerciseName }}</div>
              </div>
              <div class="p-5 bg-blue-50 rounded-2xl border border-blue-100">
                <div class="text-xs text-blue-600/80 mb-1 font-medium">운동 시간</div>
                <div class="text-xl font-bold text-blue-600">
                  {{ recordDetail?.durationMin ?? record?.data.durationMin }}
                  <span class="text-sm font-normal">분</span>
                </div>
              </div>
              <div class="p-5 bg-blue-50 rounded-2xl border border-blue-100">
                <div class="text-xs text-blue-600/80 mb-1 font-medium">소모 칼로리</div>
                <div class="text-xl font-bold text-blue-600">
                  {{ displayExerciseBurnedCalorie }} 
                  <span class="text-sm font-normal">kcal</span>
                </div>
              </div>
            </div>

            <div class="p-5 bg-gray-50 rounded-2xl">
              <h4 class="text-xs font-semibold text-gray-500 mb-4 uppercase tracking-wider">운동 세부 정보</h4>
              <div class="space-y-3 text-sm">
                <div class="flex justify-between items-center py-2 border-b border-gray-200 last:border-0 last:pb-0">
                  <span class="text-gray-500">세트 수</span>
                  <span class="font-semibold text-gray-900">{{ recordDetail?.setCount ?? "-" }} 세트</span>
                </div>
                <div class="flex justify-between items-center py-2 border-b border-gray-200 last:border-0 last:pb-0">
                  <span class="text-gray-500">반복 횟수</span>
                  <span class="font-semibold text-gray-900">{{ recordDetail?.repeatCount ?? "-" }} 회</span>
                </div>
                <div class="flex justify-between items-center py-2 border-b border-gray-200 last:border-0 last:pb-0">
                  <span class="text-gray-500">무게</span>
                  <span class="font-semibold text-gray-900">{{ recordDetail?.weight ?? "-" }} kg</span>
                </div>
              </div>
            </div>
          </div>
        </template>
      </div>
    </Card>

    <Card class="rounded-3xl border border-gray-200 shadow-sm bg-white p-6">
      <div class="flex items-center gap-2 mb-6">
        <div class="bg-emerald-50 p-2 rounded-lg">
            <MessageSquare class="w-5 h-5 text-emerald-600" />
        </div>
        <h3 class="font-semibold text-gray-900">피드백</h3>
        <Badge variant="secondary" class="bg-gray-100 text-gray-600 hover:bg-gray-200">{{ recordFeedbacks.length }}</Badge>
      </div>

      <div class="space-y-6 mb-8 max-h-[500px] overflow-y-auto pr-2 custom-scrollbar">
        <template v-if="recordFeedbacks.length">
          <div
            v-for="(feedback, idx) in recordFeedbacks"
            :key="idx"
            class="flex gap-4 group"
          >
            <Avatar class="w-10 h-10 mt-1 shrink-0">
              <AvatarFallback 
                class="w-full h-full flex items-center justify-center font-bold text-gray-900"
                :class="isTrainer(feedback.writerRole) ? 'bg-emerald-200' : 'bg-blue-200'"
              >
                {{ feedback.writerName?.[0] }}
              </AvatarFallback>
            </Avatar>

            <div class="flex-1 space-y-1.5">
              <div class="flex items-center justify-between">
                <div class="flex items-center gap-2">
                  <span class="text-sm font-semibold text-gray-900">
                    {{ feedback.writerName }}
                  </span>
                  <Badge
                    v-if="isTrainer(feedback.writerRole)"
                    class="bg-emerald-100 text-emerald-700 hover:bg-emerald-200 border-0 text-[10px] px-1.5 py-0 h-5"
                  >
                    트레이너
                  </Badge>
                  <span class="text-xs text-gray-400">{{ feedback.createdAt }}</span>
                </div>

                <div 
                  v-if="isMyFeedback(feedback) && editingFeedbackId !== (feedback.feedbackId || feedback.id)" 
                  class="flex gap-2"
                >
                  <button 
                    class="text-gray-400 hover:text-gray-600 transition-colors p-1 rounded-md hover:bg-gray-100" 
                    title="수정"
                    @click="startEdit(feedback)"
                  >
                    <Pencil class="w-3.5 h-3.5" />
                  </button>
                  <button 
                    class="text-gray-400 hover:text-red-500 transition-colors p-1 rounded-md hover:bg-red-50" 
                    title="삭제"
                    @click="handleDelete(feedback)"
                  >
                    <Trash class="w-3.5 h-3.5" />
                  </button>
                </div>
              </div>

              <template v-if="editingFeedbackId === (feedback.feedbackId || feedback.id)">
                <div class="bg-gray-50 p-3 rounded-xl border border-gray-200">
                  <Textarea
                    rows="3"
                    v-model="editingText"
                    class="bg-white border-gray-200 mb-2 focus-visible:ring-emerald-500 text-sm"
                  />
                  <div class="flex gap-2 justify-end">
                    <Button variant="ghost" size="sm" class="h-8 text-xs" @click="cancelEdit">취소</Button>
                    <Button size="sm" class="h-8 text-xs bg-gray-900 hover:bg-gray-800 text-white" @click="handleSaveEdit(feedback)">저장</Button>
                  </div>
                </div>
              </template>
              
              <template v-else>
                <div class="text-sm text-gray-800 leading-relaxed whitespace-pre-wrap">
                  {{ feedback.content }}
                </div>
              </template>
            </div>
          </div>
        </template>
        
        <div v-else class="text-center py-12 bg-gray-50 rounded-3xl border border-dashed border-gray-200">
          <MessageSquare class="w-10 h-10 mx-auto mb-3 text-gray-300" />
          <p class="text-sm text-gray-500">아직 등록된 피드백이 없습니다.</p>
          <p class="text-xs text-gray-400 mt-1">첫 번째 피드백을 남겨보세요!</p>
        </div>
      </div>

      <div class="flex gap-3 items-start pt-4 border-t border-gray-100">
        <Textarea
          rows="1"
          class="min-h-[50px] resize-none rounded-xl border-gray-200 focus-visible:ring-emerald-500 focus-visible:ring-offset-0 bg-gray-50 focus:bg-white transition-all"
          placeholder="피드백을 입력하세요..."
          v-model="feedbackText"
        />
        <Button 
            class="h-[50px] w-[50px] rounded-xl bg-gray-900 hover:bg-gray-800 shrink-0"
            :disabled="!feedbackText.trim() || submitting" 
            @click="handleSubmitFeedback"
        >
            <Send class="w-5 h-5 text-white" />
        </Button>
      </div>
    </Card>
  </div>
</template>

<style scoped>
.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: #e5e7eb;
  border-radius: 20px;
}
</style>
