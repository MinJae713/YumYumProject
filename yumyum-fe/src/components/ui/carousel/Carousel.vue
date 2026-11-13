<script setup>
import { useProvideCarousel } from './useCarousel'
import { cn } from '@/lib/utils'

const props = defineProps({
  opts: { type: Object, default: () => ({}) },
  plugins: { type: Array, default: () => [] },
  orientation: { type: String, default: 'horizontal' },
  class: { type: String, default: '' },
})

const { carouselRef, orientation: apiOrientation } = useProvideCarousel(props)
</script>

<template>
  <div
    :class="cn('relative', props.class)"
    role="region"
    aria-roledescription="carousel"
  >
    <slot />
  </div>
</template>