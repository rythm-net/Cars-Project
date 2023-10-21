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

public class CarSpecificationFrame extends JFrame {
    Connection conn = null;
    PreparedStatement state = null;
    ResultSet result;

    JPanel upPanel = new JPanel();
    JPanel midPanel = new JPanel();
    JPanel downPanel = new JPanel();

    JLabel brandL = new JLabel("Модел и версия:");
    JLabel transmitionL = new JLabel("Трансмисия:");
    JLabel colorL = new JLabel("Цвят:");
    JLabel typeL = new JLabel("Тип:");

    Object[] items;
    JComboBox<Car> modelCombo = new JComboBox<Car>();
    JTextField colorTF = new JTextField();
    JTextField typeTF = new JTextField();

    String[] item = {"Автоматична", "Ръчна", "Хибридна"};
    JComboBox<String> transmitionCombo = new JComboBox<String>(item);

    JButton addBt = new JButton("Добавяне");
    JButton deleteBt = new JButton("Изтриване");
    //JButton editBt = new JButton("Редактиране");
    //downPanel
    JTable table = new JTable();
    JScrollPane myScroll = new JScrollPane(table);

    public CarSpecificationFrame() {
        this.setSize(520, 500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(3, 1));

        upPanel.setLayout(new GridLayout(4, 2));

        upPanel.add(brandL);
        upPanel.add(modelCombo);
        upPanel.add(transmitionL);
        upPanel.add(transmitionCombo);
        upPanel.add(colorL);
        upPanel.add(colorTF);
        upPanel.add(typeL);
        upPanel.add(typeTF);

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

        addBt.addActionListener(new CarSpecificationFrame.AddAction());
        deleteBt.addActionListener(new CarSpecificationFrame.DeleteAction());
        //editBt.addActionListener(new EditAction());
        table.getModel().addTableModelListener(new CarSpecificationFrame.TableEditAction());

        this.setVisible(true);
    }//Еnd of the constructor

    private void loadComboModel(){
        conn = DBConnection.getConnection();
        String query = "select c.id, c.brand, c.model from cars as c";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Car newCar = new Car();
                newCar.setId(rs.getLong("id"));
                newCar.setBrand(rs.getString("brand"));
                newCar.setModel(rs.getString("model"));

                modelCombo.addItem(newCar);
            }
        } catch (SQLException e) {

        }
    }

    public void refreshTable(){
        conn = DBConnection.getConnection();
        try {
            state = conn.prepareStatement("select cs.ID_CS, c.BRAND, c.MODEL, cs.TYPE, cs.TRANSMISSION, cs.COLOR from car_specifications as cs, cars as c where cs.id_CS = c.id ");
            result = state.executeQuery();
            DefaultTableModel model = new DefaultTableModel();
            table.setModel(model);
            model.addColumn("ID");
            model.addColumn("марка");
            model.addColumn("модел");
            model.addColumn("тип");
            model.addColumn("трансмисия");
            model.addColumn("цвят");

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
            String sql = "insert into CAR_SPECIFICATIONS(TYPE,TRANSMISSION,COLOR)  values( ?, ?, ?)";
            //String sql = "insert into car_specifications values(null, ?, ?, ?, ?)";

            try {
                state = conn.prepareStatement(sql);
                state.setString(1, typeTF.getText());
                state.setString(2, transmitionCombo.getSelectedItem().toString());
                state.setString(3, colorTF.getText());
             //4, ((Car)modelCombo.getSelectedItem()).getId());



                state.execute();
                refreshTable();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            //System.out.println(typeTF.getText() + " " + transmitionCombo.getSelectedItem()
                   // + " " + colorTF.getText() + " " + modelCombo.getSelectedItem());
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
                stmt = DBConnection.getConnection().prepareStatement("delete from car_specifications where id = ?");
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
