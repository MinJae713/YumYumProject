<script setup>
const props = defineProps({
  modelValue: {
    type: [String, Number],
    default: null,
  },
  options: {
    type: Array,
    required: true,
  },
});

const emit = defineEmits(["update:modelValue"]);

const selectOption = (value) => {
  if (props.modelValue === value) return;
  emit("update:modelValue", value);
};
</script>

<template>
  <div class="w-full py-4">
    <div class="relative w-full">
      
      <div class="absolute top-3 left-0 w-full h-[2px] bg-gray-200 z-0 rounded-full"></div>

      <div class="relative flex justify-between w-full z-10">
        <button 
          v-for="(option, index) in options" 
          :key="option.value"
          type="button"
          class="group flex flex-col items-center gap-3 cursor-pointer"
          @click="selectOption(option.value)"
        >
          <div 
            class="w-5 h-5 rounded-full border-2 flex items-center justify-center transition-all duration-200 bg-white z-20"
            :class="[
              modelValue === option.value 
                ? 'border-emerald-500 ring-4 ring-emerald-100 scale-125'  /* 선택됨 */
                : 'border-gray-300 group-hover:border-emerald-300' /* 기본 */
            ]"
          >
            <div 
              v-if="modelValue === option.value" 
              class="w-2.5 h-2.5 rounded-full bg-emerald-500"
            ></div>
          </div>

          <span 
            class="text-xs select-none transition-colors"
            :class="modelValue === option.value ? 'font-bold text-emerald-700' : 'text-gray-400'"
          >
            {{ option.label }}
          </span>
        </button>
      </div>
    </div>
  </div>
</template>
