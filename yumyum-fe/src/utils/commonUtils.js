// ⭐️ 날짜 포맷 헬퍼 함수 (YYYY-MM-DD 형식으로 0 채우기)
export const formatDateKey = (date) => {
  const d = new Date(date);
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, "0"); // 01, 02...
  const day = String(d.getDate()).padStart(2, "0");       // 01, 02...
  return `${year}-${month}-${day}`;
};