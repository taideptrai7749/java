import java.io.*;
import java.util.*;

public class StoreManagement {
    private ArrayList<Staff> staffs;
    private ArrayList<String> workingTime;
    private ArrayList<Invoice> invoices;
    private ArrayList<InvoiceDetails> invoiceDetails;
    private ArrayList<Drink> drinks;

    public StoreManagement(String staffPath, String workingTimePath, String invoicesPath, String detailsPath, String drinksPath) {
        this.staffs = loadStaffs(staffPath);
        this.workingTime = loadFile(workingTimePath);
        this.invoices = loadInvoices(invoicesPath);
        this.invoiceDetails = loadInvoicesDetails(detailsPath);
        this.drinks = loadDrinks(drinksPath);
    }

    public ArrayList<Staff> getStaffs() {
        return this.staffs;
    }

    public void setStaffs(ArrayList<Staff> staffs){
        this.staffs = staffs;
    }
    
    public ArrayList<Drink> loadDrinks(String filePath) {
        ArrayList<Drink> drinksResult = new ArrayList<Drink>();
        ArrayList<String> drinks = loadFile(filePath);

        for (String drink : drinks) {
            String[] information = drink.split(",");
            drinksResult.add(new Drink(information[0], Integer.parseInt(information[1])));
        }

        return drinksResult;
    }

    public ArrayList<Invoice> loadInvoices(String filePath) {
        ArrayList<Invoice> invoiceResult = new ArrayList<Invoice>();
        ArrayList<String> invoices = loadFile(filePath);

        for (String invoice : invoices) {
            String[] information = invoice.split(",");
            invoiceResult.add(new Invoice(information[0], information[1], information[2]));
        }

        return invoiceResult;
    }

    public ArrayList<InvoiceDetails> loadInvoicesDetails(String filePath) {
        ArrayList<InvoiceDetails> invoiceResult = new ArrayList<InvoiceDetails>();
        ArrayList<String> invoices = loadFile(filePath);

        for (String invoice : invoices) {
            String[] information = invoice.split(",");
            invoiceResult.add(new InvoiceDetails(information[0], information[1], Integer.parseInt(information[2])));
        }

        return invoiceResult;
    }

    // requirement 1
    public ArrayList<Staff> loadStaffs(String filePath) {
        ArrayList<Staff> staffsList = new ArrayList<Staff>();
        ArrayList<String> data = loadFile(filePath);
        for(String stringData: data){
            String[] information = stringData.split(",");
            if(information.length == 3){
                staffsList.add(new SeasonalStaff(information[0], information[1], Integer.parseInt(information[2])));
            }
            else if(information.length == 4){
                staffsList.add(new FullTimeStaff(information[0], information[1], Integer.parseInt(information[2]), Double.parseDouble(information[3])));
            }
            else if(information.length == 5) {
                staffsList.add(new Manager(information[0], information[1], Integer.parseInt(information[2]), Double.parseDouble(information[3]), Integer.parseInt(information[4])));
            }
        }
        return staffsList;
    }

    // requirement 2
    public ArrayList<SeasonalStaff> getTopFiveSeasonalStaffsHighSalary() {
        ArrayList<SeasonalStaff> seasonalStaffsList = new ArrayList<SeasonalStaff>();
        for(Staff s: staffs){
            if(s instanceof SeasonalStaff)
            {
                seasonalStaffsList.add((SeasonalStaff) s);
            }
        }
        double salary[] = new double[seasonalStaffsList.size()];
        int index = 0;
        for(SeasonalStaff seasonalStaff: seasonalStaffsList){
            for(String data: workingTime){
                String[] information = data.split(",");
                if(information[0].equalsIgnoreCase(seasonalStaff.sID)){
                    salary[index] = seasonalStaff.paySalary(Integer.parseInt(information[1]));
                }
            }
            index++;
        }
        for(int i = 0; i< seasonalStaffsList.size(); i++){
            for (int j = i+1; j<seasonalStaffsList.size(); j++){
                if(salary[i] < salary[j]){
                    Collections.swap(seasonalStaffsList, i, j);
                    double temp = salary[i];
                    salary[i] = salary[j];
                    salary[j] = temp;
                }
            }
        }
        ArrayList<SeasonalStaff> result = new ArrayList<SeasonalStaff>();
        for (int i = 0; i < 5 && i < seasonalStaffsList.size(); i++){
            result.add(seasonalStaffsList.get(i));
        }
        return result;
    }

