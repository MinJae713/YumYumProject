<script setup>
import { ref } from "vue";
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Users } from "lucide-vue-next";

const props = defineProps({
  open: { type: Boolean, default: false },
  onOpenChange: { type: Function, default: undefined },
  onCreateGroup: { type: Function, default: undefined },
});

const emit = defineEmits(["update:open"]);
const groupName = ref("");
const isSubmitting = ref(false);

const handleCreate = async (event) => {
  if (event instanceof KeyboardEvent && event.isComposing) return;
  if (!groupName.value.trim() || isSubmitting.value) return;
  isSubmitting.value = true;
  try {
    await props.onCreateGroup?.(groupName.value.trim());
    groupName.value = "";
    emit("update:open", false);
    props.onOpenChange?.(false);
  } catch (error) {
    console.error(error);
  } finally {
    isSubmitting.value = false;
  }
};

const handleCancel = () => {
  groupName.value = "";
  emit("update:open", false);
  props.onOpenChange?.(false);
};
</script>

<template>
  <Dialog :open="open" @update:open="(val) => emit('update:open', val)">
    <DialogContent class="sm:max-w-md rounded-3xl border border-gray-200 p-6">
      <DialogHeader class="space-y-2">
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 bg-emerald-50 rounded-xl flex items-center justify-center">
            <Users class="w-5 h-5 text-emerald-600" />
          </div>
          <DialogTitle class="text-xl font-semibold text-gray-900">새 그룹 만들기</DialogTitle>
        </div>
        <p class="text-sm text-gray-500">
          새로운 그룹을 생성하고 그룹원들을 관리하세요
        </p>
      </DialogHeader>

      <div class="space-y-4 py-4">
        <div class="space-y-2">
          <Label for="group-name">그룹 이름</Label>
          <Input
            id="group-name"
            placeholder="예: 다이어트 챌린지 그룹"
            v-model="groupName"
            @keydown.enter.prevent="handleCreate"
            :disabled="isSubmitting"
            class="rounded-xl border border-gray-200 focus-visible:ring-emerald-500"
            autofocus
          />
        </div>
      </div>

      <DialogFooter class="flex gap-2 sm:gap-2">
        <Button
          variant="outline"
          class="flex-1 rounded-xl border border-gray-200 text-gray-900"
          @click="handleCancel"
        >
          취소
        </Button>
        <Button
          class="flex-1 rounded-xl bg-emerald-600 hover:bg-emerald-700 text-white"
          :disabled="!groupName.trim() || isSubmitting"
          @click="handleCreate"
        >
          생성
        </Button>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>
