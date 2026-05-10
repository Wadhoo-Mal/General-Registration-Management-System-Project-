import java.sql.*;
import java.util.Scanner;


class Database_Connection {
    protected final String dbUrl = "jdbc:mysql://localhost:3306/GRSystem";
    protected final String dbUser = "root";
    protected final String dbPass = "SibaSuk@2026";
    protected Connection connect() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(dbUrl,dbUser,dbPass);
    }
}

class Students {
    private String name, fName, gender, dob, className, section, addmission_date, address, phone;

    public String getName(){
            return name; 
        }
    public void setName(String name){ 
            this.name = name;  
        }
    
    public String getFatherName(){ 
            return fName;  
        }
    public void setFatherName(String fName){ 
            this.fName = fName; 
        }
    
    public String getGender(){ 
            return gender;  
        }
    public void setGender(String gender){ 
            this.gender = gender; 
        }
    
    public String getDob(){ 
            return dob; 
        }
    public void setDob(String dob){ 
            this.dob = dob;     
        }
    
    public String getClassName(){ 
            return className; 
        }
    public void setClassName(String className){     
            this.className = className; 
        }
    
    public String getSection(){ 
            return section; 
        }
    public void setSection(String section){ 
            this.section = section; 
        }
    
    public String getAddmission_date(){ 
            return addmission_date; 
        }
    public void setAdmission_date(String addmission_date){ 
            this.addmission_date = addmission_date;     
        }

    public String getAddress(){ 
            return address; 
        }
    public void setAddress(String address){ 
            this.address = address; 
        }
    
    public String getPhone(){ 
            return phone; 
        }
    public void setPhone(String phone){ 
            this.phone = phone; 
        }
}


class Student extends Database_Connection{    
    public void regStudent(Scanner input){
        Students student = new Students();

        System.out.print("Enter Student Name: "); 
        student.setName(input.nextLine());

        System.out.print("Father's Name: "); 
        student.setFatherName(input.nextLine());
        
        System.out.print("Gender: "); 
        student.setGender(input.nextLine());
        
        System.out.print("Date of Birth (YYYY-MM-DD): "); 
        student.setDob(input.nextLine());
        
        System.out.print("Enter Admission date (YYYY-MM-DD): "); 
        student.setAdmission_date(input.nextLine());

        System.out.print("Class: "); 
        student.setClassName(input.nextLine());
        
        System.out.print("Section: "); 
        student.setSection(input.nextLine());
        
        System.out.print("Address: "); 
        student.setAddress(input.nextLine());
        
        System.out.print("Phone Number: "); 
        student.setPhone(input.nextLine());

        String query = "INSERT INTO Students(student_name, father_name, gender, dob, admission_date, class, section, address, phone, status) VALUES(?,?,?,?,?,?,?,?,?,?)";
        try(Connection con = connect(); 
            PreparedStatement pstmt = con.prepareStatement(query)){
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getFatherName());
            pstmt.setString(3, student.getGender());
            pstmt.setDate(4, Date.valueOf(student.getDob()));
            pstmt.setDate(5, Date.valueOf(student.getAddmission_date())); 
            pstmt.setString(6, student.getClassName());
            pstmt.setString(7, student.getSection());
            pstmt.setString(8, student.getAddress());
            pstmt.setString(9, student.getPhone());
            pstmt.setString(10, "Active");        
            pstmt.executeUpdate();
            System.out.println("Student registered");
        }catch(Exception e){ 
            System.out.println("Database error: "+e.getMessage()); 
        }
    }

    public void viewStudents() {
        String query="Select * from Students";
        
        try (Connection con=connect(); 
            Statement stmt=con.createStatement(); 
            ResultSet rs= stmt.executeQuery(query)){
            System.out.println("\n************************************************");
            System.out.println("GR_NO | NAME | CLASS | STATUS");
            System.out.println("************************************************");
            
            while(rs.next()){
                System.out.println(rs.getInt("gr_no")+" | "+ 
                                   rs.getString("student_name")+" | "+ 
                                   rs.getString("class")+" | "+ 
                                   rs.getString("status"));
            }
        }catch(Exception e) { 
            System.out.println("Could not load student list: " + e.getMessage()); 
        }
    }

    public void promote(Scanner input){
        System.out.print("Enter GR No to promote: "); 
        int grNo = input.nextInt(); 
        input.nextLine();
        
        System.out.print("Promoting from Class: "); 
        String currentClass = input.nextLine();
        
        System.out.print("Promoting to Class: "); 
        String nextClass= input.nextLine();
        
        System.out.print("Academic Year: "); 
        int year = input.nextInt(); 
        input.nextLine();
        String query = "Insert into Promotions(gr_no, from_class, to_class, year) VALUES(?,?,?,?)";       
        try(Connection con= connect(); 
            PreparedStatement pstmt = con.prepareStatement(query)){
            pstmt.setInt(1, grNo); 
            pstmt.setString(2, currentClass); 
            pstmt.setString(3, nextClass); 
            pstmt.setInt(4, year);
            
            pstmt.executeUpdate();
            System.out.println("Student promoted successfully.");
        }catch(Exception e){ 
            System.out.println("Promotion failed: " + e.getMessage()); 
        }
    }
}

