import { useState } from "react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from "./ui/dialog";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { UserPlus, Mail } from "lucide-react";
import { toast } from "sonner@2.0.3";

interface InviteMemberModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onInviteMember: (email: string) => void;
  groupName?: string;
}

export function InviteMemberModal({
  open,
  onOpenChange,
  onInviteMember,
  groupName,
}: InviteMemberModalProps) {
  const [email, setEmail] = useState("");

  const handleInvite = () => {
    if (email.trim() && email.includes("@")) {
      onInviteMember(email.trim());
      setEmail("");
      onOpenChange(false);
      toast.success(`${email}로 초대 메일을 발송했습니다.`);
    } else {
      toast.error("올바른 이메일 주소를 입력해주세요.");
    }
  };

  const handleCancel = () => {
    setEmail("");
    onOpenChange(false);
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <div className="flex items-center gap-2 mb-2">
            <div className="w-10 h-10 bg-primary/10 rounded-lg flex items-center justify-center">
              <UserPlus className="w-5 h-5 text-primary" />
            </div>
            <DialogTitle>그룹원 초대하기</DialogTitle>
          </div>
          {groupName && (
            <p className="text-sm text-muted-foreground">
              <span className="font-medium text-foreground">{groupName}</span>{" "}
              그룹에 새로운 그룹원을 초대합니다
            </p>
          )}
        </DialogHeader>

        <div className="space-y-4 py-4">
          <div className="space-y-2">
            <Label htmlFor="member-email">초대할 회원의 이메일</Label>
            <div className="relative">
              <Mail className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                id="member-email"
                type="email"
                placeholder="example@email.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                onKeyDown={(e) => {
                  if (e.key === "Enter" && email.trim() && email.includes("@")) {
                    handleInvite();
                  }
                }}
                className="pl-10"
                autoFocus
              />
            </div>
            <p className="text-xs text-muted-foreground">
              초대 링크가 이메일로 전송됩니다
            </p>
          </div>
        </div>

        <DialogFooter className="flex gap-2 sm:gap-2">
          <Button variant="outline" onClick={handleCancel} className="flex-1">
            취소
          </Button>
          <Button
            onClick={handleInvite}
            disabled={!email.trim() || !email.includes("@")}
            className="flex-1"
          >
            초대
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
