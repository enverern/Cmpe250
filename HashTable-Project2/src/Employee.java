public class Employee {
    private String name;
    private String position;

    private int monthly_score;

    private int promotion_point;

    private int bonus;

    public Employee(String name, String position) {
        this.name = name;
        this.position = position;
        this.monthly_score = 0;
        this.promotion_point = 0;
        this.bonus = 0;
    }
    // getters
    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public int getMonthlyScore() {
        return monthly_score;
    }

    public int getPromotionPoint() {
        return promotion_point;
    }

    // update the promotion point and bonus of an employee
    public void updatePromotion_bonus(int promotion_pt, int bns) {
        promotion_point += promotion_pt;
        if (bns >= 0){
            bonus += bns;
        }
    }

    // promote an employee if they have enough promotion points
    public void promote(String current_position, String new_position) {
        if (position.equals(current_position) && new_position.equals("COOK") && promotion_point >= 3) {
            position = new_position;
            promotion_point -= 3;
        }else if (position.equals(current_position) && new_position.equals("MANAGER") && (promotion_point >= 10)) {
            position = new_position;
            promotion_point -= 10;
        }
    }
}
