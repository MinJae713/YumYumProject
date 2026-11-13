import { Card } from "./ui/card";
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend } from "recharts";
import { TrendingDown } from "lucide-react";

const mockWeightData = [
  { date: "10/01", weight: 75.5, goal: 70 },
  { date: "10/08", weight: 74.8, goal: 70 },
  { date: "10/15", weight: 74.2, goal: 70 },
  { date: "10/22", weight: 73.5, goal: 70 },
  { date: "10/29", weight: 72.8, goal: 70 },
  { date: "11/05", weight: 72.1, goal: 70 },
];

export function WeightChart() {
  const currentWeight = mockWeightData[mockWeightData.length - 1].weight;
  const startWeight = mockWeightData[0].weight;
  const weightLoss = startWeight - currentWeight;
  const goalWeight = mockWeightData[0].goal;

  return (
    <Card className="p-6">
      <div className="flex items-center justify-between mb-6">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 bg-primary/10 rounded-lg flex items-center justify-center">
            <TrendingDown className="w-5 h-5 text-primary" />
          </div>
          <div>
            <h3>몸무게 변화</h3>
            <p className="text-xs text-muted-foreground">최근 5주간 추이</p>
          </div>
        </div>
        <div className="text-right">
          <div className="text-2xl text-primary">{currentWeight} kg</div>
          <div className="text-xs text-muted-foreground">
            목표까지 {(currentWeight - goalWeight).toFixed(1)}kg
          </div>
        </div>
      </div>

      <div className="grid grid-cols-3 gap-4 mb-6">
        <div className="bg-muted/50 rounded-lg p-3 text-center">
          <div className="text-xs text-muted-foreground mb-1">시작 체중</div>
          <div className="text-lg">{startWeight} kg</div>
        </div>
        <div className="bg-primary/10 rounded-lg p-3 text-center">
          <div className="text-xs text-muted-foreground mb-1">현재 체중</div>
          <div className="text-lg text-primary">{currentWeight} kg</div>
        </div>
        <div className="bg-muted/50 rounded-lg p-3 text-center">
          <div className="text-xs text-muted-foreground mb-1">감량</div>
          <div className="text-lg text-accent">-{weightLoss.toFixed(1)} kg</div>
        </div>
      </div>

      <ResponsiveContainer width="100%" height={300}>
        <LineChart data={mockWeightData}>
          <CartesianGrid strokeDasharray="3 3" stroke="#e2e8f0" />
          <XAxis
            dataKey="date"
            stroke="#64748b"
            style={{ fontSize: "12px" }}
          />
          <YAxis
            stroke="#64748b"
            style={{ fontSize: "12px" }}
            domain={[68, 77]}
          />
          <Tooltip
            contentStyle={{
              backgroundColor: "#ffffff",
              border: "1px solid #e2e8f0",
              borderRadius: "8px",
            }}
          />
          <Legend />
          <Line
            type="monotone"
            dataKey="weight"
            stroke="#10b981"
            strokeWidth={3}
            name="실제 체중"
            dot={{ fill: "#10b981", r: 5 }}
          />
          <Line
            type="monotone"
            dataKey="goal"
            stroke="#f97316"
            strokeWidth={2}
            strokeDasharray="5 5"
            name="목표 체중"
            dot={false}
          />
        </LineChart>
      </ResponsiveContainer>
    </Card>
  );
}
