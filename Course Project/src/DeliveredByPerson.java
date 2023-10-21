import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeliveredByPerson extends JFrame {
    Connection conn = null;
    PreparedStatement state = null;
    ResultSet result;

    JPanel upPanel = new JPanel();
    JPanel midPanel = new JPanel();
    JPanel downPanel = new JPanel();


    JLabel colorL = new JLabel("Човек");
    JTextField transmissionTF = new JTextField();
    JButton reportBtn = new JButton("Направи справка");

    JTable table = new JTable();

    public DeliveredByPerson(){
        this.setSize(520, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(3, 1));

        upPanel.setLayout(new GridLayout(4, 2));
        upPanel.add(colorL);
        upPanel.add(transmissionTF);

        this.add(upPanel);

        reportBtn.addActionListener(new DeliveredByPerson.ButtonReportClick());
        midPanel.add(reportBtn);

        this.add(midPanel);

        downPanel.add(table);
        this.add(downPanel);

        this.setVisible(true);
    }

    class ButtonReportClick implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String statement = "select cd.PERSON count(*) from CARS_DELIVERY  as cd,cars as c group by cd.PERSON";

            conn = DBConnection.getConnection();
            try {
                if (!transmissionTF.getText().trim().equals("")) {
                    statement = "select cd.PERSON, count(*) from CARS_DELIVERY  as cd where cd.PERSON = ? group by cd.PERSON";
                    state = conn.prepareStatement(statement);
                    state.setString(1, transmissionTF.getText().trim());

                } else {
                    state = conn.prepareStatement(statement);
                }

                result = state.executeQuery();
                DefaultTableModel model = new DefaultTableModel();
                table.setModel(model);
                model.addColumn("Model");
                model.addColumn("Color");

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
