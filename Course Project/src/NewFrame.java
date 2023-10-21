import javax.swing.*;

public class NewFrame extends JFrame {
    JTabbedPane tab =new JTabbedPane();
    JPanel carsP= new JPanel();
    JPanel specificationsP= new JPanel();
    JPanel deliveryP=new JPanel();
    JPanel report1P =new JPanel();
    JPanel report2P= new JPanel();


    public NewFrame() {
        this.setSize(520, 500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        tab.add("Car",carsP );
        tab.add("Car Delivery",deliveryP );
        tab.add("Car Specifications",specificationsP );
        tab.add("Report 1 ",report1P );
        tab.add("Report 2",report2P );

        this.add(tab);
        this.setVisible(true);
    }


}


