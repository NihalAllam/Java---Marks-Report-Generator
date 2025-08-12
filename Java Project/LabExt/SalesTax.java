import java.util.Scanner;

class SalesTax {
    public static int calculateFinalPrice(int price, int Salestax) {
        int finalPrice  = price + ((price * Salestax) / 100);
        return finalPrice;
    }

    public static double calculateFinalPrice(double price, double Salestax) {
        double finalPrice  = price + ((price * Salestax) / 100);
        return finalPrice;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        int b = sc.nextInt();
        double m = sc.nextDouble();
        double n = sc.nextDouble();

        int ans1 = calculateFinalPrice(a, b);
        double ans2 = calculateFinalPrice(m, n);

        System.out.println("\n\n"+ans1);
        System.err.printf("%.2f",ans2);
        sc.close();
    }
}