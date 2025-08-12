class MarksOutOfBoundException extends Exception {
    MarksOutOfBoundException() {
        super("Marks out of bound Bro!");
    }
}

public class Marks {
    public static void valid(int marks) throws MarksOutOfBoundException {
        if(marks>100 || marks<0) throw new MarksOutOfBoundException();
    }

    public static void main(String[] args) {
        try {
            int marks = -20;
            valid(marks);
            System.out.println(marks + "valid");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
