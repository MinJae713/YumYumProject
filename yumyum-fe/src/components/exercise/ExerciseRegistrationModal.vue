<script setup>
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog";
import { Flame, TrendingUp, Trash2 } from "lucide-vue-next";
import useExerciseView from "@/stores/exercise/ExerciseView";
import { storeToRefs } from "pinia";
import useMain from "@/stores/main/mainView";
import { computed } from "vue";

const exerciseStore = useExerciseView();
const {
  isGroupMember,
  selectedDate,
  exerciseCategory,
  exerciseCatalog,
  modalOpen,
  selectedExercise,

  exerciseData,
  showDeleteDialog,
  calculateCalories,
  calculatePoints
} = storeToRefs(exerciseStore);
const closeModal = exerciseStore.closeModal;
const handleCancle = exerciseStore.handleCancle;
const handleSubmit = exerciseStore.handleSubmit;
const handleDelete = exerciseStore.handleDelete;
const onSelectedCategory = exerciseStore.onSelectedCategory;
const onSelectedCatalog = exerciseStore.onSelectedCatalog;
const checkSubmitAble = exerciseStore.checkSubmitAble;

const mainStore = useMain();

const isEditMode = computed(() => !!selectedExercise.value);
</script>

<template>
  <Dialog :open="modalOpen" @update:open="(val) => !val && closeModal?.()">
    <DialogContent class="max-w-2xl max-h-[90vh] overflow-y-auto bg-white border border-gray-200 rounded-3xl shadow-lg">
      <DialogHeader>
        <DialogTitle>{{ isEditMode ? "운동 수정" : "운동 기록" }}</DialogTitle>
        <DialogDescription>
          {{ new Date(selectedDate).toLocaleDateString("ko-KR", { month: "long", day: "numeric", weekday: "long" }) }}
        </DialogDescription>
      </DialogHeader>

      <div class="space-y-6">
        <div class="space-y-2">
          <Label class="text-gray-700 font-medium mb-1.5">운동 종류 *</Label>
          <Select v-model="exerciseData.categoryId" @update:modelValue="onSelectedCategory">
            <SelectTrigger class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 rounded-xl">
              <SelectValue placeholder="운동 종류를 선택하세요" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem v-for="category in exerciseCategory" :value="category.id">
                {{ category.label }}
              </SelectItem>
            </SelectContent>
          </Select>
        </div>

        <div v-if="exerciseData.type" class="space-y-2">
          <Label class="text-gray-700 font-medium mb-1.5">운동 이름 *</Label>
          <Select v-model="exerciseData.catalogId" @update:modelValue="onSelectedCatalog">
            <SelectTrigger class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 rounded-xl">
              <SelectValue placeholder="운동을 선택하세요" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem v-for="catalog in exerciseCatalog" :value="catalog.exerciseId">
                {{ catalog.exerciseName }}
              </SelectItem>
            </SelectContent>
          </Select>
        </div>

        <div v-if="exerciseData.type === 'strength'" class="grid grid-cols-1 sm:grid-cols-4 gap-4">
          <div class="space-y-2">
            <Label class="text-gray-700 font-medium mb-1.5">세트 수 *</Label>
            <Input type="number" min="0" placeholder="3" v-model.number="exerciseData.setNo" class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 rounded-xl" />
          </div>
          <div class="space-y-2">
            <Label class="text-gray-700 font-medium mb-1.5">반복 횟수 *</Label>
            <Input type="number" min="0" placeholder="10" v-model.number="exerciseData.reps" class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 rounded-xl" />
          </div>
          <div class="space-y-2">
            <Label class="text-gray-700 font-medium mb-1.5">무게 (kg) *</Label>
            <Input type="number" min="0" placeholder="20" v-model.number="exerciseData.weight" step="0.5" class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 rounded-xl" />
          </div>
          <div class="space-y-2">
            <Label class="text-gray-700 font-medium mb-1.5">소요 시간 (분) *</Label>
            <Input type="number" min="0" placeholder="30" v-model.number="exerciseData.duration" class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 rounded-xl" />
          </div>
        </div>

        <div v-if="exerciseData.type === 'cardio' || exerciseData.type === 'etc'" class="space-y-2">
          <Label class="text-gray-700 font-medium mb-1.5">소요 시간 (분) *</Label>
          <Input type="number" min="0" placeholder="30" v-model.number="exerciseData.duration" class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 rounded-xl" />
        </div>

        <div v-if="exerciseData.type === 'bodyweight'" class="grid grid-cols-1 sm:grid-cols-3 gap-4">
          <div class="space-y-2">
            <Label class="text-gray-700 font-medium mb-1.5">세트 수 *</Label>
            <Input id="setId" min="0" type="number" placeholder="3" v-model.number="exerciseData.setNo" class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 rounded-xl" />
          </div>
          <div class="space-y-2">
            <Label class="text-gray-700 font-medium mb-1.5">반복 횟수 *</Label>
            <Input type="number" min="0" placeholder="10" v-model.number="exerciseData.reps" class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 rounded-xl" />
          </div>
          <div class="space-y-2">
            <Label class="text-gray-700 font-medium mb-1.5">소요 시간 (분) *</Label>
            <Input type="number" min="0" placeholder="30" v-model.number="exerciseData.duration" class="h-12 bg-gray-50 border-gray-200 focus:ring-emerald-500 rounded-xl" />
          </div>
        </div>

        <div v-if="exerciseData.name" class="space-y-3 pt-4 border-t">
          <Label class="text-gray-700 font-medium mb-1.5">자동 계산 결과</Label>
          <div class="bg-gray-50 rounded-2xl p-5 border border-gray-100 space-y-3">
            <div class="grid grid-cols-2 gap-3">
              <div class="bg-white border border-gray-100 shadow-sm rounded-xl p-4">
                <div class="flex items-center gap-2 mb-2">
                  <div class="w-8 h-8 rounded-full bg-emerald-50 text-emerald-600 flex items-center justify-center">
                    <Flame class="w-4 h-4" />
                  </div>
                  <span class="text-xs text-gray-600">소모 칼로리</span>
                </div>
                <div v-if="calculateCalories != null">
                  <div class="text-2xl font-semibold text-emerald-600">{{ calculateCalories }}</div>
                  <div class="text-xs text-gray-500">kcal</div>
                </div>
                <div v-else class="text-xm font-semibold text-emerald-600">몸무게를 먼저 입력해주세요!</div>
              </div>

              <div v-if="isGroupMember" class="bg-white border border-gray-100 shadow-sm rounded-xl p-4">
                <div class="flex items-center gap-2 mb-2">
                  <div class="w-8 h-8 rounded-full bg-blue-50 text-blue-600 flex items-center justify-center">
                    <TrendingUp class="w-4 h-4" />
                  </div>
                  <span class="text-xs text-gray-600">획득 점수</span>
                </div>
                <div v-if="calculatePoints != null">
                  <div class="text-2xl font-semibold text-blue-600">{{ calculatePoints }}</div>
                  <div class="text-xs text-gray-500">points</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="flex gap-3 pt-4">
          <Button v-if="isEditMode" variant="outline" class="text-red-500 hover:bg-red-50 border-red-200 h-12 rounded-xl" @click="showDeleteDialog = true">
            <Trash2 class="w-4 h-4 mr-2" /> 삭제
          </Button>
          <Button variant="outline" class="flex-1 hover:bg-gray-100 h-12 rounded-xl" @click="handleCancle">
            취소
          </Button>
          <Button class="flex-1 bg-emerald-500 hover:bg-emerald-600 text-white h-12 rounded-xl font-bold shadow-md shadow-emerald-200" :disabled="!exerciseData.type || !exerciseData.name || !checkSubmitAble()" @click="handleSubmit(mainStore.setMainData)">
            {{ isEditMode ? "수정하기" : "저장하기" }}
          </Button>
        </div>
      </div>
    </DialogContent>
  </Dialog>

  <AlertDialog :open="showDeleteDialog" @update:open="(val) => (showDeleteDialog = val)">
    <AlertDialogContent>
      <AlertDialogHeader>
        <AlertDialogTitle>운동 기록 삭제</AlertDialogTitle>
        <AlertDialogDescription>정말로 삭제하시겠습니까?</AlertDialogDescription>
      </AlertDialogHeader>
      <AlertDialogFooter>
        <AlertDialogCancel>취소</AlertDialogCancel>
        <AlertDialogAction class="bg-destructive text-destructive-foreground hover:bg-destructive/90" @click="handleDelete(mainStore.setMainData)">
          삭제
        </AlertDialogAction>
      </AlertDialogFooter>
    </AlertDialogContent>
  </AlertDialog>
</template>
