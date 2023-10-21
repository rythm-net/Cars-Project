import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeliveredByModelFrame extends JFrame {
    Connection conn = null;
    PreparedStatement state = null;
    ResultSet result;

    JPanel upPanel = new JPanel();
    JPanel midPanel = new JPanel();
    JPanel downPanel = new JPanel();

    JLabel modelL = new JLabel("Марка");
    JTextField modelTF = new JTextField();

    JButton reportBtn = new JButton("Направи справка");

    JTable table = new JTable();

    public DeliveredByModelFrame(){
        this.setSize(520, 500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(3, 1));

        upPanel.setLayout(new GridLayout(4, 2));
        upPanel.add(modelL);
        upPanel.add(modelTF);

        this.add(upPanel);

        reportBtn.addActionListener(new ButtonReportClick());
        midPanel.add(reportBtn);

        this.add(midPanel);

        downPanel.add(table);
        this.add(downPanel);

        this.setVisible(true);
    }

    class ButtonReportClick implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //String statement = "select c.brand, count(*) from cars as c, car_specifications as cs, cars_delivery as cd where cd.car_specifications = cs.id and cs.cars_id = c.id group by c.brand";
           String statement = "select c.brand, count(*) from cars as c, car_specifications as cs, cars_delivery as cd where cd.id_cd = cs.id_cs and cs.id_cs = c.id group by c.brand";

            conn = DBConnection.getConnection();
            try {
                if (!modelTF.getText().trim().equals("")) {
                     //statement = "select c.brand, count(*) from cars as c, car_specifications as cs, cars_delivery as cd where cd.car_specifications = cs.id and cs.cars_id = c.id and c.brand = ? group by c.brand";
                    statement = "select c.brand, count(*) from cars as c, car_specifications as cs, cars_delivery as cd where cd.id_cd = cs.id_cs and cs.id_cs = c.id and c.brand = ? group by c.brand";

                    state = conn.prepareStatement(statement);
                    state.setString(1, modelTF.getText().trim());

                } else {
                    state = conn.prepareStatement(statement);
                }

                result = state.executeQuery();
                DefaultTableModel model = new DefaultTableModel();
                table.setModel(model);
                model.addColumn("Model");
                model.addColumn("Count");

                while(result.next()){
                    model.addRow(new Object[]{
                            result.getString(1),
                            result.getInt(2),
                    });
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
