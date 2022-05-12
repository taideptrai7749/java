public class Manager extends FullTimeStaff{
    private int allowance;

    public Manager(String sID, String sName, int baseSalary, double bonusRate, int allowance) {
        super(sID, sName, baseSalary, bonusRate);
        this.allowance = allowance;
    }

    public int getAllowance() {
        return allowance;
    }

    @Override
    public double paySalary(int workedDays){
        return super.paySalary(workedDays) + this.allowance;
    }

    public String toString(){
        return this.sID + "_" + this.sName + "_" + super.getBonusRate() + "_" + super.getBaseSalary() + "_" + this.allowance;
    }
}
