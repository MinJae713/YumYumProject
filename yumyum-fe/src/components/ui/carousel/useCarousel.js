import { createInjectionState } from '@vueuse/core'
import emblaCarouselVue from 'embla-carousel-vue'
import { onMounted, ref } from 'vue'

const [useProvideCarousel, useInjectCarousel] = createInjectionState(
  ({ opts, orientation, plugins, class: className, ...props }) => {
    const [emblaNode, emblaApi] = emblaCarouselVue(
      {
        ...opts,
        axis: orientation === 'horizontal' ? 'x' : 'y',
      },
      plugins
    )

    const canScrollPrev = ref(false)
    const canScrollNext = ref(false)

    const scrollPrev = () => {
      emblaApi.value?.scrollPrev()
    }

    const scrollNext = () => {
      emblaApi.value?.scrollNext()
    }

    const onSelect = (api) => {
      canScrollPrev.value = api.canScrollPrev()
      canScrollNext.value = api.canScrollNext()
    }

    onMounted(() => {
      if (!emblaApi.value) return

      emblaApi.value.on('init', onSelect)
      emblaApi.value.on('reInit', onSelect)
      emblaApi.value.on('select', onSelect)
    })

    return {
      carouselRef: emblaNode,
      carouselApi: emblaApi,
      canScrollPrev,
      canScrollNext,
      scrollPrev,
      scrollNext,
      orientation,
    }
  }
)

function useCarousel() {
  const carouselState = useInjectCarousel()
  if (!carouselState)
    throw new Error('useCarousel must be used within a <Carousel />')

  return carouselState
}

export { useProvideCarousel, useCarousel }