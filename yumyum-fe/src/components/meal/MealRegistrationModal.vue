<script setup>
import { ref } from "vue";
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
import { Search, Plus, Minus, X, Trash2 } from "lucide-vue-next";
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
import useMealView from "@/stores/meal/MealView";
import { storeToRefs } from "pinia";
import Select from "../ui/select/Select.vue";
import SelectTrigger from "../ui/select/SelectTrigger.vue";
import SelectValue from "../ui/select/SelectValue.vue";
import SelectContent from "../ui/select/SelectContent.vue";
import SelectItem from "../ui/select/SelectItem.vue";
import useMain from "@/stores/main/mainView";

const mealStore = useMealView();

const {
  selectedDate,
  mealSlots,
  isRegistrationOpen,
  selectedMeal,
  searchQuery,
  selectedFoods,
  showDeleteDialog,
  mealTypeLabel,
  totalNutrition,
  filteredFoods,
  currentModalStatus,
  selectedMealType
} = storeToRefs(mealStore);
const mainStore = useMain();

defineProps({
  customTitle: String,
  customDescription: String
})

const emit = defineEmits(["update:open"]);
const date = new Date(selectedDate.value);
const addFood = mealStore.addFood;
const removeFood = mealStore.removeFood;
const updateAmount = mealStore.updateAmount;
const closeModal = mealStore.closeModal;
const handleDelete = mealStore.handleDelete;
const openDeleteDialog = mealStore.openDeleteDialog;

const editingFoodId = ref(null);
const tempAmount = ref("");

const vFocus = { mounted: (el) => el.focus() };

const startEditing = (food) => {
  editingFoodId.value = food.id;
  tempAmount.value = food.amount.toString();
};

const finishEditing = (food) => {
  if (editingFoodId.value === null) return;
  const newValue = parseFloat(tempAmount.value);
  if (!isNaN(newValue) && newValue >= 0) {
    const delta = newValue - food.amount;
    if (delta !== 0) updateAmount(food.id, delta);
  }
  editingFoodId.value = null;
  tempAmount.value = "";
};

const cancelEditing = () => {
  editingFoodId.value = null;
  tempAmount.value = "";
};
</script>

