public class FullTimeStaff extends Staff{
    private int baseSalary;
    private double bonusRate;

    public FullTimeStaff(String sID, String sName, int baseSalary, double bonusRate) {
        super(sID, sName);
        this.baseSalary = baseSalary;
        this.bonusRate = bonusRate;
    }

    public int getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(int baseSalary) {
        this.baseSalary = baseSalary;
    }

    public double getBonusRate() {
        return bonusRate;
    }

    public void setBonusRate(double bonusRate) {
        this.bonusRate = bonusRate;
    }

    @Override
    public double paySalary(int workedDays) {
        double salary = this.baseSalary * this.bonusRate;
        double bonus = 0;
        if(workedDays > 21){
            bonus = (workedDays - 21)*100000;
        }
        return (salary + bonus);
    }

    public String toString(){
        return this.sID + "_" + this.sName + "_" + this.bonusRate + "_" + this.baseSalary;
    }
}
