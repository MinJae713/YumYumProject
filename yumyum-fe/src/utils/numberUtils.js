export const safeNumber = (value, fallback = null) => {
  return typeof value === "number" && !Number.isNaN(value) ? value : fallback;
};
