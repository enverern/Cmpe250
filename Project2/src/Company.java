import java.util.ArrayList;
public class Company {
    private final HashTable<String, Branch> branches;
    public ArrayList<String> log = new ArrayList<String>();

    public Company() {
        this.branches = new HashTable<>();
    }

    public ArrayList<String> getLog() {
        return log;
    }

    public void addemployee(String city, String district, String name, String position) {
        Branch branch = branches.get(city + district);
        if (branch == null) {
            branch = new Branch(city, district);
            branches.put(city + district, branch);
        }
        Employee employee = new Employee(name, position);
        branch.addEmployee(employee);
        ArrayList<String> branch_events = branch.getLog();
        log.addAll(branch_events);
        branch.resetLog();
    }

    public void remove_employee(String city, String district, String name) {
        Branch branch = branches.get(city + district);
        if (branch != null) {
            Employee employee = branch.getEmployees().get(name);
            if (employee != null) {
                ArrayList<String> leaving_events = branch.removeEmployee(employee);
                log.addAll(leaving_events);
                branch.resetLog();
            } else {
                log.add("There is no such employee.");
            }
        }
    }

    public void performanceUpdate(String city, String district, String name, int score) {
        Branch branch = branches.get(city + district);
        if (branch != null) {
            branch.updateScore(name, score);
            branch.dismission_check(name);
            branch.promotion_check(name);
            ArrayList<String> branch_events = branch.getLog();
            log.addAll(branch_events);
            branch.resetLog();
        }
    }

    public void print_monthly_bonuses(String city, String district) {
        Branch branch = branches.get(city + district);
        if (branch != null) {
            int m_bon = branch.get_monthly_bonuses();
            String month_bon = Integer.toString(m_bon);
            log.add("Total bonuses for the " + district + " branch this month are: " + month_bon);
        }
    }

    public void print_overall_bonuses(String city, String district) {
        Branch branch = branches.get(city + district);
        if (branch != null) {
            int o_bon = branch.getTotalBonuses();
            String overall_bon = Integer.toString(o_bon);
            log.add("Total bonuses for the " + district + " branch are: " + overall_bon);
        }
    }

    public void print_manager(String city, String district) {
        Branch branch = branches.get(city + district);
        if (branch != null) {
            Employee manager = branch.getManager();
            if (manager != null) {
                log.add("Manager of the " + district + " branch is " + manager.getName()+".");
            }
        }
    }
    public void reset_monthly_bonuses() {
        for (Branch branch : branches.values()) {
            branch.reset_monthly_bonuses();
        }
    }
}