class Academic extends Database_Connection {    
    public void Attendance(Scanner input){
        System.out.print("Enter Student GR No: "); 
        int grNo=input.nextInt(); 
        input.nextLine();
        
        System.out.print("Status (Present/Absent): "); 
        String status = input.nextLine();
        
        String query = "Insert into Attendance(gr_no, attendance_date, status) VALUES(?,?,?)";
        
        try (Connection con = connect(); 
            PreparedStatement pstmt = con.prepareStatement(query)){
            pstmt.setInt(1, grNo); 
            pstmt.setDate(2, new java.sql.Date(System.currentTimeMillis())); 
            pstmt.setString(3, status);
            pstmt.executeUpdate();
            System.out.println("Attendance marked for today.");
        }catch(Exception e){ 
            System.out.println("Error marking attendance: " + e.getMessage()); 
        }
    }
    public void Result(Scanner input){
        System.out.print("Student GR No: "); 
        int grNo=input.nextInt(); 
        input.nextLine();
        
        System.out.print("Subject Name: "); 
        String subject = input.nextLine();
        
        System.out.print("Marks Obtained: "); 
        int marks = input.nextInt(); input.nextLine();
        
        System.out.print("Grade: "); 
        String grade = input.nextLine();
        
        String query = "Insert into Results(gr_no, subject, marks, grade) VALUES(?,?,?,?)";     
        try(Connection con = connect(); 
            PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, grNo); 
            pstmt.setString(2, subject); 
            pstmt.setInt(3, marks); 
            pstmt.setString(4, grade);            
            pstmt.executeUpdate();
            System.out.println("Exam result uploaded.");
        } catch (Exception e) { 
            System.out.println("Error uploading result: " + e.getMessage()); 
        }
    }
}

class Finance extends Database_Connection{
    public void feeSystem(Scanner input){
        System.out.print("Student GR No: "); 
        int grNo=input.nextInt(); 
        input.nextLine();
        
        System.out.print("Fee Month: "); 
        String month =input.nextLine();
        
        System.out.print("Amount Paid: "); 
        double amount = input.nextDouble(); 
        input.nextLine();
        
        System.out.print("Payment Status (Paid/Unpaid): "); 
        String status = input.nextLine();
        
        String query = "Insert into Fees(gr_no, month, amount, payment_date, status) VALUES(?,?,?,?,?)";
        
        try(Connection con =connect(); 
            PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, grNo); 
            pstmt.setString(2, month); 
            pstmt.setDouble(3, amount);
            pstmt.setDate(4, new java.sql.Date(System.currentTimeMillis())); 
            pstmt.setString(5, status);            
            pstmt.executeUpdate();
            System.out.println("Fee record added.");
        }catch(Exception e){ 
            System.out.println("Error processing fee: " + e.getMessage()); 
        }
    }
}

