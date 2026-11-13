import { Home, Utensils, Dumbbell, Users, User, BarChart3, Award } from "lucide-react";

interface NavigationProps {
  currentPage: string;
  onNavigate: (page: string) => void;
}

export function Navigation({ currentPage, onNavigate }: NavigationProps) {
  const navItems = [
    { id: "dashboard", label: "메인 페이지", icon: Home },
    { id: "data-dashboard", label: "대시보드", icon: BarChart3 },
    { id: "meals", label: "식단", icon: Utensils },
    { id: "exercise", label: "운동", icon: Dumbbell },
    { id: "groups", label: "그룹", icon: Users },
    { id: "badges", label: "뱃지", icon: Award },
    { id: "mypage", label: "마이페이지", icon: User },
  ];

  return (
    <nav className="bg-card border-b border-border sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4">
        <div className="flex items-center justify-between h-16">
          {/* 로고 */}
          <button
            onClick={() => onNavigate("dashboard")}
            className="flex items-center gap-2"
          >
            <span className="text-2xl">🍽️</span>
            <span className="text-primary">얌얌 프로젝트</span>
          </button>

          {/* 네비게이션 메뉴 */}
          <div className="flex items-center gap-1">
            {navItems.map((item) => {
              const Icon = item.icon;
              const isActive = currentPage === item.id;
              
              return (
                <button
                  key={item.id}
                  onClick={() => onNavigate(item.id)}
                  className={`flex items-center gap-2 px-4 py-2 rounded-lg transition-all ${
                    isActive
                      ? "bg-primary text-primary-foreground"
                      : "text-foreground hover:bg-muted"
                  }`}
                >
                  <Icon className="w-4 h-4" />
                  <span className="text-sm hidden md:inline">{item.label}</span>
                </button>
              );
            })}
          </div>
        </div>
      </div>
    </nav>
  );
}
