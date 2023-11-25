import java.util.ArrayList;

public class Branch {
    private String city;
    private String district;
    private HashTable<String, Employee> employees;
    private Employee manager;
    private int num_of_cashiers;
    private int num_of_cooks;
    private int num_of_couriers;
    private int monthly_bonuses;
    private int overall_bonuses;
    private ArrayList<Employee> manager_possibilities = new ArrayList<Employee>();
    public ArrayList<String> branch_log = new ArrayList<String>();
    private ArrayList<Employee> cook_possibilities = new ArrayList<Employee>();
    private ArrayList<Employee> cashiers_at_risk = new ArrayList<Employee>();
    private ArrayList<Employee> cooks_at_risk = new ArrayList<Employee>();
    private ArrayList<Employee> couriers_at_risk = new ArrayList<Employee>();


    public Branch(String city, String district) {
        this.city = city;
        this.district = district;
        this.employees = new HashTable<>();
        this.manager = null;
    }

    public HashTable<String, Employee> getEmployees() {
        return employees;
    }

    public Employee getManager() {
        return manager;
    }

    public int getTotalBonuses() {
        return overall_bonuses;
    }


    public void updateScore(String name, int score) {
        Employee employee = employees.get(name);
        if (employee != null) {
            int promotion_score = score / 200;
            int bonus = score % 200;
            employee.updatePromotion_bonus(promotion_score, bonus);
            if (bonus > 0) {
                monthly_bonuses += bonus;
                overall_bonuses += bonus;
            }
            if (employee.getPosition().equals("COOK") && employee.getPromotionPoint() >= 10 && !manager_possibilities.contains(employee)) {
                manager_possibilities.add(employee);
            } else if (employee.getPosition().equals("COOK") && employee.getPromotionPoint() < 10) {
                manager_possibilities.remove(employee);
            }
            if (employee.getPosition().equals("CASHIER") && employee.getPromotionPoint() >= 3 && !cook_possibilities.contains(employee)) {
                cook_possibilities.add(employee);
            }else if (employee.getPosition().equals("CASHIER") && employee.getPromotionPoint() < 3) {
                cook_possibilities.remove(employee);
            }
            if (employee.getPromotionPoint() <= -5) {
                if (employee.getPosition().equals("CASHIER") && !cashiers_at_risk.contains(employee)) {
                    cashiers_at_risk.add(employee);
                } else if (employee.getPosition().equals("COOK") && !cooks_at_risk.contains(employee)) {
                    cooks_at_risk.add(employee);
                } else if (employee.getPosition().equals("COURIER") && !couriers_at_risk.contains(employee)) {
                    couriers_at_risk.add(employee);
                }
            } else if (employee.getPromotionPoint() > -5) {
                switch (employee.getPosition()) {
                    case "CASHIER" -> cashiers_at_risk.remove(employee);
                    case "COOK" -> cooks_at_risk.remove(employee);
                    case "COURIER" -> couriers_at_risk.remove(employee);
                }
            }
        } else {
            branch_log.add("There is no such employee.");
        }
    }
    public void compensationBonus(int bonus) {
        monthly_bonuses += bonus;
        overall_bonuses += bonus;
    }
    public void reset_monthly_bonuses() {
        monthly_bonuses = 0;
        //for (Employee employee : employees.values()) {
        //    employee.updatePromotion_bonus(0, 0);
        //}
    }
    public int get_monthly_bonuses() {
        return monthly_bonuses;
    }

    public ArrayList<String> getLog() {
        return branch_log;
    }

    public void resetLog() {
        branch_log.clear();
    }
    public void addEmployee(Employee employee) {
        if (employees.get(employee.getName()) != null){
            branch_log.add("Existing employee cannot be added again.");
        }else{
            employees.put(employee.getName(), employee);
            String e_position = employee.getPosition();
            if (e_position.equals("CASHIER")){
                num_of_cashiers += 1;
                if (!cook_possibilities.isEmpty()){
                    if (cook_possibilities.get(0).getPromotionPoint() >= 3){
                        cook_possibilities.get(0).promote("CASHIER", "COOK");
                        if (cook_possibilities.get(0).getPromotionPoint() >= 10 && !manager_possibilities.contains(cook_possibilities.get(0))){
                            manager_possibilities.add(cook_possibilities.get(0));
                        }
                        branch_log.add(cook_possibilities.get(0).getName() + " is promoted from Cashier to Cook.");
                        num_of_cashiers -= 1;
                        num_of_cooks += 1;
                        if (!cooks_at_risk.isEmpty()){
                            if (cooks_at_risk.get(0).getPromotionPoint() <= -5){
                                employees.remove(cooks_at_risk.get(0).getName());
                                branch_log.add(cooks_at_risk.get(0).getName() + " is dismissed from branch: " + district + ".");
                                num_of_cooks -= 1;
                            }
                            cooks_at_risk.remove(0);
                        }
                    }
                    cook_possibilities.remove(0);
                } else if (!cashiers_at_risk.isEmpty()){
                    if (cashiers_at_risk.get(0).getPromotionPoint() <= -5){
                        employees.remove(cashiers_at_risk.get(0).getName());
                        branch_log.add(cashiers_at_risk.get(0).getName() + " is dismissed from branch: " + district + ".");
                        num_of_cashiers -= 1;
                    }
                    cashiers_at_risk.remove(0);
                }
            } else if (e_position.equals("COOK")){
                num_of_cooks += 1;
                if (!manager_possibilities.isEmpty() && manager.getPromotionPoint() <= -5 && manager_possibilities.get(0).getPromotionPoint() >= 10){
                    branch_log.add(manager.getName() + " is dismissed from branch: " + district + ".");
                    make_manager(manager_possibilities.get(0));
                    num_of_cooks -= 1;
                } else if (!cooks_at_risk.isEmpty()){
                    if (cooks_at_risk.get(0).getPromotionPoint() <= -5){
                        employees.remove(cooks_at_risk.get(0).getName());
                        branch_log.add(cooks_at_risk.get(0).getName() + " is dismissed from branch: " + district + ".");
                        num_of_cooks -= 1;
                    }
                    cooks_at_risk.remove(0);
                }
            } else if (e_position.equals("COURIER")){
                num_of_couriers += 1;
                if (!couriers_at_risk.isEmpty()){
                    if (couriers_at_risk.get(0).getPromotionPoint() <= -5){
                        employees.remove(couriers_at_risk.get(0).getName());
                        branch_log.add(couriers_at_risk.get(0).getName() + " is dismissed from branch: " + district + ".");
                        num_of_couriers -= 1;
                    }
                    couriers_at_risk.remove(0);
                }
            } else if (e_position.equals("MANAGER")) {
                if (manager == null) {
                    manager = employee;
                }
            }
        }
    }

