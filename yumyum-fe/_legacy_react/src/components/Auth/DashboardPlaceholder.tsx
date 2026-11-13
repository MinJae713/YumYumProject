import { Button } from "./ui/button";
import { Card } from "./ui/card";

interface DashboardPlaceholderProps {
  onNavigate: (page: string) => void;
}

export function DashboardPlaceholder({ onNavigate }: DashboardPlaceholderProps) {
  return (
    <div className="min-h-screen bg-background p-8">
      <div className="max-w-4xl mx-auto">
        <Card className="p-12 text-center">
          <div className="mb-6">
            <div className="text-6xl mb-4">✅</div>
            <h1 className="mb-2">온보딩 완료!</h1>
            <p className="text-muted-foreground">
              회원가입과 초기 설정이 성공적으로 완료되었습니다.
            </p>
          </div>
          
          <div className="max-w-md mx-auto space-y-4 mb-8">
            <div className="bg-muted/50 p-4 rounded-lg text-left">
              <h4 className="mb-2">✨ 다음 단계에서는...</h4>
              <ul className="space-y-2 text-sm text-muted-foreground">
                <li>• 대시보드에서 건강 데이터를 확인할 수 있습니다</li>
                <li>• 식단을 기록하고 분석할 수 있습니다</li>
                <li>• 맞춤형 영양 정보를 제공받습니다</li>
              </ul>
            </div>
          </div>

          <Button onClick={() => onNavigate("login")} size="lg">
            로그인 페이지로 돌아가기
          </Button>
        </Card>
      </div>
    </div>
  );
}
