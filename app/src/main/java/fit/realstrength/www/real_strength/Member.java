package fit.realstrength.www.real_strength;

import android.net.Uri;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Member {

    protected String memberName = "Your Name";
    protected String memberLevel = "Select Level";
    protected String memberAge = "Select age range";
    protected String memberGoal = "Select goal";
    protected int memberWeight = 30;
    protected int memberHeight = 100;


    public Member() {

    }
    public Member(String name,String goal,String level,String age,int weight,int height) {
        this.memberName = name;
        this.memberAge = age;
        this.memberGoal = goal;
        this.memberLevel = level;
        this.memberWeight = weight;
        this.memberHeight = height;


    }
    public int getMemberWeight() {
        return memberWeight;
    }
    public int getMemberHeight() {
        return memberHeight;
    }

    public String getMemberName() {
        return memberName;
    }
    public String getMemberLevel() {
       return memberLevel;
    }
    public String getMemberAge() {
        return memberAge;
    }
    public String getMemberGoal() {
        return memberGoal;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
    public void setMemberAge(String memberAge) {
        this.memberName = memberAge;
    }
    public void setMemberLevel(String memberLevel) {
        this.memberName = memberLevel;
    }
    public void setMemberGoal(String memberGoal) {
        this.memberName = memberGoal;
    }

}