class Admin extends Database_Connection{    
    public void leavingCertificate(Scanner input){
        System.out.print("Enter GR No: "); 
        int grNo = input.nextInt(); 
        input.nextLine();
        
        System.out.print("Reason for leaving: "); 
        String reason = input.nextLine();

        try(Connection con = connect()){
            PreparedStatement insertStmt =con.prepareStatement("Insert into LeavingCertificate(gr_no, issue_date, reason) VALUES(?,?,?)");
            insertStmt.setInt(1, grNo); 
            insertStmt.setDate(2, new java.sql.Date(System.currentTimeMillis())); 
            insertStmt.setString(3, reason);
            insertStmt.executeUpdate();

            
            PreparedStatement fetchStmt=con.prepareStatement("Select * from Students WHERE gr_no = ?");
            fetchStmt.setInt(1, grNo);
            ResultSet rs= fetchStmt.executeQuery();
            if (rs.next()){
                System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("        SCHOOL LEAVING CERTIFICATE");
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
                System.out.println("GR No            : " + rs.getInt("gr_no"));
                System.out.println("Student Name     : " + rs.getString("student_name"));
                System.out.println("Father Name      : " + rs.getString("father_name"));
                System.out.println("Gender           : " + rs.getString("gender"));
                System.out.println("Date of Birth    : " + rs.getDate("dob"));
                System.out.println("Class            : " + rs.getString("class"));
                System.out.println("Section          : " + rs.getString("section"));
                System.out.println("Admission Date   : " + rs.getString("admission_date"));
                System.out.println("Address          : " + rs.getString("address"));
                System.out.println("Phone            : " + rs.getString("phone"));
                System.out.println("\n---------------------------------------");
                System.out.println("Issue Date       : " + java.time.LocalDate.now());
                System.out.println("Reason           : " + reason);
                System.out.println("---------------------------------------\n");
                System.out.println("Int is to certified that the above student has");
                System.out.println("left the school as per the records.");                
                System.out.println("\n\nSignature: ____________________");
                System.out.println("           (School Authority)");
                System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

            } else {
                System.out.println("Student not found!");
            }
        }catch(Exception e){ 
            System.out.println("Error issuing certificate: " + e.getMessage()); 
        }
    }

    public void showReport(){
        String query = "Select s.gr_no, s.student_name, s.class, r.subject, r.marks, f.status " +
                       "FROM Students s LEFT JOIN Results r ON s.gr_no=r.gr_no " +
                       "left join Fees f ON s.gr_no=f.gr_no";
                       
        try(Connection con = connect(); 
            Statement stmt = con.createStatement(); 
            ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("\n--- COMPREHENSIVE SCHOOL REPORT ---");
            System.out.println("GR | NAME | CLASS | SUBJECT | MARKS | FEE STATUS");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " | " + 
                                   rs.getString(2) + " | " + 
                                   rs.getString(3) + " | " + 
                                   rs.getString(4) + " | " + 
                                   rs.getInt(5) + " | " + 
                                   rs.getString(6));
            }
        }catch (Exception e){ 
            System.out.println("Failed: " + e.getMessage()); 
        }
    }
}

public class GR_System{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Student students = new Student();
        Academic academics = new Academic();
        Finance finance = new Finance();
        Admin admin = new Admin();

        int userChoice = 0;
        while (userChoice != 9) {
            System.out.println("\n=== SCHOOL MANAGEMENT MENU ===");
            System.out.println("1. Register Student");
            System.out.println("2. View Students");
            System.out.println("3. Mark Attendance");
            System.out.println("4. Process Fee ");
            System.out.println("5. Upload Result");
            System.out.println("6. Generate Report");
            System.out.println("7. Promote Student");
            System.out.println("8. Leaving Certificate");
            System.out.println("9. Exit System");
            System.out.print("Select an option (1-9): ");
            try{
                userChoice = scanner.nextInt();
                scanner.nextLine(); 
            }catch (Exception e){
                System.out.println("Invalid input.");
                scanner.nextLine();
                continue;
            }           
            switch (userChoice){
                case 1: 
                    students.regStudent(scanner);
                    break;
                case 2: 
                    students.viewStudents();
                    break;
                case 3: 
                    academics.Attendance(scanner);
                    break;
                case 4: 
                    finance.feeSystem(scanner);
                    break;
                case 5: 
                    academics.Result(scanner);
                    break;
                case 6: 
                    admin.showReport();
                    break;
                case 7: 
                    students.promote(scanner);
                    break;
                case 8: 
                    admin.leavingCertificate(scanner);
                    break;
                case 9: 
                    System.out.println("Closing system. Goodbye!");
                    break;
                default: 
                    System.out.println("Invalid choice. Try again.");
            }
        }
        scanner.close();
    }
}