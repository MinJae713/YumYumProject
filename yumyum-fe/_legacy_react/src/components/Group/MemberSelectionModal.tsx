import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "./ui/dialog";
import { Avatar, AvatarFallback, AvatarImage } from "./ui/avatar";
import { Badge } from "./ui/badge";
import { Card } from "./ui/card";
import { Users, ChevronRight } from "lucide-react";
import { Member } from "./GroupLeaderPage";

interface MemberSelectionModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  members: Member[];
  onSelectMember: (member: Member) => void;
}

export function MemberSelectionModal({
  open,
  onOpenChange,
  members,
  onSelectMember,
}: MemberSelectionModalProps) {
  const getGoalLabel = (goal: string) => {
    switch (goal) {
      case "diet":
        return "다이어트";
      case "bulk":
        return "벌크업";
      case "maintain":
        return "유지";
      default:
        return goal;
    }
  };

  const getGoalColor = (goal: string) => {
    switch (goal) {
      case "diet":
        return "bg-secondary/10 text-secondary";
      case "bulk":
        return "bg-accent/10 text-accent";
      case "maintain":
        return "bg-primary/10 text-primary";
      default:
        return "";
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-2xl max-h-[80vh]">
        <DialogHeader>
          <div className="flex items-center gap-2 mb-2">
            <div className="w-10 h-10 bg-primary/10 rounded-lg flex items-center justify-center">
              <Users className="w-5 h-5 text-primary" />
            </div>
            <DialogTitle>식단을 입력할 그룹원을 선택하세요</DialogTitle>
          </div>
          <p className="text-sm text-muted-foreground">
            선택한 그룹원의 추천 식단 목록에 추가됩니다
          </p>
        </DialogHeader>

        <div className="space-y-2 overflow-y-auto max-h-[500px] pr-2">
          {members.map((member) => (
            <Card
              key={member.id}
              className="p-4 cursor-pointer transition-all hover:shadow-md hover:border-primary/50"
              onClick={() => {
                onSelectMember(member);
                onOpenChange(false);
              }}
            >
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-3 flex-1">
                  <Avatar className="w-12 h-12">
                    <AvatarImage src={member.avatar} alt={member.name} />
                    <AvatarFallback>{member.name[0]}</AvatarFallback>
                  </Avatar>
                  
                  <div className="flex-1">
                    <div className="flex items-center gap-2 mb-1">
                      <span className="font-medium">{member.name}</span>
                      <Badge 
                        variant="secondary" 
                        className={`text-xs ${getGoalColor(member.goal)}`}
                      >
                        {getGoalLabel(member.goal)}
                      </Badge>
                    </div>
                    <div className="flex items-center gap-3 text-xs text-muted-foreground">
                      <span>현재 {member.currentWeight}kg</span>
                      <span>•</span>
                      <span>목표 {member.targetWeight}kg</span>
                      <span>•</span>
                      <span>{member.consecutiveDays}일 연속</span>
                    </div>
                  </div>
                </div>
                
                <ChevronRight className="w-5 h-5 text-muted-foreground" />
              </div>
            </Card>
          ))}
        </div>

        {members.length === 0 && (
          <div className="py-12 text-center">
            <Users className="w-12 h-12 text-muted-foreground mx-auto mb-3" />
            <p className="text-sm text-muted-foreground">
              아직 그룹원이 없습니다
            </p>
          </div>
        )}
      </DialogContent>
    </Dialog>
  );
}
