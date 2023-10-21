
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class CarsFrame extends JFrame {
    Connection conn = null;
    PreparedStatement state = null;
    ResultSet result;

    JPanel upPanel = new JPanel();
    JPanel midPanel = new JPanel();
    JPanel downPanel = new JPanel();

    JLabel brandL = new JLabel("Марка:");
    JLabel modelL = new JLabel("Модел:");
    JLabel priceL = new JLabel("Цена:");
    JLabel YearOfManufactureL = new JLabel("Година на производство:");
    JLabel engineL = new JLabel("Тип двигател:");

    JTextField brandTF = new JTextField();
    JTextField modelTF = new JTextField();
    JTextField priceTF = new JTextField();
    JTextField YearOfManufactureTF = new JTextField();

    String[] item = {"Бензинов", "Дизелов", "Газ/Безнзин", "Електрически", "Хибриден"};
    JComboBox<String> engineCombo = new JComboBox<String>(item);

    JButton addBt = new JButton("Добавяне");
    JButton deleteBt = new JButton("Изтриване");
    //JButton editBt = new JButton("Редактиране");
    //downPanel
    JTable table = new JTable();
    JScrollPane myScroll = new JScrollPane(table);

    public CarsFrame() {
        this.setSize(520, 500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(3, 1));

        upPanel.setLayout(new GridLayout(5, 2));

        upPanel.add(brandL);
        upPanel.add(brandTF);
        upPanel.add(modelL);
        upPanel.add(modelTF);
        upPanel.add(priceL);
        upPanel.add(priceTF);
        upPanel.add(YearOfManufactureL);
        upPanel.add(YearOfManufactureTF);
        upPanel.add(engineL);
        upPanel.add(engineCombo);

        this.add(upPanel);

        midPanel.add(addBt);
        midPanel.add(deleteBt);
        //midPanel.add(editBt);

        this.add(midPanel);
        //down Panel
        myScroll.setPreferredSize(new Dimension(500,150));
        downPanel.add(myScroll);
        this.add(downPanel);
        refreshTable("CARS");
        myScroll.setViewportView(table);

        addBt.addActionListener(new AddAction());
        deleteBt.addActionListener(new DeleteAction());
        //editBt.addActionListener(new EditAction());
        table.getModel().addTableModelListener(new TableEditAction());

        this.setVisible(true);
    }//Еnd of the constructor
    public void refreshTable(String mytable){
        conn = DBConnection.getConnection();
        try {
            state = conn.prepareStatement("select * from " + mytable);
            result = state.executeQuery();
            DefaultTableModel model = new DefaultTableModel();
            table.setModel(model);
            model.addColumn("ID");
            model.addColumn("марка");
            model.addColumn("модел");
            model.addColumn("двигател");
            model.addColumn("цена");
            model.addColumn("година");

            while(result.next()){
                model.addRow(new Object[]{
                        result.getBigDecimal(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4),
                        result.getInt(5),
                        result.getInt(6)
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
            //String sql = "insert into CARS values(null, ?, ?, ?, ?, ?)";
           String sql = "insert into CARS(BRAND,MODEL,ENGINE,PRICE,YEAR_C) values(?, ?, ?, ?, ?)";

            try {
                state = conn.prepareStatement(sql);
                state.setString(1, brandTF.getText());
                state.setString(2, modelTF.getText());
                state.setString(3, engineCombo.getSelectedItem().toString());
                state.setInt(4, Integer.parseInt(priceTF.getText()));
                state.setInt(5, Integer.parseInt(YearOfManufactureTF.getText()));


                state.execute();
                refreshTable("CARS");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println(brandTF.getText() + " " + modelTF.getText() + " " + engineCombo.getSelectedItem()
                    + " " + YearOfManufactureL.getText());
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
                stmt = DBConnection.getConnection().prepareStatement("delete from CARS where id = ?");
                stmt.setBigDecimal(1, (BigDecimal) table.getValueAt(table.getSelectedRow(), 0));

                stmt.execute();

                refreshTable("CARS");
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
                        .prepareStatement("update CARS set brandl = ?, modell = ?, engine = ?, price = ?, year_c = ? where id = ?");
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

