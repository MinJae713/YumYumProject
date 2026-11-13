import { useState } from "react";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { Card } from "./ui/card";
import { RadioGroup, RadioGroupItem } from "./ui/radio-group";

interface SignupPageProps {
  onNavigate: (page: string) => void;
}

export function SignupPage({ onNavigate }: SignupPageProps) {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    confirmPassword: "",
    name: "",
    userType: "general",
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (formData.password !== formData.confirmPassword) {
      alert("비밀번호가 일치하지 않습니다.");
      return;
    }

    console.log("회원가입:", formData);
    // 회원가입 후 온보딩으로 이동
    onNavigate("onboarding");
  };

  return (
    <div className="min-h-screen bg-background flex items-center justify-center p-4">
      <Card className="w-full max-w-2xl p-8">
        <div className="text-center mb-8">
          <h1 className="text-primary mb-2">🍽️ 얌얌 프로젝트</h1>
          <h2 className="mb-2">회원가입</h2>
          <p className="text-muted-foreground">건강한 식단 관리의 첫 걸음을 시작하세요</p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          {/* 회원 유형 선택 */}
          <div className="space-y-3">
            <Label>회원 유형 선택</Label>
            <RadioGroup
              value={formData.userType}
              onValueChange={(value) =>
                setFormData({ ...formData, userType: value })
              }
              className="grid grid-cols-2 gap-4"
            >
              <Label
                htmlFor="general"
                className={`flex items-center space-x-3 border-2 rounded-lg p-4 cursor-pointer transition-all ${
                  formData.userType === "general"
                    ? "border-primary bg-primary/5"
                    : "border-border hover:border-primary/50"
                }`}
              >
                <RadioGroupItem value="general" id="general" />
                <div className="flex-1">
                  <div className="font-medium">일반 회원</div>
                  <div className="text-sm text-muted-foreground">
                    개인 식단 관리를 원하는 사용자
                  </div>
                </div>
              </Label>

              <Label
                htmlFor="business"
                className={`flex items-center space-x-3 border-2 rounded-lg p-4 cursor-pointer transition-all ${
                  formData.userType === "business"
                    ? "border-primary bg-primary/5"
                    : "border-border hover:border-primary/50"
                }`}
              >
                <RadioGroupItem value="business" id="business" />
                <div className="flex-1">
                  <div className="font-medium">비즈니스 회원</div>
                  <div className="text-sm text-muted-foreground">
                    트레이너, 영양사 등 전문가
                  </div>
                </div>
              </Label>
            </RadioGroup>
          </div>

          {/* 기본 정보 입력 */}
          <div className="grid md:grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="name">이름</Label>
              <Input
                id="name"
                type="text"
                placeholder="홍길동"
                value={formData.name}
                onChange={(e) =>
                  setFormData({ ...formData, name: e.target.value })
                }
                required
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="signup-email">이메일</Label>
              <Input
                id="signup-email"
                type="email"
                placeholder="example@email.com"
                value={formData.email}
                onChange={(e) =>
                  setFormData({ ...formData, email: e.target.value })
                }
                required
              />
            </div>
          </div>

          <div className="grid md:grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="signup-password">비밀번호</Label>
              <Input
                id="signup-password"
                type="password"
                placeholder="8자 이상 입력"
                value={formData.password}
                onChange={(e) =>
                  setFormData({ ...formData, password: e.target.value })
                }
                required
                minLength={8}
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="confirm-password">비밀번호 확인</Label>
              <Input
                id="confirm-password"
                type="password"
                placeholder="비밀번호 재입력"
                value={formData.confirmPassword}
                onChange={(e) =>
                  setFormData({ ...formData, confirmPassword: e.target.value })
                }
                required
                minLength={8}
              />
            </div>
          </div>

          <div className="flex gap-3 pt-4">
            <Button
              type="button"
              variant="outline"
              className="flex-1"
              onClick={() => onNavigate("login")}
            >
              취소
            </Button>
            <Button type="submit" className="flex-1">
              다음 단계로
            </Button>
          </div>
        </form>

        <div className="mt-6 text-center text-sm">
          <span className="text-muted-foreground">이미 계정이 있으신가요? </span>
          <button
            type="button"
            className="text-primary hover:underline"
            onClick={() => onNavigate("login")}
          >
            로그인
          </button>
        </div>
      </Card>
    </div>
  );
}
