import { useState } from "react";
import { Card } from "./ui/card";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { Avatar, AvatarFallback, AvatarImage } from "./ui/avatar";
import { Badge } from "./ui/badge";
import { Separator } from "./ui/separator";
import { Switch } from "./ui/switch";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "./ui/dialog";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "./ui/select";
import {
  User,
  Lock,
  Bell,
  RefreshCw,
  ChevronRight,
  Settings,
  LogOut,
  Shield,
  Mail,
  Smartphone,
  Award,
} from "lucide-react";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "./ui/alert-dialog";

interface MyPageProps {
  onNavigate: (page: string) => void;
}

export function MyPage({ onNavigate }: MyPageProps) {
  const [nickname, setNickname] = useState("이지은");
  const [email] = useState("jieun@example.com");
  const [notificationTime, setNotificationTime] = useState("09:00");
  const [pushEnabled, setPushEnabled] = useState(true);
  const [emailNotification, setEmailNotification] = useState(false);

  const [isNicknameDialogOpen, setIsNicknameDialogOpen] = useState(false);
  const [isPasswordDialogOpen, setIsPasswordDialogOpen] = useState(false);
  const [isResetDialogOpen, setIsResetDialogOpen] = useState(false);
  const [isLogoutDialogOpen, setIsLogoutDialogOpen] = useState(false);

  const [tempNickname, setTempNickname] = useState(nickname);
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const handleNicknameChange = () => {
    setNickname(tempNickname);
    setIsNicknameDialogOpen(false);
  };

  const handlePasswordChange = () => {
    // 비밀번호 변경 로직
    if (newPassword === confirmPassword && currentPassword) {
      alert("비밀번호가 변경되었습니다.");
      setIsPasswordDialogOpen(false);
      setCurrentPassword("");
      setNewPassword("");
      setConfirmPassword("");
    } else {
      alert("비밀번호를 확인해주세요.");
    }
  };

  const handleReset = () => {
    // 리서치 재설정 로직
    alert("온보딩 리서치가 재설정되었습니다.");
    setIsResetDialogOpen(false);
  };

  const handleLogout = () => {
    // 로그아웃 처리 (실제로는 토큰 제거 등 수행)
    console.log("로그아웃 처리됨");
    setIsLogoutDialogOpen(false);
    onNavigate("login");
  };

  return (
    <div className="max-w-4xl mx-auto px-4 py-6">
      {/* 헤더 */}
      <div className="flex items-center gap-3 mb-6">
        <div className="w-12 h-12 bg-primary/10 rounded-lg flex items-center justify-center">
          <Settings className="w-6 h-6 text-primary" />
        </div>
        <div>
          <h1>마이페이지</h1>
          <p className="text-sm text-muted-foreground">
            계정 설정 및 정보를 관리하세요
          </p>
        </div>
      </div>

      {/* 프로필 카드 */}
      <Card className="p-6 mb-6">
        <div className="flex items-center gap-4">
          <Avatar className="w-20 h-20">
            <AvatarImage
              src="https://api.dicebear.com/7.x/avataaars/svg?seed=2"
              alt={nickname}
            />
            <AvatarFallback>{nickname[0]}</AvatarFallback>
          </Avatar>
          <div className="flex-1">
            <div className="flex items-center gap-2 mb-1">
              <h3>{nickname}</h3>
              <Badge variant="secondary">일반 회원</Badge>
            </div>
            <div className="text-sm text-muted-foreground mb-2">{email}</div>
            <div className="flex items-center gap-2">
              <Badge variant="outline" className="text-xs">
                <Award className="w-3 h-3 mr-1" />
                뱃지 5개 획득
              </Badge>
              <Badge variant="outline" className="text-xs">
                연속 12일
              </Badge>
            </div>
          </div>
        </div>
      </Card>

      <div className="space-y-6">
        {/* 회원 정보 섹션 */}
        <div>
          <h3 className="mb-4">회원 정보</h3>
          <Card className="divide-y">
            {/* 닉네임 변경 */}
            <Dialog
              open={isNicknameDialogOpen}
              onOpenChange={setIsNicknameDialogOpen}
            >
              <DialogTrigger asChild>
                <button className="w-full p-4 flex items-center justify-between hover:bg-muted/50 transition-colors">
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 rounded-lg bg-primary/10 flex items-center justify-center">
                      <User className="w-5 h-5 text-primary" />
                    </div>
                    <div className="text-left">
                      <div className="text-sm font-medium">닉네임 변경</div>
                      <div className="text-xs text-muted-foreground">
                        현재: {nickname}
                      </div>
                    </div>
                  </div>
                  <ChevronRight className="w-5 h-5 text-muted-foreground" />
                </button>
              </DialogTrigger>
              <DialogContent>
                <DialogHeader>
                  <DialogTitle>닉네임 변경</DialogTitle>
                  <DialogDescription>
                    새로운 닉네임을 입력해주세요
                  </DialogDescription>
                </DialogHeader>
                <div className="space-y-4 py-4">
                  <div className="space-y-2">
                    <Label htmlFor="nickname">닉네임</Label>
                    <Input
                      id="nickname"
                      value={tempNickname}
                      onChange={(e) => setTempNickname(e.target.value)}
                      placeholder="새 닉네임을 입력하세요"
                    />
                  </div>
                </div>
                <DialogFooter>
                  <Button
                    variant="outline"
                    onClick={() => setIsNicknameDialogOpen(false)}
                  >
                    취소
                  </Button>
                  <Button onClick={handleNicknameChange}>변경</Button>
                </DialogFooter>
              </DialogContent>
            </Dialog>

            {/* 비밀번호 변경 */}
            <Dialog
              open={isPasswordDialogOpen}
              onOpenChange={setIsPasswordDialogOpen}
            >
              <DialogTrigger asChild>
                <button className="w-full p-4 flex items-center justify-between hover:bg-muted/50 transition-colors">
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 rounded-lg bg-secondary/10 flex items-center justify-center">
                      <Lock className="w-5 h-5 text-secondary" />
                    </div>
                    <div className="text-left">
                      <div className="text-sm font-medium">비밀번호 변경</div>
                      <div className="text-xs text-muted-foreground">
                        비밀번호를 안전하게 관리하세요
                      </div>
                    </div>
                  </div>
                  <ChevronRight className="w-5 h-5 text-muted-foreground" />
                </button>
              </DialogTrigger>
              <DialogContent>
                <DialogHeader>
                  <DialogTitle>비밀번호 변경</DialogTitle>
                  <DialogDescription>
                    현재 비밀번호를 입력하고 새 비밀번호를 설정하세요
                  </DialogDescription>
                </DialogHeader>
                <div className="space-y-4 py-4">
                  <div className="space-y-2">
                    <Label htmlFor="current-password">현재 비밀번호</Label>
                    <Input
                      id="current-password"
                      type="password"
                      value={currentPassword}
                      onChange={(e) => setCurrentPassword(e.target.value)}
                      placeholder="현재 비밀번호"
                    />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="new-password">새 비밀번호</Label>
                    <Input
                      id="new-password"
                      type="password"
                      value={newPassword}
                      onChange={(e) => setNewPassword(e.target.value)}
                      placeholder="새 비밀번호"
                    />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="confirm-password">
                      새 비밀번호 확인
                    </Label>
                    <Input
                      id="confirm-password"
                      type="password"
                      value={confirmPassword}
                      onChange={(e) => setConfirmPassword(e.target.value)}
                      placeholder="새 비밀번호 확인"
                    />
                  </div>
                </div>
                <DialogFooter>
                  <Button
                    variant="outline"
                    onClick={() => setIsPasswordDialogOpen(false)}
                  >
                    취소
                  </Button>
                  <Button onClick={handlePasswordChange}>변경</Button>
                </DialogFooter>
              </DialogContent>
            </Dialog>

            {/* 이메일 */}
            <div className="p-4 flex items-center justify-between">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 rounded-lg bg-accent/10 flex items-center justify-center">
                  <Mail className="w-5 h-5 text-accent" />
                </div>
                <div className="text-left">
                  <div className="text-sm font-medium">이메일</div>
                  <div className="text-xs text-muted-foreground">{email}</div>
                </div>
              </div>
            </div>
          </Card>
        </div>

        {/* 알림 설정 섹션 */}
        <div>
          <h3 className="mb-4">알림 설정</h3>
          <Card className="divide-y">
            {/* 알림 시간 변경 */}
            <div className="p-4">
              <div className="flex items-center justify-between mb-3">
                <div className="flex items-center gap-3">
                  <div className="w-10 h-10 rounded-lg bg-primary/10 flex items-center justify-center">
                    <Bell className="w-5 h-5 text-primary" />
                  </div>
                  <div className="text-left">
                    <div className="text-sm font-medium">알림 시간 설정</div>
                    <div className="text-xs text-muted-foreground">
                      일일 알림을 받을 시간을 설정하세요
                    </div>
                  </div>
                </div>
              </div>
              <div className="ml-[52px]">
                <Select
                  value={notificationTime}
                  onValueChange={setNotificationTime}
                >
                  <SelectTrigger className="w-full">
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="07:00">오전 7시</SelectItem>
                    <SelectItem value="08:00">오전 8시</SelectItem>
                    <SelectItem value="09:00">오전 9시</SelectItem>
                    <SelectItem value="10:00">오전 10시</SelectItem>
                    <SelectItem value="12:00">낮 12시</SelectItem>
                    <SelectItem value="18:00">오후 6시</SelectItem>
                    <SelectItem value="20:00">오후 8시</SelectItem>
                    <SelectItem value="21:00">오후 9시</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </div>

            {/* 푸시 알림 */}
            <div className="p-4 flex items-center justify-between">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 rounded-lg bg-secondary/10 flex items-center justify-center">
                  <Smartphone className="w-5 h-5 text-secondary" />
                </div>
                <div className="text-left">
                  <div className="text-sm font-medium">푸시 알림</div>
                  <div className="text-xs text-muted-foreground">
                    앱 푸시 알림 받기
                  </div>
                </div>
              </div>
              <Switch checked={pushEnabled} onCheckedChange={setPushEnabled} />
            </div>

            {/* 이메일 알림 */}
            <div className="p-4 flex items-center justify-between">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 rounded-lg bg-accent/10 flex items-center justify-center">
                  <Mail className="w-5 h-5 text-accent" />
                </div>
                <div className="text-left">
                  <div className="text-sm font-medium">이메일 알림</div>
                  <div className="text-xs text-muted-foreground">
                    이메일로 알림 받기
                  </div>
                </div>
              </div>
              <Switch
                checked={emailNotification}
                onCheckedChange={setEmailNotification}
              />
            </div>
          </Card>
        </div>

        {/* 리서치 설정 섹션 */}
        <div>
          <h3 className="mb-4">리서치 설정</h3>
          <Card className="divide-y">
            {/* 초기 리서치 재설정 */}
            <Dialog open={isResetDialogOpen} onOpenChange={setIsResetDialogOpen}>
              <DialogTrigger asChild>
                <button className="w-full p-4 flex items-center justify-between hover:bg-muted/50 transition-colors">
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 rounded-lg bg-primary/10 flex items-center justify-center">
                      <RefreshCw className="w-5 h-5 text-primary" />
                    </div>
                    <div className="text-left">
                      <div className="text-sm font-medium">
                        초기 리서치 재설정
                      </div>
                      <div className="text-xs text-muted-foreground">
                        온보딩 설문을 다시 진행하고 정보를 업데이트하세요
                      </div>
                    </div>
                  </div>
                  <ChevronRight className="w-5 h-5 text-muted-foreground" />
                </button>
              </DialogTrigger>
              <DialogContent>
                <DialogHeader>
                  <DialogTitle>초기 리서치 재설정</DialogTitle>
                  <DialogDescription>
                    온보딩 설문을 다시 진행하시겠습니까? 기존 설정이 초기화됩니다.
                  </DialogDescription>
                </DialogHeader>
                <DialogFooter>
                  <Button
                    variant="outline"
                    onClick={() => setIsResetDialogOpen(false)}
                  >
                    취소
                  </Button>
                  <Button onClick={handleReset}>재설정</Button>
                </DialogFooter>
              </DialogContent>
            </Dialog>
          </Card>
        </div>

        {/* 기타 섹션 */}
        <div>
          <h3 className="mb-4">기타</h3>
          <Card className="divide-y">
            {/* 개인정보 처리방침 */}
            <button className="w-full p-4 flex items-center justify-between hover:bg-muted/50 transition-colors">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 rounded-lg bg-muted flex items-center justify-center">
                  <Shield className="w-5 h-5" />
                </div>
                <div className="text-left">
                  <div className="text-sm font-medium">
                    개인정보 처리방침
                  </div>
                </div>
              </div>
              <ChevronRight className="w-5 h-5 text-muted-foreground" />
            </button>

            {/* 로그아웃 */}
            <button 
              className="w-full p-4 flex items-center justify-between hover:bg-muted/50 transition-colors"
              onClick={() => setIsLogoutDialogOpen(true)}
            >
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 rounded-lg bg-destructive/10 flex items-center justify-center">
                  <LogOut className="w-5 h-5 text-destructive" />
                </div>
                <div className="text-left">
                  <div className="text-sm font-medium text-destructive">
                    로그아웃
                  </div>
                </div>
              </div>
              <ChevronRight className="w-5 h-5 text-muted-foreground" />
            </button>
          </Card>
        </div>
      </div>

      {/* 로그아웃 확인 모달 */}
      <AlertDialog open={isLogoutDialogOpen} onOpenChange={setIsLogoutDialogOpen}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>로그아웃</AlertDialogTitle>
            <AlertDialogDescription>
              정말 로그아웃 하시겠습니까?
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>취소</AlertDialogCancel>
            <AlertDialogAction onClick={handleLogout}>
              확인
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
}
