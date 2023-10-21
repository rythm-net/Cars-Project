import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {
    JLabel label = new JLabel("Система за управление на автомобил");
    JButton car = new JButton("Коли");
    JButton carSpecs = new JButton("Спецификации");
    JButton deliveries = new JButton("Доставка");
    JButton report1 = new JButton("Справка 1");
    JButton report2 = new JButton("Справка 2");
    JButton report3 = new JButton("Справка 3");


    MainMenu(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        panel.setSize(200,500);
        this.setSize(200, 500);
        c.weightx = 1;
        c.weighty = .25;
        c.insets = new Insets(30, 20, 30, 20);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;

        label.setFont(new Font("Arial", Font.PLAIN, 40));
        car.setFont(new Font("Arial", Font.PLAIN, 40));
        carSpecs.setFont(new Font("Arial", Font.PLAIN, 40));
        deliveries.setFont(new Font("Arial", Font.PLAIN, 40));
        report1.setFont(new Font("Arial", Font.PLAIN, 40));
        report2.setFont(new Font("Arial", Font.PLAIN, 40));
        report3.setFont(new Font("Arial", Font.PLAIN, 40));

        car.addActionListener(new ButtonCarClick());
        carSpecs.addActionListener(new ButtonCarSpecsClick());
        deliveries.addActionListener(new ButtonCarDeliveryClick());
        report1.addActionListener(new ButtonReport1Click());
        report2.addActionListener(new ButtonReport2Click());
        report3.addActionListener(new ButtonReport3Click());

        panel.add(label, c);
        panel.add(car, c);
        panel.add(carSpecs, c);
        panel.add(deliveries, c);
        panel.add(report1, c);
        panel.add(report2, c);
        panel.add(report3,c);
        this.add(panel);
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    class ButtonCarClick implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            CarsFrame cars = new CarsFrame();
        }
    }

    class ButtonCarSpecsClick implements  ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            CarSpecificationFrame carSpecFrame = new CarSpecificationFrame();
        }
    }
    class ButtonCarDeliveryClick implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            CarDeliveryFrame carDeliveryFrame = new CarDeliveryFrame();
        }
    }

    class ButtonReport1Click implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) { DeliveredByModelFrame report1 = new DeliveredByModelFrame(); }
    }

    class ButtonReport2Click implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            DeliveredByColorFrame report2 = new DeliveredByColorFrame();
        }

    }
    class ButtonReport3Click implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            DeliveredByPerson report3 = new DeliveredByPerson();
        }

    }
}
