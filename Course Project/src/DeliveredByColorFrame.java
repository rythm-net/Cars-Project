import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeliveredByColorFrame extends JFrame {
    Connection conn = null;
    PreparedStatement state = null;
    ResultSet result;

    JPanel upPanel = new JPanel();
    JPanel midPanel = new JPanel();
    JPanel downPanel = new JPanel();


    JLabel colorL = new JLabel("Цвят");
    JTextField colorTF = new JTextField();
    JButton reportBtn = new JButton("Направи справка");

    JTable table = new JTable();

    public DeliveredByColorFrame(){
        this.setSize(520, 500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(3, 1));

        upPanel.setLayout(new GridLayout(4, 2));
        upPanel.add(colorL);
        upPanel.add(colorTF);

        this.add(upPanel);

        reportBtn.addActionListener(new DeliveredByColorFrame.ButtonReportClick());
        midPanel.add(reportBtn);

        this.add(midPanel);

        downPanel.add(table);
        this.add(downPanel);

        this.setVisible(true);
    }

    class ButtonReportClick implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String statement = "select cs.color count(*) from car_specifications as cs,cars as c group by cs.color";

            conn = DBConnection.getConnection();
            try {
                if (!colorTF.getText().trim().equals("")) {
                    statement = "select cs.color, count(*) from car_specifications as cs where cs.color = ? group by cs.color";
                    state = conn.prepareStatement(statement);
                    state.setString(1, colorTF.getText().trim());

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
