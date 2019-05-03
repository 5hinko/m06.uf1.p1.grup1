package m06.uf1.audioplayer.model;

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
        super();
        // A completar ...
        nombreCampos = new ArrayList();
        nombreCampos.add("Nombre");
        nombreCampos.add("Minutos");
        nombreCampos.add("ruta");
        registros = dades;

    }

    @Override
    public int getRowCount() {
        return registros.size();
    }

    @Override
    public int getColumnCount() {
        return nombreCampos.size();
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