    // requirement 3
    public ArrayList<FullTimeStaff> getFullTimeStaffsHaveSalaryGreaterThan(int lowerBound) {
        ArrayList<FullTimeStaff> fullTimeStaffsList = new ArrayList<FullTimeStaff>();
        for(Staff s: this.staffs){
            if(s instanceof FullTimeStaff){
                fullTimeStaffsList.add((FullTimeStaff) s);
            }
            else if(s instanceof Manager){
                fullTimeStaffsList.add((FullTimeStaff) s);
            }
        }
        double[] salary = new double[fullTimeStaffsList.size()];
        int index = 0;
        for(FullTimeStaff s: fullTimeStaffsList){
            for(String data: workingTime){
                String[] information = data.split(",");
                if(information[0].equalsIgnoreCase(s.sID)){
                    salary[index] = s.paySalary(Integer.parseInt(information[1]));
                }
            }
            index++;
        }
        ArrayList<FullTimeStaff> result = new ArrayList<FullTimeStaff>();
        for(int i = 0; i<fullTimeStaffsList.size(); i++){
            if(salary[i] > lowerBound){
                result.add(fullTimeStaffsList.get(i));
            }
        }
        return result;
    }

    // requirement 4
    public double totalInQuarter(int quarter) {
        double total = 0;
        ArrayList<Invoice> invoiceList = new ArrayList<Invoice>();
        ArrayList<InvoiceDetails> details = new ArrayList<InvoiceDetails>();
        String t1 = "";
        String t2 = "";
        String t3 = "";
        if(quarter == 1){
            t1 = "01";
            t2 = "02";
            t3 = "03";
        }
        if(quarter == 2){
            t1 = "04";
            t2 = "05";
            t3 = "06";
        }
        else if(quarter == 3){
            t1 = "07";
            t2 = "08";
            t3 = "09";
        }
        else if(quarter == 4){
            t1 = "10";
            t2 = "11";
            t3 = "12";
        }
        for(Invoice in: invoices){
            String[] information = in.getDate().split("/");
            if(information[1].equalsIgnoreCase(t1) || information[1].equalsIgnoreCase(t2) || information[1].equalsIgnoreCase(t3)){
                invoiceList.add(in);
                for(InvoiceDetails detail: invoiceDetails){
                    if(in.getInvoiceID().equalsIgnoreCase(detail.getInvoiceID())){
                        details.add(detail);
                    }
                }
            }
        }
        for(InvoiceDetails detail: details){
            for (Drink d: drinks){
                if(detail.getDName().equalsIgnoreCase(d.getdName())){
                    total = total + (detail.getAmount()*d.getPrice());
                }
            }
        }
        return total;
    }

    // requirement 5
    public Staff getStaffHighestBillInMonth(int month) {
        Staff maxStaff = null;
        ArrayList<Staff> staffsList = new ArrayList<Staff>();
        ArrayList<Invoice> in = new ArrayList<Invoice>();
        ArrayList<InvoiceDetails> details = new ArrayList<InvoiceDetails>();
        for(Staff s: staffs){
            staffsList.add(s);
            for(Invoice invoice: invoices){
                String[] data = invoice.getDate().split("/");
                if(s.sID.equalsIgnoreCase(invoice.getStaffID()) && (month == Integer.parseInt(data[1]))){
                    in.add(invoice);
                    for(InvoiceDetails d: invoiceDetails){
                        if(d.getInvoiceID().equalsIgnoreCase(invoice.getInvoiceID())){
                            details.add(d);
                        }
                    }
                }
            }
        }
        double[] income = new double[staffsList.size()];
        int index = 0;
        for(Staff s: staffsList){
            for(Invoice invoice: in){
                if(s.sID.equalsIgnoreCase(invoice.getStaffID())){
                    for(InvoiceDetails d: details){
                        if(d.getInvoiceID().equalsIgnoreCase(invoice.getInvoiceID())){
                            for(Drink drink: drinks){
                                if(drink.getdName().equalsIgnoreCase(d.getDName())){
                                    income[index] += (d.getAmount()*drink.getPrice());
                                }
                            }
                        }
                    }
                }
            }
            index++;
        }
        for(int i = 0; i<staffsList.size();i++){
            if(maxStaff == null){
                maxStaff = staffsList.get(i);
            }
            else if(income[i] > income[staffsList.indexOf(maxStaff)]){
                maxStaff = staffsList.get(i);
            }
        }
        return maxStaff;
    }

    // load file as list
    public static ArrayList<String> loadFile(String filePath) {
        String data = "";
        ArrayList<String> list = new ArrayList<String>();

        try {
            FileReader reader = new FileReader(filePath);
            BufferedReader fReader = new BufferedReader(reader);

            while ((data = fReader.readLine()) != null) {
                list.add(data);
            }

            fReader.close();
            reader.close();

        } catch (Exception e) {
            System.out.println("Cannot load file");
        }
        return list;
    }

    public void displayStaffs() {
        for (Staff staff : this.staffs) {
            System.out.println(staff);
        }
    }

    public <E> boolean writeFile(String path, ArrayList<E> list) {
        try {
            FileWriter writer = new FileWriter(path);
            for (E tmp : list) {
                writer.write(tmp.toString());
                writer.write("\r\n");
            }

            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("Error.");
            return false;
        }

        return true;
    }

    public <E> boolean writeFile(String path, E object) {
        try {
            FileWriter writer = new FileWriter(path);

            writer.write(object.toString());
            writer.close();

            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("Error.");
            return false;
        }

        return true;
    }
}