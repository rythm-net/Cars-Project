import Model.Car;
import Model.CarSpecifications;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;

public class CarDeliveryFrame extends JFrame {
    Connection conn = null;
    PreparedStatement state = null;
    ResultSet result;

    JPanel upPanel = new JPanel();
    JPanel midPanel = new JPanel();
    JPanel downPanel = new JPanel();

    JLabel brandL = new JLabel("Модел:");
    JLabel serialNumberL = new JLabel("Сериен номер:");
    JLabel personL = new JLabel("Човек:");
    JLabel cityL = new JLabel("Град:");

    //Object[] items;
    JComboBox<CarSpecifications> carSpecificationsCombo = new JComboBox<CarSpecifications>();
    JTextField serialNumberTF = new JTextField();
    JTextField personTF = new JTextField();
    JTextField cityTF = new JTextField();


    JButton addBt = new JButton("Добавяне");
    JButton deleteBt = new JButton("Изтриване");
    //JButton editBt = new JButton("Редактиране");
    //downPanel
    JTable table = new JTable();
    JScrollPane myScroll = new JScrollPane(table);

    public CarDeliveryFrame() {
        this.setSize(520, 500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(3, 1));

        upPanel.setLayout(new GridLayout(4, 2));

        upPanel.add(brandL);
        upPanel.add(carSpecificationsCombo);
        upPanel.add(serialNumberL);
        upPanel.add(serialNumberTF);
        upPanel.add(personL);
        upPanel.add(personTF);
        upPanel.add(cityL);
        upPanel.add(cityTF);

        this.add(upPanel);

        midPanel.add(addBt);
        midPanel.add(deleteBt);
        //midPanel.add(editBt);

        this.add(midPanel);
        //down Panel
        myScroll.setPreferredSize(new Dimension(500,150));
        downPanel.add(myScroll);
        this.add(downPanel);
        refreshTable();
        loadComboModel();
        myScroll.setViewportView(table);

        addBt.addActionListener(new CarDeliveryFrame.AddAction());
        deleteBt.addActionListener(new CarDeliveryFrame.DeleteAction());
        //editBt.addActionListener(new EditAction());
        table.getModel().addTableModelListener(new CarDeliveryFrame.TableEditAction());

        this.setVisible(true);
    }//Еnd of the constructor

    private void loadComboModel(){
        conn = DBConnection.getConnection();
        String query = "select c.id, c.brand, c.model, cs.color, cs.transmission from cars as c, car_specifications as cs where cs.id_cs = c.id";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                CarSpecifications newCar = new CarSpecifications();
                newCar.setId(rs.getLong("id"));
                newCar.setBrand(rs.getString("brand"));
                newCar.setModel(rs.getString("model"));
                newCar.setColor(rs.getString("color"));
                newCar.setTransmission(rs.getString("transmission"));

                carSpecificationsCombo.addItem(newCar);
            }
        } catch (SQLException e) {

        }
    }

    public void refreshTable(){
        conn = DBConnection.getConnection();
        try {
    // state = conn.prepareStatement("select cd.ID, c.BRAND, c.MODEL, cd.CAR_SERIAL_NUMBER, cd.PERSON, cd.CITY from CARS_DELIVERY as cd, car_specifications as cs, cars as c where cd.car_specifications = cs.id and cs.id = c.id");

               state = conn.prepareStatement("select cd.ID_CD, c.BRAND, c.MODEL, cd.CAR_SERIAL_NUMBER, cd.PERSON, cd.CITY from CARS_DELIVERY as cd, cars as c where cd.ID_CD= c.id ");

            result = state.executeQuery();
            DefaultTableModel model = new DefaultTableModel();
            table.setModel(model);
            model.addColumn("ID");
            model.addColumn("марка");
            model.addColumn("модел");
            model.addColumn("сериен номер");
            model.addColumn("човек");
            model.addColumn("град");

            while(result.next()){
                model.addRow(new Object[]{
                        result.getBigDecimal(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4),
                        result.getString(5),
                        result.getString(6)
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class AddAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            conn = DBConnection.getConnection();
            String sql = "insert into CARS_DELIVERY(CAR_SERIAL_NUMBER,PERSON,CITY) values(?, ?, ?)";

            try {
                state = conn.prepareStatement(sql);
                state.setString(1, serialNumberTF.getText());
                state.setString(2, personTF.getText());
                state.setString(3, cityTF.getText());
               // state.setLong(4, ((CarSpecifications)carSpecificationsCombo.getSelectedItem()).getId());

                state.execute();
                refreshTable();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            //System.out.println(brandTF.getText() + " " + modelTF.getText() + " " + engineCombo.getSelectedItem()
            //        + " " + YearOfManufactureL.getText());
        }
    }

    class DeleteAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (table.getSelectedRow() == -1){
                return;
            }

            PreparedStatement stmt = null;
            try {
                stmt = DBConnection.getConnection().prepareStatement("delete from cars_delivery where id = ?");
                stmt.setBigDecimal(1, (BigDecimal) table.getValueAt(table.getSelectedRow(), 0));

                stmt.execute();

                refreshTable();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    class EditAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    class TableEditAction implements TableModelListener {

        @Override
        public void tableChanged(TableModelEvent e) {
            int row = e.getFirstRow();
            try {
                PreparedStatement stmt = conn
                        .prepareStatement("update cars set brandl = ?, modell = ?, engine = ?, pricel = ?, yearofmanufacturel = ? where id = ?");
                stmt.setString(1, table.getValueAt(row, 1).toString());
                stmt.setString(2, table.getValueAt(row, 2).toString());
                stmt.setString(3, table.getValueAt(row, 3).toString());
                stmt.setInt(4, Integer.parseInt(table.getValueAt(row, 4).toString()));
                stmt.setInt(5, (int)table.getValueAt(row, 5));
                stmt.setInt(6, Integer.parseInt(table.getValueAt(row, 0).toString()));

                stmt.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
