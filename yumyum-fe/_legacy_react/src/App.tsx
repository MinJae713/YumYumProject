import { useState } from "react";
import { LoginPage } from "./components/LoginPage";
import { SignupPage } from "./components/SignupPage";
import { OnboardingSurvey } from "./components/OnboardingSurvey";
import { Navigation } from "./components/Navigation";
import { MainDashboard } from "./components/MainDashboard";
import { DataDashboard } from "./components/DataDashboard";
import { MealPage } from "./components/MealPage";
import { ExercisePage } from "./components/ExercisePage";
import { GroupPage } from "./components/GroupPage";
import { BadgesPage } from "./components/BadgesPage";
import { MyPage } from "./components/MyPage";
import { Toaster } from "./components/ui/sonner";

export default function App() {
  const [currentPage, setCurrentPage] = useState<string>("data-dashboard");

  const handleNavigate = (page: string) => {
    setCurrentPage(page);
  };

  // 로그인/회원가입/온보딩 페이지는 네비게이션 바 없음
  const showNavigation = !["login", "signup", "onboarding"].includes(currentPage);

  return (
    <>
      {showNavigation && <Navigation currentPage={currentPage} onNavigate={handleNavigate} />}
      
      {currentPage === "login" && <LoginPage onNavigate={handleNavigate} />}
      {currentPage === "signup" && <SignupPage onNavigate={handleNavigate} />}
      {currentPage === "onboarding" && <OnboardingSurvey onNavigate={handleNavigate} />}
      {currentPage === "dashboard" && <MainDashboard />}
      {currentPage === "data-dashboard" && <DataDashboard />}
      {currentPage === "meals" && <MealPage />}
      {currentPage === "exercise" && <ExercisePage />}
      {currentPage === "groups" && <GroupPage />}
      {currentPage === "badges" && <BadgesPage />}
      {currentPage === "mypage" && <MyPage onNavigate={handleNavigate} />}
      <Toaster />
    </>
  );
}