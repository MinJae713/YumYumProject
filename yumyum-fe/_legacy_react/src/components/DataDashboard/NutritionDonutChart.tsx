import { Card } from "./ui/card";
import { PieChart, Pie, Cell, ResponsiveContainer, Legend, Tooltip } from "recharts";
import { Apple } from "lucide-react";

const nutritionData = [
  { name: "탄수화물", value: 45, color: "#3b82f6" },
  { name: "단백질", value: 30, color: "#10b981" },
  { name: "지방", value: 25, color: "#f97316" },
];

export function NutritionDonutChart() {
  const totalCalories = 1850;

  return (
    <Card className="p-6">
      <div className="flex items-center gap-3 mb-6">
        <div className="w-10 h-10 bg-secondary/10 rounded-lg flex items-center justify-center">
          <Apple className="w-5 h-5 text-secondary" />
        </div>
        <div>
          <h3>영양소 섭취 비율</h3>
          <p className="text-xs text-muted-foreground">탄수화물 · 단백질 · 지방</p>
        </div>
      </div>

      <div className="flex flex-col items-center">
        <ResponsiveContainer width="100%" height={280}>
          <PieChart>
            <Pie
              data={nutritionData}
              cx="50%"
              cy="50%"
              innerRadius={70}
              outerRadius={100}
              paddingAngle={5}
              dataKey="value"
            >
              {nutritionData.map((entry, index) => (
                <Cell key={`cell-${index}`} fill={entry.color} />
              ))}
            </Pie>
            <Tooltip
              contentStyle={{
                backgroundColor: "#ffffff",
                border: "1px solid #e2e8f0",
                borderRadius: "8px",
              }}
              formatter={(value: number) => `${value}%`}
            />
          </PieChart>
        </ResponsiveContainer>

        <div className="w-full space-y-3 mt-4">
          {nutritionData.map((item) => (
            <div
              key={item.name}
              className="flex items-center justify-between p-3 bg-muted/30 rounded-lg"
            >
              <div className="flex items-center gap-3">
                <div
                  className="w-4 h-4 rounded-full"
                  style={{ backgroundColor: item.color }}
                />
                <span className="text-sm">{item.name}</span>
              </div>
              <div className="flex items-center gap-3">
                <span className="text-sm text-muted-foreground">{item.value}%</span>
                <span className="text-sm">
                  {Math.round((totalCalories * item.value) / 100)} kcal
                </span>
              </div>
            </div>
          ))}
        </div>

        <div className="w-full mt-4 p-4 bg-primary/10 rounded-lg text-center">
          <div className="text-xs text-muted-foreground mb-1">총 섭취 칼로리</div>
          <div className="text-2xl text-primary">{totalCalories} kcal</div>
        </div>
      </div>
    </Card>
  );
}
