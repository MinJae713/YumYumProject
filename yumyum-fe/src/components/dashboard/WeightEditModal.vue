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
import { storeToRefs } from "pinia";
import useWeightChart from "@/stores/dashboard/WeightChart";
import useDashboard from "@/stores/dashboard/DashboardView";

const weightChartStore = useWeightChart();
const {
  modalOpen,
  date,
  weight
} = storeToRefs(weightChartStore);
const closeModal = weightChartStore.closeModal;
const onDateSelect = weightChartStore.onDateSelect;
const handleSubmit = weightChartStore.handleSubmit;

const dashboardMainStore = useDashboard();
const setDashboardData = dashboardMainStore.setDashboardData;

</script>

<template>
  <Dialog :open="modalOpen" @update:open="(val) => !val && closeModal()">
    <DialogContent class="sm:max-w-md max-h-[90vh] bg-white rounded-3xl border border-gray-200">
      <DialogHeader>
        <DialogTitle class="text-lg font-bold text-gray-900">몸무게 수정</DialogTitle>
        <DialogDescription class="text-sm text-gray-500">
          선택한 날짜의 몸무게를 입력해주세요
        </DialogDescription>
      </DialogHeader>

      <div class="space-y-6">
        <!-- 날짜 입력 -->
        <div class="space-y-2">
          <Label class="text-sm font-medium text-gray-700">날짜 *</Label>
          <Input
            type="date"
            v-model="date"
            @input="onDateSelect"
            class="h-12 bg-gray-50 border border-gray-200 rounded-xl focus-visible:ring-emerald-500"
          />
        </div>

        <!-- 몸무게 입력 -->
        <div class="space-y-2">
          <Label class="text-sm font-medium text-gray-700">몸무게 (kg) *</Label>
          <Input
            type="number"
            step="0.1"
            placeholder="예: 72.5"
            v-model="weight"
            class="h-12 bg-gray-50 border border-gray-200 rounded-xl focus-visible:ring-emerald-500"
          />
        </div>

        <!-- 버튼 영역 -->
        <div class="flex gap-2 pt-4">
          <Button
            variant="outline"
            class="flex-1 rounded-xl hover:bg-gray-100"
            @click="closeModal"
          >
            취소
          </Button>
          <Button
            class="flex-1 bg-emerald-500 hover:bg-emerald-600 text-white rounded-xl font-bold"
            @click="handleSubmit(setDashboardData)"
          >
            수정
          </Button>
        </div>
      </div>
    </DialogContent>
  </Dialog>
</template>