    public void dismission_check(String name) {
        Employee employee = employees.get(name);
        if (employee != null) {
            String e_position = employee.getPosition();
            if (e_position.equals("CASHIER") && employee.getPromotionPoint() <= -5 && num_of_cashiers > 1) {
                employees.remove(name);
                num_of_cashiers -= 1;
                branch_log.add(name + " is dismissed from branch: " + district + ".");
                cashiers_at_risk.remove(employee);
            } else if (e_position.equals("COOK") && employee.getPromotionPoint() <= -5 && num_of_cooks > 1) {
                employees.remove(name);
                num_of_cooks -= 1;
                branch_log.add(name + " is dismissed from branch: " + district + ".");
                cooks_at_risk.remove(employee);
            } else if (e_position.equals("COURIER") && employee.getPromotionPoint() <= -5 && num_of_couriers > 1) {
                employees.remove(name);
                num_of_couriers -= 1;
                branch_log.add(name + " is dismissed from branch: " + district + ".");
                couriers_at_risk.remove(employee);
            } else if (e_position.equals("MANAGER") && employee.getPromotionPoint() <= -5 && !manager_possibilities.isEmpty() && num_of_cooks > 1) {
                branch_log.add(name + " is dismissed from branch: " + district + ".");
                make_manager(manager_possibilities.get(0));
                num_of_cooks -= 1;
            }
        }
    }

    public void promotion_check(String name) {
        Employee employee = employees.get(name);
        if (employee != null) {
            if (employee.getPosition().equals("CASHIER") && employee.getPromotionPoint() >= 3) {
                if (!cook_possibilities.contains(employee)){
                    cook_possibilities.add(employee);
                }
                if (num_of_cashiers > 1 && cook_possibilities.get(0).getPromotionPoint() >= 3){
                    cook_possibilities.get(0).promote("CASHIER", "COOK");
                    num_of_cashiers -= 1;
                    num_of_cooks += 1;
                    branch_log.add(cook_possibilities.get(0).getName()+ " is promoted from Cashier to Cook.");
                    cook_possibilities.remove(0);
                }
            }
            if (employee.getPosition().equals("COOK") && employee.getPromotionPoint() >= 10) {
                if (!manager_possibilities.contains(employee)){
                    manager_possibilities.add(employee);
                }
                if (manager.getPromotionPoint() <= -5 && num_of_cooks > 1 && manager_possibilities.get(0).getPromotionPoint() >= 10){
                    branch_log.add(manager.getName() + " is dismissed from branch: " + district + ".");
                    make_manager(manager_possibilities.get(0));
                    num_of_cooks -= 1;
                }
            }
        }
    }
    public ArrayList<String> removeEmployee(Employee employee) {
        String e_position = employee.getPosition();
        switch (e_position) {
            case "CASHIER" -> {
                if (num_of_cashiers > 1) {
                    employees.remove(employee.getName());
                    num_of_cashiers -= 1;
                    cook_possibilities.remove(employee);
                    branch_log.add(employee.getName() + " is leaving from branch: " + district + ".");
                } else if (employee.getPromotionPoint() > -5) {
                    compensationBonus(200);
                }
            }
            case "COOK" -> {
                if (num_of_cooks > 1) {
                    employees.remove(employee.getName());
                    num_of_cooks -= 1;
                    manager_possibilities.remove(employee);
                    branch_log.add(employee.getName() + " is leaving from branch: " + district + ".");
                } else if (employee.getPromotionPoint() > -5) {
                    compensationBonus(200);
                }
            }
            case "COURIER" -> {
                if (num_of_couriers > 1) {
                    employees.remove(employee.getName());
                    num_of_couriers -= 1;
                    branch_log.add(employee.getName() + " is leaving from branch: " + district + ".");
                } else if (employee.getPromotionPoint() > -5) {
                    compensationBonus(200);
                }
            }
            case "MANAGER" -> {
                if (!manager_possibilities.isEmpty() && num_of_cooks > 1 && manager_possibilities.get(0).getPromotionPoint() >= 10) {
                    Employee new_manager = manager_possibilities.get(0);
                    branch_log.add(employee.getName() + " is leaving from branch: " + district + ".");
                    make_manager(new_manager);
                    num_of_cooks -= 1;
                } else if (employee.getPromotionPoint() > -5) {
                    compensationBonus(200);
                }
            }
        }
        return branch_log;
    }
    private void make_manager(Employee new_manager){
        employees.remove(manager.getName());
        new_manager.promote("COOK", "MANAGER");
        manager = new_manager;
        branch_log.add(manager.getName() + " is promoted from Cook to Manager.");
        manager_possibilities.remove(0);
    }

}