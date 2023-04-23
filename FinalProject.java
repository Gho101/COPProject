package COPProject;
/*
 * Date:4/14/2023
 * Name Steven Luciano-Aguilar
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.*;

abstract class Lecture{
    private String crn;
    private String prefix;
    private String titl;
    private String grad;
    private String building;
    private String labs;
    private String modality;
    private int idNum;

    

    public int getIdNum() {
        return idNum;
    }
    public void setIdNum(int idNum) {
        this.idNum = idNum;
    }

    public String getModality() {
        return modality;
    }
    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getCRN() {
        return crn;
    }
    public void setCRN(String cRN) {
        crn = cRN;
    }

    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getTitl() {
        return titl;
    }
    public void setTitl(String titl) {
        this.titl = titl;
    }

    public String getGrad() {
        return grad;
    }
    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getBuilding() {
        return building;
    }
    public void setBuilding(String building) {
        this.building = building;
    }

    public String getLabs() {
        return labs;
    }
    public void setLabs(String labs) {
        this.labs = labs;
    }
} 

class LectureLabsNoLabs extends Lecture{
    
    public LectureLabsNoLabs(String crn, String prefix, String titl, String grad, String modality, String building, String labs) {
        setCRN(crn);
        setPrefix(prefix);
        setTitl(titl);
        setGrad(grad);
        setModality(modality); 
        setBuilding(building);
        setLabs(labs);
    }

    @Override
    public String toString() {
        return getCRN() + "," + getPrefix() + "," + getTitl() + "," + getGrad() + "," + getModality() + "," + getBuilding() + "," + getLabs();
    }
    
}

class LabClass extends Lecture{
    public LabClass(String crn, String building){
        setCRN(crn);
        setBuilding(building);
    }

    @Override
    public String toString(){
        return getCRN() + "," + getBuilding();
    }
} 

class Online extends Lecture{
    public Online(String crn, String prefix, String titl, String grad, String modality) {
        setCRN(crn);
        setPrefix(prefix);
        setTitl(titl);
        setGrad(grad);
        setModality(modality); 
    }
    @Override
    public String toString() {
        return getCRN() + "," + getPrefix() + "," + getTitl() + "," + getGrad() + "," + getModality();
    }
}

class LectureList{
    private Lecture[] list;
    private int size;
    private Scanner myScan;
    public Lecture[] getList() {
        return list;
    }

    public void setList(Lecture[] list) {
        this.list = list;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void listLabs(String crn, String building, int counter){
        list[counter] = new LabClass(crn, building);
    }
    public void listLecture(String crn, String prefix, String titl, String grad, String modality, String building, String labs, int counter){
        list [counter] = new LectureLabsNoLabs(crn, prefix, titl, grad, modality, building, labs);
    }
    public void listOnline(String crn, String prefix, String titl, String grad, String modality, int counter){
        list [counter] = new Online(crn, prefix, titl, grad, modality);
    }
    
    public LectureList(){
        list = new Lecture[1];
    }

    //open the file to find how big the array has to be to store contents
    public LectureList(String FILE, Scanner myScan){
        boolean done = false;
        this.myScan = myScan;
        
        while(!done){
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
                System.out.print("File Found! Let's proceed...\n");
                int lines = 0;
                while (reader.readLine() != null) lines++;
                size = lines;
                reader.close();
                done = true;
            } catch (IOException e) {
                System.out.print("Sorry no such file.\n");
                System.out.print("Try again:");
                FILE = myScan.nextLine();
            }
        }
        list = new Lecture[size];
        for(int i = 0; i < size; i++) list[i]= null;

        FillList(FILE);
        CleanFile(FILE);
    }

    //take the contents of the file and then place it in an array
    private void FillList(String FILE){
        int counter = 0;
        try (Scanner reader = new Scanner(new File(FILE))) {
            while (reader.hasNextLine()){
                String input = reader.nextLine();
                String[] parts = input.split(",");
                int check = parts.length;

                if(check == 7){
                    listLecture(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], counter);
                }else if(check == 5){
                    listOnline(parts[0], parts[1], parts[2], parts[3], parts[4], counter);
                }else if(check == 2){
                    listLabs(parts[0], parts[1], counter);
                }
                counter++;
            }           
            reader.close();//close the file
        } catch (Exception e) {
            System.out.print("Somthing went wrong\n");
        }
        
    }

    //look up class by CRN
    public Lecture[] classLookUp(String CRN, int numLec){
        String [] parts = CRN.split(" ");
        Lecture[] lec;
        int inputs = parts.length;
        while(numLec != inputs){
            System.out.print("Number of crn entered is incorrect.\n Enter the crns of the lectures: ");
            CRN = myScan.nextLine();
            parts = CRN.split(" ");
            inputs = parts.length;
        }
        lec = new Lecture[size];

        for(int j = 0; j < inputs; j++){
            for(int i = 0; i < size; i ++){
                if(list[i].getCRN() != null){
                    if(list[i].getCRN().compareTo(parts[j]) == 0){
                        if(list[i] instanceof LectureLabsNoLabs){
                            if(list[i].getLabs().compareTo("No") == 0){
                                System.out.print("\t ["+list[i].getCRN()+ "/" + list[i].getPrefix()+"/"+ list[i].getTitl()+"] Added!\n");
                                lec[i] = list[i];
                            }else{
                                System.out.print("\t ["+ list[i].getPrefix()+"/"+ list[i].getTitl()+"] has these labs:\n");
                                lec[i] = list[i];
                                printLab(i);
                            }
                        }else{
                            System.out.print("\t ["+list[i].getCRN()+ "/" + list[i].getPrefix()+"/"+ list[i].getTitl() +"] Added!\n"); 
                            lec[i] = list[i];
                        }
                    }
                }
            }
        }
        return lec;
    }

    //print the labs 
    public void printLab(int index){
        index = index + 1;
        int end = index + 3;

        for(int i = index; i < end; i++){
            System.out.print("\t \t "+list[i]+"\n");
        }
    }

    public LabClass[] getLab(String CRN){
        LabClass [] parts = new LabClass[3];
        int index = 0;
        for(int i = 0; i < size; i++){
            if(list[i].getCRN().compareTo(CRN) == 0){
                for(int j =(i+1); j < (i + 3); j++){
                    if(list[j] instanceof LabClass){
                        parts[index] = (LabClass)list[j];
                    }
                    index++;
                }
            }
        }

        return parts;
    }

    //find the lecture delete the lecture and exit the function
    public void deleteLecture(String CRN){
        for(int i = 0; i < size; i ++){
            if(list[i].getCRN().compareTo(CRN) == 0){
                if(list[i] instanceof LectureLabsNoLabs){
                    if(list[i].getLabs().compareTo("No") == 0){
                        list[i] = null;
                        shiftLift(i);
                        shrink(1);
                        return;
                    }else{
                        list[i] = null;
                        shiftLift(i);
                        deleteLab(i);
                        return;
                    }
                }else{
                    list[i] = null; 
                    shiftLift(i);
                    shrink(1);
                    return;
                }
            }
        }
    }

    //turning the next 3 indexes to null
    public void deleteLab(int index){
        int nullCount = 4;
        int end = index +3;

        for(int i = index; i < end; i++){
            list[index] = null;
            shiftLift(index);
        }
        shrink(nullCount);
    }

    //this will shift all contents from the starting points to the left
    public void shiftLift(int index){
        for(int i = index; i < size - 1; i++)
            list[i] = list[i + 1];
    }

    //This will shrink the array taking out the last index of the array 
    public void shrink(int nullCount){
        Lecture[] newList = new Lecture[list.length - nullCount];
        System.arraycopy(list, 0, newList, 0, list.length - nullCount);
        
        list = newList;
        size = list.length;
    }
    
    //creat the file of lecturs with changes if any 
    public void rewrite(){
        try {
            FileWriter myWriter = new FileWriter("lec.txt");
            for (Lecture l:list){
                myWriter.write(l +"\n");
            }
            myWriter.close();
            
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        myScan.close();
    }

    
    
    //empty the file
    private void CleanFile(String FILE){
        try {
            new FileWriter(FILE, false).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

abstract class User{
    private int id;
    private String name;
    private int numLec;
    private Lecture[] lec;

    public User(int id, String name, int numLec){
        setId(id);
        setName(name);
        setNumLec(numLec);
        setCRNList();
        
    }
     

    public void setCRNList(){
        lec = new Lecture[numLec];
    }
    public void insertCRN(Lecture[] parts){
        lec = parts;
    }
    
    public int getNumLec() {
        return numLec;
    }
    public void setNumLec(int numLec) {
        this.numLec = numLec;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Lecture[] getLec() {
        return lec;
    }

    public void printCRN(){
        for(int i = 0; i < lec.length; i++){
            System.out.print(lec[i]);
        }
    }

}

class Student extends User{
    public Student(int id, String name, int numLec){
        super(id, name, numLec);
    }
}

class TA extends User{
    private String supervisor;
    private String degree;

    public TA(int id, String name, String supervisor, String degree , int numLec){
        super(id, name, numLec);
        setSupervisor(supervisor);
        setDegree(degree);
    }

    public String getSupervisor() {
        return supervisor;
    }
    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }
    public String getDegree() {
        return degree;
    }
    public void setDegree(String degree) {
        this.degree = degree;
    }
    
    
    
}

class Faculty extends User{
    private String rank;
    private String office;
    
    public Faculty(int id, String name, String rank, String office, LectureList list, int numLec) {
        super(id, name, numLec);
        setRank(rank);
        setOffice(office);

    }

    public String getRank() {
        return rank;
    }
    public void setRank(String rank) {
        this.rank = rank;
    }
    public String getOffice() {
        return office;
    }
    public void setOffice(String office) {
        this.office = office;
    }
    
}

class UserList{
    private int spot = 0;
    private User[] list;
    private Scanner myScan;

    public int getSpot() {
        return spot;
    }

    public void setSpot(int spot) {
        this.spot = spot;
    }

    public UserList(Scanner myScan){
        this.myScan = myScan;
        list = new User[1];
        list[0] = null;
    }

    public void expand(){
        User[] newList = new User[list.length + 1];

        System.arraycopy(list, 0, newList, 0, list.length);
        
        list = newList;
        list[list.length] = null;
    }

    public int randomNum(){
        Random rand = new Random(); 
        int num = rand.nextInt(2) + 1;
        return num;
    }
    

    //Incomplete
    //           TODO link the classes to falculty
    public void falcultyEntry(LectureList lec){
        
        System.out.print("Enter UCF id:");
        int id = myScan.nextInt();
        myScan.nextLine();

        //id Should be added Here
        System.out.print("Enter name: ");
        String name = myScan.nextLine();

        System.out.print("Enter rank:");
        String rank = myScan.nextLine().toLowerCase();

        System.out.print("Enter office location:");
        String office = myScan.nextLine();

        System.out.print("How many Lectures: ");
        int numLec = myScan.nextInt();
        myScan.nextLine();

        System.out.print("Enter the crns of the lectures:");
        Lecture[] parts = lec.classLookUp(myScan.nextLine(), numLec);

        list[spot] = new Faculty(id, name, rank, office, lec, numLec);
        list[spot].insertCRN(parts);
        list[spot].printCRN();
        spot++;
    }

    public void studentEntry(LectureList lec){


        System.out.print("Enter UCF id:");
        int id = myScan.nextInt();
        myScan.nextLine();
        //id Should be added Here
        System.out.print("Enter name: ");
        String name = myScan.nextLine();

        System.out.printf("Which lecture to enroll [%s] in?", name);
        Lecture[] parts = lec.classLookUp(myScan.nextLine(), 1);
        
        list[spot] = new Student(id, name, 1);

        if(parts[0] instanceof LectureLabsNoLabs){
            if(parts[0].getLabs().compareTo("yes") == 0){
                LabClass[] labs = lec.getLab(parts[0].getCRN());
                LabClass randLab = labs[randomNum()]; 
                System.out.printf("[%s]is added to lab: %s", name, randLab.getCRN());
            }
        } 
        

    }

    public void addLeclist(){
        
    }

    
}



public class FinalProject{
    
    public static void main(String[] args) {
        boolean keepGoing = true;
        int choice;
        Scanner myScan = new Scanner(System.in);

        System.out.print("Enter the absolute path of the file:");
        String FILE = myScan.next();
        myScan.nextLine();//flush

        LectureList list = new LectureList(FILE,myScan);
        UserList user = new UserList(myScan);
        do{
            
            System.out.print("***********************************************\n");
            System.out.print("Choose one of these options:\n");
            System.out.print("\t 1- Add a new Faculty to the schedule\n"); 
            System.out.print("\t 2- Enroll a Student to a Lecture\n");
            System.out.print("\t 3- Print the schedule of a Faculty\n"); 
            System.out.print("\t 4- Print the schedule of an TA\n");
            System.out.print("\t 5- Print the schedule of a Student\n"); 
            System.out.print("\t 6- Delete a Lecture\n"); 
            System.out.print("\t 7- Exit\n");
            System.out.print("Enter your choice:"); 
            

            choice = myScan.nextInt();
            myScan.nextLine();

            switch(choice){
                case 1:
                    user.falcultyEntry(list);
                    break;
                case 2:
                    user.studentEntry(list);
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:

                    //I was able to delete only lectures for now
                    System.out.print("Enter the CRN of the lecture to delete:");
                    list.deleteLecture(myScan.nextLine());//get CRN to delete lecture
                    break;
                case 7:
                    keepGoing = false;
                    break;
                default:
                    System.out.print("Pleae choose the proper choices\n");
            }

        }while(keepGoing);

        list.rewrite();
        
        System.out.print("Done");
        myScan.close();
    }

}


//_________________________________________________________________________
class CheckId extends IdException{
    
    boolean tf = false;

    public void idCheck(){
        while(tf == false){
            Scanner myScan = new Scanner(System.in);
            String  id = myScan.nextLine();
            try{
                if(id.length() == 7){
                    tf = true;
                }else{
                    throw new IdException();
                }
            }catch(IdException e){
                IdException obj = new IdException();
                System.out.println(obj.getLocalizedMessage());
            }
        }
    }
}
//___________________________________________________________________
class IdException extends Exception{
    @Override
    public String getLocalizedMessage(){
        return ">>>>>> Sorry incorrect format. (Ids  are 7 digits)";
    }
}


