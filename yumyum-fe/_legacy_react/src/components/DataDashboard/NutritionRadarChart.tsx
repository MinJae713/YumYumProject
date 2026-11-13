import { Card } from "./ui/card";
import { RadarChart, PolarGrid, PolarAngleAxis, PolarRadiusAxis, Radar, ResponsiveContainer, Legend, Tooltip } from "recharts";
import { Activity } from "lucide-react";

const nutritionComparisonData = [
  { subject: "칼로리", actual: 92, recommended: 100 },
  { subject: "단백질", actual: 85, recommended: 100 },
  { subject: "탄수화물", actual: 95, recommended: 100 },
  { subject: "지방", actual: 78, recommended: 100 },
  { subject: "식이섬유", actual: 65, recommended: 100 },
  { subject: "비타민", actual: 70, recommended: 100 },
];

export function NutritionRadarChart() {
  const averageAchievement = Math.round(
    nutritionComparisonData.reduce((sum, item) => sum + item.actual, 0) /
      nutritionComparisonData.length
  );

  return (
    <Card className="p-6">
      <div className="flex items-center justify-between mb-6">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 bg-accent/10 rounded-lg flex items-center justify-center">
            <Activity className="w-5 h-5 text-accent" />
          </div>
          <div>
            <h3>권장량 대비 섭취량</h3>
            <p className="text-xs text-muted-foreground">영양소별 목표 달성률</p>
          </div>
        </div>
        <div className="text-right">
          <div className="text-2xl text-primary">{averageAchievement}%</div>
          <div className="text-xs text-muted-foreground">평균 달성률</div>
        </div>
      </div>

      <ResponsiveContainer width="100%" height={300}>
        <RadarChart data={nutritionComparisonData}>
          <PolarGrid stroke="#e2e8f0" />
          <PolarAngleAxis
            dataKey="subject"
            style={{ fontSize: "12px" }}
            stroke="#64748b"
          />
          <PolarRadiusAxis
            angle={90}
            domain={[0, 100]}
            style={{ fontSize: "10px" }}
            stroke="#64748b"
          />
          <Radar
            name="권장량"
            dataKey="recommended"
            stroke="#cbd5e1"
            fill="#cbd5e1"
            fillOpacity={0.3}
          />
          <Radar
            name="실제 섭취량"
            dataKey="actual"
            stroke="#10b981"
            fill="#10b981"
            fillOpacity={0.5}
          />
          <Legend />
          <Tooltip
            contentStyle={{
              backgroundColor: "#ffffff",
              border: "1px solid #e2e8f0",
              borderRadius: "8px",
            }}
            formatter={(value: number) => `${value}%`}
          />
        </RadarChart>
      </ResponsiveContainer>

      <div className="grid grid-cols-3 gap-3 mt-6">
        {nutritionComparisonData.slice(0, 3).map((item) => (
          <div
            key={item.subject}
            className="bg-muted/30 rounded-lg p-3 text-center"
          >
            <div className="text-xs text-muted-foreground mb-1">
              {item.subject}
            </div>
            <div
              className={`text-lg ${
                item.actual >= 90
                  ? "text-primary"
                  : item.actual >= 70
                  ? "text-accent"
                  : "text-destructive"
              }`}
            >
              {item.actual}%
            </div>
          </div>
        ))}
      </div>
    </Card>
  );
}