<template>
  <Dialog :open="isRegistrationOpen" @update:open="(val) => !val && closeModal()">
    <DialogContent class="sm:max-w-3xl max-h-[90vh] overflow-y-auto">
      <DialogHeader>
        <DialogTitle>{{ currentModalStatus.isRecommend ? currentModalStatus.modalTitle : `${mealTypeLabel} ${currentModalStatus.modalTitle}` }}</DialogTitle>
        <DialogDescription>
          {{ customDescription || date?.toLocaleDateString("ko-KR", { month: "long", day: "numeric", weekday: "long" }) }}
        </DialogDescription>
      </DialogHeader>

      <div class="space-y-6">
        <div class="space-y-2">
          <Label>음식 검색</Label>
          <div v-if="currentModalStatus.searchAble" class="relative">
            <Search class="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
            <Input
              placeholder="음식 이름을 검색하세요 (예: 닭가슴살, 계란)"
              @input="(text) => { searchQuery = text }"
              class="pl-10"
            />
          </div>
          <div v-else class="relative">
            <Label>추천 식단은 음식 목록을 수정할 수 없습니다!</Label>
          </div>
        </div>

        <div v-if="currentModalStatus.mealTypeSelectAble" class="space-y-2">
          <Label>식단 종류 *</Label>
          <Select v-model="selectedMealType">
            <SelectTrigger>
              <SelectValue placeholder="식단 종류를 선택하세요" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem v-for="mealType in mealSlots" :value="mealType.key">
                {{ mealType.label }}
              </SelectItem>
            </SelectContent>
          </Select>
        </div>

        <div v-if="searchQuery" class="space-y-2">
          <Label>검색 결과</Label>
          <div class="max-h-48 overflow-y-auto space-y-2 border rounded-lg p-2">
            <div v-if="filteredFoods.length > 0" v-for="food in filteredFoods" :key="food.id"
              class="flex items-center justify-between p-2 hover:bg-muted rounded-lg cursor-pointer"
              @click="addFood(food)"
            >
              <div>
                <div class="text-sm">{{ food.name }}</div>
                <div class="text-xs text-muted-foreground">{{ food.servingSize }}{{ food.servingUnit }} · {{ food.calories }}kcal</div>
              </div>
              <Button size="sm" variant="ghost"><Plus class="w-4 h-4" /></Button>
            </div>
            <div v-else class="text-center text-sm text-muted-foreground py-4">검색 결과가 없습니다</div>
          </div>
        </div>

        <div class="space-y-2">
          <Label>선택한 음식</Label>
          <div v-if="selectedFoods.length > 0" class="space-y-3 border rounded-lg p-4">
            <div v-for="food in selectedFoods" :key="food.id" class="flex items-center gap-3 p-3 bg-muted/50 rounded-lg">
              <div class="flex-1">
                <div class="text-sm mb-1">{{ food.name }}</div>
                <div class="flex items-center gap-2 text-xs text-muted-foreground">
                  <span>{{ Math.round(food.calories * (food.amount / food.servingSize)) }}kcal ·</span>
                  <span>{{ food.amount }}{{ food.servingUnit }}</span>
                </div>
              </div>
              <div class="flex items-center gap-2">
                <Button size="sm" variant="outline" class="w-8 h-8 rounded-full" @click="updateAmount(food.id, -10)" :disabled="food.amount <= 0">
                  <Minus class="w-3 h-3" />
                </Button>
                
                <div class="min-w-[80px] flex justify-center">
                  <Input
                    v-if="editingFoodId === food.id"
                    type="number"
                    v-model="tempAmount"
                    v-focus
                    class="h-8 text-center px-1 text-sm bg-white border-emerald-500"
                    @blur="finishEditing(food)"
                    @keydown.enter="finishEditing(food)"
                    @keydown.esc="cancelEditing"
                    @click.stop 
                  />
                  <div
                    v-else
                    class="text-sm font-medium text-gray-700 text-center w-full py-1 cursor-text hover:bg-gray-100 rounded border border-transparent hover:border-gray-200 transition-all select-none"
                    title="더블 클릭하여 숫자 입력"
                    @dblclick="startEditing(food)"
                  >
                    {{ food.amount }}{{ food.servingUnit }}
                  </div>
                </div>

                <Button size="sm" variant="outline" class="w-8 h-8 rounded-full" @click="updateAmount(food.id, 10)">
                  <Plus class="w-3 h-3" />
                </Button>
                <Button v-if="!currentModalStatus.isRecommend" size="sm" variant="ghost" class="text-red-500 hover:bg-red-50" @click="removeFood(food.id)">
                  <X class="w-4 h-4" />
                </Button>
              </div>
            </div>
          </div>
          <div v-else class="text-center text-sm text-muted-foreground py-8 border rounded-lg">
            음식을 검색하여 추가해주세요
          </div>
        </div>

        <div v-if="selectedFoods.length > 0" class="space-y-2">
          <Label>영양소 합계</Label>
          <div class="grid grid-cols-4 gap-3">
            <div class="bg-primary/10 rounded-lg p-3 text-center">
              <div class="text-xs text-muted-foreground mb-1">칼로리</div>
              <div class="text-lg text-primary">{{ Math.round(totalNutrition.calories) }}</div>
            </div>
            </div>
        </div>

        <div class="flex gap-2 pt-4">
          <Button v-if="selectedMeal" variant="destructive" @click="openDeleteDialog"><Trash2 class="w-4 h-4 mr-2" />삭제</Button>
          <Button variant="outline" @click="closeModal" class="flex-1">취소</Button>
          <Button @click="currentModalStatus.handleSubmit(mainStore.setMainData)" :disabled="selectedFoods.length === 0" class="flex-1">{{ currentModalStatus.label }}</Button>
        </div>
      </div>
    </DialogContent>
  </Dialog>

  <AlertDialog :open="showDeleteDialog" @update:open="(val) => (showDeleteDialog = val)">
    <AlertDialogContent>
      <AlertDialogHeader>
        <AlertDialogTitle>식단 기록 삭제</AlertDialogTitle>
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