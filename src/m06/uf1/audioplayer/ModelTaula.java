package m06.uf1.audioplayer;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 *
 * @author jmartin
 */
public class ModelTaula extends AbstractTableModel {

    private ArrayList<String> nombreCampos;
    private ArrayList<ArrayList> registros;

    public ModelTaula(ArrayList<ArrayList> dades) {
        // A completar ...
        super();
        nombreCampos = new ArrayList();
        nombreCampos.add("Nombre");
        nombreCampos.add("Minutos");
        registros = dades;

    }

    @Override
    public int getRowCount() {
        return nombreCampos.size();
    }

    @Override
    public int getColumnCount() {
        return registros.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return registros.get(rowIndex).get(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return nombreCampos.get(column);
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
